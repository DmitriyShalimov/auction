package ua.auction.bidme.dao.jdbc;

import org.slf4j.Logger;
import ua.auction.bidme.dao.MessageDao;
import ua.auction.bidme.dao.jdbc.mapper.implementation.MessageMapper;
import ua.auction.bidme.entity.Message;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

public class JdbcMessageDao implements MessageDao {
    private final Logger logger = getLogger(getClass());
    private final MessageMapper MESSAGE_MAPPER = new MessageMapper();
    private final DataSource dataSource;
    private final String GET_MESSAGES_BY_USER_ID_SQL;
    private final String ADD_NEW_MESSAGE_SQL;

    public JdbcMessageDao(DataSource dataSource, Properties queryProperties) {
        this.dataSource = dataSource;
        this.GET_MESSAGES_BY_USER_ID_SQL = queryProperties.getProperty("GET_MESSAGES_BY_USER_ID_SQL");
        this.ADD_NEW_MESSAGE_SQL = queryProperties.getProperty("ADD_NEW_MESSAGE_SQL");
    }

    @Override
    public List<Message> getAll(int userId) {
        logger.info("starting query getMessagesByUserId {} started ...");
        List<Message> messages = new ArrayList<>();
        long start = currentTimeMillis();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MESSAGES_BY_USER_ID_SQL)) {
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

    @Override
    public void add(Message message) {
        logger.info("Start of the new product's upload to the database");
        long start = currentTimeMillis();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_MESSAGE_SQL)) {
            preparedStatement.setString(1, message.getText());
            preparedStatement.setString(2, message.getIndicator().getId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(message.getDateTime()));
            preparedStatement.setInt(4, message.getLotId());
            preparedStatement.setInt(5, message.getUserId());
            preparedStatement.executeUpdate();
            logger.info("query addNewMessage to db finished.it took {} ms", currentTimeMillis() - start);
        } catch (SQLException e) {
            logger.error("an error {} occurred during addNewMessage to db", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
