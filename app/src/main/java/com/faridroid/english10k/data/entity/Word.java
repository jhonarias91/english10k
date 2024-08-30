package com.faridroid.english10k.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "words")
public class Word {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "word", defaultValue = "")
    @NotNull
    private String word ;

    @ColumnInfo(name = "spanish", defaultValue = "")
    @NotNull
    private String spanish;

    @ColumnInfo(name = "range", defaultValue = "0")
    @NotNull
    private int range;

    @ColumnInfo(name = "updated")
    private Long updated;


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
