package pl.edu.utp.wtie.service_station.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public interface quickReturn {

    default Button addButtonBack() {
        Button backButton = new Button("Powrót do menu", new Icon(VaadinIcon.ARROW_LEFT));
        backButton.addClickListener(buttonClickEvent ->
                backButton.getUI().ifPresent(ui -> ui.navigate("menu")));
        return backButton;
    }
}
