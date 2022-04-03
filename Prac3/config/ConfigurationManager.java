import java.io.*;

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
	public void loadConfigurationFile(String filepath){
		FileReader fileReader = new FileReader(filepath);
		StringBuffer sb = new StringBuffer();

		while ()
	}

	/**
	 * 
	 * Returns the Current loaded Configuration
	 * 
	 */
	public void getCurrentConfiguration(){

	}

}