package ua.auction.bidme.util

import org.junit.Test

import static junit.framework.TestCase.assertNotNull

class PropertyReaderTest {
    private PropertyReader reader = new PropertyReader("properties/database-connection.properties")

    @Test
    void testReadProperties() {
        Properties properties = reader.readProperties()
        assertNotNull(properties)
        assertNotNull(properties.get("user"))
        assertNotNull(properties.get("password"))
    }
}
