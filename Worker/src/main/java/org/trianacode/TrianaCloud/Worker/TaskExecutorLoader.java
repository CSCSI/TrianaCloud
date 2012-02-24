/*
 *
 *  * Copyright - TrianaCloud
 *  * Copyright (C) 2012. Kieran Evans. All Rights Reserved.
 *  *
 *  * This program is free software; you can redistribute it and/or
 *  * modify it under the terms of the GNU General Public License
 *  * as published by the Free Software Foundation; either version 2
 *  * of the License, or (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program; if not, write to the Free Software
 *  * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package org.trianacode.TrianaCloud.Worker;

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


public class TaskExecutorLoader extends URLClassLoader {

    File[] jars;
    HashMap<String, Class> addons = new HashMap<String, Class>();
    ArrayList<Class> services = new ArrayList<Class>();

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

    public TaskExecutor getExecutor(String exClass) throws IllegalAccessException, InstantiationException{
        Class c = addons.get(exClass);
        if(c!=null){
            Object o = c.newInstance();
            if(o instanceof TaskExecutor){
                return (TaskExecutor)o;
            }
        }
        return null;
    }
}