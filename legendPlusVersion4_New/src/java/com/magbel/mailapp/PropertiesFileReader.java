package com.magbel.mailapp;

import java.io.FileInputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

public class PropertiesFileReader
{

    static Class class$com$magbel$mailapp$PropertiesFileReader; /* synthetic field */
    private static Properties props;

    static Class _mthclass$(String a)
    {
        try
        {
            Class class1 = Class.forName(a);
            return class1;
        }
        catch(ClassNotFoundException e)
        {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private PropertiesFileReader()
    {
    }

    private static void initialize()
    {
        try
        {
            props = new Properties();
            URL configFileURL = (class$com$magbel$mailapp$PropertiesFileReader != null ? class$com$magbel$mailapp$PropertiesFileReader : (class$com$magbel$mailapp$PropertiesFileReader = _mthclass$("com.magbel.mailapp.PropertiesFileReader"))).getResource("init.properties");
            String filePath = URLDecoder.decode(configFileURL.getPath(), "UTF-8");
            FileInputStream configFile = new FileInputStream(filePath);
            props.load(configFile);
            configFile.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Could not read the configuration from the properties file.");
        }
    }

    public static Properties getProperties()
    {
        if(props == null)
        {
            initialize();
        }
        return props;
    }

    static 
    {
        initialize();
    }
}