package se.kth.id1212.appserv.bank.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * This is an ugly hack that is needed because spring can not inject beans into
 * JPA entities, since those are created by JPA and the spring DI container is
 * unaware of them.
 */
@Service
class BeanFactory implements ApplicationContextAware {
    private static ApplicationContext context;

    /**
     * Retrieves a bean of the specified class from the application context.
     * This method is intended for use only by JPA entities, other classes
     * should use @Autowired instead.
     *
     * @param beanClass The type of the bean to retrieve.
     */
    static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }
}