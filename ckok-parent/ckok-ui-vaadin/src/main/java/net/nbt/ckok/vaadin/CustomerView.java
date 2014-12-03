package net.nbt.ckok.vaadin;

import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class CustomerView extends VerticalLayout implements View {

	private final ResourceBundle messages;
	private final CkokService service;

	public CustomerView(CkokService service, ResourceBundle messages) {
		this.service = service;
		this.messages = messages;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
