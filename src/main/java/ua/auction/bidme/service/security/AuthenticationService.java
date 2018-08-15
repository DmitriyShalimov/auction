package ua.auction.bidme.service.security;

import ua.auction.bidme.service.UserService;

import java.util.Optional;

import static org.mindrot.jbcrypt.BCrypt.checkpw;

public class AuthenticationService {

    private final UserService userService;
    private final LoggedUserStorage storage;

    public AuthenticationService(UserService userService, LoggedUserStorage storage) {
        this.userService = userService;
        this.storage = storage;
    }

    public boolean tryAuthenticate(String email, String password, String sessionId) {
        return Optional.ofNullable(userService.get(email))
                .filter(user -> checkpw(password, user.getPassword()))
                .map(user -> storage.log(sessionId, user))
                .isPresent();
    }
}
