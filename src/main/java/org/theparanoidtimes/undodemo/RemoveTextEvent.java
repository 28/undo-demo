package org.theparanoidtimes.undodemo;

public class RemoveTextEvent implements Event {

    private final int beginIndex;
    private final int length;

    private String removedText;

    public RemoveTextEvent(int beginIndex, int length) {
        this.beginIndex = beginIndex;
        this.length = length;
    }

    @Override
    public void apply(Environment environment) {
        String currentText = environment.getText();
        this.removedText = environment.getText().substring(beginIndex, beginIndex + length);
        String beforeChange = currentText.substring(0, beginIndex);
        String afterChange = currentText.substring(beginIndex + length);
        environment.setText(beforeChange + afterChange);
    }

    @Override
    public void unapply(Environment environment) {
        if (removedText != null) {
            String currentText = environment.getText();
            String before = currentText.substring(0, beginIndex);
            String after = currentText.substring(beginIndex);
            environment.setText(before + removedText + after);
        }
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public int getLength() {
        return length;
    }

    public String getRemovedText() {
        return removedText;
    }
}
