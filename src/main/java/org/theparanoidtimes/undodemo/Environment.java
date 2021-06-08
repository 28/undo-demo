package org.theparanoidtimes.undodemo;

public class Environment {

    private String text;

    public Environment(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Environment{" +
                "text='" + text + '\'' +
                '}';
    }

    public String appendToExisting(String textToAppend) {
        text += textToAppend;
        return text;
    }

    public AddTextEvent addText(String text) {
        AddTextEvent event = new AddTextEvent(text);
        event.apply(this);
        return event;
    }

    public RemoveTextEvent removeText(int beginIndex, int length) {
        RemoveTextEvent event = new RemoveTextEvent(beginIndex, length);
        event.apply(this);
        return event;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
