package pl.edu.utp.wtie.service_station.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.utp.wtie.service_station.model.Service;
import pl.edu.utp.wtie.service_station.repository.ServiceCategoryDao;
import pl.edu.utp.wtie.service_station.repository.ServiceDao;
import pl.edu.utp.wtie.service_station.repository.VatDao;

import java.util.List;

@Route("services")
public class ServiceGui extends VerticalLayout implements quickReturn {

    private ServiceDao serviceDao;
    private ServiceCategoryDao serviceCategoryDao;
    private VatDao vatDao;
    private Grid.Column<Service> idColumn;
    private Dialog serviceDialog;

    @Autowired
    public ServiceGui(ServiceDao serviceDao, ServiceCategoryDao serviceCategoryDao, VatDao vatDao) {
        this.serviceDao = serviceDao;
        this.serviceCategoryDao = serviceCategoryDao;
        this.vatDao = vatDao;
        serviceDialog = new Dialog();
        serviceDialog.setWidth("300px");
        serviceDialog.setHeight("100px");

        add(addButtonBack());

        Grid<Service> serviceGrid = initServiceGrid();
        addNewService(serviceGrid);

        add(serviceGrid);
    }

    private Grid<Service> initServiceGrid() {
        Grid<Service> serviceGrid = new Grid<>();
        List<Service> serviceList = serviceDao.showAllServices();
        serviceGrid.setItems(serviceList);
        idColumn = serviceGrid.addColumn(Service::getId).setHeader("Id").setWidth("1em")
                .setFooter(String.valueOf(serviceList.size()));
        Grid.Column<Service> categoryColumn = serviceGrid.addColumn(Service::getServiceCategory).setHeader("Kategoria")
                .setWidth("20em");
        Grid.Column<Service> serviceColumn = serviceGrid.addColumn(Service::getService).setHeader("Nazwa usługi")
                .setWidth("30em");
        serviceGrid.addColumn(Service::getNetPrice).setHeader("Cena netto [zł]").setWidth("5em");
        serviceGrid.addColumn(Service::getVatRate).setHeader("Podatek VAT").setWidth("5em");
        serviceGrid.addColumn(new NativeButtonRenderer<>("Usuń", service -> {
            serviceDialog.removeAll();
            try {
                serviceDao.deleteService(service.getId());
                serviceGrid.setItems(serviceDao.showAllServices());
                idColumn.setFooter(String.valueOf(serviceDao.showAllServices().size()));
                serviceDialog.add(new Label("Pomyślnie usunięto usługę!"));
            } catch (Exception ex) {
                serviceDialog.add(new Label("Nie można usunąć podanej usługi, ponieważ została ona wykorzystana " +
                        "w realizacji zlecenia!"));
            }
            serviceDialog.open();
        })).setWidth("4em");
        serviceGrid.setItemDetailsRenderer(TemplateRenderer.<Service>of(
                "<div style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'>"
                        + "<div><b>Zakres usługi</b><br />[[item.scopeService]]</div></div>")
                .withProperty("scopeService", Service::getScopeService)
                .withEventHandler("handleClick", service ->
                        serviceGrid.getDataProvider().refreshItem(service)));

        ListDataProvider<Service> serviceListDataProvider = new ListDataProvider<>(serviceList);
        serviceGrid.setDataProvider(serviceListDataProvider);
        HeaderRow filterRow = serviceGrid.appendHeaderRow();

        // First filter
        TextField categoryTextField = new TextField();
        categoryTextField.addValueChangeListener(event -> serviceListDataProvider.addFilter(
                service -> StringUtils.containsIgnoreCase(String.valueOf(service.getServiceCategory()),
                        categoryTextField.getValue())));
        categoryTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(categoryColumn).setComponent(categoryTextField);
        categoryTextField.setSizeFull();
        categoryTextField.setPlaceholder("Filter");

        // Second filter
        TextField serviceTextField = new TextField();
        serviceTextField.addValueChangeListener(event -> serviceListDataProvider.addFilter(
                service -> StringUtils.containsIgnoreCase(String.valueOf(service.getService()),
                        serviceTextField.getValue())));
        serviceTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(serviceColumn).setComponent(serviceTextField);
        serviceTextField.setSizeFull();
        serviceTextField.setPlaceholder("Filter");

        return serviceGrid;
    }

    private void addNewService(Grid<Service> serviceGrid) {
        FormLayout formLayout1 = new FormLayout();
        FormLayout formLayout2 = new FormLayout();
        FormLayout formLayout3 = new FormLayout();

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

        TextField serviceLabelField = new TextField("Nazwa usługi");
        TextArea scopeServiceTextArea = new TextArea("Zakres usługi");
        NumberField netPriceNumberField = new NumberField("Cena netto");

        ComboBox<Double> vatComboBox = new ComboBox<>("Podatek VAT");
        vatComboBox.setItems(vatDao.showAllVatRates());

        Button newServiceButton = new Button("Zapisz", new Icon(VaadinIcon.ACCORDION_MENU));
        newServiceButton.setIconAfterText(true);
        newServiceButton.addClickListener(clickEvent -> {
            serviceDialog.removeAll();
            if (categoryComboBox.getValue() == null || serviceLabelField.getValue().equals("") ||
                    scopeServiceTextArea.getValue().equals("") || netPriceNumberField.getValue() == null ||
                    vatComboBox.getValue() == null) {
                serviceDialog.add(new Label("Wprowadź szczegóły usługi (kategoria, nazwa usługi, zakres usługi, " +
                        "cenę netto, stawkę podatku VAT)!"));
            } else {
                Service service = new Service(((subcategoryComboBox.getValue() == null) ? categoryComboBox.getValue() :
                        subcategoryComboBox.getValue()), serviceLabelField.getValue(), scopeServiceTextArea.getValue(),
                        netPriceNumberField.getValue(), vatComboBox.getValue());
                serviceDao.saveService(service);
                serviceGrid.setItems(serviceDao.showAllServices());
                idColumn.setFooter(String.valueOf(serviceDao.showAllServices().size()));
                serviceDialog.add(new Label("Pomyślnie dodano szczegóły usługi!"));
                categoryComboBox.setValue(null);
                subcategoryComboBox.setValue(null);
                serviceLabelField.setValue("");
                scopeServiceTextArea.setValue("");
                netPriceNumberField.setValue(null);
                vatComboBox.setValue(null);
            }
            serviceDialog.open();
        });

        formLayout1.add(categoryComboBox, subcategoryComboBox, serviceLabelField);
        formLayout1.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("25em", 2),
                new FormLayout.ResponsiveStep("25em", 3));

        formLayout2.add(scopeServiceTextArea, netPriceNumberField, vatComboBox, newServiceButton);
        formLayout2.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50em", 1),
                new FormLayout.ResponsiveStep("5em", 2),
                new FormLayout.ResponsiveStep("5em", 3),
                new FormLayout.ResponsiveStep("2em", 4));

        add(formLayout1, formLayout2, formLayout3);
    }
}
