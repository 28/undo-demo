package org.theparanoidtimes.undodemo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnvironmentWithEventBasedUndoEngineTest {

    @Test
    void environmentWillAddTextAndPerformUndo() {
        String initialText = "Test";
        Environment environment = new Environment(initialText);
        EventBasedUndoEngine undoEngine = new EventBasedUndoEngine(50);
        undoEngine.registerNewChange(environment, new WindowOpenEvent(initialText));

        AddTextEvent addTextEvent = environment.addText("New");
        undoEngine.registerNewChange(environment, addTextEvent);
        assertThat(environment.getText()).isEqualTo("TestNew");

        Event event = undoEngine.performUndo();
        event.unapply(environment);
        assertThat(environment.getText()).isEqualTo(initialText);
    }

    @Test
    void environmentWillRemoveTextAndPerformUndo() {
        String initialText = "Test";
        Environment environment = new Environment(initialText);
        EventBasedUndoEngine undoEngine = new EventBasedUndoEngine(50);
        undoEngine.registerNewChange(environment, new WindowOpenEvent(initialText));

        RemoveTextEvent removeTextEvent = environment.removeText(0, 3);
        undoEngine.registerNewChange(environment, removeTextEvent);
        assertThat(environment.getText()).isEqualTo("t");
        assertThat(removeTextEvent.getRemovedText()).isEqualTo("Tes");

        Event event = undoEngine.performUndo();
        event.unapply(environment);
        assertThat(environment.getText()).isEqualTo(initialText);
    }

    @Test
    void environmentWillApplyAndUnApplyChangesAsTheyAppear() {
        String initialText = "Test";
        Environment environment = new Environment(initialText);
        EventBasedUndoEngine undoEngine = new EventBasedUndoEngine(50);
        undoEngine.registerNewChange(environment, new WindowOpenEvent(initialText));

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
        assertThat(environment.getText()).isEqualTo(initialText);

        Event thirdUndo = undoEngine.performUndo();
        thirdUndo.unapply(environment);
        assertThat(environment.getText()).isEmpty();
    }
}
