package org.theparanoidtimes.undodemo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventBasedUndoEngineTest {

    @Test
    void undoEngineWillSaveAndRetrieveAddTextEventChange() {
        String initialText = "Test";
        Environment environment = new Environment(initialText);
        EventBasedUndoEngine undoEngine = new EventBasedUndoEngine(2);

        undoEngine.registerNewChange(environment, new WindowOpenEvent(initialText));
        AddTextEvent addTextEvent = environment.addText("1");
        undoEngine.registerNewChange(environment, addTextEvent);

        Event firstUndo = undoEngine.performUndo();
        assertThat(firstUndo).isInstanceOf(AddTextEvent.class);
        AddTextEvent ate = (AddTextEvent) firstUndo;
        assertThat(ate.textToAdd()).isEqualTo("1");

        Event secondUndo = undoEngine.performUndo();
        assertThat(secondUndo).isInstanceOf(WindowOpenEvent.class);
        WindowOpenEvent woe = (WindowOpenEvent) secondUndo;
        assertThat(woe.initialText()).isEqualTo(initialText);
    }

    @Test
    void undoEngineWillSaveAndRetrieveEventInOrderTheyAppear() {
        String initialText = "Test";
        Environment environment = new Environment(initialText);
        EventBasedUndoEngine undoEngine = new EventBasedUndoEngine(3);

        undoEngine.registerNewChange(environment, new WindowOpenEvent(initialText));

        AddTextEvent addTextEvent = environment.addText("1");
        undoEngine.registerNewChange(environment, addTextEvent);

        RemoveTextEvent removeTextEvent = environment.removeText(0, 3);
        undoEngine.registerNewChange(environment, removeTextEvent);

        Event firstUndo = undoEngine.performUndo();
        assertThat(firstUndo).isInstanceOf(RemoveTextEvent.class);
        RemoveTextEvent rte = (RemoveTextEvent) firstUndo;
        assertThat(rte.getBeginIndex()).isEqualTo(0);
        assertThat(rte.getLength()).isEqualTo(3);
        assertThat(rte.getRemovedText()).isEqualTo("Tes");

        Event secondUndo = undoEngine.performUndo();
        assertThat(secondUndo).isInstanceOf(AddTextEvent.class);
        AddTextEvent ate = (AddTextEvent) secondUndo;
        assertThat(ate.textToAdd()).isEqualTo("1");

        Event thirdUndo = undoEngine.performUndo();
        assertThat(thirdUndo).isInstanceOf(WindowOpenEvent.class);
        WindowOpenEvent woe = (WindowOpenEvent) thirdUndo;
        assertThat(woe.initialText()).isEqualTo(initialText);
    }

    @Test
    void undoEngineWillHonorTheCapacityLimit() {
        String initialText = "Test";
        Environment environment = new Environment(initialText);
        EventBasedUndoEngine undoEngine = new EventBasedUndoEngine(2);

        undoEngine.registerNewChange(environment, new WindowOpenEvent(initialText));

        AddTextEvent addTextEvent = environment.addText("1");
        undoEngine.registerNewChange(environment, addTextEvent);

        RemoveTextEvent removeTextEvent = environment.removeText(0, 3);
        undoEngine.registerNewChange(environment, removeTextEvent);

        Event firstUndo = undoEngine.performUndo();
        assertThat(firstUndo).isInstanceOf(RemoveTextEvent.class);
        RemoveTextEvent rte = (RemoveTextEvent) firstUndo;
        assertThat(rte.getBeginIndex()).isEqualTo(0);
        assertThat(rte.getLength()).isEqualTo(3);
        assertThat(rte.getRemovedText()).isEqualTo("Tes");

        Event secondUndo = undoEngine.performUndo();
        assertThat(secondUndo).isInstanceOf(AddTextEvent.class);
        AddTextEvent ate = (AddTextEvent) secondUndo;
        assertThat(ate.textToAdd()).isEqualTo("1");

        Event thirdUndo = undoEngine.performUndo();
        assertThat(thirdUndo).isNull();
    }
}
