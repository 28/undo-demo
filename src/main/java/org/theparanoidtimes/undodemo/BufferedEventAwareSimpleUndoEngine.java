package org.theparanoidtimes.undodemo;

import java.util.Stack;

public class BufferedEventAwareSimpleUndoEngine implements UndoEngine<Event> {

    private final int capacity;
    private final int bufferSize;

    private final Stack<Event> undoStack = new Stack<>();
    private int buffer;

    public BufferedEventAwareSimpleUndoEngine(int capacity, int bufferSize) {
        this.capacity = capacity;
        this.bufferSize = bufferSize;
    }

    @Override
    public void registerNewChange(Environment environment, Event change) {
        if (change instanceof GenericEvent) {
            Event lastEntry = lastEntry();
            if (lastEntry == null || !environment.getText().equals(extractText(lastEntry))) { // a helper method to get the saved text
                if (buffer + 1 == bufferSize) {
                    pushToStack(new GenericEvent(environment.getText())); // 'collect' all changes
                    flushBuffer();
                } else
                    buffer++;
            }
        } else if (change instanceof WindowOpenEvent) {
            // Store initial changes without checking the buffer since
            // the initial state must be saved.
            // This is expected to be called only once so there will be
            // no interference with other (generic) events.
            pushToStack(change);
        } else if (change instanceof FocusLostEvent) {
            if (buffer > 0) {
                pushToStack(new GenericEvent(environment.getText())); // 'collect' all changes
                flushBuffer();
            }
        } else
            throw new IllegalStateException();
    }

    private void pushToStack(Event event) {
        if (undoStack.size() + 1 > capacity) {
            undoStack.remove(0);
        }
        undoStack.push(event);
    }

    private void flushBuffer() {
        buffer = 0;
    }

    private String extractText(Event event) {
        if (event instanceof WindowOpenEvent woe)
            return woe.initialText();
        else if (event instanceof GenericEvent ge)
            return ge.text();
        else
            return null;
    }

    @Override
    public Event performUndo() {
        flushBuffer();
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
