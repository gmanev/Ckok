package net.nbt.ckok.dao.jpa.test;


import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import net.nbt.ckok.dao.jpa.impl.ProductDAOImpl;
import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.ProductType;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.Test;

public class PersonServiceImplTest {

    public final class MyInitialcontextFactoryBuilder implements InitialContextFactoryBuilder {
        @Override
        public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> environment) throws NamingException {
            return new MyInitialContextFactory();
        }
    }

    @Test
    public void testWriteRead() throws Exception {
        NamingManager.setInitialContextFactoryBuilder(new MyInitialcontextFactoryBuilder());
        InitialContext ic = new InitialContext();
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("target/test");
        ds.setCreateDatabase("create");
        ic.bind("osgi:service/javax.sql.DataSource/(osgi.jndi.service.name=jdbc/derbyds)", ds);
        ProductDAOImpl personService = new ProductDAOImpl();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ckokTest", System.getProperties());
        EntityManager em = emf.createEntityManager();
        personService.setEntityManager(em);
        em.getTransaction().begin();
        //personService.deleteAll();
        ProductType pt = new ProductType();

        pt.setMeasure("count");
        pt.setPartnum("NBT-001");
        pt.setName("NOMEN");
        Product p = new Product();
        p.setProductType(pt);
        personService.add(p);
        em.getTransaction().commit();
//        List<Person> persons = personService.getAll();
//        Assert.assertEquals(1, persons.size());
//        Assert.assertEquals("Christian Schneider", persons.get(0).getName());
//        Assert.assertEquals("@schneider_chris", persons.get(0).getTwitterName());
    }

}
