package net.nbt.ckok.vaadin;

import java.util.Locale;
import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

@Theme("valo")
public class CkokApplication extends UI {

	private static final String PRODUCTVIEW = "product";
	private static final String CUSTOMERVIEW = "customer";
	
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
		
		if (getSession() != null) {
			getSession().setLocale(locale);
		}

		initLayout();
	}

	private void initLayout() {

        setSizeFull();
        
		Tree menu = new Tree();
		final String customerViewName = messages.getString(CUSTOMERVIEW);
		final String productViewName = messages.getString(PRODUCTVIEW);
		//menu.setStyleName("menu");
		menu.setSizeFull();
		menu.addItem(customerViewName);
		menu.addItem(productViewName);
		menu.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.getItemId() != null) {
					if (event.getItemId().equals(productViewName)) {
						navigator.navigateTo(PRODUCTVIEW);						
					}
					else if (event.getItemId().equals(customerViewName)) {
						navigator.navigateTo(CUSTOMERVIEW);						
					}
				}
			}
			
		});

		Panel panel = new Panel();
		panel.setSizeFull();

        HorizontalSplitPanel split = new HorizontalSplitPanel();
        split.setFirstComponent(menu);
        split.setSecondComponent(panel);
        split.setSplitPosition(15);
       
        
		setContent(split);
		
		navigator = new Navigator(this, panel);
		navigator.addView(CUSTOMERVIEW, new CustomerView(service, messages));
		navigator.addView(PRODUCTVIEW, new ProductView(service, messages));
		navigator.navigateTo(CUSTOMERVIEW);
	}
}
