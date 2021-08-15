package org.theparanoidtimes.undodemo;

import java.util.Stack;

public class BufferedSimpleUndoEngine implements UndoEngine<GenericEvent> {

    private final int capacity;
    private final int bufferSize;

    private final Stack<GenericEvent> undoStack = new Stack<>();
    private int buffer;

    public BufferedSimpleUndoEngine(int capacity, int bufferSize) {
        this.capacity = capacity;
        this.bufferSize = bufferSize;
    }

    @Override
    public void registerNewChange(Environment environment, GenericEvent change) {
        GenericEvent lastEntry = lastEntry();
        if (lastEntry == null || !environment.getText().equals(lastEntry.text())) {
            if (buffer + 1 == bufferSize) {
                pushToStack(new GenericEvent(environment.getText())); // 'collect' all changes
                flushBuffer();
            } else
                buffer++;
        }
    }

    private void pushToStack(GenericEvent event) {
        if (undoStack.size() + 1 > capacity) {
            undoStack.remove(0);
        }
        undoStack.push(event);
    }

    private void flushBuffer() {
        buffer = 0;
    }

    @Override
    public GenericEvent performUndo() {
        flushBuffer(); // flush the buffer on undo to refresh the counter
        if (undoStack.size() > 0)
            return undoStack.pop();
        else
            return null;
    }

    @Override
    public GenericEvent lastEntry() {
        if (undoStack.size() > 0)
            return undoStack.peek();
        else
            return null;
    }
}
