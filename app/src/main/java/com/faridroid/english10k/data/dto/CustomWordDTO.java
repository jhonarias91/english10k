package com.faridroid.english10k.data.dto;

public class CustomWordDTO {

    private String id;

    private String listId;  // Relationship with the custom list

    private String word;  // The word in the custom list

    private String spanish;  // The translation of the word

    public CustomWordDTO(String listId, String word, String spanish) {
        this.listId = listId;
        this.word = word;
        this.spanish = spanish;
    }

    public CustomWordDTO(String id, String listId, String word, String spanish) {
        this.id = id;
        this.listId = listId;
        this.word = word;
        this.spanish = spanish;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
