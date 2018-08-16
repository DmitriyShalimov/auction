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

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.auction.bidme.dao.util.QueryGenerator.where;

public class JdbcLotDao implements LotDao {
    private static final LotRowMapper LOT_ROW_MAPPER = new LotRowMapper();

    private static final String GET_ALL_LOTS_SQL = "SELECT id, name, description, start_price, current_price, start_time," +
            "end_time,status, picture_link FROM auction.lot ";
    private static final String GET_LOTS_COUNT = "SELECT COUNT(*) AS rowcount FROM auction.lot ";
    private static final String GET_LOTS_BY_ID_SQL = "SELECT id,name, description, start_price, current_price, start_time, end_time,status, picture_link FROM auction.lot WHERE id = ?";
    private static final String UPDATE_LOT_SQL = "UPDATE  auction.lot SET current_price=?, user_id=? WHERE id=? AND current_price=?";

    private final DataSource dataSource;
    private final Logger logger = getLogger(getClass());

    public JdbcLotDao(DataSource dataSource) {
        this.dataSource = dataSource;
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
            logger.info("query getAllLots from db finished. {} lots returned. it took {} ms",
                    lots.size(), currentTimeMillis() - start);
            return lots;
        } catch (SQLException e) {
            logger.error("an error {} occurred during getAllLots from db", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getPageCount(LotFilter lotFilter) {
        logger.info("starting query getLotsCount to db ...");

        StringBuilder query = new StringBuilder(GET_LOTS_COUNT);
        if (lotFilter.getLotStatus() != null) {
            query = where(query, "status", lotFilter.getLotStatus().getId(), true);
        }
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query.toString())) {
            resultSet.next();
            int lotCount = resultSet.getInt("rowcount");
            logger.info("starting query getLotsCount to finished. count {}", lotCount);
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
            logger.info("query getLot from db finished. lot {}. It  took {} ms",
                    lot, currentTimeMillis() - start);
            return lot;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean makeBid(int lotId, int price, int userId) {
        logger.info("starting query updateLot to db ...");
        long start = currentTimeMillis();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_LOT_SQL)) {
            statement.setInt(1, (int) (price * 1.1));
            statement.setInt(2, userId);
            statement.setInt(3, lotId);
            statement.setInt(4, price);
            int result = statement.executeUpdate();
            logger.info("query updateLot from db finished. lotId {}. It  took {} ms", lotId,
                    currentTimeMillis() - start);
            return result == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
