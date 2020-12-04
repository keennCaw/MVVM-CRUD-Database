//must extend RoomDatabase and must be abstract
//this class connect all the different parts and create an actual instance of the database
//3
package com.keennhoward.mvvmdb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//you can use more the one entity you can use {Note.class,Random.class}
//whenever whe make changes to the database we need to increment the version number and provide a migration strategy. check documentation https://developer.android.com/training/data-storage/room/migrating-db-versions
@Database(entities = Note.class, version = 1) //tells room this is a database
public abstract class NoteDatabase extends RoomDatabase {

    //we use this variable so we can turn this class into a singleton
    //singleton means we cannot make multiple instances of this database instead we use only 1 instance anywhere on our app which we can access through the static variable
    private static NoteDatabase instance;

    //abstract because we don't provide a method body. similar to method in Dao
    //no body because room takes care of it all
    public abstract NoteDao noteDao(); //used to access Dao database methods. we use this in the repository class(Room subclasses this abstract class)

    //create database
    //make it a singleton
    //synchronized means only 1 thread at a time can access this method
    public static synchronized NoteDatabase getInstance(Context context){
        //here we create our single database instance and then we can call this method from the outside and get a handle to this instance
        if(instance == null){ //check if there is already an instance
            //create instance if null
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration() //if we don't add this and increase the version number the app will crash(deletes all tables and creates it from scratch)
                    .addCallback(roomCallback)//executes onCreate from roomCallback class to populate DB
                    .build(); // builds the instance
        }
        return instance; //returns the instance
    }


    //used to populate the database
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{

        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db){
            this.noteDao = db.noteDao(); //this is possible because onCreate is called after the database was created
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Sample","This is a Sample", 1));
            noteDao.insert(new Note("Sample1","This is a Sample", 2));
            noteDao.insert(new Note("Sample2","This is a Sample", 3));
            return null;
        }
    }
}
