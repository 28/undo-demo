package org.theparanoidtimes.undodemo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SlightlyOptimizedSimpleUndoEngineTest {

    @Test
    void undoEngineWillSaveAndReturnASimpleTextChange() {
        Environment environment = new Environment("Test");
        SlightlyOptimizedSimpleUndoEngine undoEngine = new SlightlyOptimizedSimpleUndoEngine(1);

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
        SlightlyOptimizedSimpleUndoEngine undoEngine = new SlightlyOptimizedSimpleUndoEngine(3);

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
        SlightlyOptimizedSimpleUndoEngine undoEngine = new SlightlyOptimizedSimpleUndoEngine(2);

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

    @Test
    void undoEngineWillNotStoreTwoSameEntries() {
        Environment environment = new Environment("Test");
        SlightlyOptimizedSimpleUndoEngine undoEngine = new SlightlyOptimizedSimpleUndoEngine(2);

        String change = environment.appendToExisting("1");
        undoEngine.registerNewChange(environment, "ignored");

        String newChange = environment.appendToExisting(""); // empty so that environment is unchanged
        undoEngine.registerNewChange(environment, "ignored");

        assertThat(change).isEqualTo(newChange);
        String firstUndo = undoEngine.performUndo();
        assertThat(firstUndo).isEqualTo(change);
        assertThat(firstUndo).isEqualTo(newChange);
        String secondUndo = undoEngine.performUndo();
        assertThat(secondUndo).isNull();
    }
}
