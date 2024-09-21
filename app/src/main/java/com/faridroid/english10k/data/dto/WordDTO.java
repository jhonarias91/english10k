package com.faridroid.english10k.data.dto;

import com.faridroid.english10k.data.dto.interfaces.WordInterface;

import org.jetbrains.annotations.NotNull;

public class WordDTO implements WordInterface {

    private String id;

    private String word;

    private String spanish;

    private int range;

    private Long updated;

    private boolean learned;


    public WordDTO(int id, String word, String spanish, int range, Long updated) {
        this.id = String.valueOf(id);
        this.word = word;
        this.spanish = spanish;
        this.range = range;
        this.updated = updated;
    }


    public WordDTO(int id,String word, String spanish) {
        this.id = String.valueOf(id);
        this.word = word;
        this.spanish = spanish;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    @NotNull
    public String getSpanish() {
        return spanish;
    }

    @Override
    public boolean isLearned() {
        return this.learned;
    }

    @Override
    public void setLearned(boolean learned) {
        this.learned = learned;
    }

    public void setSpanish(@NotNull String spanish) {
        this.spanish = spanish;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }
}

