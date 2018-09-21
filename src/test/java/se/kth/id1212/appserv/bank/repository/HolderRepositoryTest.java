package se.kth.id1212.appserv.bank.repository;

import net.jcip.annotations.NotThreadSafe;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import se.kth.id1212.appserv.bank.domain.Holder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"se.kth.id1212.appserv.bank"})
    //@SpringBootTest can be used instead of @SpringJUnitWebConfig,
    // @EnableAutoConfiguration and @ComponentScan, but are we using
    // JUnit5 in that case?
@NotThreadSafe
class HolderRepositoryTest {
    @Autowired
    private HolderRepository instance;
    private Holder holder;

    @BeforeAll
    static void enableCreatingEMFWhichIsNeededForTheApplicationContext()
        throws SQLException, IOException, ClassNotFoundException {
        DbUtil.emptyDb();
    }

    @AfterAll
    static void cleanup()
        throws SQLException, IOException, ClassNotFoundException {
        DbUtil.emptyDb();
    }

    @BeforeEach
    void setup() throws SQLException, IOException, ClassNotFoundException {
        holder = new Holder("holderName");
        DbUtil.emptyDb();
    }

    @Test
    void testCreateHolder() {
        instance.save(holder);
        List<Holder> holdersInDb = instance.findAll();
        assertThat(holdersInDb, containsInAnyOrder(holder));
    }

    @Test
    void testFindExistingHolderByHolderNo() {
        instance.save(holder);
        Holder holderInDb =
            instance.findHolderByHolderNo(holder.getHolderNo());
        assertThat(holderInDb.getHolderNo(), is(holder.getHolderNo()));
    }

    @Test
    void testFindNonExistingHolderByHolderNo() {
        instance.save(holder);
        Holder holderInDb =
            instance.findHolderByHolderNo(0);
        assertThat(holderInDb == null, is(true));
    }
}
