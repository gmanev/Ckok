package net.nbt.ckok.service.impl;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.GetProductById;
import net.nbt.ckok.service.NoSuchProductException;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CkokServiceImplTest {
    
    private static final String CKOKSERVICE_TESTURL = "http://localhost:8282/ckokService";
    private static Server server;

    @BeforeClass
    public static void startServer() {
        CkokServiceImpl ckokService = new CkokServiceImpl();
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setAddress(CKOKSERVICE_TESTURL);
        factory.setServiceBean(ckokService);
        server = factory.create();
        server.start();
    }
    
    @Test
    public void testPutProduct() throws NoSuchProductException {
        CkokService service = getService();
        
        GetProductById request = new GetProductById();
        request.setProductId(1);
       // service.getProductById(request);
//        personService.addPerson(createPerson());
//        Product person2 = personService.getProductById(new GetProductById());
//        assertCorrectPerson(person2);
    }
    
    @AfterClass
    public static void stopServer() {
        server.stop();
    }

    private CkokService getService() {
        JaxWsProxyFactoryBean proxy = new JaxWsProxyFactoryBean();
        proxy.setServiceClass(CkokService.class);
        proxy.setAddress(CKOKSERVICE_TESTURL);
        CkokService personService = (CkokService)proxy.create();
        return personService;
    }

    private Product createProduct() {
    	Product person = new Product();
        person.setId(1001);
        person.setSerial("OKS");
        return person;
    }

    private void assertCorrectPerson(Product product) {
        Assert.assertNotNull(product);
        Assert.assertEquals("OKS", product.getSerial());
    }
    
}
