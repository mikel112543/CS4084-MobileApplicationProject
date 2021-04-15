package com.example.cs4084_project_farm_market;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

//Firebase UI has its own powerful RecyclerAdapter
public class ListingAdapter extends FirestoreRecyclerAdapter<Listing, ListingAdapter.ListingHolder> {

    private OnItemClickListener listener;
    private OnButtonClickListener buttonClickListener;

    /**
     * Interface for onClick of card
     */
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Interface created for the button clicks here instead of implementation at the top of class
     */
    public interface OnButtonClickListener {
        void onButtonClick(DocumentSnapshot documentSnapshot, int position);
    }

    /**
     * Setting onClick Listeners
     *
     * @param buttonClickListener
     */
    public void setOnButtonClickListener(OnButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }


    //Creates a list of options to display
    public ListingAdapter(@NonNull FirestoreRecyclerOptions<Listing> options) {
        super(options);
    }

    /***
     *
     * @param holder object that will hold the view
     * @param position position of card on screen so can listen for onCLicks
     * @param model object to pull the data from
     */
    @Override
    protected void onBindViewHolder(@NonNull ListingHolder holder, int position, @NonNull Listing model) {
        holder.listingTitle.setText(model.getTitle());
        holder.listingLocation.setText(model.getLocation());
        holder.listingDate.setText(model.getDate());
        holder.listingPrice.setText(String.format("€%s", model.getPrice()));
        Picasso.get().load(model.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.listingImage);

    }

    @NonNull
    @Override
    public ListingAdapter.ListingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_card,
                parent, false);
        return new ListingHolder(v);
    }

    class ListingHolder extends RecyclerView.ViewHolder {
        TextView listingTitle;
        TextView listingLocation;
        TextView listingDate;
        TextView listingPrice;
        ImageView listingImage;
        Button saveButton;

        /**
         * Holder constructor to prepare view
         */
        public ListingHolder(@NonNull View itemView) {
            super(itemView);
            listingTitle = itemView.findViewById(R.id.listing_title);
            listingLocation = itemView.findViewById(R.id.location_text);
            listingDate = itemView.findViewById(R.id.dateCreated_text);
            listingPrice = itemView.findViewById(R.id.listing_price);
            listingImage = itemView.findViewById(R.id.listing_image);
            saveButton = itemView.findViewById(R.id.save_button);


            //Set onClicks ready to be called in fragment
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        buttonClickListener.onButtonClick(getSnapshots().getSnapshot(position), position);
                        saveButton.setBackground(ContextCompat.getDrawable(saveButton.getContext(), R.drawable.ic_baseline_favorite_24));
                    }

                }
            });
        }
    }
}
