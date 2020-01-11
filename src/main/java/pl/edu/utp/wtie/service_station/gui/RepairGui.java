package pl.edu.utp.wtie.service_station.gui;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.utp.wtie.service_station.model.Repair;
import pl.edu.utp.wtie.service_station.repository.RepairDao;

import java.util.List;

@Route("repairs")
public class RepairGui extends VerticalLayout implements quickReturn {

    private RepairDao repairDao;
    private Dialog descriptionDialog;

    @Autowired
    public RepairGui(RepairDao repairDao) {
        this.repairDao = repairDao;
        descriptionDialog = new Dialog();
        descriptionDialog.setWidth("400px");
        descriptionDialog.setHeight("150px");

        add(addButtonBack());

        Grid<Repair> repairGrid = initRepairGrid();

        add(repairGrid);
    }

    private Grid<Repair> initRepairGrid() {
        Grid<Repair> repairGrid = new Grid<>();
        List<Repair> repairList = repairDao.showAllRepairs();
        repairGrid.setItems(repairList);
        Grid.Column<Repair> idColumn = repairGrid.addColumn(Repair::getIdOrder).setHeader("Nr zlecenia").setWidth("2em")
                .setFooter(String.valueOf(repairList.size()));
        Grid.Column<Repair> nameColumn = repairGrid.addColumn(Repair::getName)
                .setHeader("Imię").setWidth("8em");
        Grid.Column<Repair> surnameColumn = repairGrid.addColumn(Repair::getSurname)
                .setHeader("Nazwisko").setWidth("8em");
        Grid.Column<Repair> phoneNumberColumn = repairGrid.addColumn(Repair::getPhoneNumber)
                .setHeader("Numer telefonu").setWidth("8em");
        Grid.Column<Repair> contactPhoneColumn = repairGrid.addColumn(Repair::getContactPhone)
                .setHeader("Telefon kontaktowy").setWidth("8em");
        Grid.Column<Repair> titleColumn = repairGrid.addColumn(Repair::getTitle)
                .setHeader("Tytuł zlecenia").setWidth("12em");
        Grid.Column<Repair> descriptionColumn = repairGrid.addColumn(new NativeButtonRenderer<>("Opis zlecenia",
                repair -> {
                    descriptionDialog.removeAll();
                    descriptionDialog.add(new Label(repair.getDescription()));
                    descriptionDialog.open();
                })).setWidth("6em");
        repairGrid.setItemDetailsRenderer(TemplateRenderer.<Repair>of(
                "<div style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'>"
                        + "<div><b>Dane samochodu</b><br />-> Marka: [[item.make]]<br />-> Model: [[item.model]]<br />" +
                        "-> Numer VIN: [[item.vin]]<br />-> Numer rejestracyjny: [[item.registrationNumber]]</div></div>")
                .withProperty("make", Repair::getMake)
                .withProperty("model", Repair::getModel)
                .withProperty("vin", Repair::getVin)
                .withProperty("registrationNumber", Repair::getRegistrationNumber)
                .withEventHandler("handleClick", repair ->
                        repairGrid.getDataProvider().refreshItem(repair)));

        HeaderRow halfheaderRow = repairGrid.prependHeaderRow();

        Div customerHeader = new Div(new Span("Klient"));
        customerHeader.getStyle().set("text-align", "center");
        customerHeader.setSizeFull();
        halfheaderRow.join(nameColumn, surnameColumn, phoneNumberColumn).setComponent(customerHeader);

        Div orderHeader = new Div(new Span("Zlecenie"));
        orderHeader.getStyle().set("text-align", "center");
        orderHeader.setSizeFull();
        halfheaderRow.join(contactPhoneColumn, titleColumn, descriptionColumn).setComponent(orderHeader);

        ListDataProvider<Repair> repairListDataProvider = new ListDataProvider<>(repairList);
        repairGrid.setDataProvider(repairListDataProvider);
        HeaderRow filterRow = repairGrid.appendHeaderRow();

        // First filter
        TextField idTextField = new TextField();
        idTextField.addValueChangeListener(event -> repairListDataProvider.addFilter(
                repair -> StringUtils.containsIgnoreCase(String.valueOf(repair.getIdOrder()),
                        idTextField.getValue())));
        idTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(idColumn).setComponent(idTextField);
        idTextField.setSizeFull();
        idTextField.setPlaceholder("Filter");

        // Second filter
        TextField nameTextField = new TextField();
        nameTextField.addValueChangeListener(event -> repairListDataProvider.addFilter(
                repair -> StringUtils.containsIgnoreCase(String.valueOf(repair.getName()),
                        nameTextField.getValue())));
        nameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(nameColumn).setComponent(nameTextField);
        nameTextField.setSizeFull();
        nameTextField.setPlaceholder("Filter");

        // Third filter
        TextField surnameTextField = new TextField();
        surnameTextField.addValueChangeListener(event -> repairListDataProvider.addFilter(
                repair -> StringUtils.containsIgnoreCase(String.valueOf(repair.getSurname()),
                        surnameTextField.getValue())));
        surnameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(surnameColumn).setComponent(surnameTextField);
        surnameTextField.setSizeFull();
        surnameTextField.setPlaceholder("Filter");

        // Fourth filter
        TextField phoneNumberTextField = new TextField();
        phoneNumberTextField.addValueChangeListener(event -> repairListDataProvider.addFilter(
                repair -> StringUtils.containsIgnoreCase(String.valueOf(repair.getPhoneNumber()),
                        phoneNumberTextField.getValue())));
        phoneNumberTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(phoneNumberColumn).setComponent(phoneNumberTextField);
        phoneNumberTextField.setSizeFull();
        phoneNumberTextField.setPlaceholder("Filter");

        // Fifth filter
        TextField contactPhoneTextField = new TextField();
        contactPhoneTextField.addValueChangeListener(event -> repairListDataProvider.addFilter(
                repair -> StringUtils.containsIgnoreCase(String.valueOf(repair.getContactPhone()),
                        contactPhoneTextField.getValue())));
        contactPhoneTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(contactPhoneColumn).setComponent(contactPhoneTextField);
        contactPhoneTextField.setSizeFull();
        contactPhoneTextField.setPlaceholder("Filter");

        // Sixth filter
        TextField titleTextField = new TextField();
        titleTextField.addValueChangeListener(event -> repairListDataProvider.addFilter(
                repair -> StringUtils.containsIgnoreCase(String.valueOf(repair.getTitle()),
                        titleTextField.getValue())));
        titleTextField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(titleColumn).setComponent(titleTextField);
        titleTextField.setSizeFull();
        titleTextField.setPlaceholder("Filter");

        return repairGrid;
    }
}
