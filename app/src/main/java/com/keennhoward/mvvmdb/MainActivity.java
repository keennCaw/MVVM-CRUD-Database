package com.keennhoward.mvvmdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //don't call new NoteViewModel because this will make a new instance every new activity instead ask the android system for a view model
        //the system knows whether to create a new instance or provide an existing instance
        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) { //this will be triggered every time the data in the live data changes(List<Notes>)
                //update recycler view

                Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
            }
        }); //LiveData is lifecycle aware and will only update the activity if it is in the foreground if the activity is destroyed it will also clean the reference
    }


}
