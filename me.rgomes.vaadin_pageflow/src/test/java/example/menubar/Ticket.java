package example.menubar;


import com.vaadin.Application;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


public class Ticket extends CustomComponent implements com.vaadin.navigator.Navigator.View {

    private static final long serialVersionUID = -1920926909974843957L;

    private final VerticalLayout layout;
    private final TextField textField;
    private final Button buttonShow;
    private final Panel panelDetails;

    public Ticket() {
        this.layout = new VerticalLayout();
        this.textField = new TextField("Ticket number");
        this.buttonShow = new Button("Show ticket");
        this.panelDetails = new Panel();
    }

    @Override
    public void init(final Navigator navigator, final Application application) {
        setCompositionRoot(layout);
        layout.addComponent(textField);
        layout.addComponent(buttonShow);
        layout.addComponent(panelDetails);
        buttonShow.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                navigator.navigateTo(navigator.getUri(Ticket.class) + "/" + textField.toString());
            }
        });
    }

    @Override
    public String getWarningForNavigatingFrom() {
        return null;
    }

    @Override
    public void navigateTo(final String requestedDataId) {
        if (requestedDataId == null) {
            textField.setValue("");
            panelDetails.setVisible(false);
        } else {
            textField.setValue(requestedDataId);
            panelDetails.setVisible(true);
            panelDetails.setCaption("Ticket #" + requestedDataId);
        }
    }

}
