package ua.auction.bidme.web.templater;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.thymeleaf.templatemode.TemplateMode.HTML;

public class PageGenerator {
    private static PageGenerator pageGenerator;
    private final TemplateEngine templateEngine;

    private PageGenerator() {
        templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(HTML);
        templateEngine.addDialect(new Java8TimeDialect());
        templateResolver.setPrefix("/webapp/");
        templateResolver.setSuffix(".html");
        templateEngine.setTemplateResolver(templateResolver);
    }

    public static PageGenerator instance() {
        if (pageGenerator == null) {
            pageGenerator = new PageGenerator();
        }
        return pageGenerator;
    }

    public String getPage(WebContext context, String name) {
        return templateEngine.process(name, context);
    }
}
