package org.theparanoidtimes.undodemo;

public record WindowOpenEvent(String initialText) implements Event {

    @Override
    public void apply(Environment environment) {
        environment.setText(initialText);
    }

    @Override
    public void unapply(Environment environment) {
        environment.setText("");
    }
}
