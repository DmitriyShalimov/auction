package ua.auction.bidme.util;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.Thread.currentThread;
import static org.slf4j.LoggerFactory.getLogger;

public class PropertyReader {
    private final Logger logger = getLogger(PropertyReader.class);
    private String propertyFileName;

    public PropertyReader(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public Properties readProperties() {
        logger.info("reading properties from file {}", propertyFileName);
        Properties prop = new Properties();

        try (final InputStream inputStream = currentThread()
                .getContextClassLoader()
                .getResourceAsStream(propertyFileName)) {
            prop.load(inputStream);
        } catch (IOException e) {
            logger.error("error {} occured duaring reading property file {}", e.getMessage(), propertyFileName);
            throw new RuntimeException(e);
        }

        return prop;
    }
}
