package net.nbt.ckok.vaadin;

import net.nbt.ckok.model.CkokDAOService;

import org.example.utils.vaadinbridge.ApplicationFactory;

import com.vaadin.Application;
import com.vaadin.Application.SystemMessages;

public class CkokAppFactory implements ApplicationFactory {
    
    private final CkokDAOService daoService;
    private final String title;

    public CkokAppFactory(CkokDAOService daoService, String title) {
        this.daoService = daoService;
        this.title = title;
    }
    
    public String getApplicationCSSClassName() {
        return "CKOKApplication";
    }

    public SystemMessages getSystemMessages() {
        return null;
    }

    public Application newInstance() {
        return new CkokApplication(daoService, title);
    }
}