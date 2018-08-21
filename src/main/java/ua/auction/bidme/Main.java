package ua.auction.bidme;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import ua.auction.bidme.dao.LotDao;
import ua.auction.bidme.dao.MessageDao;
import ua.auction.bidme.dao.UserDao;
import ua.auction.bidme.dao.jdbc.JdbcLotDao;
import ua.auction.bidme.dao.jdbc.JdbcMessageDao;
import ua.auction.bidme.dao.jdbc.JdbcUserDao;
import ua.auction.bidme.service.LotService;
import ua.auction.bidme.service.MessageService;
import ua.auction.bidme.service.UserService;
import ua.auction.bidme.service.impl.DefaultLotService;
import ua.auction.bidme.service.impl.DefaultMessageService;
import ua.auction.bidme.service.impl.DefaultUserService;
import ua.auction.bidme.service.listener.BidListener;
import ua.auction.bidme.service.security.AuthenticationService;
import ua.auction.bidme.service.security.LoggedUserStorage;
import ua.auction.bidme.util.PropertyReader;
import ua.auction.bidme.web.security.LogOutServlet;
import ua.auction.bidme.web.security.LoginServlet;
import ua.auction.bidme.web.security.SecurityFilter;
import ua.auction.bidme.web.servlets.AssetsServlet;
import ua.auction.bidme.web.servlets.HomeServlet;
import ua.auction.bidme.web.servlets.LotServlet;
import ua.auction.bidme.web.servlets.UserServlet;

import javax.servlet.DispatcherType;
import javax.sql.DataSource;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Properties;

import static java.lang.Integer.valueOf;
import static javax.servlet.DispatcherType.REQUEST;
import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;
import static org.slf4j.LoggerFactory.getLogger;

public class Main {
    private static Logger logger = getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("initializing project started ... ");
        DataSource dataSource = getDataSource();

        //dao
        Properties queryProperties = new PropertyReader("properties/query.properties").readProperties();
        LotDao lotDao = new JdbcLotDao(dataSource, queryProperties);
        UserDao userDao = new JdbcUserDao(dataSource, queryProperties);
        MessageDao messageDao = new JdbcMessageDao(dataSource, queryProperties);

        //services
        BidListener bidListener = new BidListener(messageDao);
        LotService lotService = new DefaultLotService(lotDao, bidListener);
        MessageService messageService = new DefaultMessageService(messageDao);
        UserService userService = new DefaultUserService(userDao, messageService);

        LoggedUserStorage storage = new LoggedUserStorage();
        AuthenticationService authenticationService = new AuthenticationService(userService, storage);

        //register servlets
        ServletContextHandler context = new ServletContextHandler(SESSIONS);

        //servlets
        registerServlets(lotService, storage, authenticationService, context, userService);

        //security
        addSecurityFilterToContext(storage, context);

        //server config
        startServer(context);
    }

    private static void addSecurityFilterToContext(LoggedUserStorage storage, ServletContextHandler context) {
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(REQUEST);
        SecurityFilter securityFilter = new SecurityFilter(storage);
        context.addFilter(new FilterHolder(securityFilter), "/lot", dispatcherTypes);
        context.addFilter(new FilterHolder(securityFilter), "/logout", dispatcherTypes);
        context.addFilter(new FilterHolder(securityFilter), "/user", dispatcherTypes);
    }

    private static void registerServlets(LotService lotService, LoggedUserStorage storage,
                                         AuthenticationService authenticationService,
                                         ServletContextHandler context, UserService userService) {
        context.addServlet(new ServletHolder(new HomeServlet(lotService, storage)), "/");
        context.addServlet(new ServletHolder(new LotServlet(lotService, storage)), "/lot");
        context.addServlet(new ServletHolder(new LoginServlet(authenticationService, storage)), "/login");
        context.addServlet(new ServletHolder(new LogOutServlet(storage)), "/logout");
        context.addServlet(new ServletHolder(new UserServlet(userService)), "/user");
        context.addServlet(new ServletHolder(new AssetsServlet()), "/webapp/assets/*");
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
        config.addDataSourceProperty("sslmode", properties.getProperty("sslmode"));

        return new HikariDataSource(config);
    }

    private static void startServer(ServletContextHandler context) throws Exception {
        logger.info("starting server ...");
        int port = Optional.ofNullable(System.getenv("PORT")).map(Integer::valueOf).orElse(8080);
        Server server = new Server(port);
        server.setHandler(context);
        server.start();
        logger.info("server started successful");
    }
}
