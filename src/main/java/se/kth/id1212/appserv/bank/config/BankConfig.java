package se.kth.id1212.appserv.bank.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Loads all configuration for the entire bank web app. Note that there are
 * config settings also in the file <code>application.properties</code>.
 */
@EnableWebMvc
@Configuration
public class BankConfig
        implements WebMvcConfigurer, ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * @param applicationContext The application context used by the running
     *                           application.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Create a bean with all server related properties from
     * application.properties.
     */
    @Bean
    public ServerProperties serverProperties() {
        return new ServerProperties();
    }

    /**
     * Create a <code>org.springframework.web.servlet .ViewResolver</code> bean
     * that delegates all views to thymeleaf's template engine. There is no need
     * to specify view name patterns since the will be the only existing view
     * resolver.
     */
    @Bean

    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }

    /**
     * Create a <code>org.thymeleaf.ITemplateEngine</code> bean that manages
     * thymeleaf template integration with Spring. All template resolution will
     * be delegated to the specified template resolver.
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
        // speed up execution in most scenarios, but might be incompatible
        // with specific cases when expressions in one template are reused
        // across different data types, so this flag is "false" by default
        // for safer backwards compatibility.
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    /**
     * Create a <code>org.thymeleaf.templateresolver.ITemplateResolver</code>
     * that can handle thymeleaf template integration with Spring. This will be
     * the only existing template resolver.
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver =
                new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        // Templates file shall have the path /web-root/<template name>.html
        templateResolver.setPrefix("classpath:/web-root/");
        templateResolver.setSuffix(".html");
        // HTML is the default template mode, added here for the sake of
        // clarity.
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // Template cache is true by default. Set to false to automatically
        // update templates that have been modified.
        templateResolver.setCacheable(true);
        return templateResolver;
    }

    /**
     * Configuration of requests for static files.
     **/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        int cachePeriodForStaticFilesInSecs = 1;
        String rootDirForStaticFiles = "classpath:/web-root/";

        registry.addResourceHandler("/**")
                .addResourceLocations(rootDirForStaticFiles)
                .setCachePeriod(cachePeriodForStaticFilesInSecs)
                .resourceChain(true).addResolver(new PathResourceResolver());
    }
}
