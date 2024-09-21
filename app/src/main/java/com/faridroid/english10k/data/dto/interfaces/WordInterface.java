package com.faridroid.english10k.data.dto.interfaces;

public interface WordInterface {

    String getId();

    String getWord();

    String getSpanish();

    boolean isLearned();

    void setLearned(boolean learned);
}
