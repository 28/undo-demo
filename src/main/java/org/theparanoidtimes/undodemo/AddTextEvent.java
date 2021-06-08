package org.theparanoidtimes.undodemo;

public record AddTextEvent(String textToAdd) implements Event {

    @Override
    public void apply(Environment environment) {
        environment.appendToExisting(textToAdd);
    }

    @Override
    public void unapply(Environment environment) {
        String currentText = environment.getText();
        int lastChangeStartIndex = currentText.lastIndexOf(textToAdd);
        String newText = currentText.substring(0, lastChangeStartIndex);
        environment.setText(newText);
    }
}
