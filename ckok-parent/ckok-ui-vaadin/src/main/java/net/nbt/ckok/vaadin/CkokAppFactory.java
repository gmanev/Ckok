package net.nbt.ckok.vaadin;

import net.nbt.ckok.model.ProductDAO;

import com.jjinterna.vaadin.vaadinbridge.ApplicationFactory;
import com.vaadin.Application;
import com.vaadin.Application.SystemMessages;

public class CkokAppFactory implements ApplicationFactory {
    
    private final ProductDAO productDAO;
    private final String title;

    public CkokAppFactory(ProductDAO productDAO, String title) {
        this.productDAO = productDAO;
        this.title = title;
    }
    
    public String getApplicationCSSClassName() {
        return "CKOKApplication";
    }

    public SystemMessages getSystemMessages() {
        return null;
    }

    public Application newInstance() {
        return new CkokApplication(productDAO, title);
    }
}