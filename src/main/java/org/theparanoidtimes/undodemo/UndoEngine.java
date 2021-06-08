package org.theparanoidtimes.undodemo;

public interface UndoEngine<T> {

    void registerNewChange(Environment environment, T change);

    T performUndo();

    T lastEntry();
}
