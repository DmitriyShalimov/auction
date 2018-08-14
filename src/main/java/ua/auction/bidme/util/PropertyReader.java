package ua.auction.bidme.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.Thread.currentThread;

public class PropertyReader {

    private String propertyFileName;

    public PropertyReader(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public Properties readProperties() {
        Properties prop = new Properties();

        try (final InputStream inputStream = currentThread()
                .getContextClassLoader()
                .getResourceAsStream(propertyFileName)) {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }
}
