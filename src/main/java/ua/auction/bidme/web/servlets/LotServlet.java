package ua.auction.bidme.web.servlets;

import org.slf4j.Logger;
import org.thymeleaf.context.WebContext;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.entity.User;
import ua.auction.bidme.service.LotService;
import ua.auction.bidme.service.security.LoggedUserStorage;
import ua.auction.bidme.web.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.auction.bidme.entity.LotStatus.ACTIVE;

public class LotServlet extends HttpServlet {
    private static final String SUCCESS = "Your bid is high";
    private static final String FAIL = "Your bid did not pass";
    private final LotService lotService;
    private final LoggedUserStorage storage;
    private final Logger logger = getLogger(getClass());

    public LotServlet(LotService lotService, LoggedUserStorage storage) {
        this.storage = storage;
        this.lotService = lotService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long start = currentTimeMillis();
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        String id = request.getParameter("id");

        logger.info("start getting lot with id {} ", id);
        Lot lot = lotService.get(Integer.parseInt(id));

        context.setVariables(fillPageVariables(request.getSession().getId(), lot));
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage(context, "lot"));
        response.setStatus(HttpServletResponse.SC_OK);
        logger.info("lot id: {} has been received. it took {}ms", id, currentTimeMillis() - start);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long start = currentTimeMillis();
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        int id = Integer.parseInt(request.getParameter("id"));
        logger.info("start making bid for lo id: {}", id);

        int price = Integer.parseInt(request.getParameter("price"));
        Map<String, Object> pageVariables = fillBidPageVariableInfo(request, id, price);

        context.setVariables(pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage(context, "lot"));
        response.setStatus(HttpServletResponse.SC_OK);

        logger.info("bit for lot finished. result {}. it took {} ms",
                id, pageVariables.get("result"), currentTimeMillis() - start);
    }

    private Map<String, Object> fillBidPageVariableInfo(HttpServletRequest request, int id, int price) {
        Map<String, Object> pageVariables = new HashMap<>();
        User user = storage.getLoggedUser(request.getSession().getId());
        pageVariables.put("result", lotService.makeBid(id, price, user) ? SUCCESS : FAIL);
        Lot lot = lotService.get(id);
        pageVariables.put("user", user);
        pageVariables.put("lot", lot);
        return pageVariables;
    }

    private Map<String, Object> fillPageVariables(String sessionId, Lot lot) {
        Map<String, Object> pageVariables = new HashMap<>();
        User user = storage.getLoggedUser(sessionId);
        pageVariables.put("user", user);
        pageVariables.put("lot", lot);
        if (lot.getStatus().equals(ACTIVE)) {
            pageVariables.put("active", true);
        }
        return pageVariables;
    }
}