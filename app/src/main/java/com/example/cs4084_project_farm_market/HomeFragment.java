package com.example.cs4084_project_farm_market;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference listingRef = db.collection("listings");
    private ListingAdapter adapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home, container, false);
        setUpRecyclerView(mView);
        FirebaseFirestore.setLoggingEnabled(true);
        return mView;
    }

    /***
     *
     * @param view Inflated layout in parent activity
     *             Firestore Recycler builds options from inputted query to the database
     *
     */
    private void setUpRecyclerView(View view) {

        Query query = listingRef.orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                .setQuery(query, Listing.class)
                .build();

        adapter = new ListingAdapter(options);

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //OnClick for Listing
        adapter.setOnItemClickListener(new ListingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Listing listing = documentSnapshot.toObject(Listing.class);
                Intent intent = new Intent(getActivity(), ExpandedListing.class);
                String id = documentSnapshot.getId();
                intent.putExtra("documentId", id);
                startActivity(intent);


            }
        });
        //OnClick for save button
        adapter.setOnButtonClickListener(new ListingAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(DocumentSnapshot documentSnapshot, int position) {
                Listing listing = documentSnapshot.toObject(Listing.class);
                String title = listing.getTitle();
                String description = listing.getDescription();
                String price = listing.getPrice();
                String imageUrl = listing.getImageUrl();

            }
        });
    }

    /***
     * Adapter will start listening when on screen
     */
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    /***
     * Adapter will stop listening when off screen
     */
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}