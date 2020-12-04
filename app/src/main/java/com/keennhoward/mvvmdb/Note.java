package com.keennhoward.mvvmdb;
//1
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table") //creates all necessary code to create an sqlite table for this object, tableName changes the name of the table default is the clas name
public class Note {

    //Room will generate columns for these fields

    @PrimaryKey(autoGenerate = true) //auto increment
    private int id;

    private String title;

    private String description;

    private int priority;

    //if any field is not in this constructor room cannot recreate it
    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    //Room will use this setter to set the id on the note object
    public void setId(int id) {
        this.id = id;
    }

    //Getter methods
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
