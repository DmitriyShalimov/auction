package ua.auction.bidme.web.servlets;

import org.thymeleaf.context.WebContext;
import ua.auction.bidme.entity.Lot;
import ua.auction.bidme.entity.LotStatus;
import ua.auction.bidme.entity.User;
import ua.auction.bidme.filter.LotFilter;
import ua.auction.bidme.service.LotService;
import ua.auction.bidme.service.security.LoggedUserStorage;
import ua.auction.bidme.web.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeServlet extends HttpServlet {
    private final LoggedUserStorage storage;
    private LotService lotService;

    public HomeServlet(LotService lotService, LoggedUserStorage storage) {
        this.storage = storage;
        this.lotService = lotService;
    }

    //todo [getAll] fix encoding
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        Map<String, Object> pageVariables = new HashMap<>();
        LotFilter lotFilter = new LotFilter();
        String status = request.getParameter("status");
        String parameterPage = request.getParameter("page");
        User user = storage.getLoggedUser(request.getSession().getId());
        int page;
        if (parameterPage == null) {
            page = 1;
        } else {
            page = Integer.parseInt(parameterPage);
        }
        if (status != null) {
            lotFilter.setLotStatus(LotStatus.getTypeById(status));
            pageVariables.put("active", true);
        }
        lotFilter.setPage(page);
        lotFilter.setLotPerPage(6);
        List<Lot> list = lotService.getAll(lotFilter);
        int pageCount = lotService.getPageCount(lotFilter);
        pageVariables.put("user", user);
        pageVariables.put("lots", list);
        pageVariables.put("CurrentPage", page);
        pageVariables.put("pageCount", pageCount);

        context.setVariables(pageVariables);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage(context, "home"));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
