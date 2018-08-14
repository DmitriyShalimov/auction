package ua.auction.bidme.web.servlets;

import org.thymeleaf.context.WebContext;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.service.LotService;
import ua.auction.bidme.web.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LotServlet extends HttpServlet {

    private LotService lotService;

    //todo [getAll] fix encoding
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        List<Lot> list = lotService.getAll();
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("lots", list);

        context.setVariables(pageVariables);
        response.getWriter().println(PageGenerator.instance().getPage(context, "home"));
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void setLotService(LotService lotService) {
        this.lotService = lotService;
    }
}
