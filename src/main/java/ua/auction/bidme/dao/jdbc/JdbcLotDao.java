package ua.auction.bidme.dao.jdbc;

import org.slf4j.Logger;
import ua.auction.bidme.dao.LotDao;
import ua.auction.bidme.dao.jdbc.mapper.implementation.LotRowMapper;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.filter.LotFilter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

public class JdbcLotDao implements LotDao {
    private static final LotRowMapper LOT_ROW_MAPPER = new LotRowMapper();
    private static final String GET_ALL_LOTS_SQL = "SELECT name, description, start_price, current_price, start_time, end_time,status, picture_link FROM \"auction.lot\" ";

    private final DataSource dataSource;
    private final Logger logger = getLogger(getClass());

    private static final String GET_LOTS_COUNT = "SELECT COUNT(*) AS rowcount FROM \"auction.lot\"";


    public JdbcLotDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public List<Lot> getAll(LotFilter lotFilter) {
        logger.info("starting query getAllLots to db ...");
        long start = currentTimeMillis();
        StringBuilder query = new StringBuilder();
        query.append(GET_ALL_LOTS_SQL);
        if (lotFilter.getLotStatus() != null) {
            query.append("WHERE status ='").append(lotFilter.getLotStatus().getId()).append("'");
        }
        query.append("limit ").append(lotFilter.getLotPerPage()).append("offset ").append((lotFilter.getPage() - 1) * lotFilter.getLotPerPage());

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
        StringBuilder query = new StringBuilder();
        query.append(GET_LOTS_COUNT);
        if (lotFilter.getLotStatus() != null) {
            query.append("WHERE status ='").append(lotFilter.getLotStatus().getId()).append("'");
        }
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query.toString())) {
            resultSet.next();
            int lotCount = resultSet.getInt("rowcount");
            return (int) Math.ceil(lotCount * 1.0 / lotFilter.getLotPerPage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
