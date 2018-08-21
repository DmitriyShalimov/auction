package ua.auction.bidme.dao.jdbc;

import org.slf4j.Logger;
import ua.auction.bidme.dao.LotDao;
import ua.auction.bidme.dao.jdbc.mapper.implementation.LotRowMapper;
import ua.auction.bidme.dao.util.QueryGenerator;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.filter.LotFilter;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.auction.bidme.dao.util.QueryGenerator.where;

public class JdbcLotDao implements LotDao {
    private static final LotRowMapper LOT_ROW_MAPPER = new LotRowMapper();
    private final DataSource dataSource;
    private final Logger logger = getLogger(getClass());
    private final String GET_ALL_LOTS_SQL;
    private final String GET_LOTS_COUNT;
    private final String GET_LOTS_BY_ID_SQL;
    private final String UPDATE_LOT_SQL;
    private final String MAKE_BID_SQL;

    public JdbcLotDao(DataSource dataSource, Properties queryProperties) {
        this.dataSource = dataSource;
        this.GET_ALL_LOTS_SQL = queryProperties.getProperty("GET_ALL_LOTS_SQL");
        this.GET_LOTS_COUNT = queryProperties.getProperty("GET_LOTS_COUNT");
        this.GET_LOTS_BY_ID_SQL = queryProperties.getProperty("GET_LOTS_BY_ID_SQL");
        this.UPDATE_LOT_SQL = queryProperties.getProperty("UPDATE_LOT_SQL");
        this.MAKE_BID_SQL = queryProperties.getProperty("MAKE_BID_SQL");
    }

    public List<Lot> getAll(LotFilter lotFilter) {
        logger.info("starting query getAllLots to db ...");
        long start = currentTimeMillis();

        StringBuilder query = generateQuery(lotFilter);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query.toString())) {
            List<Lot> lots = new ArrayList<>();
            while (resultSet.next()) {
                lots.add(LOT_ROW_MAPPER.mapRow(resultSet));
            }
            logger.info("query getAllLots from db finished. {} lots returned. it took {} ms", lots.size(), currentTimeMillis() - start);
            return lots;
        } catch (SQLException e) {
            logger.error("an error {} occurred during getAllLots from db", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getPageCount(LotFilter lotFilter) {
        logger.info("starting query getLotsCount to db ...");
        long start = currentTimeMillis();
        StringBuilder query = new StringBuilder(GET_LOTS_COUNT);
        if (lotFilter.getLotStatus() != null) {
            query = where(query, "status", lotFilter.getLotStatus().getId(), true);
        }
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query.toString())) {
            resultSet.next();
            int lotCount = resultSet.getInt("rowcount");
            logger.info("starting query getLotsCount to finished. count {}. It  took {} ms", lotCount, currentTimeMillis() - start);
            return (int) Math.ceil(lotCount * 1.0 / lotFilter.getLotPerPage());
        } catch (SQLException e) {
            logger.error("an error {} occurred during getLotsCount", e);
            throw new RuntimeException(e);
        }
    }


    private StringBuilder generateQuery(LotFilter lotFilter) {
        StringBuilder query = new StringBuilder(GET_ALL_LOTS_SQL);
        if (lotFilter.getLotStatus() != null) {
            query = where(query, "status", lotFilter.getLotStatus().getId(), true);
        }
        int offset = (lotFilter.getPage() - 1) * lotFilter.getLotPerPage();
        query = QueryGenerator.limitOffset(query, lotFilter.getLotPerPage(), offset);
        return query;
    }

    @Override
    public Lot get(int id) {
        logger.info("starting query getLot to db ...");
        long start = currentTimeMillis();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_LOTS_BY_ID_SQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Lot lot = LOT_ROW_MAPPER.mapRow(resultSet);
            logger.info("query getLot from db finished. lot {}. It  took {} ms", lot, currentTimeMillis() - start);
            return lot;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean makeBid(int lotId, int price, int userId) {
        logger.info("starting query makeBid to db ...");
        long start = currentTimeMillis();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(MAKE_BID_SQL)) {
            statement.setInt(1, (int) (price * 1.1));
            statement.setInt(2, userId);
            statement.setInt(3, lotId);
            statement.setInt(4, price);
            int result = statement.executeUpdate();
            logger.info("query makeBid from db finished. lotId {}. It  took {} ms", lotId, currentTimeMillis() - start);
            return result == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Lot lot) {
        logger.info("starting query updateLot to db ...");
        long start = currentTimeMillis();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_LOT_SQL)) {
            statement.setInt(1, lot.getCurrentPrice());
            statement.setString(2, lot.getStatus().getId());
            statement.setInt(3, lot.getId());
            int result = statement.executeUpdate();
            logger.info("query updateLot from db finished. lotId {}. It  took {} ms", lot.getId(), currentTimeMillis() - start);
            return result == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
