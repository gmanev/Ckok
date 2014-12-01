package net.nbt.ckok.vaadin;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;

import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class ProductView extends VerticalLayout implements View {

	private TextField searchField = new TextField();
	private FormLayout editorLayout = new FormLayout();
	private FieldGroup editorFields = new FieldGroup();
	private Table productList = new Table();
	private LazyQueryContainer container;
	private final ResourceBundle messages;
	private final CkokService service;

	public ProductView(CkokService service, ResourceBundle messages) {
		this.service = service;
		this.messages = messages;
		initLayout();
		initProductList();
		initEditor();
		initSearch();
	}
	
	private static final Object[] tableFieldNames = new String[] {
		"productType.name", "productType.partnum",
		"serial", "supplier", "createdOn", "warranty", "notes"
	};

	private static final Object[] editorFieldNames = new String[] {
		"productType.name", "productType.partnum",
		"serial", "supplier",
		"createdOn", "warranty",
		"notes"
	};
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	public void initLayout() {
		setSizeFull();
		
		VerticalSplitPanel splitPanel = new VerticalSplitPanel();

		VerticalLayout topLayout = new VerticalLayout();
		splitPanel.addComponent(topLayout);
		splitPanel.addComponent(editorLayout);
		HorizontalLayout bottomLeftLayout = new HorizontalLayout();
		topLayout.addComponent(bottomLeftLayout);
		bottomLeftLayout.addComponent(searchField);
		topLayout.addComponent(productList);

		/* Set the contents in the left of the split panel to use all the space */
		topLayout.setSizeFull();

		/*
		 * On the left side, expand the size of the productList so that it uses
		 * all the space left after from bottomLeftLayout
		 */
		topLayout.setExpandRatio(productList, 1);
		productList.setSizeFull();

		/*
		 * In the bottomLeftLayout, searchField takes all the width there is
		 * after adding addNewContactButton. The height of the layout is defined
		 * by the tallest component.
		 */
		bottomLeftLayout.setWidth("100%");
		searchField.setWidth("100%");
		bottomLeftLayout.setExpandRatio(searchField, 1);

		/* Put a little margin around the fields in the right side editor */
		editorLayout.setMargin(true);
		editorLayout.setVisible(false);

		addComponent(splitPanel);
	}

	private void initProductList() {
		BeanQueryFactory<ProductBeanQuery> queryFactory = new
				BeanQueryFactory<ProductBeanQuery>(ProductBeanQuery.class);

		Map<String,Object> queryConfiguration = new HashMap<String,Object>();
		queryConfiguration.put("service", service);
		queryFactory.setQueryConfiguration(queryConfiguration);

		container = new LazyQueryContainer(queryFactory, "id", 50, false);
		container.getQueryView().getQueryDefinition().setMaxNestedPropertyDepth(1);

		for (int i=0; i<tableFieldNames.length; i++) {
			container.addContainerProperty(tableFieldNames[i], String.class, "", true, true);
			productList.setColumnHeader(tableFieldNames[i], messages.getString("product." + tableFieldNames[i]));
		}
		
		productList.setContainerDataSource(container);
		productList.setSelectable(true);
		productList.setImmediate(true);


		productList.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object productId = productList.getValue();

				/*
				 * When a contact is selected from the list, we want to show
				 * that in our editor on the right. This is nicely done by the
				 * FieldGroup that binds all the fields to the corresponding
				 * Properties in our contact at once.
				 */
				if (productId != null)
					editorFields.setItemDataSource(productList
							.getItem(productId));
				
				editorLayout.setVisible(productId != null);
			}
		});
	}

	private void initSearch() {
		/*
		 * We want to show a subtle prompt in the search field. We could also
		 * set a caption that would be shown above the field or description to
		 * be shown in a tooltip.
		 */
		searchField.setInputPrompt(messages.getString("product.search"));

		/*
		 * Granularity for sending events over the wire can be controlled. By
		 * default simple changes like writing a text in TextField are sent to
		 * server with the next Ajax call. You can set your component to be
		 * immediate to send the changes to server immediately after focus
		 * leaves the field. Here we choose to send the text over the wire as
		 * soon as user stops writing for a moment.
		 */
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

		/*
		 * When the event happens, we handle it in the anonymous inner class.
		 * You may choose to use separate controllers (in MVC) or presenters (in
		 * MVP) instead. In the end, the preferred application architecture is
		 * up to you.
		 */
		searchField.addTextChangeListener(new TextChangeListener() {
			public void textChange(final TextChangeEvent event) {

				/* Reset the filter for the contactContainer. */
				container.removeAllContainerFilters();
				container.addContainerFilter(new QuickSearchFilter(event
						.getText()));
				container.refresh();
				
			}
		});
	}


	private void initEditor() {

		/* User interface can be created dynamically to reflect underlying data. */
		for (Object fieldName : editorFieldNames) {
			TextField field = new TextField(messages.getString("product." + fieldName));
			editorLayout.addComponent(field);
			field.setWidth("100%");
			field.setNullRepresentation("");

			/*
			 * We use a FieldGroup to connect multiple components to a data
			 * source at once.
			 */
			editorFields.bind(field, fieldName);
		}

		/*
		 * Data can be buffered in the user interface. When doing so, commit()
		 * writes the changes to the data source. Here we choose to write the
		 * changes automatically without calling commit().
		 */
		editorFields.setBuffered(true);
	}

}
