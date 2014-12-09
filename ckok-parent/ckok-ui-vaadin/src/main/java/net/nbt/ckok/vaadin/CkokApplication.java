package net.nbt.ckok.vaadin;

import java.util.Locale;
import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
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
	private static final String[][] menuItems = {
		{ "customer", null },
		{ "product", null },
		{ "store", null },
		{ "product/store", "store" },
		{ "store/nomen", "store" }
	};
	
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
		menu.setItemCaptionPropertyId("name");
		menu.getContainerDataSource().addContainerProperty("name", String.class, "");
		//menu.setStyleName("menu");
		menu.setSizeFull();
		
		for (String[] s : menuItems) {
			String itemId = s[0];
			String parentId = s[1];
			
			Item item = menu.addItem(itemId);
			menu.setChildrenAllowed(itemId, false);
			item.getItemProperty("name").setValue(messages.getString(itemId));
			if (parentId != null) {
				menu.setChildrenAllowed(parentId, true);
				menu.setParent(itemId, parentId);
				menu.expandItem(parentId);
			}
		}
		
		menu.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.getItemId() != null) {
					navigator.navigateTo(event.getItemId().toString());
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
		navigator.setErrorView(new Navigator.EmptyView());
		navigator.addView(CUSTOMERVIEW, new CustomerView(service, messages));
		navigator.addView(PRODUCTVIEW, new ProductView(service, messages));
		navigator.navigateTo(CUSTOMERVIEW);
	}
}
