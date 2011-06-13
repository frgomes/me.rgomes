package example.menubar;


import com.vaadin.Application;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Label;

public class MockupView extends Label implements Navigator.View {

    private static final long serialVersionUID = 1380432815050486046L;

    @Override
    public void init(final Navigator navigator, final Application application) {
        setValue("This is just a mockup of a view");
    }

    @Override
    public String getWarningForNavigatingFrom() {
        return "You did not click save button, are you sure you want to navigate away?";
    }

    @Override
    public void navigateTo(final String requestedDataId) {
        // This is called each time the view is navigated to
        // Requested data id can be passed in URL
    }
}