package org.vaadin.erik;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Route("")
@Theme(Lumo.class)
public class DemoView extends Div {

    public DemoView() {
        TimerBar timerBar = new TimerBar();

        Button startButton = new Button("Start");
        startButton.addClickListener(e -> timerBar.start());
        startButton.getStyle().set("margin", "8px 4px");

        Button stopButton = new Button("Stop");
        stopButton.addClickListener(e -> timerBar.stop());
        stopButton.getStyle().set("margin", "8px 4px");

        Button resetButton = new Button("Reset");
        resetButton.addClickListener(e -> timerBar.reset());
        resetButton.getStyle().set("margin", "8px 4px");

        add(startButton, stopButton, resetButton, timerBar);

        timerBar.setMilliseconds(7000);
        timerBar.addTimerEndedListener(e -> System.out.println("The timer has ended"));
    }
}
