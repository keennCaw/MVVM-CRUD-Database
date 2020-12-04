package com.keennhoward.mvvmdb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> { //<NoteAdapter.NoteHolder> tells the recyclerview to use this ViewHolder

    private List<Note> notes = new ArrayList<>(); //assign to new ArrayList otherwise it wil be null

   //implement methods
    //<NoteAdapter.NoteHolder> is used in these methods
    @NonNull
    @Override //this is where we have to create and return a NoteHolder to use its layout
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override //this is where we get the data from the single note java objects into the views of the note holder
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    //this is where we assign how many items we want to see in the recyclerview
    @Override
    public int getItemCount() {
        return notes.size();
    }
    //to get the observed list of notes live data to the recyclerview
    public void setNotes(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged(); //to tell the adapter to redraw layout(Not the best solution)
    }

    //View Holder Class
    //this will hold the views in our single recyclerview items
    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        //create constructor matching super
        //here we can assign our three textViews
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            //assign textViews
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }
}
