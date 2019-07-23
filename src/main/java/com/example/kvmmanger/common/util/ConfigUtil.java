package com.example.kvmmanger.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author zhh [zhouhaihong@youedata.com].
 * @Date 2018/4/2
 */
public class ConfigUtil {
    //Sub folder under 'conf' folder, eg: home/conf/queryengine
    private static String confFolder = "";
    public static boolean DEBUG_MODE = false;

    public static Properties getConfFileProperties(String conFilePath) throws Exception {
        int index = conFilePath.lastIndexOf("/");
        //String configFolder = conFilePath.substring(0,index);
        String configFileName = conFilePath.substring(index, conFilePath.length());

        Properties property = new Properties();
        InputStream is = null;
        try {
            if (DEBUG_MODE) {
                is = ConfigUtil.class.getResourceAsStream(configFileName);
            } else {
                File file = new File(confFolder + "/" + conFilePath);
                if (!file.exists()) {
                    file = new File(confFolder + "/" + configFileName);
                }
                is = new FileInputStream(file);
            }
            property.load(is);
        } catch (IOException e) {
            property = null;
            throw e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            return property;
        }
    }

    public static InputStream getConfFile(String conFilePath) throws Exception {
        int index = conFilePath.lastIndexOf("/");
        String configFileName = conFilePath.substring(index, conFilePath.length());
        InputStream is = null;
        try {
            if (DEBUG_MODE) {
                is = ConfigUtil.class.getResourceAsStream(configFileName);
            } else {
                File file = new File(confFolder + "/" + conFilePath);
                if (!file.exists()) {
                    file = new File(confFolder + "/" + configFileName);
                }
                is = new FileInputStream(file);
            }
        } catch (IOException e) {
            throw e;
        }

        return is;
    }

    public static void setConfFolder(String confFolder) {
        ConfigUtil.confFolder = confFolder;
    }

    public static String getConfFolder() {
        return confFolder;
    }
}
