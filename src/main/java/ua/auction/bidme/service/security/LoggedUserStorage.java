package ua.auction.bidme.service.security;

import org.slf4j.Logger;
import ua.auction.bidme.entity.User;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class LoggedUserStorage {
    private final Logger logger = getLogger(getClass());

    private Map<String, User> storage;

    public LoggedUserStorage() {
        storage = new HashMap<>();
    }

    public boolean log(String sessionId, User user) {
        logger.info("user with email {} logged and stored into cache", user.getEmail());
        return storage.put(sessionId, user) != null;
    }

    public User getLoggedUser(String sessionId) {
        return storage.get(sessionId);
    }

}
