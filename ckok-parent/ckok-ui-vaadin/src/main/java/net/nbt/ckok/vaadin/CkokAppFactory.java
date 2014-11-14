package net.nbt.ckok.vaadin;

import net.nbt.ckok.service.CkokService;

import com.jjinterna.vaadin.vaadinbridge.ApplicationFactory;
import com.vaadin.Application;
import com.vaadin.Application.SystemMessages;

public class CkokAppFactory implements ApplicationFactory {
    
    private final CkokService service;
    private final String title;

    public CkokAppFactory(CkokService service, String title) {
        this.service = service;
        this.title = title;
    }
    
    public String getApplicationCSSClassName() {
        return "CKOKApplication";
    }

    public SystemMessages getSystemMessages() {
        return null;
    }

    public Application newInstance() {
        return new CkokApplication(service, title);
    }
}