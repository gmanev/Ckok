package net.nbt.ckok.vaadin.view;

import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class SupplyView extends VerticalLayout implements View {

	private final CkokService service;
	private final ResourceBundle bundle;
	
	public SupplyView(CkokService service, ResourceBundle bundle) {
		this.service = service;
		this.bundle = bundle;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
