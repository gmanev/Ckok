package net.nbt.ckok.vaadin;


import java.util.Locale;

import net.nbt.ckok.model.CkokDAOService;
import net.nbt.ckok.model.Product;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
class CkokApplication extends Application {

    private static final Object[] VISIBLE_COLUMNS = new Object[] {"serial", "supplier", "notes"};
    private final CkokDAOService daoService;
    private final String title;

    CkokApplication(CkokDAOService daoService, String title) {
        this.daoService = daoService;
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
  //      MenuBar menu = createMenuBar(beans, table);
  //      layout.addComponent(menu);
        
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
        form.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                @SuppressWarnings("unchecked")
                BeanItem<Product> item = (BeanItem<Product>) form.getItemDataSource();
                //taskService.updateTask(item.getBean());
            }
        });
        layout.addComponent(form);
    }
/*
    private MenuBar createMenuBar(final BeanContainer<String, Task> beans, final Table table) {
        MenuBar menu = new MenuBar();
        menu.setImmediate(true);
        menu.addItem("Reload", new Command() {
            public void menuSelected(MenuItem selectedItem) {
                update(beans);
            }
        });
        menu.addItem("Add", new Command() {
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
        });
        return menu;
    }
*/
    private void update(final BeanContainer<String, Product> beans) {
        beans.removeAllItems();
        beans.addAll(daoService.getProductDAO().getAll());
    }

    
}
