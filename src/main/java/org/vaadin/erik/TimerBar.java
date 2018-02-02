package org.vaadin.erik;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.shared.Registration;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The timer bar is a Java implementation of a timer bar web component.
 *
 * This implementation does not rely on the time reported by the web component, but rather
 * implements it's own timer to ensure the time is accurate.
 */
@Tag("timer-bar")
@HtmlImport("bower_components/timer-bar/timer-bar.html")
public class TimerBar extends Component {
    private static final String TIMER_SECONDS = "timerSeconds";
    private static final String SECONDS_REMAINING = "secondsRemaining";
    private static final String START_FUNCTION = "start";
    private static final String STOP_FUNCTION = "stop";
    private static final String RESET_FUNCTION = "reset";

    private Timer timer = new Timer();
    private TimerBarTask timerBarTask;
    private long milliseconds;
    private long millisecondsRemaining;
    private long startedAtMilliseconds;
    private boolean isRunning = false;

    /**
     * Creates a TimerBar with a 10 second timer
     */
    public TimerBar() {
        this(10000);
    }

    /**
     * Creates a TimerBar with the given time in milliseconds
     *
     * @param milliseconds  The time to count down from
     */
    public TimerBar(long milliseconds) {
         setMilliseconds(milliseconds);
    }

    private void resetTask() {
        if(timerBarTask != null) {
            timerBarTask.cancel();
        }
        timerBarTask = new TimerBarTask();
    }

    private void onEnd() {
        isRunning = false;
        millisecondsRemaining = 0;
        this.fireEvent(new TimerEndedEvent(this, false));
    }

    private static double toSeconds(long milliseconds) {
        return milliseconds/1000;
    }

    /**
     * Sets the number of milliseconds to count down from
     *
     * @param milliseconds  The number of milliseconds
     */
    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
        getElement().setProperty(TIMER_SECONDS, TimerBar.toSeconds(milliseconds));
        setMillisecondsRemaining(milliseconds);
    }

    /**
     *
     * @return  The number of milliseconds this timer counts down from
     */
    public long getMilliseconds() {
        return milliseconds;
    }

    /**
     * Sets the number of milliseconds remaining, which also affects how the timer bar is drawn.
     *
     * @param milliseconds  The number of milliseconds remaining
     */
    public void setMillisecondsRemaining(long milliseconds) {
        this.millisecondsRemaining = milliseconds;
        getElement().setProperty(SECONDS_REMAINING, TimerBar.toSeconds(milliseconds));
    }

    /**
     *
     * @return  The number of milliseconds remaining
     */
    public long getMillisecondsRemaining() {
        if(!isRunning) {
            return millisecondsRemaining;
        }
        return milliseconds - (System.currentTimeMillis() - startedAtMilliseconds);
    }

    /**
     *
     * @return  True if the timer is currently running
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Starts the timer from where it last was stopped.
     */
    public void start() {
        startedAtMilliseconds = System.currentTimeMillis();
        resetTask();
        timer.schedule(timerBarTask, millisecondsRemaining);
        isRunning = true;
        getElement().callFunction(START_FUNCTION);
    }

    /**
     * Stops the timer and saves the number of milliseconds remaining.
     */
    public void stop() {
        millisecondsRemaining = getMillisecondsRemaining();
        resetTask();
        isRunning = false;
        getElement().callFunction(STOP_FUNCTION);
    }

    /**
     * Resets the milliseconds remaining. If running, restarts the timer.
     */
    public void reset() {
        setMillisecondsRemaining(milliseconds);
        if(isRunning) {
            // Restart
            start();
        }
        getElement().callFunction(RESET_FUNCTION);
    }

    class TimerBarTask extends TimerTask {
        @Override
        public void run() {
            TimerBar.this.onEnd();
        }
    }

    /**
     * Addes a listener for the timer ended event.
     * @param listener  The listener
     * @return          A registration handle that can be used to remove the listener.
     */
    public Registration addTimerEndedListener(
        ComponentEventListener<TimerEndedEvent> listener) {
        return addListener(TimerEndedEvent.class, listener);
    }

    public class TimerEndedEvent extends ComponentEvent<TimerBar> {
        public TimerEndedEvent(TimerBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }
}
