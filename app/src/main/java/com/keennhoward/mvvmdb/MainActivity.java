package com.keennhoward.mvvmdb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //put requests code in constants so that it is easily distinguished
    public static final int ADD_NOTE_REQUEST = 1;

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Floating Action Button
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //use MainActivity.this instead of just this because it will point to the onClickListener
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST); //to get input back (Request Code is used to distinguish between requests)
            }
        });

        //setup recyclerview after NoteAdapter Class
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//this takes care of displaying the single items below each other
        recyclerView.setHasFixedSize(true); //set this if you know the recyclerview size will not change

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter); //by default the list in adapter is empty update it on onChanged in the observer

        //don't call new NoteViewModel because this will make a new instance every new activity instead ask the android system for a view model
        //the system knows whether to create a new instance or provide an existing instance
        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) { //this will be triggered every time the data in the live data changes(List<Notes>)
                //update recycler view
                //accessing adapter here will make it a final variable
                //update adapter every time there is a change in the note_table
                adapter.setNotes(notes);
            }
        }); //LiveData is lifecycle aware and will only update the activity if it is in the foreground if the activity is destroyed it will also clean the reference


        //adds swipe function to recycler view
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) { //(drag support(0 means not supported) ,swipe direction support)
            //onMove is for drag and drop functionality
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            //onSwiped is for swiping
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //deletes the note at viewHolder.getAdapterPosition()
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView); //use this to attach the ItemTouchHelper to your recycler view otherwise it will not be implemented

    }


    //get result back from AddNoteActivity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check which request we are handling && if resultCode is RESULT_OK
        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);

            //create a note from all the data to insert it into the database
            Note note = new Note(title, description,priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
        //this will also be triggered over the back button in the other activity(RESULT_CANCELLED)
        //add android:parentActivityName=".MainActivity" to the activity in the manifest you want a back button to show
        //also add android:launchMode="singleTop" on the main activity in the manifest so that once it goes back it will not trigger on create again and just go back to the previous one
        else{
            Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    //add menu to actionbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //add function to menu item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
