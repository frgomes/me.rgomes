package example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.vaadin.Application;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.SimpleNavigator;
import com.vaadin.navigator.State;
import com.vaadin.navigator.Transition;
import com.vaadin.navigator.Navigator.NavigableApplication;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import example.workflow.DisclaimerView;
import example.workflow.FirstView;
import example.workflow.ForumsView;
import example.workflow.SecondView;
import example.workflow.ThirdView;
import example.workflow.TutorialView;

public class PageflowExample extends Application implements NavigableApplication {

    private static final long serialVersionUID = -4706534621957001935L;

    private final List<State> template = new Pageflow().getStates();

    final List<State> pageflow;


    public PageflowExample() {
        try {
            // this.pageflow = writeConfig(template);
            this.pageflow = readConfig();
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private List<State> writeConfig(final List<State> states) throws IOException {
        final XStream xstream = new XStream(new JettisonMappedXmlDriver()); // JsonHierarchicalStreamDriver()
        xstream.setMode(XStream.NO_REFERENCES);
        final FileWriter fw = new FileWriter(new File("pageflow.json"));
        fw.write(xstream.toXML(states));
        fw.close();
        return states;
    }

    private List<State> readConfig() throws IOException {
        final XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        final InputStream is = new FileInputStream(new File("pageflow.json"));
        return (List<State>)xstream.fromXML(is);
    }


    @Override
    public void init() {
        setMainWindow(createNewWindow());
    }

    @Override
    public Window getWindow(final String name) {
        // Use navigator to manage multiple browser windows
        return Navigator.getWindow(this, name, super.getWindow(name));
    }

    @Override
    public Window createNewWindow() {
        final VerticalLayout layout = new VerticalLayout();
        final Window w = new Window("Workflow example", layout);
        final Navigator navigator = new SimpleNavigator();
        final HorizontalLayout container = new HorizontalLayout();
        w.addComponent(navigator);
        w.addComponent(container);
        layout.setMargin(false);
        layout.setSpacing(true);
        layout.setSizeFull();
        layout.setExpandRatio(navigator, 1.0f);
        container.setWidth("100%");

        // wire up the navigation
        for (final State state : pageflow) {
            final Class<Navigator.View> viewClass = state.getViewClass();
            final String name = viewClass.getSimpleName();
            final List<Transition> transitions = state.getTransitions();
            navigator.addView(name, viewClass, transitions);
        }

        // navigate to the starting view
        navigator.navigateTo(DisclaimerView.class);

        return w;
    }


    private class Pageflow {

        public List<State> getStates() {
            return Arrays.asList(
                    new State[] {

                            new State(TutorialView.class, new Transition[] {
                                new Transition("back",              "Back"), // "back" is a reserved transition
                            } ),

                            new State(ForumsView.class, new Transition[] {
                                new Transition("back",              "Back"), // "back" is a reserved transition
                            } ),

                            new State(DisclaimerView.class, new Transition[] {
                                new Transition("ForumsView",        "Forums"),
                                new Transition("TutorialView",      "Tutorial"),
                                new Transition("FirstView",         "Next >"),
                            } ),

                            new State(FirstView.class, new Transition[] {
                                new Transition("ForumsView",        "Forums"),
                                new Transition("TutorialView",      "Tutorial"),
                                new Transition("DisclaimerView",    "< Previous"),
                                new Transition("SecondView",        "Next >"),
                            } ),

                            new State(SecondView.class, new Transition[] {
                                new Transition("ForumsView",        "Forums"),
                                new Transition("TutorialView",      "Tutorial"),
                                new Transition("FirstView",         "< Previous"),
                                new Transition("ThirdView",         "Next >"),
                            } ),

                            new State(ThirdView.class, new Transition[] {
                                new Transition("ForumsView",        "Forums"),
                                new Transition("TutorialView",      "Tutorial"),
                                new Transition("SecondView",        "< Previous"),
                            } ),

                        });
        }
    }

}
