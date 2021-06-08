package org.theparanoidtimes.undodemo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleUndoEngineUnitTest {

    @Test
    void undoEngineWillSaveAndReturnASimpleTextChange() {
        Environment environment = new Environment("Test");
        SimpleUndoEngine undoEngine = new SimpleUndoEngine(1);

        String newText = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, "ignored");

        String oldText = undoEngine.performUndo();
        assertThat(oldText).isEqualTo(newText);

        String initialState = undoEngine.performUndo();
        assertThat(initialState).isNull();
    }

    @Test
    void undoEngineWillSaveAndReturnSimpleTextChangesInOrderTheyHappened() {
        Environment environment = new Environment("Test");
        SimpleUndoEngine undoEngine = new SimpleUndoEngine(3);

        String firstChange = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, "ignored");

        String secondChange = environment.appendToExisting("2");
        undoEngine.registerNewChange(environment, "ignored");

        String thirdChange = environment.appendToExisting("3");
        undoEngine.registerNewChange(environment, "ignored");

        String firstUndo = undoEngine.performUndo();
        assertThat(firstUndo).isEqualTo(thirdChange);

        String secondUndo = undoEngine.performUndo();
        assertThat(secondUndo).isEqualTo(secondChange);

        String thirdUndo = undoEngine.performUndo();
        assertThat(thirdUndo).isEqualTo(firstChange);
    }

    @Test
    void undoEngineWillHonorTheCapacityLimit() {
        Environment environment = new Environment("Test");
        SimpleUndoEngine undoEngine = new SimpleUndoEngine(2);

        environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, "ignored");

        String secondChange = environment.appendToExisting("2");
        undoEngine.registerNewChange(environment, "ignored");

        String thirdChange = environment.appendToExisting("3");
        undoEngine.registerNewChange(environment, "ignored");

        String firstUndo = undoEngine.performUndo();
        assertThat(firstUndo).isEqualTo(thirdChange);

        String secondUndo = undoEngine.performUndo();
        assertThat(secondUndo).isEqualTo(secondChange);

        String thirdUndo = undoEngine.performUndo();
        assertThat(thirdUndo).isNull();
    }
}
