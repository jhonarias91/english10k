package com.faridroid.english10k.viewmodel;

public class SeekBarRange {
    private final int min;
    private final int progress;
    private final int max;

    public SeekBarRange(int min, int progress, int max) {
        this.min = min;
        this.progress = progress;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getProgress() {
        return progress;
    }

    public int getMax() {
        return max;
    }
}
