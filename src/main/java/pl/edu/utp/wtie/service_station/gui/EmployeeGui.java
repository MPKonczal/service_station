package pl.edu.utp.wtie.service_station.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
import pl.edu.utp.wtie.service_station.model.Employee;
import pl.edu.utp.wtie.service_station.repository.EmployeeDao;
import pl.edu.utp.wtie.service_station.repository.PositionDao;

import java.util.List;

@Route("employees")
public class EmployeeGui extends VerticalLayout implements quickReturn {

    private EmployeeDao employeeDao;
    private PositionDao positionDao;
    private Grid.Column<Employee> idColumn;
    private Dialog employeeDialog;

    @Autowired
    public EmployeeGui(EmployeeDao employeeDao, PositionDao positionDao) {
        this.employeeDao = employeeDao;
        this.positionDao = positionDao;
        employeeDialog = new Dialog();
        employeeDialog.setWidth("300px");
        employeeDialog.setHeight("100px");

        add(addButtonBack());

        Grid<Employee> employeeGrid = initEmployeeGrid();
        addNewEmployee(employeeGrid);

        add(employeeGrid);
    }

    private Grid<Employee> initEmployeeGrid() {
        Grid<Employee> employeeGrid = new Grid<>();
        List<Employee> employeeList = employeeDao.showAllEmployees();
        employeeGrid.setItems(employeeList);
        idColumn = employeeGrid.addColumn(Employee::getId).setHeader("Id").setWidth("2em")
                .setFooter(String.valueOf(employeeList.size()));
        Grid.Column<Employee> positionColumn = employeeGrid.addColumn(Employee::getPosition)
                .setHeader("Stanowisko").setWidth("20em");
        Grid.Column<Employee> nameColumn = employeeGrid.addColumn(Employee::getName).setHeader("Imię").setWidth("8em");
        Grid.Column<Employee> surnameColumn = employeeGrid.addColumn(Employee::getSurname)
                .setHeader("Nazwisko").setWidth("8em");
        Grid.Column<Employee> peselColumn = employeeGrid.addColumn(Employee::getPesel)
                .setHeader("Pesel").setWidth("8em");
        Grid.Column<Employee> phoneNumberColumn = employeeGrid.addColumn(Employee::getPhoneNumber).
                setHeader("Numer telefonu").setWidth("8em");
        Grid.Column<Employee> commentsColumn = employeeGrid.addColumn(Employee::getComments)
                .setHeader("Uwagi").setWidth("12em");
        employeeGrid.addColumn(new NativeButtonRenderer<>("Usuń", employee -> {
            employeeDialog.removeAll();
            try {
                employeeDao.deleteEmployee(employee.getId());
                employeeGrid.setItems(employeeDao.showAllEmployees());
                idColumn.setFooter(String.valueOf(employeeDao.showAllEmployees().size()));
                employeeDialog.add(new Label("Pomyślnie usunięto pracownika!"));
            } catch (Exception ex) {
                employeeDialog.add(new Label("Nie można usunąć podanego pracownika, ponieważ jest on powiązany " +
                        "ze zrealizowaną usługą!"));
            }
            employeeDialog.open();
        })).setWidth("4em");

        Binder<Employee> employeeBinder = new Binder<>(Employee.class);
        Editor<Employee> employeeEditor = employeeGrid.getEditor();
        employeeEditor.setBinder(employeeBinder);

        Select<Long> idSelect = new Select<>();
        employeeBinder.bind(idSelect, "id");

        Select<String> positionSelect = new Select<>();
        positionSelect.setItems(positionDao.showAllPositions());
        employeeBinder.bind(positionSelect, "position");
        positionColumn.setEditorComponent(positionSelect);
        positionSelect.getElement().addEventListener("keydown", event -> employeeGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && !event.shiftKey");

        TextField nameField = new TextField();
        nameField.getElement().addEventListener("keydown", event -> employeeGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        employeeBinder.bind(nameField, "name");
        nameColumn.setEditorComponent(nameField);

        TextField surnameField = new TextField();
        surnameField.getElement().addEventListener("keydown", event -> employeeGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        employeeBinder.bind(surnameField, "surname");
        surnameColumn.setEditorComponent(surnameField);

        TextField peselField = new TextField();
        peselField.getElement().addEventListener("keydown", event -> employeeGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        employeeBinder.bind(peselField, "pesel");
        peselColumn.setEditorComponent(peselField);

        TextField phoneNumberField = new TextField();
        phoneNumberField.getElement().addEventListener("keydown", event -> employeeGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        employeeBinder.bind(phoneNumberField, "phoneNumber");
        phoneNumberColumn.setEditorComponent(phoneNumberField);

        TextArea commentsArea = new TextArea();
        commentsArea.getElement().addEventListener("keydown", event -> employeeGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        employeeBinder.bind(commentsArea, "comments");
        commentsColumn.setEditorComponent(commentsArea);

        employeeGrid.addItemDoubleClickListener(event -> employeeGrid.getEditor().editItem(event.getItem()));
        employeeBinder.addValueChangeListener(event -> employeeGrid.getEditor().refresh());

        employeeGrid.getEditor().addCloseListener(event -> {
            if (employeeBinder.getBean() != null) {
                employeeDialog.removeAll();
                if (positionSelect.getValue() == null || nameField.getValue().equals("") ||
                        surnameField.getValue().equals("") || peselField.getValue().equals("") ||
                        phoneNumberField.getValue().equals("")) {
                    employeeDialog.add(new Label("Wprowadź wymagane dane pracownika (stanowisko, " +
                            "imię, nazwisko, pesel, numer telefonu)!"));
                } else {
                    Employee employee = new Employee(idSelect.getValue(), positionSelect.getValue(),
                            nameField.getValue(), surnameField.getValue(), peselField.getValue(),
                            phoneNumberField.getValue(), commentsArea.getValue());
                    employeeDao.updateEmployee(employee);
                    employeeDialog.add(new Label("Pomyślnie zmodyfikowano dane pracownika!"));
                }
                employeeDialog.open();
            }
        });

        ListDataProvider<Employee> employeeListDataProvider = new ListDataProvider<>(employeeList);
        employeeGrid.setDataProvider(employeeListDataProvider);
        HeaderRow filterRow = employeeGrid.appendHeaderRow();

        // First filter
        TextField positionTextField = new TextField();
        positionTextField.addValueChangeListener(event -> employeeListDataProvider.addFilter(
                employee -> StringUtils.containsIgnoreCase(String.valueOf(employee.getPosition()),
                        positionTextField.getValue())));
        positionTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(positionColumn).setComponent(positionTextField);
        positionTextField.setSizeFull();
        positionTextField.setPlaceholder("Filter");

        // Second filter
        TextField nameTextField = new TextField();
        nameTextField.addValueChangeListener(event -> employeeListDataProvider.addFilter(
                employee -> StringUtils.containsIgnoreCase(String.valueOf(employee.getName()),
                        nameTextField.getValue())));
        nameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(nameColumn).setComponent(nameTextField);
        nameTextField.setSizeFull();
        nameTextField.setPlaceholder("Filter");

        // Third filter
        TextField surnameTextField = new TextField();
        surnameTextField.addValueChangeListener(event -> employeeListDataProvider.addFilter(
                employee -> StringUtils.containsIgnoreCase(String.valueOf(employee.getSurname()),
                        surnameTextField.getValue())));
        surnameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(surnameColumn).setComponent(surnameTextField);
        surnameTextField.setSizeFull();
        surnameTextField.setPlaceholder("Filter");

        // Fourth filter
        TextField peselTextField = new TextField();
        peselTextField.addValueChangeListener(event -> employeeListDataProvider.addFilter(
                employee -> StringUtils.containsIgnoreCase(String.valueOf(employee.getPesel()),
                        peselTextField.getValue())));
        peselTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(peselColumn).setComponent(peselTextField);
        peselTextField.setSizeFull();
        peselTextField.setPlaceholder("Filter");

        // Fifth filter
        TextField phoneNumberTextField = new TextField();
        phoneNumberTextField.addValueChangeListener(event -> employeeListDataProvider.addFilter(
                employee -> StringUtils.containsIgnoreCase(String.valueOf(employee.getPhoneNumber()),
                        phoneNumberTextField.getValue())));
        phoneNumberTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(phoneNumberColumn).setComponent(phoneNumberTextField);
        phoneNumberTextField.setSizeFull();
        phoneNumberTextField.setPlaceholder("Filter");

        // Sixth filter
        TextField commentsTextField = new TextField();
        commentsTextField.addValueChangeListener(event -> employeeListDataProvider.addFilter(
                employee -> StringUtils.containsIgnoreCase(String.valueOf(employee.getComments()),
                        commentsTextField.getValue())));
        commentsTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(commentsColumn).setComponent(commentsTextField);
        commentsTextField.setSizeFull();
        commentsTextField.setPlaceholder("Filter");

        return employeeGrid;
    }

    private void addNewEmployee(Grid<Employee> employeeGrid) {
        FormLayout formLayout1 = new FormLayout();
        FormLayout formLayout2 = new FormLayout();
        FormLayout formLayout3 = new FormLayout();

        ComboBox<String> positionComboBox = new ComboBox<>("Stanowisko");
        positionComboBox.setItems(positionDao.showAllPositions());

        TextField nameLabelField = new TextField("Imię");
        TextField surnameLabelField = new TextField("Nazwisko");
        TextField peselLabelField = new TextField("Pesel");
        TextField phoneNumberLabelField = new TextField("Numer telefonu");
        TextArea commentsTextArea = new TextArea("Uwagi");

        Button newEmplyeeButton = new Button("Zapisz", new Icon(VaadinIcon.WORKPLACE));
        newEmplyeeButton.setIconAfterText(true);
        newEmplyeeButton.addClickListener(clickEvent -> {
            employeeDialog.removeAll();
            if (positionComboBox.getValue() == null || nameLabelField.getValue().equals("") ||
                    surnameLabelField.getValue().equals("") || peselLabelField.getValue().equals("") ||
                    phoneNumberLabelField.getValue().equals("")) {
                employeeDialog.add(new Label("Wprowadź wymagane dane pracownika (stanowisko, " +
                        "imię, nazwisko, pesel, numer telefonu)!"));
            } else {
                Employee employee = new Employee(positionComboBox.getValue(), nameLabelField.getValue(),
                        surnameLabelField.getValue(), peselLabelField.getValue(), phoneNumberLabelField.getValue(),
                        commentsTextArea.getValue());
                employeeDao.saveEmployee(employee);
                employeeGrid.setItems(employeeDao.showAllEmployees());
                idColumn.setFooter(String.valueOf(employeeDao.showAllEmployees().size()));
                employeeDialog.add(new Label("Pomyślnie dodano dane pracownika!"));
                positionComboBox.setValue(null);
                nameLabelField.setValue("");
                surnameLabelField.setValue("");
                peselLabelField.setValue("");
                phoneNumberLabelField.setValue("");
                commentsTextArea.setValue("");
            }
            employeeDialog.open();
        });

        formLayout1.add(positionComboBox, nameLabelField, surnameLabelField, peselLabelField);
        formLayout1.setResponsiveSteps(
                new FormLayout.ResponsiveStep("10em", 1),
                new FormLayout.ResponsiveStep("10em", 2),
                new FormLayout.ResponsiveStep("10em", 3),
                new FormLayout.ResponsiveStep("10em", 4));

        formLayout2.add(phoneNumberLabelField, commentsTextArea, newEmplyeeButton);
        formLayout2.setResponsiveSteps(
                new FormLayout.ResponsiveStep("10em", 1),
                new FormLayout.ResponsiveStep("100em", 2),
                new FormLayout.ResponsiveStep("1em", 3));

        add(formLayout1, formLayout2, formLayout3);
    }
}
