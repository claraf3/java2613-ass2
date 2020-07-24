/*
 * @author Clara Fok A00
 * @version May 27, 2020
 */
package a00.data.util;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.FileInputStream;
import java.io.IOException;

public class Logging {
	
	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";

	
	public static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			System.out.format("Can't find the log4j logging configuration file %s.", LOG4J_CONFIG_FILENAME);
		}
	}


}
