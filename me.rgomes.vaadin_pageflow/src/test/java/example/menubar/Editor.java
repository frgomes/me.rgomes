package example.menubar;


import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


public class Editor extends CustomComponent implements com.vaadin.navigator.Navigator.View {

    private static final long serialVersionUID = 2398722989346574014L;

    private final TextField textField;
    private final VerticalLayout layout;
    private final Button buttonSave;

    boolean saved;

    public Editor() {
        this.textField = new TextField();
        this.layout = new VerticalLayout();
        this.buttonSave = new Button("Save");
        this.saved = true;
    }

    @Override
    public void init(final Navigator navigator, final Application application) {
        setSizeFull();
        layout.addComponent(textField);
        textField.setRows(10);
        textField.setSizeFull();
        layout.setSizeFull();
        layout.setExpandRatio(textField, 1.0F);
        layout.addComponent(buttonSave);
        layout.setSpacing(true);
        layout.setComponentAlignment(buttonSave, "r");
        setCompositionRoot(layout);

        buttonSave.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                saved = true;
            }
        });

        textField.addListener(new Property.ValueChangeListener() {
            public void valueChange(final ValueChangeEvent event) {
                saved = false;
            }
        });
    }

    @Override
    public String getWarningForNavigatingFrom() {
        return saved ? null : "The text you are editing has not been saved.";
    }

    @Override
    public void navigateTo(final String requestedDataId) {
        // nothing
    }
}
