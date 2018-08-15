package ua.auction.bidme.dao.jdbc;

import org.slf4j.Logger;
import ua.auction.bidme.dao.MessageDao;
import ua.auction.bidme.dao.jdbc.mapper.implementation.MessageMapper;
import ua.auction.bidme.entity.Message;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

public class JdbcMessageDao implements MessageDao {

    private final Logger logger = getLogger(getClass());
    private final MessageMapper MESSAGE_MAPPER = new MessageMapper();
    private final String GET_MESSAGE_SQL = "SELECT m.id, m.text, m.status, m.date, l.id, l.name" +
            "FROM auction.message as m " +
            "INNER JOIN auction.lot as l on (m.lotId = l.id)  WHERE m.userId = ?";
    private final DataSource dataSource;

    public JdbcMessageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Message> getAll(int userId) {
        logger.info("starting query getMessagesByUserId {} started ...");
        List<Message> messages = new ArrayList<>();
        long start = currentTimeMillis();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MESSAGE_SQL)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                messages.add(MESSAGE_MAPPER.mapRow(resultSet));
            }

        } catch (SQLException e) {
            logger.error("an error {} occurred during getMessagesByUserId {} from db", e.getMessage(), userId);
            throw new RuntimeException(e);
        }

        logger.info("query getMessagesByUserId {} to db finished. message size is {} .it took {} ms",
                userId, messages.size(), currentTimeMillis() - start);
        return messages;
    }
}
