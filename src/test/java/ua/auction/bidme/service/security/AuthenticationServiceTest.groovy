package ua.auction.bidme.service.security

import org.junit.Test
import org.mindrot.jbcrypt.BCrypt
import ua.auction.bidme.entity.User
import ua.auction.bidme.service.UserService

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class AuthenticationServiceTest {

    @Test
    void testAuthentication() {
        //prepare

        UserService userService = mock(UserService.class)
        LoggedUserStorage storage = new LoggedUserStorage()

        User user = generateUser()
        AuthenticationService service = new AuthenticationService(userService, storage)

        when(userService.get('email')).thenReturn(user)
        //when
        boolean result = service.tryAuthenticate('email', 'password', 'sessionId')
        //then
        assertEquals(true, result);
        assertNotNull(storage.getLoggedUser('sessionId'))

    }

    private static User generateUser() {
        new User.Builder('email')
                .password('$2a$10$uPEUn8tOOVV1249OvCbTx.AEeNQcbStpV3vLz95MKs/lntMNVgiX2')
                .id(1)
                .build()
    }
}
