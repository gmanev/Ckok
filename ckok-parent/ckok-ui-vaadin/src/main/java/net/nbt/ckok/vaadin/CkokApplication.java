package net.nbt.ckok.vaadin;

import java.util.Locale;
import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("valo")
public class CkokApplication extends UI {

	private static final String PRODUCTVIEW = "product";
	
	private Locale locale;
	private ResourceBundle messages;
	private String title;
	private CkokService service;
	private Navigator navigator;
	
	public CkokApplication(CkokService service, String title) {
		this.service = service;
		this.title = title;
	}

	@Override
	public void init(VaadinRequest request) {
		getPage().setTitle(title);

		locale = new Locale("bg", "BG");
		messages = ResourceBundle.getBundle("Messages", locale);

		navigator = new Navigator(this, this);
		navigator.addView(PRODUCTVIEW, new ProductView(service, messages));
		
		navigator.navigateTo(PRODUCTVIEW);
	}

}
