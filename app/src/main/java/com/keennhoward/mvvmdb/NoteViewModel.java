package com.keennhoward.mvvmdb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
//5
//you should never store a context of an activity or a view that references an activity in the ViewModel because it is designed to outlive an activity after it is destroyed(if you hold a reference to a destroyed activity you will have a memory leak)
//you need to pass a context to the repository for the database instance which is why you extend AndroidViewModel to get application and pass it as the context
//android activity should have a reference to the viewModel and not to the repository
public class NoteViewModel extends AndroidViewModel { //android view model is a subclass of view model
    //member variables

    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    //create constructor matching super
    public NoteViewModel(@NonNull Application application) { //AndroidViewModel gets passed the application in the constructor which can be used when the application context is needed
        super(application);

        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }


    //repository methods
    public void insert(Note note){
        repository.insert(note);
    }

    public void update(Note note){
        repository.update(note);
    }

    public void delete(Note note){
        repository.delete(note);
    }

    public void deleteAllNotes(){
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
}
