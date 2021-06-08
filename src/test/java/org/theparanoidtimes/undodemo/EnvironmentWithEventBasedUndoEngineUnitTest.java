package org.theparanoidtimes.undodemo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentWithEventBasedUndoEngineUnitTest {

    @Test
    void environmentWillAddTextAndPerformUndo() {
        Environment environment = new Environment("Test");
        EventBasedUndoEngine undoEngine = new EventBasedUndoEngine(50);
        undoEngine.registerNewChange(environment, new WindowOpenEvent(environment.getText()));

        AddTextEvent addTextEvent = environment.addText("New");
        undoEngine.registerNewChange(environment, addTextEvent);
        assertThat(environment.getText()).isEqualTo("TestNew");

        Event event = undoEngine.performUndo();
        event.unapply(environment);
        assertThat(environment.getText()).isEqualTo("Test");
    }

    @Test
    void environmentWillRemoveTextAndPerformUndo() {
        Environment environment = new Environment("Test");
        EventBasedUndoEngine undoEngine = new EventBasedUndoEngine(50);
        undoEngine.registerNewChange(environment, new WindowOpenEvent(environment.getText()));

        RemoveTextEvent removeTextEvent = environment.removeText(0, 3);
        undoEngine.registerNewChange(environment, removeTextEvent);
        assertThat(environment.getText()).isEqualTo("t");
        assertThat(removeTextEvent.getRemovedText()).isEqualTo("Tes");

        Event event = undoEngine.performUndo();
        event.unapply(environment);
        assertThat(environment.getText()).isEqualTo("Test");
    }

    @Test
    void environmentWillApplyAndUnApplyChangesAsTheyAppear() {
        Environment environment = new Environment("Test");
        EventBasedUndoEngine undoEngine = new EventBasedUndoEngine(50);
        undoEngine.registerNewChange(environment, new WindowOpenEvent(environment.getText()));

        AddTextEvent addTextEvent = environment.addText("Test");
        undoEngine.registerNewChange(environment, addTextEvent);
        assertThat(environment.getText()).isEqualTo("TestTest");
        assertThat(addTextEvent.textToAdd()).isEqualTo("Test");

        undoEngine.registerNewChange(environment, new FocusLostEvent());

        RemoveTextEvent removeTextEvent = environment.removeText(3, 4);
        undoEngine.registerNewChange(environment, removeTextEvent);
        assertThat(environment.getText()).isEqualTo("Test");
        assertThat(removeTextEvent.getRemovedText()).isEqualTo("tTes");

        Event firstUndo = undoEngine.performUndo();
        firstUndo.unapply(environment);
        assertThat(environment.getText()).isEqualTo("TestTest");

        Event secondUndo = undoEngine.performUndo();
        secondUndo.unapply(environment);
        assertThat(environment.getText()).isEqualTo("Test");

        Event thirdUndo = undoEngine.performUndo();
        thirdUndo.unapply(environment);
        assertThat(environment.getText()).isEqualTo("");
    }
}
