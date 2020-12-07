package com.keennhoward.mvvmdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    //psfs for auto complete public static final String
    //constants
    //best practice for intent Extra keys is to use the package name to keep them unique
    public static final String EXTRA_ID =
            "com.keennhoward.mvvmdb.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.keennhoward.mvvmdb.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.keennhoward.mvvmdb.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY=
            "com.keennhoward.mvvmdb.EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        //set Max min of number picker
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        //to get X on top left corner
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){ //check intent if it has the EXTRA with the key EXTRA_ID
            setTitle("Edit Note");
            //fill the fields with the values passed from the intent
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));

        }else {
            setTitle("Add Note"); //set action bar title
        }
    }

    private void saveNote(){
        //get input from fields
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        //check if editTexts are empty
        //trim removes spaces
        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return; //leaves the method (does not execute anything below it)
        }
        //best practice is to make 2 separate viewModel classes
        //but we will just send data back to the main activity using startActivityForResult
        //this activity just works like an input form and does not communicate with any other layout

        //create intent and put extra
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title); //(KEY, VALUE)
        data.putExtra(EXTRA_DESCRIPTION,description);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);//default is -1 because the database will not have an id that is -1 so if it is -1 it is invalid

        if(id != -1){
            data.putExtra(EXTRA_ID,id);
        }
        //to indicate if the input was successful or not
        setResult(RESULT_OK, data);

        finish();// close activity
    }

    //add Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //must be above return
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu); //use add_note_menu as the ,emu in this activity

        //must return true to display the menu false if not
        return true;
    }

    //to handle clicks on menu icons
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) { //to find out which one was clicked
            case R.id.save_note:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
