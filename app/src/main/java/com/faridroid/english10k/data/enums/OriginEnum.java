package com.faridroid.english10k.data.enums;

public enum OriginEnum {

    ENGLISH10K(1),
    CUSTOM_WORDS(2);

    private final int value;

    OriginEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OriginEnum getByValue(int value) {
        for (OriginEnum originEnum : OriginEnum.values()) {
            if (originEnum.getValue() == value) {
                return originEnum;
            }
        }
        return null;
    }
}
