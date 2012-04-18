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

package org.trianacode.TrianaCloud.TrianaTaskExecutor;

import org.apache.log4j.Logger;
import org.shiwa.desktop.data.description.SHIWABundle;
import org.shiwa.desktop.data.description.core.Configuration;
import org.shiwa.desktop.data.description.resource.ConfigurationResource;
import org.shiwa.desktop.data.description.resource.ReferableResource;
import org.shiwa.desktop.data.description.workflow.OutputPort;
import org.shiwa.desktop.data.transfer.WorkflowController;
import org.shiwa.desktop.data.util.exception.SHIWADesktopIOException;
import org.trianacode.TrianaCloud.Utils.MD5;
import org.trianacode.TrianaCloud.Utils.Task;
import org.trianacode.TrianaCloud.Utils.TaskExecutor;
import org.trianacode.TrianaCloud.Utils.TaskOps;
import org.trianacode.shiwa.bundle.TrianaBundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Hello world!
 */
public class Executor extends TaskExecutor implements Runnable{

    private Logger logger = Logger.getLogger(this.getClass().toString());

    Task task;
    File temp;
    private byte[] out = new byte[0];

    @Override
    public byte[] executeTask() {
        ///TODO: pass temp to triana via shiwa bundles.
        ///TODO: get the returned file, read it into task.setReturnData(), setReturnType to binary
        ///TODO return the task as below

        try {

//            ThreadGroup threadGroup = new ThreadGroup("Executor");
//            Thread thread = new Thread(threadGroup, this);
            Thread thread = new Thread(this);
            thread.run();
            thread.join();

//            threadGroup.destroy();
        }catch (Exception e) {
            System.out.println("Error in executor");
            e.printStackTrace();
        }

        try {
            task.setReturnDataType("binary");
            task.setReturnData(out);
            task.setReturnDataMD5(MD5.getMD5Hash(task.getReturnData()));
            task.setReturnCode("1");
            return TaskOps.encodeTask(task);  //To change body of implemented methods use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return new byte[0];
        }
    }

    @Override
    public void setTask(Task t) {
        this.task = t;
        temp = null;
        try {
            temp = File.createTempFile("data", ".bundle");
            //temp.deleteOnExit();

            System.out.println(temp.getPath());

            FileOutputStream o = new FileOutputStream(temp);
            o.write(task.getData());

            System.out.println(task.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRoutingKey() {
        return "*.triana";
    }

    @Override
    public void run() {

//        URL[] urls = new URL[0];
//        String jarPath = Executor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        try {
//            String decodedPath = URLDecoder.decode(jarPath, "UTF-8");
//            System.out.println("This class = " + decodedPath);
//            File file = new File(decodedPath);
//            if(decodedPath.endsWith(".jar") && file.exists()){
//                urls = new URL[]{file.toURI().toURL()};
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        ClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader().getParent());
//        Thread.currentThread().setContextClassLoader(classLoader);

        try {

//            Class mainClass = classLoader.loadClass("org.trianacode.shiwa.bundle.TrianaBundle");
//            for(Method method : mainClass.getDeclaredMethods()){
//                if(method.getName().contains("executeBundle")){
////                    Method method = mainClass.getDeclaredMethod("executeBundle", new Class[]{SHIWABundle.class, String.class});
//                    SHIWABundle b = new SHIWABundle(temp);
//                    out = (byte[]) method.invoke(b, null);
//
//                }
//            }

            TrianaBundle tb = new TrianaBundle();
            SHIWABundle b = new SHIWABundle(temp);
            out = tb.executeBundle(b, null);

//            SHIWABundle execedBundle = testExecution(b);
//            out = TrianaBundle.getFileBytes(
//                    DataUtils.bundle(File.createTempFile("execed", ".tmp"),
//                            execedBundle.getAggregatedResource()));

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private SHIWABundle testExecution(SHIWABundle shiwaBundle) {
        try {
            WorkflowController workflowController = new WorkflowController(shiwaBundle);
            Configuration execConfig = new Configuration(Configuration.ConfigType.EXECUTION_CONFIGURATION);

            ArrayList<ConfigurationResource> configurationResourceArrayList = new ArrayList<ConfigurationResource>();
            for(ReferableResource resource : workflowController.getWorkflowImplementation().getSignature().getPorts()){
                if(resource instanceof OutputPort){
                    OutputPort outputPort = (OutputPort) resource;
                    //TODO check name
                    ConfigurationResource configurationResource = new ConfigurationResource(outputPort);
//                    File outputFile = new File("output2.txt");
//                    System.out.println(outputFile.getAbsolutePath());
//                    BundleFile bf = DataUtils.createBundleFile(outputFile, execConfig.getId() + "/");
//                    bf.setType(BundleFile.FileType.INPUT_FILE);
//                    execConfig.getBundleFiles().add(bf);
//                    configurationResource.setBundleFile(bf);
//                    configurationResource.setRefType(ConfigurationResource.RefTypes.FILE_REF);
//                    execConfig.addResourceRef(configurationResource);
                    configurationResource.setValue("Some text " + outputPort.getTitle());
                    configurationResource.setRefType(ConfigurationResource.RefTypes.INLINE_REF);
                    configurationResourceArrayList.add(configurationResource);
                }
            }
            execConfig.setResources(configurationResourceArrayList);
            workflowController.getWorkflowImplementation().getAggregatedResources().add(execConfig);

            System.out.println(execConfig + " will have " + execConfig.getResources().size() + " resources.");



        } catch (SHIWADesktopIOException e) {
            e.printStackTrace();
        }
        return shiwaBundle;
    }
}
