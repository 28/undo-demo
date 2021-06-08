package org.theparanoidtimes.undodemo;

public record GenericEvent(String text) implements Event {

    @Override
    public void apply(Environment environment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unapply(Environment environment) {
        throw new UnsupportedOperationException();
    }
}
