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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.utp.wtie.service_station.model.Customer;
import pl.edu.utp.wtie.service_station.repository.CustomerDao;

import java.util.List;

@Route("customers")
public class CustomerGui extends VerticalLayout {

    private CustomerDao customerDao;
    private Grid.Column<Customer> idColumn;
    private Dialog customerDialog;

    @Autowired
    public CustomerGui(CustomerDao customerDao) {
        this.customerDao = customerDao;
        customerDialog = new Dialog();
        customerDialog.setWidth("300px");
        customerDialog.setHeight("100px");

        addButtonCarPage();

        Grid<Customer> customerGrid = initCustomerGrid();
        addNewCustomer(customerGrid);

        add(customerGrid);
    }

    private void addButtonCarPage() {
        Button carButton = new Button("Dane samochodu", new Icon(VaadinIcon.ARROW_RIGHT));
        carButton.setIconAfterText(true);
        carButton.addClickListener(buttonClickEvent ->
                carButton.getUI().ifPresent(ui -> ui.navigate("cars")));
        add(carButton);
    }

    private Grid<Customer> initCustomerGrid() {
        Grid<Customer> customerGrid = new Grid<>();
        List<Customer> customerList = customerDao.showAllCustomers();
        customerGrid.setItems(customerList);
        idColumn = customerGrid.addColumn(Customer::getId).setHeader("Id").setWidth("2em")
                .setFooter(String.valueOf(customerList.size()));
        Grid.Column<Customer> nameColumn = customerGrid.addColumn(Customer::getName).setHeader("Imię").setWidth("8em");
        Grid.Column<Customer> surnameColumn = customerGrid.addColumn(Customer::getSurname)
                .setHeader("Nazwisko").setWidth("8em");
        Grid.Column<Customer> phoneNumberColumn = customerGrid.addColumn(Customer::getPhoneNumber)
                .setHeader("Numer telefonu").setWidth("8em");
        Grid.Column<Customer> companyNameColumn = customerGrid.addColumn(Customer::getCompanyName)
                .setHeader("Nazwa firmy").setWidth("15em");
        Grid.Column<Customer> nipColumn = customerGrid.addColumn(Customer::getNip).setHeader("NIP").setWidth("8em");
        Grid.Column<Customer> commentsColumn = customerGrid.addColumn(Customer::getComments)
                .setHeader("Uwagi").setWidth("22em");

        Binder<Customer> customerBinder = new Binder<>(Customer.class);
        Editor<Customer> customerEditor = customerGrid.getEditor();
        customerEditor.setBinder(customerBinder);

        Select<Long> idSelect = new Select<>();
        customerBinder.bind(idSelect, "id");

        TextField nameField = new TextField();
        nameField.getElement().addEventListener("keydown", event -> customerGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        customerBinder.bind(nameField, "name");
        nameColumn.setEditorComponent(nameField);

        TextField surnameField = new TextField();
        surnameField.getElement().addEventListener("keydown", event -> customerGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        customerBinder.bind(surnameField, "surname");
        surnameColumn.setEditorComponent(surnameField);

        TextField phoneNumberField = new TextField();
        phoneNumberField.getElement().addEventListener("keydown", event -> customerGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        customerBinder.bind(phoneNumberField, "phoneNumber");
        phoneNumberColumn.setEditorComponent(phoneNumberField);

        TextField companyNameField = new TextField();
        companyNameField.getElement().addEventListener("keydown", event -> customerGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        customerBinder.bind(companyNameField, "companyName");
        companyNameColumn.setEditorComponent(companyNameField);

        TextField nipField = new TextField();
        nipField.getElement().addEventListener("keydown", event -> customerGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        customerBinder.bind(nipField, "nip");
        nipColumn.setEditorComponent(nipField);

        TextArea commentsArea = new TextArea();
        commentsArea.getElement().addEventListener("keydown", event -> customerGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        customerBinder.bind(commentsArea, "comments");
        commentsColumn.setEditorComponent(commentsArea);

        customerGrid.addItemDoubleClickListener(event -> customerGrid.getEditor().editItem(event.getItem()));
        customerBinder.addValueChangeListener(event -> customerGrid.getEditor().refresh());

        customerGrid.getEditor().addCloseListener(event -> {
            if (customerBinder.getBean() != null) {
                customerDialog.removeAll();
                if (nameField.getValue().equals("") || surnameField.getValue().equals("") ||
                        phoneNumberField.getValue().equals("")) {
                    customerDialog.add(new Label("Wprowadź wymagane dane klienta " +
                            "(imię, nazwisko, numer telefonu)!"));
                } else {
                    Customer customer = new Customer(idSelect.getValue(), nameField.getValue(), surnameField.getValue(),
                            phoneNumberField.getValue(), companyNameField.getValue(), nipField.getValue(),
                            commentsArea.getValue());
                    customerDao.updateCustomer(customer);
                    customerDialog.add(new Label("Pomyślnie zmodyfikowano dane klienta!"));
                }
                customerDialog.open();
            }
        });

        ListDataProvider<Customer> customerListDataProvider = new ListDataProvider<>(customerList);
        customerGrid.setDataProvider(customerListDataProvider);
        HeaderRow filterRow = customerGrid.appendHeaderRow();

        // First filter
        TextField nameTextField = new TextField();
        nameTextField.addValueChangeListener(event -> customerListDataProvider.addFilter(
                customer -> StringUtils.containsIgnoreCase(String.valueOf(customer.getName()),
                        nameTextField.getValue())));
        nameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(nameColumn).setComponent(nameTextField);
        nameTextField.setSizeFull();
        nameTextField.setPlaceholder("Filter");

        // Second filter
        TextField surnameTextField = new TextField();
        surnameTextField.addValueChangeListener(event -> customerListDataProvider.addFilter(
                customer -> StringUtils.containsIgnoreCase(String.valueOf(customer.getSurname()),
                        surnameTextField.getValue())));
        surnameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(surnameColumn).setComponent(surnameTextField);
        surnameTextField.setSizeFull();
        surnameTextField.setPlaceholder("Filter");

        // Third filter
        TextField phoneNumberTextField = new TextField();
        phoneNumberTextField.addValueChangeListener(event -> customerListDataProvider.addFilter(
                customer -> StringUtils.containsIgnoreCase(String.valueOf(customer.getPhoneNumber()),
                        phoneNumberTextField.getValue())));
        phoneNumberTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(phoneNumberColumn).setComponent(phoneNumberTextField);
        phoneNumberTextField.setSizeFull();
        phoneNumberTextField.setPlaceholder("Filter");

        // Fourth filter
        TextField companyNameTextField = new TextField();
        companyNameTextField.addValueChangeListener(event -> customerListDataProvider.addFilter(
                customer -> StringUtils.containsIgnoreCase(String.valueOf(customer.getCompanyName()),
                        companyNameTextField.getValue())));
        companyNameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(companyNameColumn).setComponent(companyNameTextField);
        companyNameTextField.setSizeFull();
        companyNameTextField.setPlaceholder("Filter");

        // Fifth filter
        TextField nipTextField = new TextField();
        nipTextField.addValueChangeListener(event -> customerListDataProvider.addFilter(
                customer -> StringUtils.containsIgnoreCase(String.valueOf(customer.getNip()),
                        nipTextField.getValue())));
        nipTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(nipColumn).setComponent(nipTextField);
        nipTextField.setSizeFull();
        nipTextField.setPlaceholder("Filter");

        // Sixth filter
        TextField commentsTextField = new TextField();
        commentsTextField.addValueChangeListener(event -> customerListDataProvider.addFilter(
                customer -> StringUtils.containsIgnoreCase(String.valueOf(customer.getComments()),
                        commentsTextField.getValue())));
        commentsTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(commentsColumn).setComponent(commentsTextField);
        commentsTextField.setSizeFull();
        commentsTextField.setPlaceholder("Filter");

        return customerGrid;
    }

    private void addNewCustomer(Grid<Customer> customerGrid) {
        FormLayout formLayout1 = new FormLayout();
        FormLayout formLayout2 = new FormLayout();
        FormLayout formLayout3 = new FormLayout();

        TextField nameLabelField = new TextField("Imię");
        TextField surnameLabelField = new TextField("Nazwisko");
        TextField phoneNumberLabelField = new TextField("Numer telefonu");
        TextField companyNameLabelField = new TextField("Nazwa firmy");
        TextField nipLabelField = new TextField("NIP");
        TextArea commentsTextArea = new TextArea("Uwagi");

        Button newCustomerButton = new Button("Zapisz", new Icon(VaadinIcon.PENCIL));
        newCustomerButton.setIconAfterText(true);
        newCustomerButton.addClickListener(clickEvent -> {
            customerDialog.removeAll();
            if (nameLabelField.getValue().equals("") || surnameLabelField.getValue().equals("") ||
                    phoneNumberLabelField.getValue().equals("")) {
                customerDialog.add(new Label("Wprowadź wymagane dane klienta " +
                        "(imię, nazwisko, numer telefonu)!"));
            } else {
                Customer customer = new Customer(nameLabelField.getValue(), surnameLabelField.getValue(),
                        phoneNumberLabelField.getValue(), companyNameLabelField.getValue(), nipLabelField.getValue(),
                        commentsTextArea.getValue());
                customerDao.saveCustomer(customer);
                customerGrid.setItems(customerDao.showAllCustomers());
                idColumn.setFooter(String.valueOf(customerDao.showAllCustomers().size()));
                customerDialog.add(new Label("Pomyślnie dodano dane klienta!"));
                nameLabelField.setEnabled(false);
                surnameLabelField.setEnabled(false);
                phoneNumberLabelField.setEnabled(false);
                companyNameLabelField.setEnabled(false);
                nipLabelField.setEnabled(false);
                commentsTextArea.setEnabled(false);
                newCustomerButton.setEnabled(false);
            }
            customerDialog.open();
        });

        formLayout1.add(nameLabelField, surnameLabelField, phoneNumberLabelField);
        formLayout1.setResponsiveSteps(
                new FormLayout.ResponsiveStep("15em", 1),
                new FormLayout.ResponsiveStep("15em", 2),
                new FormLayout.ResponsiveStep("15em", 3));

        formLayout2.add(companyNameLabelField, nipLabelField, commentsTextArea, newCustomerButton);
        formLayout2.setResponsiveSteps(
                new FormLayout.ResponsiveStep("10em", 1),
                new FormLayout.ResponsiveStep("10em", 2),
                new FormLayout.ResponsiveStep("100em", 3),
                new FormLayout.ResponsiveStep("2em", 4));

        add(formLayout1, formLayout2, formLayout3);
    }
}
