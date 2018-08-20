package ua.auction.bidme.web.servlets;

import org.thymeleaf.context.WebContext;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.entity.LotStatus;
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

public class LotServlet extends HttpServlet {
    private final LotService lotService;
    private final LoggedUserStorage storage;

    public LotServlet(LotService lotService, LoggedUserStorage storage) {
        this.storage = storage;
        this.lotService = lotService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        Map<String, Object> pageVariables = new HashMap<>();
        User user = storage.getLoggedUser(request.getSession().getId());
        String id = request.getParameter("id");
        Lot lot = lotService.get(Integer.parseInt(id));
        pageVariables.put("user", user);
        pageVariables.put("lot", lot);
        if (lot.getStatus().equals(LotStatus.ACTIVE)) {
            pageVariables.put("active", true);
        }
        context.setVariables(pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage(context, "lot"));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        Map<String, Object> pageVariables = new HashMap<>();
        Lot tempLot = new Lot();
        int id = Integer.parseInt(request.getParameter("id"));
        int price = Integer.parseInt(request.getParameter("price"));
        tempLot.setId(id);
        User user = storage.getLoggedUser(request.getSession().getId());
        boolean result = lotService.makeBid(id, price, user);
        if (result) {
            pageVariables.put("result", "Your bid is high");
        } else {
            pageVariables.put("result", "Your bid did not pass");
        }
        Lot lot = lotService.get(id);
        pageVariables.put("user", user);
        pageVariables.put("lot", lot);
        context.setVariables(pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage(context, "lot"));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}