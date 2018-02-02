package org.vaadin.erik;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("test")
public class TestView extends Div {

    public TestView() {
        Input input = new Input();
        input.getElement().setProperty("value", "foo");
        add(input);
    }
}
