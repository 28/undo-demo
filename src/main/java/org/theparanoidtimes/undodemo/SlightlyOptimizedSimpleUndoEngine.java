package org.theparanoidtimes.undodemo;

import java.util.Stack;

public class SlightlyOptimizedSimpleUndoEngine implements UndoEngine<String> {

    private final int capacity;

    private final Stack<String> undoStack = new Stack<>();

    public SlightlyOptimizedSimpleUndoEngine(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void registerNewChange(Environment environment, String change) {
        if (!environment.getText().equals(lastEntry())) {
            if (undoStack.size() + 1 > capacity)
                undoStack.remove(0);

            undoStack.push(environment.getText());
        }
    }

    @Override
    public String performUndo() {
        if (undoStack.size() > 0)
            return undoStack.pop();
        else
            return null;
    }

    @Override
    public String lastEntry() {
        if (undoStack.size() > 0)
            return undoStack.peek();
        else
            return null;
    }
}
