package org.trianacode.TrianaCloud.PegasusExecutor;

import org.shiwa.desktop.data.description.SHIWABundle;
import org.shiwa.desktop.data.description.core.Configuration;
import org.shiwa.desktop.data.description.resource.ConfigurationResource;
import org.shiwa.desktop.data.transfer.WorkflowController;
import org.shiwa.desktop.data.util.exception.SHIWADesktopIOException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ian Harvey
 * Date: 12/03/2012
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
public class RunPegasusPlan {

    public static final String dax = "dax";
    public static final String sitesCatalog = "sites";
    public static final String replicaCatalog = "replicaCatalog";
    public static final String properties = "properties";
    private File daxFile;
    private File propertiesFile;

    private String userHome;
    private File cacheDirectory;


    public byte[] executeBundle(SHIWABundle b, Object o) {
        setOutputDir();

        HashMap<String, ConfigurationResource> flies = getFiles(b);
        for(String name : flies.keySet()){
            if(name.equals(dax)){
                daxFile = writeFile(flies.get(name), dax);
            }
            if (name.equals(properties)){
                propertiesFile = writeFile(flies.get(name), properties);
            }
        }

        String cmd = "pegasus-plan" +
                " -D pegasus.user.properties=" + propertiesFile.getAbsolutePath() +
                " --sites condorpool" +
                " --dir " + cacheDirectory.getAbsolutePath() +
                " --output local" + " --dax " + daxFile.getAbsolutePath() + " --submit";

        System.out.println(cmd);
        runProcess(cmd);

        return new byte[0];  //To change body of created methods use File | Settings | File Templates.
    }

    private File writeFile(ConfigurationResource resource, String name) {
        try {
            File tempFile = File.createTempFile(name, ".tmp", cacheDirectory);
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            resource.getBundleFile().getByteArrayOutputStream().writeTo(fileOutputStream);
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] runProcess(String string) {
        try {
            String line;
            Process p = Runtime.getRuntime().exec(string);
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));

            StringBuilder b = new StringBuilder();

            while ((line = bri.readLine()) != null) {
                b.append(line);
            }
            bri.close();
            while ((line = bre.readLine()) != null) {
                b.append(line);
            }
            bre.close();
            p.waitFor();

//            task.setReturnDataType("String");
//            String retData = b.toString();
//
//            task.setReturnData(retData.getBytes());
//            task.setReturnDataMD5(MD5.getMD5Hash(retData.getBytes()));
//
//            System.out.println(new String(task.getReturnData(), "UTF-8") + "Done.");
//            return TaskOps.encodeTask(task);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return new byte[0];
    }

    private HashMap<String, ConfigurationResource> getFiles(SHIWABundle bundle){
        HashMap<String, ConfigurationResource> results = new HashMap<String, ConfigurationResource>();
        try {

            WorkflowController workflowController = new WorkflowController(bundle);

            for(Configuration configuration : workflowController.getConfigurations()){
                System.out.println("Config type : " + configuration.getType().getString());
                if(configuration.getType() == Configuration.ConfigType.DATA_CONFIGURATION){
                    System.out.println("Received bundle has a data config");

                    System.out.println(configuration.getAggregatedResources().size()
                            + " aggregated resources");

                    System.out.println("Exec config contains "
                            + configuration.getResources().size() + " resources.");
                    for( ConfigurationResource r : configuration.getResources()){
                        results.put(r.getReferableResource().getTitle(), r);
                    }
                    System.out.println(results.size() + " outputs found.");
                    return results;
                }
            }
        } catch (SHIWADesktopIOException e) {
            System.out.println("Returned bundle was corrupt or null.");
        }

        return results;
    }

    private void setOutputDir() {
        userHome = System.getProperty("user.home");
        cacheDirectory = new File(userHome + File.separator + "Pegasus-runtime-files");
        if (!cacheDirectory.exists()) cacheDirectory.mkdir();
        String tempLoc = cacheDirectory.getAbsolutePath() + File.separator + "run_" + getTimeStamp();
        cacheDirectory = new File(tempLoc);
        if (!cacheDirectory.exists()) cacheDirectory.mkdir();
    }

    private String getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd_HH-mm-ss-SS_z");
        return dateFormat.format(new Date());
    }
}
