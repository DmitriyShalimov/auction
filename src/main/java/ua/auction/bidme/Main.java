package ua.auction.bidme;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import ua.auction.bidme.dao.LotDao;
import ua.auction.bidme.dao.UserDao;
import ua.auction.bidme.dao.jdbc.JdbcLotDao;
import ua.auction.bidme.dao.jdbc.JdbcUserDao;
import ua.auction.bidme.service.LotService;
import ua.auction.bidme.service.UserService;
import ua.auction.bidme.service.impl.DefaultLotService;
import ua.auction.bidme.service.impl.DefaultUserService;
import ua.auction.bidme.service.security.AuthenticationService;
import ua.auction.bidme.service.security.LoggedUserStorage;
import ua.auction.bidme.util.PropertyReader;
import ua.auction.bidme.web.servlets.LoginServlet;
import ua.auction.bidme.web.servlets.LotServlet;

import javax.sql.DataSource;
import java.util.Properties;

import static java.lang.Integer.valueOf;
import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;
import static org.slf4j.LoggerFactory.getLogger;


public class Main {
    private static Logger logger = getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("initializing project started ... ");
        DataSource dataSource = getDataSource();

        //dao
        LotDao lotDao = new JdbcLotDao(dataSource);
        UserDao userDao = new JdbcUserDao(dataSource);

        //services
        LotService lotService = new DefaultLotService(lotDao);

        UserService userService = new DefaultUserService(userDao);
        LoggedUserStorage storage = new LoggedUserStorage();
        AuthenticationService authenticationService = new AuthenticationService(userService, storage);

        //register servlets
        ServletContextHandler context = new ServletContextHandler(SESSIONS);

        //servlets
        context.addServlet(new ServletHolder(getLotServlet(lotService)), "/auction");
        context.addServlet(new ServletHolder(getLoginServlet(authenticationService)), "/login");

        //server config
        startServer(context);
    }

    private static LoginServlet getLoginServlet(AuthenticationService authenticationService) {
        return new LoginServlet(authenticationService);
    }

    private static HikariDataSource getDataSource() {
        logger.info("initializing datasource ... ");
        Properties properties =
                new PropertyReader("properties/database-connection.properties")
                        .readProperties();
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty("driver"));
        config.setJdbcUrl(properties.getProperty("url"));
        config.setUsername(properties.getProperty("user"));
        config.setPassword(properties.getProperty("password"));
        config.setMinimumIdle(valueOf(properties.getProperty("minIdle")));
        config.setMaximumPoolSize(valueOf(properties.getProperty("maxActive")));
        return new HikariDataSource(config);
    }

    private static LotServlet getLotServlet(LotService lotService) {
        LotServlet lotServlet = new LotServlet();
        lotServlet.setLotService(lotService);
        return lotServlet;
    }

    private static void startServer(ServletContextHandler context) throws Exception {
        logger.info("starting server ...");
        Server server = new Server(8080);
        server.setHandler(context);
        server.start();
    }
}
