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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.utp.wtie.service_station.model.Car;
import pl.edu.utp.wtie.service_station.repository.*;

import java.util.List;

@Route("cars")
public class CarGui extends VerticalLayout {

    private CarDao carDao;
    private MakeDao makeDao;
    private ModelDao modelDao;
    private TypeDao typeDao;
    private ColorDao colorDao;
    private ProductionYearDao productionYearDao;
    private Grid.Column<Car> idColumn;
    private Dialog carDialog;

    @Autowired
    public CarGui(CarDao carDao, MakeDao makeDao, ModelDao modelDao, TypeDao typeDao, ColorDao colorDao,
                  ProductionYearDao productionYearDao) {
        this.carDao = carDao;
        this.makeDao = makeDao;
        this.modelDao = modelDao;
        this.typeDao = typeDao;
        this.colorDao = colorDao;
        this.productionYearDao = productionYearDao;
        carDialog = new Dialog();
        carDialog.setWidth("300px");
        carDialog.setHeight("100px");

        addButtonOrderPage();

        Grid<Car> carGrid = initCarGrid();
        addNewCar(carGrid);

        add(carGrid);
    }

    private void addButtonOrderPage() {
        Button orderButton = new Button("Szczegóły zlecenia", new Icon(VaadinIcon.ARROW_RIGHT));
        orderButton.setIconAfterText(true);
        orderButton.addClickListener(buttonClickEvent -> {
            orderButton.getUI().ifPresent(ui -> ui.navigate("orders"));
        });
        add(orderButton);
    }

    private Grid<Car> initCarGrid() {
        Grid<Car> carGrid = new Grid<>();
        List<Car> carList = carDao.showAllCars();
        carGrid.setItems(carList);
        idColumn = carGrid.addColumn(Car::getId).setHeader("Id").setWidth("1em")
                .setFooter(String.valueOf(carList.size()));
        Grid.Column<Car> makeColumn = carGrid.addColumn(Car::getMake).setHeader("Marka").setWidth("7em");
        Grid.Column<Car> modelColumn = carGrid.addColumn(Car::getModel).setHeader("Model").setWidth("9em");
        Grid.Column<Car> typeColumn = carGrid.addColumn(Car::getType).setHeader("Typ").setWidth("5em");
        Grid.Column<Car> colorColumn = carGrid.addColumn(Car::getColor).setHeader("Kolor").setWidth("6em");
        Grid.Column<Car> productionYearColumn = carGrid.addColumn(Car::getProductionYear).
                setHeader("Rok produkcji").setWidth("6em");
        Grid.Column<Car> vinColumn = carGrid.addColumn(Car::getVin).setHeader("Numer VIN").setWidth("10em");
        Grid.Column<Car> mileageColumn = carGrid.addColumn(Car::getMileage).setHeader("Przebieg [km]").setWidth("6em");
        Grid.Column<Car> registerNumberColumn = carGrid.addColumn(Car::getRegistrationNumber)
                .setHeader("Numer rejestracyjny").setWidth("8em");

        Binder<Car> carBinder = new Binder<>(Car.class);
        Editor<Car> carEditor = carGrid.getEditor();
        carEditor.setBinder(carBinder);

        Select<Long> idSelect = new Select<>();
        carBinder.bind(idSelect, "id");

        Select<String> makeSelect = new Select<>();
        makeSelect.setItems(makeDao.showAllMakes());
        carBinder.bind(makeSelect, "make");
        makeColumn.setEditorComponent(makeSelect);
        makeSelect.getElement().addEventListener("keydown", event -> carGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && !event.shiftKey");

        Select<String> modelSelect = new Select<>();
        makeSelect.addValueChangeListener(event ->
                modelSelect.setItems(modelDao.showModels(makeSelect.getValue())));
        carBinder.bind(modelSelect, "model");
        modelColumn.setEditorComponent(modelSelect);
        modelSelect.getElement().addEventListener("keydown", event -> carGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && !event.shiftKey");

        Select<String> typeSelect = new Select<>();
        typeSelect.setItems(typeDao.showAllTypes());
        carBinder.bind(typeSelect, "type");
        typeColumn.setEditorComponent(typeSelect);
        typeSelect.getElement().addEventListener("keydown", event -> carGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && !event.shiftKey");

        Select<String> colorSelect = new Select<>();
        colorSelect.setItems(colorDao.showAllColors());
        carBinder.bind(colorSelect, "color");
        colorColumn.setEditorComponent(colorSelect);
        colorSelect.getElement().addEventListener("keydown", event -> carGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && !event.shiftKey");

        Select<Integer> productionYearSelect = new Select<>();
        productionYearSelect.setItems(productionYearDao.showAllProductionYears());
        carBinder.bind(productionYearSelect, "productionYear");
        productionYearColumn.setEditorComponent(productionYearSelect);
        productionYearSelect.getElement().addEventListener("keydown", event -> carGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && !event.shiftKey");

        TextField vinField = new TextField();
        vinField.getElement().addEventListener("keydown", event -> carGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        carBinder.bind(vinField, "vin");
        vinColumn.setEditorComponent(vinField);

        NumberField mileageField = new NumberField();
        mileageField.getElement().addEventListener("keydown", event -> carGrid.getEditor().cancel())
                .setFilter("event.key === 'Tab' && event.shiftKey");
        carBinder.bind(mileageField, "mileage");
        mileageColumn.setEditorComponent(mileageField);

        TextField registrationNumberField = new TextField();
        registrationNumberField.getElement().addEventListener("keydown", event -> carGrid.getEditor()
                .cancel()).setFilter("event.key === 'Tab' && event.shiftKey");
        carBinder.bind(registrationNumberField, "registrationNumber");
        registerNumberColumn.setEditorComponent(registrationNumberField);

        carGrid.addItemDoubleClickListener(event -> carGrid.getEditor().editItem(event.getItem()));
        carBinder.addValueChangeListener(event -> carGrid.getEditor().refresh());

        carGrid.getEditor().addCloseListener(event -> {
            if (carBinder.getBean() != null) {
                carDialog.removeAll();
                if (makeSelect.getValue() == null || modelSelect.getValue() == null ||
                        modelSelect.getValue().equals("") || typeSelect.getValue() == null ||
                        colorSelect.getValue() == null || productionYearSelect.getValue() == null ||
                        vinField.getValue().equals("") || mileageField.getValue() == null ||
                        registrationNumberField.getValue().equals("")) {
                    carDialog.add(new Label("Wprowadź wszystkie dane samochodu!"));
                } else {
                    Car car = new Car(idSelect.getValue(), modelSelect.getValue(),
                            typeSelect.getValue(), colorSelect.getValue(), productionYearSelect.getValue(),
                            vinField.getValue(), mileageField.getValue(), registrationNumberField.getValue());
                    carDao.updateCar(car);
                    carDialog.add(new Label("Pomyślnie zmodyfikowano dane samochodu!"));
                }
                carDialog.open();
            }
        });

        ListDataProvider<Car> carListDataProvider = new ListDataProvider<>(carList);
        carGrid.setDataProvider(carListDataProvider);
        HeaderRow filterRow = carGrid.appendHeaderRow();

        // First filter
        TextField makeTextField = new TextField();
        makeTextField.addValueChangeListener(event -> carListDataProvider.addFilter(
                car -> StringUtils.containsIgnoreCase(String.valueOf(car.getMake()), makeTextField.getValue())));
        makeTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(makeColumn).setComponent(makeTextField);
        makeTextField.setSizeFull();
        makeTextField.setPlaceholder("Filter");

        // Second filter
        TextField modelTextField = new TextField();
        modelTextField.addValueChangeListener(event -> carListDataProvider.addFilter(
                car -> StringUtils.containsIgnoreCase(String.valueOf(car.getModel()), modelTextField.getValue())));
        modelTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(modelColumn).setComponent(modelTextField);
        modelTextField.setSizeFull();
        modelTextField.setPlaceholder("Filter");

        // Third filter
        TextField typeTextField = new TextField();
        typeTextField.addValueChangeListener(event -> carListDataProvider.addFilter(
                car -> StringUtils.containsIgnoreCase(String.valueOf(car.getType()), typeTextField.getValue())));
        typeTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(typeColumn).setComponent(typeTextField);
        typeTextField.setSizeFull();
        typeTextField.setPlaceholder("Filter");

        // Fourth filter
        TextField colorTextField = new TextField();
        colorTextField.addValueChangeListener(event -> carListDataProvider.addFilter(
                car -> StringUtils.containsIgnoreCase(String.valueOf(car.getColor()), colorTextField.getValue())));
        colorTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(colorColumn).setComponent(colorTextField);
        colorTextField.setSizeFull();
        colorTextField.setPlaceholder("Filter");

        // Fifth filter
        TextField productionYearTextField = new TextField();
        productionYearTextField.addValueChangeListener(event -> carListDataProvider.addFilter(
                car -> StringUtils.containsIgnoreCase(String.valueOf(car.getProductionYear()), productionYearTextField
                        .getValue())));
        productionYearTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(productionYearColumn).setComponent(productionYearTextField);
        productionYearTextField.setSizeFull();
        productionYearTextField.setPlaceholder("Filter");

        // Sixth filter
        TextField vinTextField = new TextField();
        vinTextField.addValueChangeListener(event -> carListDataProvider.addFilter(
                car -> StringUtils.containsIgnoreCase(String.valueOf(car.getVin()), vinTextField.getValue())));
        vinTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(vinColumn).setComponent(vinTextField);
        vinTextField.setSizeFull();
        vinTextField.setPlaceholder("Filter");

        // Seventh filter
        TextField mileageTextField = new TextField();
        mileageTextField.addValueChangeListener(event -> carListDataProvider.addFilter(
                car -> StringUtils.containsIgnoreCase(String.valueOf(car.getMileage()), mileageTextField.getValue())));
        mileageTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(mileageColumn).setComponent(mileageTextField);
        mileageTextField.setSizeFull();
        mileageTextField.setPlaceholder("Filter");

        // Eighth filter
        TextField registrationNumberTextField = new TextField();
        registrationNumberTextField.addValueChangeListener(event -> carListDataProvider.addFilter(
                car -> StringUtils.containsIgnoreCase(String.valueOf(car.getRegistrationNumber()),
                        registrationNumberTextField.getValue())));
        registrationNumberTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(registerNumberColumn).setComponent(registrationNumberTextField);
        registrationNumberTextField.setSizeFull();
        registrationNumberTextField.setPlaceholder("Filter");

        return carGrid;
    }

    private void addNewCar(Grid<Car> carGrid) {
        FormLayout formLayout1 = new FormLayout();
        FormLayout formLayout2 = new FormLayout();
        FormLayout formLayout3 = new FormLayout();

        ComboBox<String> makeComboBox = new ComboBox<>("Marka");
        makeComboBox.setItems(makeDao.showAllMakes());

        ComboBox<String> modelComboBox = new ComboBox<>("Model");
        modelComboBox.setEnabled(false);
        makeComboBox.addValueChangeListener(event -> {
            if (makeComboBox.getValue() != null) {
                modelComboBox.setEnabled(true);
                modelComboBox.setItems(modelDao.showModels(makeComboBox.getValue()));
            } else {
                modelComboBox.setValue(null);
                modelComboBox.setEnabled(false);
            }
        });

        ComboBox<String> typeComboBox = new ComboBox<>("Typ");
        typeComboBox.setItems(typeDao.showAllTypes());

        ComboBox<String> colorComboBox = new ComboBox<>("Kolor");
        colorComboBox.setItems(colorDao.showAllColors());

        ComboBox<Integer> productionYearComboBox = new ComboBox<>("Rok produkcji");
        productionYearComboBox.setItems(productionYearDao.showAllProductionYears());

        TextField vinLabelField = new TextField("Numer VIN");
        NumberField mileageNumberField = new NumberField("Przebieg [km]");
        TextField registrationNumberLabelField = new TextField("Numer rejestracyjny");

        Button newCarButton = new Button("Zapisz", new Icon(VaadinIcon.CAR));
        newCarButton.setIconAfterText(true);
        newCarButton.addClickListener(clickEvent -> {
            carDialog.removeAll();
            if (makeComboBox.getValue() == null || modelComboBox.getValue() == null ||
                    modelComboBox.getValue().equals("") || typeComboBox.getValue() == null ||
                    colorComboBox.getValue() == null || productionYearComboBox.getValue() == null ||
                    vinLabelField.getValue().equals("") || mileageNumberField.getValue() == null ||
                    registrationNumberLabelField.getValue().equals("")) {
                carDialog.add(new Label("Wprowadź wszystkie dane samochodu!"));
            } else {
                Car car = new Car(modelComboBox.getValue(), typeComboBox.getValue(),
                        colorComboBox.getValue(), productionYearComboBox.getValue(), vinLabelField.getValue(),
                        mileageNumberField.getValue(), registrationNumberLabelField.getValue());
                carDao.saveCar(car);
                carGrid.setItems(carDao.showAllCars());
                idColumn.setFooter(String.valueOf(carDao.showAllCars().size()));
                carDialog.add(new Label("Pomyślnie dodano wszystkie dane samochodu!"));
                makeComboBox.setEnabled(false);
                modelComboBox.setEnabled(false);
                typeComboBox.setEnabled(false);
                colorComboBox.setEnabled(false);
                productionYearComboBox.setEnabled(false);
                vinLabelField.setEnabled(false);
                mileageNumberField.setEnabled(false);
                registrationNumberLabelField.setEnabled(false);
                newCarButton.setEnabled(false);
            }
            carDialog.open();
        });

        formLayout1.add(makeComboBox, modelComboBox, typeComboBox, colorComboBox);
        formLayout1.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("25em", 2),
                new FormLayout.ResponsiveStep("25em", 3),
                new FormLayout.ResponsiveStep("25em", 4));

        formLayout2.add(productionYearComboBox, vinLabelField, mileageNumberField, registrationNumberLabelField,
                newCarButton);
        formLayout2.setResponsiveSteps(
                new FormLayout.ResponsiveStep("10em", 1),
                new FormLayout.ResponsiveStep("10em", 2),
                new FormLayout.ResponsiveStep("10em", 3),
                new FormLayout.ResponsiveStep("10em", 4),
                new FormLayout.ResponsiveStep("2em", 5));

        add(formLayout1, formLayout2, formLayout3);
    }
}
