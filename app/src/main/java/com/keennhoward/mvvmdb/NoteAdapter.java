package com.keennhoward.mvvmdb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//changed from RecyclerView.Adapter<NoteAdapter.NoteHolder> to ListAdapter<Note, NoteAdapter.NoteHolder>
public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> { //<NoteAdapter.NoteHolder> tells the recyclerview to use this ViewHolder

    //removed : instead of keeping the list in the noteAdapter the list will be passed to the super class the ListAdapter
    //private List<Note> notes = new ArrayList<>(); //assign to new ArrayList otherwise it wil be null

    private OnItemClickListener listener; //use OnItemClickListener with your package name

    //create constructor matching super diffCallback #ListAdapter class
    public NoteAdapter() {

        super(DIFF_CALLBACK);
    }
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) { //check table entry
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {//checks if there is a change in the data
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() == newItem.getPriority();
        }
    };

    //implement methods
    //<NoteAdapter.NoteHolder> is used in these methods
    @NonNull
    @Override //this is where we have to create and return a NoteHolder to use its layout
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    //this is where we get the data from the single note java objects into the views of the note holder
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        Note currentNote = getItem(position); //Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    //this is where we assign how many items we want to see in the recyclerview
    //removed because list adapter will now take care of this
    //@Override
    //public int getItemCount() { return notes.size(); }

    //to get the observed list of notes live data to the recyclerview
    /*public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged(); //to tell the adapter to redraw layout(Not the best solution)
    }*/

    //public because we want to call it from the main activity
    //gets the note at the specified position and returns it
    public Note getNoteAt(int position) {
        return getItem(position); //to get the note from this adapter to the outside
    }

    //View Holder Class
    //this will hold the views in our single recyclerview items
    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        //create constructor matching super
        //here we can assign our three textViews
        //the itemView is the whole card view
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            //assign textViews
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            //set click listener to itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();//gets the position of the clicked item
                    //check if listener is = null because it is not guaranteed to be called
                    if (listener != null && position != RecyclerView.NO_POSITION) { //position != RecyclerView.NO_POSITION check so that you don't call an item with an invalid position
                        listener.onItemClick(getItem(position));// call onItemClick on this packages OnItemClickListener and then passes the note at the position
                    }
                }
            });
        }
    }

    //add click event to main activity
    public interface OnItemClickListener {
        //what ever implements this interface must also implement this method
        void onItemClick(Note note);
    }

    //to call methods from this adapter onto OnItemClickListener we need a reference to it
    //you can use the listener variable to call the onItemClick on it to forward the note object to whatever implements the interface it is on
    //save listener on a member variable(Global)
    public void setOnItemClickListener(OnItemClickListener listener) { //use OnItemClickListener with your package name
        //assign member variable listener to the listener that is passed
        this.listener = listener;
    }
}
