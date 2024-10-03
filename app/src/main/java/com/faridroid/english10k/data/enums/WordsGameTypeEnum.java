package com.faridroid.english10k.data.enums;

public enum WordsGameTypeEnum {

    TO_LEARN(1),
    LEARNED(2);

    private final int value;

    WordsGameTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static WordsGameTypeEnum getByValue(int value) {
        for (WordsGameTypeEnum originEnum : WordsGameTypeEnum.values()) {
            if (originEnum.getValue() == value) {
                return originEnum;
            }
        }
        return null;
    }
}
