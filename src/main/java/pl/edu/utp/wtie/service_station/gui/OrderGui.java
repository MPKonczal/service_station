package pl.edu.utp.wtie.service_station.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.utp.wtie.service_station.model.Order;
import pl.edu.utp.wtie.service_station.repository.OrderDao;

import java.util.List;

@Route("orders")
public class OrderGui extends VerticalLayout implements quickReturn {

    private OrderDao orderDao;
    private Grid.Column<Order> idColumn;
    private Dialog orderDialog;
    private Dialog descriptionDialog;

    @Autowired
    public OrderGui(OrderDao orderDao) {
        this.orderDao = orderDao;
        orderDialog = new Dialog();
        orderDialog.setWidth("300px");
        orderDialog.setHeight("100px");
        descriptionDialog = new Dialog();
        descriptionDialog.setWidth("400px");
        descriptionDialog.setHeight("150px");

        add(addButtonBack());

        Grid<Order> orderGrid = initOrderGrid();
        addNewOrder(orderGrid);

        add(orderGrid);
    }

    private Grid<Order> initOrderGrid() {
        Grid<Order> orderGrid = new Grid<>();
        List<Order> orderList = orderDao.showAllOrders();
        orderGrid.setItems(orderList);
        idColumn = orderGrid.addColumn(Order::getId).setHeader("Id").setWidth("2em")
                .setFooter(String.valueOf(orderList.size()));
        Grid.Column<Order> issueDateColumn = orderGrid.addColumn(Order::getIssueDate)
                .setHeader("Data wystawienia zlecenia").setWidth("12em");
        Grid.Column<Order> contactPhoneColumn = orderGrid.addColumn(Order::getContactPhone)
                .setHeader("Telefon kontaktowy").setWidth("8em");
        Grid.Column<Order> titleColumn = orderGrid.addColumn(Order::getTitle)
                .setHeader("Tytuł zlecenia").setWidth("20em");
        Grid.Column<Order> commentsColumn = orderGrid.addColumn(Order::getComments).setHeader("Uwagi").setWidth("22em");
        orderGrid.addColumn(new NativeButtonRenderer<>("Opis zlecenia", order -> {
            descriptionDialog.removeAll();
            descriptionDialog.add(new Label(order.getDescription()));
            descriptionDialog.open();
        })).setWidth("6em");

        Binder<Order> orderBinder = new Binder<>(Order.class);
        Editor<Order> orderEditor = orderGrid.getEditor();
        orderEditor.setBinder(orderBinder);

        Select<Long> idSelect = new Select<>();
        orderBinder.bind(idSelect, "id");

        TextField contactPhoneField = new TextField();
        contactPhoneField.getElement().addEventListener("keydown", event -> orderGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        orderBinder.bind(contactPhoneField, "contactPhone");
        contactPhoneColumn.setEditorComponent(contactPhoneField);

        TextField titleField = new TextField();
        titleField.getElement().addEventListener("keydown", event -> orderGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        orderBinder.bind(titleField, "title");
        titleColumn.setEditorComponent(titleField);

        TextArea commentsArea = new TextArea();
        commentsArea.getElement().addEventListener("keydown", event -> orderGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        orderBinder.bind(commentsArea, "comments");
        commentsColumn.setEditorComponent(commentsArea);

        orderGrid.addItemDoubleClickListener(event -> orderGrid.getEditor().editItem(event.getItem()));
        orderBinder.addValueChangeListener(event -> orderGrid.getEditor().refresh());

        orderGrid.getEditor().addCloseListener(event -> {
            if (orderBinder.getBean() != null) {
                orderDialog.removeAll();
                if (contactPhoneField.getValue().equals("") || titleField.getValue().equals("")) {
                    orderDialog.add(new Label("Wprowadź wymagane szczegóły zlecenia " +
                            "(telefon kontaktowy, tytuł zlecenia)!"));
                } else {
                    Order order = new Order(idSelect.getValue(), contactPhoneField.getValue(),
                            titleField.getValue(), commentsArea.getValue());
                    orderDao.updateOrder(order);
                    orderDialog.add(new Label("Pomyślnie zmodyfikowano szczegóły zlecenia!"));
                }
                orderDialog.open();
            }
        });

        ListDataProvider<Order> orderListDataProvider = new ListDataProvider<>(orderList);
        orderGrid.setDataProvider(orderListDataProvider);
        HeaderRow filterRow = orderGrid.appendHeaderRow();

        // First filter
        TextField issueDateTextField = new TextField();
        issueDateTextField.addValueChangeListener(event -> orderListDataProvider.addFilter(
                order -> StringUtils.containsIgnoreCase(String.valueOf(order.getIssueDate()),
                        issueDateTextField.getValue())));
        issueDateTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(issueDateColumn).setComponent(issueDateTextField);
        issueDateTextField.setSizeFull();
        issueDateTextField.setPlaceholder("Filter");

        // Second filter
        TextField contactPhoneTextField = new TextField();
        contactPhoneTextField.addValueChangeListener(event -> orderListDataProvider.addFilter(
                order -> StringUtils.containsIgnoreCase(String.valueOf(order.getContactPhone()),
                        contactPhoneTextField.getValue())));
        contactPhoneTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(contactPhoneColumn).setComponent(contactPhoneTextField);
        contactPhoneTextField.setSizeFull();
        contactPhoneTextField.setPlaceholder("Filter");

        // Third filter
        TextField titleTextField = new TextField();
        titleTextField.addValueChangeListener(event -> orderListDataProvider.addFilter(
                order -> StringUtils.containsIgnoreCase(String.valueOf(order.getTitle()), titleTextField.getValue())));
        titleTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(titleColumn).setComponent(titleTextField);
        titleTextField.setSizeFull();
        titleTextField.setPlaceholder("Filter");

        // Fourth filter
        TextField commentsTextField = new TextField();
        commentsTextField.addValueChangeListener(event -> orderListDataProvider.addFilter(
                order -> StringUtils.containsIgnoreCase(String.valueOf(order.getComments()),
                        commentsTextField.getValue())));
        commentsTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(commentsColumn).setComponent(commentsTextField);
        commentsTextField.setSizeFull();
        commentsTextField.setPlaceholder("Filter");

        return orderGrid;
    }

    private void addNewOrder(Grid<Order> orderGrid) {
        FormLayout formLayout1 = new FormLayout();
        FormLayout formLayout2 = new FormLayout();

        TextField contactPhoneLabelField = new TextField("Telefon kontaktowy");
        TextField titleLabelField = new TextField("Tytuł zlecenia");
        TextArea descriptionTextArea = new TextArea("Opis zlecenia");
        TextArea commentsTextArea = new TextArea("Uwagi");

        Button newOrderButton = new Button("Zapisz", new Icon(VaadinIcon.NOTEBOOK));
        newOrderButton.setIconAfterText(true);
        newOrderButton.addClickListener(clickEvent -> {
            orderDialog.removeAll();
            if (contactPhoneLabelField.getValue().equals("") || titleLabelField.getValue().equals("") ||
                    descriptionTextArea.getValue().equals("")) {
                orderDialog.add(new Label("Wprowadź wymagane dane klienta " +
                        "(telefon kontaktowy, tytuł zlecenia, opis zlecenia)!"));
            } else {
                Order order = new Order(contactPhoneLabelField.getValue(), titleLabelField.getValue(),
                        descriptionTextArea.getValue(), commentsTextArea.getValue());
                orderDao.saveOrder(order);
                orderGrid.setItems(orderDao.showAllOrders());
                idColumn.setFooter(String.valueOf(orderDao.showAllOrders().size()));
                orderDialog.add(new Label("Pomyślnie dodano szczegóły zlecenia!"));
                contactPhoneLabelField.setEnabled(false);
                titleLabelField.setEnabled(false);
                descriptionTextArea.setEnabled(false);
                commentsTextArea.setEnabled(false);
                newOrderButton.setEnabled(false);
            }
            orderDialog.open();
        });

        formLayout1.add(contactPhoneLabelField, titleLabelField, descriptionTextArea, commentsTextArea, newOrderButton);
        formLayout1.setResponsiveSteps(
                new FormLayout.ResponsiveStep("5em", 1),
                new FormLayout.ResponsiveStep("10em", 2),
                new FormLayout.ResponsiveStep("50em", 3),
                new FormLayout.ResponsiveStep("50em", 4),
                new FormLayout.ResponsiveStep("2em", 5));

        add(formLayout1, formLayout2);
    }
}
