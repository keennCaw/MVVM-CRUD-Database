package com.keennhoward.mvvmdb;
//2
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Database operations for the note entity. must be an interface (Good approach: create one Dao per entity)
//Typos and errors are detected while coding

@Dao //tells room that this interface is a Dao
public interface NoteDao {

    @Insert //the database operation
    void insert(Note note); //(return type) (method name) (parameter)

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    //if there is no ready made annotation you can use @Query
    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    //get all from note_table
    //room will check if the columns fit the java object
    //add LiveData to make the object observable. if there is any change in the note_table the value will automatically be updated and the activity will be notified
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes(); //just add LiveData Room will do the rest
}
