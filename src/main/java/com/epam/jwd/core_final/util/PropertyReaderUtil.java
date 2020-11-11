package com.epam.jwd.core_final.util;

import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.exception.InvalidStateException;
import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyReaderUtil {

    private static final Logger logger = Logger.getLogger(PropertyReaderUtil.class);
    private static final Properties properties = new Properties();

    private PropertyReaderUtil() {
    }

    /**
     * try-with-resource using FileInputStream
     *
     * @see {https://www.netjstech.com/2017/09/how-to-read-properties-file-in-java.html for an example}
     * <p>
     * as a result - you should populate {@link ApplicationProperties} with corresponding
     * values from property file
     */
    public static void loadProperties() {
        final String propertiesFileName = "src/main/resources/application.properties";

        try(InputStream inputStream = new FileInputStream(propertiesFileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.fatal("File '" + propertiesFileName + "' is inaccessible or does not exist.");
            throw new InvalidStateException(propertiesFileName);
        }
    }

    public static ApplicationProperties populateApplicationProperties() {
        loadProperties();

        return new ApplicationProperties(properties.getProperty("inputRootDir"),
                                         properties.getProperty("outputRootDir"),
                                         properties.getProperty("crewFileName"),
                                         properties.getProperty("missionsFileName"),
                                         properties.getProperty("spaceshipsFileName"),
                                         Integer.parseInt(properties.getProperty("fileRefreshRate")),
                                         properties.getProperty("dateTimeFormat"));
    }
}
