package example.menubar;


import com.vaadin.Application;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;


@SuppressWarnings("serial")
public class Dashboard extends CustomComponent implements Navigator.View {

    private final GridLayout gl;

    public Dashboard() {
        this.gl = new GridLayout(3, 3);
    }

    @Override
    public void init(final Navigator navigator, final Application application) {
        setSizeFull();
        gl.setSizeFull();
        setCompositionRoot(gl);
        gl.setSpacing(true);
        for (int i = 0; i < 9; i++) {
            final Panel p = new Panel("Board " + i);
            gl.addComponent(p);
            p.setSizeFull();
        }
    }

    @Override
    public String getWarningForNavigatingFrom() {
        return null;
    }

    @Override
    public void navigateTo(final String requestedDataId) {
        // nothing
    }

}
