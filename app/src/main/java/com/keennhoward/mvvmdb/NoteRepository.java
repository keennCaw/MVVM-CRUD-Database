package com.keennhoward.mvvmdb;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    //member variables
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    //constructor
    public NoteRepository(Application application){ //application is the subclass of Context which will be used to create the database instance
        NoteDatabase database = NoteDatabase.getInstance(application);

        //assign
        noteDao = database.noteDao(); //from note database class
        allNotes = noteDao.getAllNotes(); //from the NoteDao Interface
    }

    //methods for different database operations
    //Room does not allow database operations on the main thread(if we were to do this the app would crash)
    //the view model calls these methods
    public void insert(Note note){
        //create instance of InsertNoteAsyncTask
        new InsertNoteAsyncTask(noteDao).execute(note); //pass noteDao and note(what we want to insert)
    }
    public void update(Note note){
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(Note note){
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    public void deleteAllNotes(){
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    //Room executes LiveData return from the background thread but for the other method you have to do it manually
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


    //it has to be static so it does not have a reference to the repository itself otherwise this would cause a memory leak
    //Async Task for the database operations

    //Insert Async Task
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;
        //constructor to access the noteDao from the repository
        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]); //notes[0] because we get passed var args(similar to array).
            return null;
        }
    }

    //Update Async Task
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;
        //constructor to access the noteDao from the repository
        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]); //notes[0] because we get passed var args(similar to array).
            return null;
        }
    }

    //DeleteNote Async task
    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;
        //constructor to access the noteDao from the repository
        private DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]); //notes[0] because we get passed var args(similar to array).
            return null;
        }
    }


    //Delete all
    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void>{

        private NoteDao noteDao;
        //constructor to access the noteDao from the repository
        private DeleteAllNotesAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
