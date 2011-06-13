package example.workflow;


import com.vaadin.Application;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Label;

public class ForumsView extends Label implements Navigator.View {

    private static final long serialVersionUID = 1380432815050486046L;

    @Override
    public void init(final Navigator navigator, final Application application) {
        setValue("Forums");
    }

    @Override
    public void navigateTo(final String requestedDataId) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getWarningForNavigatingFrom() {
        // TODO Auto-generated method stub
        return null;
    }
}