package ua.auction.bidme.dao.jdbc;

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

public class JdbcLotDao implements LotDao {
    private static final LotRowMapper LOT_ROW_MAPPER = new LotRowMapper();
    private static final String GET_ALL_LOTS_SQL = "SELECT name, description, start_price, current_price, start_time, end_time,status, picture_link FROM \"Lots\" ";
    private final DataSource dataSource;

    public JdbcLotDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Lot> getAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_LOTS_SQL)) {
            List<Lot> lots = new ArrayList<>();
            while (resultSet.next()) {
                lots.add(LOT_ROW_MAPPER.mapRow(resultSet));
            }
            return lots;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
