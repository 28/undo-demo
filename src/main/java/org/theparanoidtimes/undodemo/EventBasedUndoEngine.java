package org.theparanoidtimes.undodemo;

import java.util.Stack;

public class EventBasedUndoEngine implements UndoEngine<Event> {

    private final int capacity;

    private final Stack<Event> undoStack = new Stack<>();

    public EventBasedUndoEngine(int capacity) {
        this.capacity = capacity;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void registerNewChange(Environment environment, Event change) {
        if (change instanceof AddTextEvent ate) {
            pushToStack(ate);
        } else if (change instanceof RemoveTextEvent rte) {
            pushToStack(rte);
        } else if (change instanceof WindowOpenEvent woe) {
            // The initial text will be pushed to the stack,
            // but it can be recreated even without saving it.
            // With it we can udo all the way back to the empty screen if needed.
            pushToStack(woe);
        } else if (change instanceof FocusLostEvent) {
            // ignored in this implementation, since all changes are saved always (no buffer)
        }
    }

    private void pushToStack(Event event) {
        Event lastEntry = lastEntry();
        if (!event.equals(lastEntry)) {
            if (undoStack.size() + 1 > capacity)
                undoStack.remove(0);
            undoStack.push(event);
        }
    }

    @Override
    public Event performUndo() {
        if (undoStack.size() > 0)
            return undoStack.pop();
        else
            return null;
    }

    @Override
    public Event lastEntry() {
        if (undoStack.size() > 0)
            return undoStack.peek();
        else
            return null;
    }
}
