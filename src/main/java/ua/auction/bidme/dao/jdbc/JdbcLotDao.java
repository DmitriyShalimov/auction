package ua.auction.bidme.dao.jdbc;

import org.slf4j.Logger;
import ua.auction.bidme.dao.LotDao;
import ua.auction.bidme.dao.jdbc.mapper.implementation.LotRowMapper;
import ua.auction.bidme.entity.Lot;

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
    private static final String GET_ALL_LOTS_SQL = "SELECT name, description, start_price, current_price, start_time, end_time,status, picture_link FROM \"Lots\" ";
    private final DataSource dataSource;
    private final Logger logger = getLogger(JdbcLotDao.class);

    public JdbcLotDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Lot> getAll() {
        logger.info("starting query getAllLots to db ...");
        long start = currentTimeMillis();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_LOTS_SQL)) {
            List<Lot> lots = new ArrayList<>();
            while (resultSet.next()) {
                lots.add(LOT_ROW_MAPPER.mapRow(resultSet));
            }
            logger.info("query getAllLots from db finished. {} lots returnedit took {} ms",
                    lots.size(), currentTimeMillis() - start);
            return lots;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
