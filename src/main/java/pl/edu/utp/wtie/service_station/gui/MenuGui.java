package pl.edu.utp.wtie.service_station.gui;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("menu")
public class MenuGui extends VerticalLayout {

    public MenuGui() {

        ListBox<String> listBox = new ListBox<>();
        listBox.setItems("Rejestracja klienta", "Spis zleceń", "Realizacje zleceń", "Pracownicy", "Usługi",
                "-----------------------", "Samochody", "Klienci", "Zlecenia");
        listBox.prependComponents("Rejestracja klienta", new H3("Witamy w warsztacie samochodowym!"));

        listBox.addValueChangeListener(event -> {
            if (listBox.getValue() != null) {
                switch (listBox.getValue()) {
                    case "Rejestracja klienta":
                        listBox.getUI().ifPresent(ui -> ui.navigate("customers"));
                        break;
                    case "Spis zleceń":
                        listBox.getUI().ifPresent(ui -> ui.navigate("repairs"));
                        break;
                    case "Realizacje zleceń":
                        listBox.getUI().ifPresent(ui -> ui.navigate("realizations"));
                        break;
                    case "Pracownicy":
                        listBox.getUI().ifPresent(ui -> ui.navigate("employees"));
                        break;
                    case "Usługi":
                        listBox.getUI().ifPresent(ui -> ui.navigate("services"));
                        break;
                    case "Samochody":
                        listBox.getUI().ifPresent(ui -> ui.navigate("cars"));
                        break;
                    case "Klienci":
                        listBox.getUI().ifPresent(ui -> ui.navigate("customers"));
                        break;
                    case "Zlecenia":
                        listBox.getUI().ifPresent(ui -> ui.navigate("orders"));
                        break;
                }
            }
        });

        Image image = new Image("https://cdn1.rentingcarz.com/cdn/images/landing-pages/xexotic-cars-midbanner.jpg.pagespeed.ic.lqGm6ZAgoo.jpg", "Banner");
        image.setWidth("90%");

        add(listBox, image);
    }
}
