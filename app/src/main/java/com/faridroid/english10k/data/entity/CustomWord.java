package com.faridroid.english10k.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "custom_words",
        foreignKeys = @ForeignKey(entity = CustomList.class,
                                  parentColumns = "id",
                                  childColumns = "list_id",
                                  onDelete = ForeignKey.CASCADE))
public class CustomWord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "list_id")
    private int listId;  // Relationship with the custom list

    @ColumnInfo(name = "word")
    private String word;  // The word in the custom list

    @ColumnInfo(name = "spanish")
    private String spanish;  // The translation of the word

    // Constructor, getters, and setters

    public CustomWord(int listId, String word, String translation) {
        this.listId = listId;
        this.word = word;
        this.spanish = translation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSpanish() {
        return spanish;
    }

    public void setSpanish(String spanish) {
        this.spanish = spanish;
    }
}
