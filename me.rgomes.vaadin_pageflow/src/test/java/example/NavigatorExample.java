package example;


import com.vaadin.Application;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.SimpleNavigator;
import com.vaadin.navigator.Navigator.NavigableApplication;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import example.menubar.Dashboard;
import example.menubar.Editor;
import example.menubar.MockupView;
import example.menubar.Ticket;

public class NavigatorExample extends Application implements NavigableApplication {

    private static final long serialVersionUID = -4706534621957001935L;

    @Override
    public void init() {
        setMainWindow(createNewWindow());
    }

    @Override
    public Window getWindow(final String name) {
        // Use navigator to manage multiple browser windows
        return Navigator.getWindow(this, name, super.getWindow(name));
    }

    public Window createNewWindow() {
        final VerticalLayout layout = new VerticalLayout();
        final Window w = new Window("Navigator example", layout);
        final MenuBar menu = new MenuBar();
        final Navigator navigator = new SimpleNavigator();

        w.addComponent(menu);
        w.addComponent(navigator);
        layout.setMargin(false);
        layout.setSpacing(true);
        layout.setSizeFull();
        layout.setExpandRatio(navigator, 1.0f);
        menu.setWidth("100%");

        // Wire up the navigation
        for (final Class<Navigator.View> viewClass : new Class[] {
                Dashboard.class,
                Editor.class,
                Ticket.class,
                MockupView.class }) {

            navigator.addView(viewClass.getSimpleName(), viewClass);
            menu.addItem(viewClass.getSimpleName(), new MenuBar.Command() {
                public void menuSelected(final MenuItem selectedItem) {
                    navigator.navigateTo(viewClass);
                }
            });
        }

        return w;
    }
}
