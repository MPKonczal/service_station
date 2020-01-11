package pl.edu.utp.wtie.service_station.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.utp.wtie.service_station.model.Realization;
import pl.edu.utp.wtie.service_station.repository.*;

import java.util.List;

@Route("realizations")
public class RealizationGui extends VerticalLayout implements quickReturn {

    private RealizationDao realizationDao;
    private OrderDao orderDao;
    private ServiceCategoryDao serviceCategoryDao;
    private ServiceDao serviceDao;
    private PositionDao positionDao;
    private EmployeeDao employeeDao;
    private Grid.Column<Realization> idColumn;
    private Dialog realizationDialog;
    private Dialog descriptionDialog;
    private Dialog scopeServiceDialog;

    @Autowired
    public RealizationGui(RealizationDao realizationDao, OrderDao orderDao, ServiceCategoryDao serviceCategoryDao,
                          ServiceDao serviceDao, PositionDao positionDao, EmployeeDao employeeDao) {
        this.realizationDao = realizationDao;
        this.orderDao = orderDao;
        this.serviceCategoryDao = serviceCategoryDao;
        this.serviceDao = serviceDao;
        this.positionDao = positionDao;
        this.employeeDao = employeeDao;
        realizationDialog = new Dialog();
        realizationDialog.setWidth("300px");
        realizationDialog.setHeight("100px");
        descriptionDialog = new Dialog();
        descriptionDialog.setWidth("400px");
        descriptionDialog.setHeight("200px");
        scopeServiceDialog = new Dialog();
        scopeServiceDialog.setWidth("800px");
        scopeServiceDialog.setHeight("200px");

        add(addButtonBack());

        Grid<Realization> realizationGrid = initRealizationGrid();
        addNewRealization(realizationGrid);

        add(realizationGrid);
    }

    private Grid<Realization> initRealizationGrid() {
        Grid<Realization> realizationGrid = new Grid<>();
        List<Realization> realizationList = realizationDao.showAllRealizations();
        realizationGrid.setItems(realizationList);
        idColumn = realizationGrid.addColumn(Realization::getIdOrder)
                .setHeader("Nr").setWidth("2em").setFooter(String.valueOf(realizationList.size()));
        Grid.Column<Realization> titleColumn = realizationGrid.addColumn(Realization::getTitle)
                .setHeader("Tytuł zlecenia").setWidth("8em");
        Grid.Column<Realization> descriptionColumn = realizationGrid.addColumn(new NativeButtonRenderer<>(
                "Opis zlecenia", realization -> {
            descriptionDialog.removeAll();
            descriptionDialog.add(new Label(realization.getDescription()));
            descriptionDialog.open();
        })).setWidth("6em");
        Grid.Column<Realization> contactPhoneColumn = realizationGrid.addColumn(Realization::getContactPhone)
                .setHeader("Telefon kontaktowy").setWidth("7em");
        Grid.Column<Realization> serviceColumn = realizationGrid.addColumn(Realization::getService)
                .setHeader("Nazwa usługi").setWidth("17em");
        Grid.Column<Realization> scopeServiceColumn = realizationGrid.addColumn(new NativeButtonRenderer<>(
                "Zakres usługi", realization -> {
            scopeServiceDialog.removeAll();
            scopeServiceDialog.add(new Label(realization.getScopeService()));
            scopeServiceDialog.open();
        })).setWidth("6em");
        Grid.Column<Realization> grossPriceColumn = realizationGrid.addColumn(Realization::getGrossPrice)
                .setHeader("Cena brutto [zł]").setWidth("6em");
        Grid.Column<Realization> realizationDateColumn = realizationGrid.addColumn(Realization::getRealizationDate)
                .setHeader("Data relizacji usługi").setWidth("10em");
        realizationGrid.setItemDetailsRenderer(TemplateRenderer.<Realization>of(
                "<div style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'><div>" +
                        "<b>Dane pracownika</b><br />-> Stanowisko: [[item.position]]<br />-> Imię: [[item.name]]" +
                        "<br />-> Nazwisko: [[item.surname]]<br />-> Pesel: [[item.pesel]]" +
                        "<br />-> Numer telefonu: [[item.phoneNumber]]</div></div>")
                .withProperty("position", Realization::getPosition)
                .withProperty("name", Realization::getName)
                .withProperty("surname", Realization::getSurnme)
                .withProperty("pesel", Realization::getPesel)
                .withProperty("phoneNumber", Realization::getPhoneNumber)
                .withEventHandler("handleClick", repair ->
                        realizationGrid.getDataProvider().refreshItem(repair)));

        HeaderRow halfheaderRow = realizationGrid.prependHeaderRow();

        Div realizationHeader = new Div(new Span("Zlecenie"));
        realizationHeader.getStyle().set("text-align", "center");
        realizationHeader.setSizeFull();
        halfheaderRow.join(idColumn, titleColumn, descriptionColumn, contactPhoneColumn).setComponent(realizationHeader);

        Div serviceHeader = new Div(new Span("Usługa"));
        serviceHeader.getStyle().set("text-align", "center");
        serviceHeader.setSizeFull();
        halfheaderRow.join(serviceColumn, scopeServiceColumn, grossPriceColumn, realizationDateColumn)
                .setComponent(serviceHeader);

        ListDataProvider<Realization> realizationListDataProvider = new ListDataProvider<>(realizationList);
        realizationGrid.setDataProvider(realizationListDataProvider);
        HeaderRow filterRow = realizationGrid.appendHeaderRow();

        // First filter
        TextField idTextField = new TextField();
        idTextField.addValueChangeListener(event -> realizationListDataProvider.addFilter(
                realization -> StringUtils.containsIgnoreCase(String.valueOf(realization.getIdOrder()),
                        idTextField.getValue())));
        idTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(idColumn).setComponent(idTextField);
        idTextField.setSizeFull();
        idTextField.setPlaceholder("Filter");

        // Second filter
        TextField titleTextField = new TextField();
        titleTextField.addValueChangeListener(event -> realizationListDataProvider.addFilter(
                realization -> StringUtils.containsIgnoreCase(String.valueOf(realization.getTitle()),
                        titleTextField.getValue())));
        titleTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(titleColumn).setComponent(titleTextField);
        titleTextField.setSizeFull();
        titleTextField.setPlaceholder("Filter");

        // Third filter
        TextField contactPhoneTextField = new TextField();
        contactPhoneTextField.addValueChangeListener(event -> realizationListDataProvider.addFilter(
                realization -> StringUtils.containsIgnoreCase(String.valueOf(realization.getContactPhone()),
                        contactPhoneTextField.getValue())));
        contactPhoneTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(contactPhoneColumn).setComponent(contactPhoneTextField);
        contactPhoneTextField.setSizeFull();
        contactPhoneTextField.setPlaceholder("Filter");

        // Fourth filter
        TextField serviceTextField = new TextField();
        serviceTextField.addValueChangeListener(event -> realizationListDataProvider.addFilter(
                realization -> StringUtils.containsIgnoreCase(String.valueOf(realization.getService()),
                        serviceTextField.getValue())));
        serviceTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(serviceColumn).setComponent(serviceTextField);
        serviceTextField.setSizeFull();
        serviceTextField.setPlaceholder("Filter");

        // Fifth filter
        TextField grossPriceTextField = new TextField();
        grossPriceTextField.addValueChangeListener(event -> realizationListDataProvider.addFilter(
                realization -> StringUtils.containsIgnoreCase(String.valueOf(realization.getGrossPrice()),
                        grossPriceTextField.getValue())));
        grossPriceTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(grossPriceColumn).setComponent(grossPriceTextField);
        grossPriceTextField.setSizeFull();
        grossPriceTextField.setPlaceholder("Filter");

        // Sixth filter
        TextField realizationDateTextField = new TextField();
        realizationDateTextField.addValueChangeListener(event -> realizationListDataProvider.addFilter(
                realization -> StringUtils.containsIgnoreCase(String.valueOf(realization.getRealizationDate()),
                        realizationDateTextField.getValue())));
        realizationDateTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(realizationDateColumn).setComponent(realizationDateTextField);
        realizationDateTextField.setSizeFull();
        realizationDateTextField.setPlaceholder("Filter");

        return realizationGrid;
    }

    private void addNewRealization(Grid<Realization> realizationGrid) {
        FormLayout formLayout1 = new FormLayout();
        FormLayout formLayout2 = new FormLayout();
        FormLayout formLayout3 = new FormLayout();

        ComboBox<Long> idComboBox = new ComboBox<>("Nr zlecenia");
        idComboBox.setItems(orderDao.showAllOrderNumbers());

        TextField titleLabelField = new TextField("Tytuł zlecenia");
        titleLabelField.setEnabled(false);
        idComboBox.addValueChangeListener(event -> {
            if (idComboBox.getValue() != null) {
                titleLabelField.setValue(orderDao.showOrder(idComboBox.getValue()));
            } else {
                titleLabelField.setValue(null);
            }
        });

        ComboBox<String> categoryComboBox = new ComboBox<>("Kategoria");
        categoryComboBox.setItems(serviceCategoryDao.showAllServiceCategories());

        ComboBox<String> subcategoryComboBox = new ComboBox<>("Podkategoria");
        subcategoryComboBox.setEnabled(false);
        categoryComboBox.addValueChangeListener(event -> {
            if (categoryComboBox.getValue() != null) {
                List<String> subcategories = serviceCategoryDao.showServiceSubcategories(
                        serviceCategoryDao.findServiceCategoryKey(categoryComboBox.getValue()));
                if (!subcategories.isEmpty()) {
                    subcategoryComboBox.setEnabled(true);
                    subcategoryComboBox.setItems(subcategories);
                } else {
                    subcategoryComboBox.setValue(null);
                    subcategoryComboBox.setEnabled(false);
                }
            } else {
                subcategoryComboBox.setValue(null);
                subcategoryComboBox.setEnabled(false);
            }
        });

        ComboBox<String> serviceComboBox = new ComboBox<>("Nazwa usługi");
        serviceComboBox.setEnabled(false);
        subcategoryComboBox.addValueChangeListener(event1 -> {
            if (categoryComboBox.getValue() != null && subcategoryComboBox.getValue() != null) {
                serviceComboBox.setEnabled(true);
                serviceComboBox.setItems(serviceDao.showServiceNames(subcategoryComboBox.getValue()));
            }
        });
        categoryComboBox.addValueChangeListener(event3 -> {
            if (categoryComboBox.getValue() != null && subcategoryComboBox.getValue() == null) {
                serviceComboBox.setEnabled(true);
                serviceComboBox.setItems(serviceDao.showServiceNames(categoryComboBox.getValue()));
            }
        });

        ComboBox<String> positionComboBox = new ComboBox<>("Stanowisko pracownika");
        positionComboBox.setItems(positionDao.showAllPositions());

        ComboBox<String> surnameComboBox = new ComboBox<>("Nazwisko pracownika");
        surnameComboBox.setEnabled(false);
        positionComboBox.addValueChangeListener(event -> {
            if (positionComboBox.getValue() != null) {
                surnameComboBox.setEnabled(true);
                surnameComboBox.setItems(employeeDao.showSurnames(positionComboBox.getValue()));
            } else {
                surnameComboBox.setValue(null);
                surnameComboBox.setEnabled(false);
            }
        });

        Button newRealizationButton = new Button("Zapisz", new Icon(VaadinIcon.HANDSHAKE));
        newRealizationButton.setIconAfterText(true);
        newRealizationButton.addClickListener(clickEvent -> {
            realizationDialog.removeAll();
            if (idComboBox.getValue() == null || titleLabelField.getValue().equals("") ||
                    categoryComboBox.getValue() == null || serviceComboBox.getValue() == null ||
                    positionComboBox.getValue() == null || surnameComboBox.getValue() == null) {
                realizationDialog.add(new Label("Wprowadź szczegóły realizacji usługi!"));
            } else {
                Realization realization = new Realization(idComboBox.getValue(), titleLabelField.getValue(),
                        ((subcategoryComboBox.getValue() == null) ? categoryComboBox.getValue() :
                                subcategoryComboBox.getValue()), serviceComboBox.getValue(),
                        positionComboBox.getValue(), surnameComboBox.getValue());
                realizationDao.saveRealization(realization);
                realizationGrid.setItems(realizationDao.showAllRealizations());
                idColumn.setFooter(String.valueOf(realizationDao.showAllRealizations().size()));
                realizationDialog.add(new Label("Pomyślnie dodano szczegóły realizacji zlecenia!"));
                categoryComboBox.setValue(null);
                subcategoryComboBox.setValue(null);
                serviceComboBox.setValue(null);
                positionComboBox.setValue(null);
                surnameComboBox.setValue(null);
            }
            realizationDialog.open();
        });

        formLayout1.add(idComboBox, titleLabelField, categoryComboBox, subcategoryComboBox);
        formLayout1.setResponsiveSteps(
                new FormLayout.ResponsiveStep("5em", 1),
                new FormLayout.ResponsiveStep("20em", 2),
                new FormLayout.ResponsiveStep("25em", 3),
                new FormLayout.ResponsiveStep("25em", 4));

        formLayout2.add(serviceComboBox, positionComboBox, surnameComboBox, newRealizationButton);
        formLayout2.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("15em", 2),
                new FormLayout.ResponsiveStep("15em", 3),
                new FormLayout.ResponsiveStep("5em", 4));

        add(formLayout1, formLayout2, formLayout3);
    }
}
