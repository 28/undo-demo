package org.theparanoidtimes.undodemo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BufferedEventAwareSimpleUndoEngineTest {
    @Test
    void undoEngineWillSaveASimpleChange() {
        Environment environment = new Environment("Test");
        BufferedEventAwareSimpleUndoEngine undoEngine = new BufferedEventAwareSimpleUndoEngine(1, 1);

        String firstChange = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, new GenericEvent(firstChange));

        GenericEvent firstUndoEvent = undoEngine.performUndo();
        assertThat(firstUndoEvent.text()).isEqualTo(firstChange);
    }

    @Test
    void undoEngineWillFlushBufferWhenFull() {
        Environment environment = new Environment("Test");
        BufferedEventAwareSimpleUndoEngine undoEngine = new BufferedEventAwareSimpleUndoEngine(1, 3);

        String firstChange = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, new GenericEvent(firstChange));

        String secondChange = environment.appendToExisting("2");
        undoEngine.registerNewChange(environment, new GenericEvent(secondChange));

        String thirdChange = environment.appendToExisting("3");
        undoEngine.registerNewChange(environment, new GenericEvent(thirdChange));

        GenericEvent undoEvent = undoEngine.performUndo();
        assertThat(undoEvent.text()).isEqualTo(thirdChange);
        assertThat(undoEngine.performUndo()).isNull();
    }

    @Test
    void undoEngineWillSaveAndReturnChangesInOrderTheyAppeared() {
        Environment environment = new Environment("Test");
        BufferedEventAwareSimpleUndoEngine undoEngine = new BufferedEventAwareSimpleUndoEngine(2, 2);

        String firstChange = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, new GenericEvent(firstChange));

        String secondChange = environment.appendToExisting("2");
        undoEngine.registerNewChange(environment, new GenericEvent(secondChange));

        String thirdChange = environment.appendToExisting("3");
        undoEngine.registerNewChange(environment, new GenericEvent(thirdChange));

        String fourthChange = environment.appendToExisting("4");
        undoEngine.registerNewChange(environment, new GenericEvent(fourthChange));

        GenericEvent firstUndo = undoEngine.performUndo();
        assertThat(firstUndo.text()).isEqualTo(fourthChange);
        assertThat(firstUndo.text()).contains(firstChange);

        GenericEvent secondUndo = undoEngine.performUndo();
        assertThat(secondUndo.text()).isEqualTo(secondChange);
        assertThat(secondUndo.text()).contains(firstChange);
    }

    @Test
    void undoEngineWillHonorTheStackCapacity() {
        Environment environment = new Environment("Test");
        BufferedEventAwareSimpleUndoEngine undoEngine = new BufferedEventAwareSimpleUndoEngine(2, 2);

        String firstChange = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, new GenericEvent(firstChange));

        String secondChange = environment.appendToExisting("2");
        undoEngine.registerNewChange(environment, new GenericEvent(secondChange));

        String thirdChange = environment.appendToExisting("3");
        undoEngine.registerNewChange(environment, new GenericEvent(thirdChange));

        String fourthChange = environment.appendToExisting("4");
        undoEngine.registerNewChange(environment, new GenericEvent(fourthChange));

        String fifthChange = environment.appendToExisting("5");
        undoEngine.registerNewChange(environment, new GenericEvent(fifthChange));

        String sixthChange = environment.appendToExisting("6");
        undoEngine.registerNewChange(environment, new GenericEvent(sixthChange));

        GenericEvent firstUndo = undoEngine.performUndo();
        assertThat(firstUndo.text()).isEqualTo(sixthChange);

        GenericEvent secondUndo = undoEngine.performUndo();
        assertThat(secondUndo.text()).isEqualTo(fourthChange);

        GenericEvent thirdUndo = undoEngine.performUndo();
        assertThat(thirdUndo).isNull();
    }

    @Test
    void undoEngineWillFlushBufferOnFocusLostEvent() {
        Environment environment = new Environment("Test");
        BufferedEventAwareSimpleUndoEngine undoEngine = new BufferedEventAwareSimpleUndoEngine(2, 2);

        String firstChange = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, new GenericEvent(firstChange));

        String secondChange = environment.appendToExisting("2");
        undoEngine.registerNewChange(environment, new GenericEvent(secondChange));

        String thirdChange = environment.appendToExisting("3");
        undoEngine.registerNewChange(environment, new GenericEvent(thirdChange));

        undoEngine.registerNewChange(environment, new FocusLostEvent());

        GenericEvent firstUndo = undoEngine.performUndo();
        assertThat(firstUndo.text()).isEqualTo(thirdChange);

        GenericEvent secondUndo = undoEngine.performUndo();
        assertThat(secondUndo.text()).isEqualTo(secondChange);
    }
}
