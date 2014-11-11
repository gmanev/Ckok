package net.nbt.ckok.vaadin;


import java.util.Locale;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.ProductDAO;

import org.apache.log4j.Logger;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
class CkokApplication extends Application {

    private static final Object[] VISIBLE_COLUMNS = new Object[] {"serial", "supplier", "notes"};
    private final String title;
    private ProductDAO productDAO;
    
    private final Logger log = Logger.getLogger(CkokApplication.class);
    
    CkokApplication(ProductDAO productDAO, String title) {
        this.productDAO = productDAO;
        this.title = title;
    }

    @Override
    public void init() {
        final GridLayout layout = new GridLayout(1, 3);
        layout.setWidth("100%");
        layout.setMargin(false);
        setMainWindow(new Window(this.title, layout));

        final BeanContainer<String, Product> beans = new BeanContainer<String, Product>(Product.class);
        beans.setBeanIdProperty("id");

        final Form form = new Form();
        form.setLocale(Locale.GERMAN);
        final Table table = new Table(this.title, beans);
        
        MenuBar menu = createMenuBar(beans, table);
        layout.addComponent(menu);
        
        table.setSelectable(true);
        table.setImmediate(true);
        table.setVisibleColumns(VISIBLE_COLUMNS);
        table.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                Object selectedId = table.getValue();
                @SuppressWarnings("unchecked")
                BeanItem<Product> item = (BeanItem<Product>) table.getItem(selectedId);
                form.setItemDataSource(item);
                form.setVisibleItemProperties(VISIBLE_COLUMNS);
           }
        });
        update(beans);
        layout.addComponent(table);

        Object first = table.getItemIds().iterator().next();
        Item item = table.getItem(first);
        form.setItemDataSource(item);

        form.setCaption("Edit Task");
        form.setVisibleItemProperties(VISIBLE_COLUMNS);
        form.setImmediate(true);
        form.setFormFieldFactory(new DefaultFieldFactory() {
            @Override
            public Field createField(Item item, Object propertyId, com.vaadin.ui.Component uiContext) {
                final AbstractField field = (AbstractField)
                        super.createField(item, propertyId, uiContext);
                field.addListener(new ValueChangeListener() {
                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        BeanItem<Product> item = (BeanItem<Product>) form.getItemDataSource();
                        productDAO.update(item.getBean());
                        //submit.setEnabled(form.isModified());
                    }
                });
                field.setImmediate(true);
               
                return field;
            }        	
        });
        /*
        form.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
               @SuppressWarnings("unchecked")
               BeanItem<Product> item = (BeanItem<Product>) form.getItemDataSource();
               productDAO.update(item.getBean());
            }
        });*/
        layout.addComponent(form);
    }

    private MenuBar createMenuBar(final BeanContainer<String, Product> beans, final Table table) {
        MenuBar menu = new MenuBar();
        menu.setImmediate(true);
        menu.addItem("Reload", new Command() {
            public void menuSelected(MenuItem selectedItem) {
                update(beans);
            }
        });
/*        menu.addItem("Add", new Command() {
            public void menuSelected(MenuItem selectedItem) {
                Task task = new Task();
                task.setId(UUID.randomUUID().toString());
                task.setTitle("New Task");
                task.setDescription("None");
                taskService.addTask(task);
                beans.addBean(task);
            }
        });
        menu.addItem("Delete", new Command() {
            public void menuSelected(MenuItem selectedItem) {
                String id = (String) table.getValue();
                taskService.deleteTask(id);
                table.removeItem(id);
            }
        });*/
        return menu;
    }

    private void update(final BeanContainer<String, Product> beans) {
        beans.removeAllItems();
        beans.addAll(productDAO.getAll());
    }

    
}
