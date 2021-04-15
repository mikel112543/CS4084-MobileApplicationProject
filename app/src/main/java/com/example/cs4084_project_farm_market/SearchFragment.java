package com.example.cs4084_project_farm_market;

import android.os.Bundle;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class SearchFragment extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference userRef = db.collection("users");
    private SearchUserAdapter adapter;
    private RecyclerView recyclerView;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_search, container, false);
        Query querySearch = userRef.orderBy("firstName");
        FirestoreRecyclerOptions<UserModal> options = new FirestoreRecyclerOptions.Builder<UserModal>()
                .setQuery(querySearch, UserModal.class)
                .build();

        adapter = new SearchUserAdapter(options);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnProfileClickListener(new SearchUserAdapter.OnProfileClickListener() {
            @Override
            public void onProfileClick(DocumentSnapshot documentSnapshot, int position) {
                //Intent to go to the users profile onClick
                /*Intent intent = new Intent(getActivity(), UserProfile.class);
                String id = documentSnapshot.getId();
                intent.putExtra("userID", id);
                startActivity(intent);*/

                /*Toast.makeText(getActivity(), "ID=" + id, Toast.LENGTH_SHORT).show();*/
            }
        });

        return mView;
        // Inflate the layout for this fragment
    }

    /***
     *
     * @param menu Search menu xml is inflated.
     * @param inflater inflates the view
     *
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            //Query is constantly searching while text is being input.
            //Options gets built from the query and then is sent to the adapter.
            //RecyclerView in displays filtered list of users.
            public boolean onQueryTextChange(String newText) {
                Query querySearch = userRef.orderBy("firstName").startAt(newText).endAt(newText + "\uf8ff");
                recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);

                FirestoreRecyclerOptions<UserModal> options = new FirestoreRecyclerOptions.Builder<UserModal>()
                        .setQuery(querySearch, UserModal.class)
                        .build();
                if (adapter == null) {
                    adapter = new SearchUserAdapter(options);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateOptions(options);
                }

                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        /*adapter.startListening();*/
        if (adapter != null) adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        /* adapter.stopListening();*/
        if (adapter != null) adapter.stopListening();
    }

}