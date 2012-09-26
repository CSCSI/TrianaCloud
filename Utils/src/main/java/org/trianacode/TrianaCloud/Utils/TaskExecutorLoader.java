/*
 * Copyright (c) 2012, SHIWA
 *
 *     This file is part of TrianaCloud.
 *
 *     TrianaCloud is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     TrianaCloud is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with TrianaCloud.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.trianacode.TrianaCloud.Utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * @author Kieran David Evans & Ian Harvey
 * @version 1.0.0 Feb 26, 2012
 */


public class TaskExecutorLoader extends URLClassLoader {

    private Logger logger = Logger.getLogger(this.getClass().toString());

    File[] jars;
    public HashMap<String, Class> addons = new HashMap<String, Class>();

    public ArrayList<String> routingKeys = new ArrayList<String>();

    public TaskExecutorLoader(URL[] urls, ClassLoader classLoader) {
        super(urls, classLoader);

        try {
            findJars();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nFound " + addons.size() + " addon(s).\n");
    }

    private void findJars() throws ClassNotFoundException, URISyntaxException {
        URL[] urls = this.getURLs();
        for (URL url : urls) {
            findJars(new File(url.toURI()));
        }
    }

    private void findJars(File dir) {
        jars = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return !file.isHidden() && file.getName().endsWith(".jar");
            }
        });
        System.out.println("Found " + jars.length + " jars in addon folder.");

        URL[] urls = new URL[jars.length];
        for (int i = 0; i < jars.length; i++) {
            File jar = jars[i];
            try {
                urls[i] = jar.toURI().toURL();
                addURL(urls[i]);
            } catch (MalformedURLException ignored) {
            }
        }

        for (File jar : jars) {
            try {
                JarFile jarFile = new JarFile(jar);
                ZipEntry config = jarFile.getEntry("META-INF/config");
                if (config != null) {
                    System.out.println("Found META-INF/services/ folder");
                    InputStream zin = jarFile.getInputStream(config);
                    BufferedReader reader = new
                            BufferedReader(new InputStreamReader(zin));

                    String line;
                    List<String> done = new ArrayList<String>();
                    while ((line = reader.readLine()) != null) {
                        // check if the class is in this jar
                        ZipEntry sp = jarFile.getEntry(line.replace(".", "/") + ".class");
                        if (sp != null) {
                            try {
                                if (!done.contains(line)) {

                                    System.out.println("Registering addon class : " + line);
                                    Class cls = this.loadClass(line);
                                    if (TaskExecutor.class.isAssignableFrom(cls)) {
                                        addons.put(line, cls);
                                        TaskExecutor executor = (TaskExecutor) cls.newInstance();
                                        routingKeys.add(executor.getRoutingKey());
                                    }
                                    done.add(line);
                                }
                            } catch (Exception e1) {
                                System.out.println("Exception thrown trying to load service provider class " + line);
                            }
                        }
                    }
                } else {
                    System.out.println("no entries in META-INF/config file");
                }
            } catch (IOException ignored) {
                System.out.println("IOException : " + jar.getName());
            }
        }
    }

    public TaskExecutor getExecutor(String exClass) throws IllegalAccessException, InstantiationException {
        Class c = addons.get(exClass);
        if (c != null) {
            Object o = c.newInstance();
            if (o instanceof TaskExecutor) {
                return (TaskExecutor) o;
            }
        }
        return null;
    }
}