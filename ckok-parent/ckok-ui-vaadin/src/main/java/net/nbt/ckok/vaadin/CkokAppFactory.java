package net.nbt.ckok.vaadin;

import net.nbt.ckok.service.CkokService;

import com.jjinterna.vaadin.vaadinbridge.ApplicationFactory;
import com.vaadin.ui.UI;

public class CkokAppFactory implements ApplicationFactory {
    
    private final CkokService service;
    private final String title;

    public CkokAppFactory(CkokService service, String title) {
        this.service = service;
        this.title = title;
    }
    
	@Override
	public Class<? extends UI> getUIClass() {
		return CkokApplication.class;
	}

	@Override
	public UI getInstance() {
		return new CkokApplication(service, title);
	}
}