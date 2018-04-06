package com.example.gianmarcocotellessa.flickrphotogallery;


import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    NetworkClient mNetworkClient;
    Listener mListener;
    List<GalleryItem> mItems;

    private static final String TAG = GalleryFragment.class.getSimpleName();
    private static final int COLUMN_NUM = 3;
    private static final int ITEM_PER_PAGE = 10;

    private RequestQueue mRq;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;

    private GalleryAdapter mAdapter;

    private SearchView mSearchView;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mRq = Volley.newRequestQueue(getActivity());

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), COLUMN_NUM);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GalleryAdapter(getActivity(), new ArrayList<GalleryItem>());
        mRecyclerView.setAdapter(mAdapter);


//        populateView();

        startLoading();
        return view;

    }

    public void refresh() {
        mAdapter.clear();
        startLoading();
//        populateView();
    }

    private void populateView() {

        int totalItem = mLayoutManager.getItemCount();
        final int page = totalItem / ITEM_PER_PAGE + 1;

        String query = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(UrlManager.PREF_SEARCH_QUERY, null);

        mItems = mListener.loading(query, page, mRq);

        mAdapter.addAll(mItems);

        mAdapter.notifyDataSetChanged();

    }

    private void startLoading() {

        Log.d(TAG, "startLoading");
        int totalItem = mLayoutManager.getItemCount();
        final int page = totalItem / ITEM_PER_PAGE + 1;

        String query = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(UrlManager.PREF_SEARCH_QUERY, null);

        mNetworkClient = new NetworkClient(query, page, mRq);

        mNetworkClient.getItems(new RetrieveItemsInterface.OnGetElementsListener() {
            @Override
            public void onReceiveItems(List<GalleryItem> items) {

                if (items == null) {

                    Log.d("errore", "rrrrrr");

                }

                mAdapter.addAll(items);

                mAdapter.notifyDataSetChanged();

            }

        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        mSearchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getActivity()
                .getSystemService(Context.SEARCH_SERVICE);
        ComponentName name = getActivity().getComponentName();
        SearchableInfo searchInfo = searchManager.getSearchableInfo(name);
        mSearchView.setSearchableInfo(searchInfo);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean selectionHandled;

        switch (item.getItemId()) {

            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                selectionHandled = true;
                break;

            default:
                selectionHandled = super.onOptionsItemSelected(item);
                break;

        }
        return selectionHandled;

    }

    /**
     * Implemented by GalleryActivity.
     */

    public interface Listener {

        List<GalleryItem> loading(String query, int page, RequestQueue rq);

    }

}


