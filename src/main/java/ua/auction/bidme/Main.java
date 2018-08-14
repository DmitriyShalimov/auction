package ua.auction.bidme;

import ua.auction.bidme.dao.DatabaseConnector;
import ua.auction.bidme.dao.LotDao;
import ua.auction.bidme.dao.jdbc.JdbcLotDao;
import ua.auction.bidme.service.LotService;
import ua.auction.bidme.service.impl.DefaultLotService;

import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ua.auction.bidme.web.servlets.*;

import javax.sql.DataSource;


public class Main {

    public static void main(String[] args) throws Exception {

        //register servlets
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        //dao
        DataSource dataSource = DatabaseConnector.getInstance().getDataSource();

        LotDao lotDao=new JdbcLotDao();
        ((JdbcLotDao) lotDao).setDataSource(dataSource);

        //services
        LotService lotService =new DefaultLotService();
        ((DefaultLotService) lotService).setLotDao(lotDao);

        //servlets
        context.addServlet(new ServletHolder(getLotServlet(lotService)), "/auction");

        //server config
        startServer(context);
    }

    private static LotServlet getLotServlet(LotService lotService) {
        LotServlet lotServlet = new LotServlet();
        lotServlet.setLotService(lotService);
        return lotServlet;
    }

    private static void startServer(ServletContextHandler context) throws Exception {
        Server server = new Server(8080);
        server.setHandler(context);
        server.start();
    }
}
