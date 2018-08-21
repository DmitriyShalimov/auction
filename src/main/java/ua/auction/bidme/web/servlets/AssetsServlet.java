package ua.auction.bidme.web.servlets;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.slf4j.LoggerFactory.getLogger;

public class AssetsServlet extends HttpServlet {
    private static final int PATH_START_POSITION = 1;
    private final Logger logger = getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getRequestURI();
        logger.info("reading resource {}", fileName);
        try (InputStream resourceAsStream = getStreamResource(fileName)) {
            String mimeType = req.getServletContext().getMimeType(fileName);
            if (mimeType == null) {
                resp.setStatus(SC_NOT_FOUND);
                logger.warn("cannot define mime type of resource {}. resource wasn't loaded" + fileName);
                return;
            }
            byte[] buffer = new byte[8192];
            int count;
            while ((count = resourceAsStream.read(buffer)) != -1) {
                resp.setContentType(mimeType);
                resp.getOutputStream().write(buffer, 0, count);
            }
        }
    }

    private InputStream getStreamResource(String fileName) {
        return getClass()
                .getClassLoader()
                .getResourceAsStream(fileName.substring(PATH_START_POSITION));
    }
}
