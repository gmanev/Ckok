package net.nbt.ckok.vaadin;

import java.util.Locale;
import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
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

		initLayout();
	}

	private void initLayout() {

        setSizeFull();
        
        // Layout with menu on left and view area on right
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setSizeFull();
        
		Tree menu = new Tree();
		menu.setStyleName("menu");
		menu.addItem(PRODUCTVIEW);

		Panel panel = new Panel();
		Label l = new Label("test");
		panel.setContent(l);
		panel.setSizeFull();

		hLayout.addComponent(menu);
		hLayout.addComponent(panel);
        hLayout.setExpandRatio(panel, 1);
        
		setContent(hLayout);
		
		navigator = new Navigator(this, panel);
		navigator.addView(PRODUCTVIEW, new ProductView(service, messages));
		navigator.navigateTo(PRODUCTVIEW);		
	}
}
