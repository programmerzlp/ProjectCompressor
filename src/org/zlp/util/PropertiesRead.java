package org.zlp.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public enum PropertiesRead {

	INSTANCE {

		@Override
		public void load(String propertiesPath) throws FileNotFoundException, IOException {
			properties.load(PropertiesRead.class.getResourceAsStream(propertiesPath));
		}

		@Override
		public void load(String propertiesPath, boolean classPath) throws FileNotFoundException,
				IOException {
			properties.load(classPath ? PropertiesRead.class
					.getResourceAsStream(propertiesPath) : new FileInputStream(propertiesPath));
		}

	};

	protected Properties properties = new Properties();

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public String getProperty(String key, String... params) {
		String str = getProperty(key);
		for (int i = 0; i < params.length; i++) {
			str = str.replace("{" + i + "}", params[i]);
		}
		return str;
	}

	public abstract void load(String propertiesPath, boolean classPath)
			throws FileNotFoundException, IOException;

	public abstract void load(String propertiesPath) throws FileNotFoundException, IOException;

}