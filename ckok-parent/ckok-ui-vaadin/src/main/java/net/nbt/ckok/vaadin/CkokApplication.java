package net.nbt.ckok.vaadin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.GetAllProducts;

import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@PreserveOnRefresh
public class CkokApplication extends UI {

	private static final Object[] VISIBLE_COLUMNS = new Object[] { "serial",
			"supplier", "notes", "createdOn", "warranty" };
	private final String title;
	private CkokService service;
	
	CkokApplication(CkokService service, String title) {
		this.service = service;
		this.title = title;
	}

	@Override
	public void init(VaadinRequest request) {
		final GridLayout layout = new GridLayout(1, 3);
		layout.setWidth("100%");
		layout.setMargin(false);

		//final BeanContainer<String, Product> beans = new BeanContainer<String, Product>(
		//		Product.class);
		//beans.setBeanIdProperty("id");

		// Text field for inputting a filter
		final TextField tf = new TextField("Search");
		tf.focus();
		layout.addComponent(tf);

		BeanQueryFactory<ProductBeanQuery> queryFactory = new
				BeanQueryFactory<ProductBeanQuery>(ProductBeanQuery.class);
		

		Map<String,Object> queryConfiguration = new HashMap<String,Object>();
		queryConfiguration.put("service", service);
		queryFactory.setQueryConfiguration(queryConfiguration);

//		final Form form = new Form();
//		form.setLocale(Locale.ENGLISH);
		final Table table = new Table();

		final LazyQueryContainer container = new LazyQueryContainer(queryFactory, "id", 50, false);
		container.addContainerProperty("productType.name", String.class, "", true, true);
		container.addContainerProperty("productType.partnum", String.class, "", true, true);
		container.addContainerProperty("serial", String.class, "", true, true);		
		container.addContainerProperty("supplier", String.class, "", true, true);
		container.addContainerProperty("notes", String.class, "", true, true);
		container.addContainerProperty("createdOn", Date.class, "", true, true);
		container.addContainerProperty("warranty", Date.class, "", true, true);
		table.setContainerDataSource(container);
		container.getQueryView().getQueryDefinition().setMaxNestedPropertyDepth(2);

		//MenuBar menu = createMenuBar(beans, table);
		//layout.addComponent(menu);
/*
		table.setSelectable(true);
		table.setImmediate(true);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.addListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object selectedId = table.getValue();
				if (selectedId == null) {
					return;
				}
				@SuppressWarnings("unchecked")
				BeanItem<Product> item = (BeanItem<Product>) table
						.getItem(selectedId);

				form.setItemDataSource(item);
				form.setVisibleItemProperties(VISIBLE_COLUMNS);

			}
		});
*/		
		//update(beans);
		layout.addComponent(table);

		/*
		if (table.getItemIds().iterator().hasNext()) {
			Object first = table.getItemIds().iterator().next();
			Item item = table.getItem(first);
			form.setItemDataSource(item);
		}

		form.setCaption("Edit Task");
		form.setVisibleItemProperties(VISIBLE_COLUMNS);
		form.setImmediate(true);
		*/
		/*
		form.setFormFieldFactory(new DefaultFieldFactory() {
			@Override
			public Field createField(Item item, Object propertyId,
					com.vaadin.ui.Component uiContext) {
				final AbstractField field = (AbstractField) super.createField(
						item, propertyId, uiContext);
				field.addListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						BeanItem<Product> item = (BeanItem<Product>) form
								.getItemDataSource();
						UpdateProduct request = new UpdateProduct();
						request.setProduct(item.getBean());
						service.updateProduct(request);
						// submit.setEnabled(form.isModified());
					}
				});
				field.setImmediate(true);

				return field;
			}
		});*/

		// Filter table according to typed input
		tf.addTextChangeListener(new TextChangeListener() {
			Filter filter = null;

			public void textChange(TextChangeEvent event) {
				Filterable f = (Filterable) table.getContainerDataSource();

				// Remove old filter
				if (filter != null)
					f.removeContainerFilter(filter);

				filter = new QuickSearchFilter(event.getText());
				
				f.addContainerFilter(filter);
				container.refresh();
			}
		});

		/*
		 * form.addListener(new Property.ValueChangeListener() { public void
		 * valueChange(ValueChangeEvent event) {
		 * 
		 * @SuppressWarnings("unchecked") BeanItem<Product> item =
		 * (BeanItem<Product>) form.getItemDataSource();
		 * productDAO.update(item.getBean()); } });
		 */

		setContent(layout);
	}

	private MenuBar createMenuBar(final BeanContainer<String, Product> beans,
			final Table table) {
		MenuBar menu = new MenuBar();
		menu.setImmediate(true);
		menu.addItem("Reload", new Command() {
			public void menuSelected(MenuItem selectedItem) {
				update(beans);
			}
		});
		/*
		 * menu.addItem("Add", new Command() { public void menuSelected(MenuItem
		 * selectedItem) { Task task = new Task();
		 * task.setId(UUID.randomUUID().toString()); task.setTitle("New Task");
		 * task.setDescription("None"); taskService.addTask(task);
		 * beans.addBean(task); } }); menu.addItem("Delete", new Command() {
		 * public void menuSelected(MenuItem selectedItem) { String id =
		 * (String) table.getValue(); taskService.deleteTask(id);
		 * table.removeItem(id); } });
		 */
		return menu;
	}

	private void update(final BeanContainer<String, Product> beans) {
		beans.removeAllItems();
		beans.addAll(service.getAllProducts(new GetAllProducts()).getReturn());
	}

}
