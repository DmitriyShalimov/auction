package ua.auction.bidme.dao.util;

public class QueryGenerator {
    private static final String WHERE = " WHERE ";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String SINGLE_QUOTE = "'";
    private static final String EQUALS = " = ";

    public static StringBuilder where(StringBuilder query, String parameter, String value, boolean quote) {
        return query
                .append(WHERE)
                .append(parameter)
                .append(EQUALS)
                .append(quote ? quote(value) : value);
    }

    public static StringBuilder limit(StringBuilder query, int value) {
        return query
                .append(LIMIT)
                .append(value);
    }

    public static StringBuilder limitOffset(StringBuilder query, int value, int offset) {
        return offset(limit(query, value), offset);
    }

    private static StringBuilder offset(StringBuilder query, int offset) {
        return query
                .append(OFFSET)
                .append(offset);
    }

    private static StringBuilder quote(String param) {
        return new StringBuilder(SINGLE_QUOTE).append(param).append(SINGLE_QUOTE);
    }
}
