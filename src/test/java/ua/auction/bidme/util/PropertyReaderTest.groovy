package ua.auction.bidme.util

import org.junit.Test

import static junit.framework.Assert.assertNull
import static junit.framework.TestCase.assertNotNull
import static junit.framework.TestCase.assertNull

class PropertyReaderTest{
    private PropertyReader reader = new PropertyReader("properties/database-connection.properties")

    @Test
    void testReadProperties(){
        Properties properties = reader.readProperties()
        assertNotNull(properties)
        assertNotNull(properties.get("user"))
        assertNotNull(properties.get("password"))
        assertNotNull(properties.get("db_name"))
    }
}
