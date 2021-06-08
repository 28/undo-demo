package org.theparanoidtimes.undodemo;

public interface Event {

    void apply(Environment environment);

    void unapply(Environment environment);
}
