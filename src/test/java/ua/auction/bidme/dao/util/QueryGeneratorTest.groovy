package ua.auction.bidme.dao.util

import org.junit.Test

import static org.junit.Assert.assertEquals

class QueryGeneratorTest {
    private StringBuilder getAllLotsCount = new StringBuilder("SELECT COUNT(*) AS rowcount FROM auction.lot")
    private QueryGenerator queryGenerator = new QueryGenerator()

    @Test
    void testWhere() {
        String result = queryGenerator.where(getAllLotsCount, "status", "A", true).toString();
        assertEquals("SELECT COUNT(*) AS rowcount FROM auction.lot WHERE status = 'A'", result);
    }

    @Test
    void testLimit() {
        String result = queryGenerator.limit(getAllLotsCount, 5).toString();
        assertEquals("SELECT COUNT(*) AS rowcount FROM auction.lot LIMIT 5", result);
    }


    @Test
    void testOffset() {
        String result = queryGenerator.offset(getAllLotsCount, 5).toString();
        assertEquals("SELECT COUNT(*) AS rowcount FROM auction.lot OFFSET 5", result);
    }

    @Test
    void testLimitOffset() {
        String result = queryGenerator.limitOffset(getAllLotsCount, 5, 4).toString();
        assertEquals("SELECT COUNT(*) AS rowcount FROM auction.lot LIMIT 5 OFFSET 4", result);
    }
}
