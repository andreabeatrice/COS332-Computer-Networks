package com.coderfromscratch.simplehttpserver.config;

import com.coderfromscratch.simplehttpserver.util.Json;
import com.fasterxml.jackson.databind.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager{

    private static ConfigurationManager myConfigurationManager; //Singleton

    private static Configuration currentConfig;

    private ConfigurationManager(){

    }

    public static ConfigurationManager getInstance(){
        if (myConfigurationManager==null)
            myConfigurationManager = new ConfigurationManager();

        return myConfigurationManager;
    }

    /**
     *
     * Used to load a config file by the path provided
     */
    public void loadConfigurationFile(String filepath) {
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(filepath);
        } catch (FileNotFoundException e){
            throw new HttpConfigurationException(e);
        }

        StringBuffer sb = new StringBuffer();
        int i;

        try{
            while ( ( i = fileReader.read()) != -1){
                sb.append((char) i);
            }
        } catch (IOException e){
            throw new HttpConfigurationException(e);
        }

        JsonNode conf = null;

        try{
            conf = Json.parse(sb.toString());
        } catch (IOException e){
            throw new HttpConfigurationException("Error parsing the Configuration file", e);
        }

        try {
            currentConfig = Json.fromJson(conf, Configuration.class);
        } catch (IOException e){
            throw new HttpConfigurationException("Error parsing the Configuration file, internal", e);
        }
    }

    /**
     *
     * Returns the Current loaded Configuration
     *
     */
    public Configuration getCurrentConfiguration(){
        if (currentConfig == null) {
            throw new HttpConfigurationException("No Current Configuration Set.");
        }

        return currentConfig;
    }

}