package org.theparanoidtimes.undodemo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BufferedSimpleUndoEngineTest {

    @Test
    void undoEngineWillSaveASimpleChange() {
        Environment environment = new Environment("Test");
        BufferedSimpleUndoEngine undoEngine = new BufferedSimpleUndoEngine(1, 1);

        String firstChange = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, new GenericEvent(firstChange));

        GenericEvent firstUndoEvent = undoEngine.performUndo();
        assertThat(firstUndoEvent.text()).isEqualTo(firstChange);
    }

    @Test
    void undoEngineWillFlushBufferWhenFull() {
        Environment environment = new Environment("Test");
        BufferedSimpleUndoEngine undoEngine = new BufferedSimpleUndoEngine(1, 3);

        String firstChange = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, new GenericEvent(firstChange)); // buffer = 1

        String secondChange = environment.appendToExisting("2");
        undoEngine.registerNewChange(environment, new GenericEvent(secondChange)); // buffer = 2

        String thirdChange = environment.appendToExisting("3");
        undoEngine.registerNewChange(environment, new GenericEvent(thirdChange)); // buffer = 3

        GenericEvent undoEvent = undoEngine.performUndo();
        assertThat(undoEvent.text()).isEqualTo(thirdChange);
        assertThat(undoEngine.performUndo()).isNull();
    }

    @Test
    void undoEngineWillSaveAndReturnChangesInOrderTheyAppeared() {
        Environment environment = new Environment("Test");
        BufferedSimpleUndoEngine undoEngine = new BufferedSimpleUndoEngine(2, 2);

        String firstChange = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, new GenericEvent(firstChange)); // buffer = 1, capacity = 0

        String secondChange = environment.appendToExisting("2");
        undoEngine.registerNewChange(environment, new GenericEvent(secondChange));  // buffer = 2, capacity = 0

        String thirdChange = environment.appendToExisting("3");
        undoEngine.registerNewChange(environment, new GenericEvent(thirdChange));  // buffer = 1, capacity = 1

        String fourthChange = environment.appendToExisting("4");
        undoEngine.registerNewChange(environment, new GenericEvent(fourthChange));  // buffer = 2, capacity = 1

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
        BufferedSimpleUndoEngine undoEngine = new BufferedSimpleUndoEngine(2, 2);

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
}
