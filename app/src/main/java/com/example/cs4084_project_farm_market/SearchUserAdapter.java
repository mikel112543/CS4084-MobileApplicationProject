package com.example.cs4084_project_farm_market;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class SearchUserAdapter extends FirestoreRecyclerAdapter<User, SearchUserAdapter.UserModalHolder> {

    private OnProfileClickListener listener;

    public interface OnProfileClickListener {
        void onProfileClick(DocumentSnapshot documentSnapshot, int position);
    }

    /**
     * Set up on OnClick Listeners
     *
     * @param listener
     */
    public void setOnProfileClickListener(OnProfileClickListener listener) {
        this.listener = listener;
    }


    /**
     * List of options pull from Firestore
     *
     * @param options
     */
    public SearchUserAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    /**
     * @param holder   to hold the information and display to recyclerview
     * @param position postision of card on screen
     * @param model    POJO of User Modal
     */
    @Override
    protected void onBindViewHolder(@NonNull SearchUserAdapter.UserModalHolder holder, int position, @NonNull User model) {
        holder.userName.setText(String.format("%s %s", model.getFirstName(), model.getLastName()));
        holder.userDob.setText(model.getDob());
        holder.userNumber.setText(model.getNumber());
        Picasso.get().load(model.getUrl())
                .fit()
                .centerCrop()
                .into(holder.profileImage);
    }

    @NonNull
    @Override
    public SearchUserAdapter.UserModalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_search_card,
                parent, false);
        return new UserModalHolder(v);
    }

    /**
     * UserModalHolder class
     */
    class UserModalHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView userDob;
        TextView userNumber;
        ImageView profileImage;

        /**
         * Constructor
         *
         * @param itemView
         */
        public UserModalHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.search_profile_name);
            userDob = itemView.findViewById(R.id.search_profile_dob);
            userNumber = itemView.findViewById(R.id.search_profile_number);
            profileImage = itemView.findViewById(R.id.search_profile_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onProfileClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
}