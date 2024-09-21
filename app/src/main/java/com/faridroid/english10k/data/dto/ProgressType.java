package com.faridroid.english10k.data.dto;

public enum ProgressType {

    WORD_LEARNED(1),
    WORD_NOT_LEARNED(2),
    WORD_LEARNING(3);

    private final int value;

    ProgressType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


    public static ProgressType fromValue(int value) {
        for (ProgressType type : ProgressType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ProgressType value: " + value);
    }

}
