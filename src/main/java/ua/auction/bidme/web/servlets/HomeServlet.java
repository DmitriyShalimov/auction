package ua.auction.bidme.web.servlets;

import org.slf4j.Logger;
import org.thymeleaf.context.WebContext;
import ua.auction.bidme.entity.Lot;
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
import java.util.Optional;

import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;
import static ua.auction.bidme.entity.LotStatus.getTypeById;

public class HomeServlet extends HttpServlet {
    private static final int LOTS_PER_PAGE = 6;
    private static final int FIRST_PAGE = 1;
    private final Logger logger = getLogger(getClass());
    private final LoggedUserStorage storage;
    private final LotService lotService;

    public HomeServlet(LotService lotService, LoggedUserStorage storage) {
        this.storage = storage;
        this.lotService = lotService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long start = currentTimeMillis();
        logger.info("starting request home page to get all lots");
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
        int page = Optional.ofNullable(request.getParameter("page")).map(Integer::parseInt).orElse(FIRST_PAGE);
        LotFilter lotFilter = new LotFilter();
        lotFilter.setPage(page);
        Map<String, Object> pageVariables = new HashMap<>();
        Optional.ofNullable(request.getParameter("status")).ifPresent(s -> {
            lotFilter.setLotStatus(getTypeById(s));
            pageVariables.put("active", true);
        });
        lotFilter.setLotPerPage(LOTS_PER_PAGE);
        List<Lot> list = lotService.getAll(lotFilter);
        context.setVariables(fillPageVariables(request, pageVariables, page, list, lotService.getPageCount(lotFilter)));
        setResponse(response, context);
        logger.info("logs has been received. amount {}. it took {} ms", list.size(), currentTimeMillis() - start);
    }

    private void setResponse(HttpServletResponse response, WebContext context) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage(context, "home"));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private Map<String, Object> fillPageVariables(HttpServletRequest request, Map<String, Object> pageVariables, int page, List<Lot> list, int pageCount) {
        User user = storage.getLoggedUser(request.getSession().getId());
        pageVariables.put("user", user);
        pageVariables.put("lots", list);
        pageVariables.put("currentPage", page);
        pageVariables.put("pageCount", pageCount);
        return pageVariables;
    }
}
