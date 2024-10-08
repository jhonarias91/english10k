package com.faridroid.english10k.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "custom_words",
        foreignKeys = @ForeignKey(entity = CustomList.class,
                                  parentColumns = "id",
                                  childColumns = "list_id",
                                  onDelete = ForeignKey.CASCADE))
public class CustomWord {

    @PrimaryKey
    @NotNull
    private String id;

    @ColumnInfo(name = "list_id")
    @NotNull
    private String listId;  // Relationship with the custom list

    @ColumnInfo(name = "word")
    private String word;  // The word in the custom list

    @ColumnInfo(name = "spanish")
    private String spanish;  // The translation of the word

    @Ignore
    public CustomWord(String listId, String word, String spanish) {
        this.listId = listId;
        this.word = word;
        this.spanish = spanish;
    }

    public CustomWord(String id, String listId, String word, String spanish) {
        this.id = id;
        this.listId = listId;
        this.word = word;
        this.spanish = spanish;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
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
