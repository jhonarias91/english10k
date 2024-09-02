package com.faridroid.english10k.data.dto;

import org.jetbrains.annotations.NotNull;

public class WordDTO {

    private int id;

    private String word;

    private String spanish;

    private int range;

    private Long updated;


    public WordDTO(int id, String word, String spanish, int range, Long updated) {
        this.id = id;
        this.word = word;
        this.spanish = spanish;
        this.range = range;
        this.updated = updated;
    }


    public WordDTO(String word, String spanish) {
        this.word = word;
        this.spanish = spanish;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @NotNull
    public String getSpanish() {
        return spanish;
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

