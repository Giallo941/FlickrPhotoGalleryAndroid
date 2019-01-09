package com.example.gianmarcocotellessa.flickrphotogallery;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;

import java.util.List;

public class GalleryActivity extends AppCompatActivity implements GalleryFragment.Listener {

    NetworkClient mNetworkClient;
    List<GalleryItem> mNewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(UrlManager.PREF_SEARCH_QUERY, query)
                    .apply();

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.gallery_fragment);
            if (fragment != null) {
                ((GalleryFragment) fragment).refresh();
            }
        }
    }

    // *** GalleryFragment.Listener Implementation *** //

    @Override
    public List<GalleryItem> loading(String query, int page, RequestQueue rq) {
        mNetworkClient = new NetworkClient(query, page, rq);
        mNetworkClient.getItems(new RetrieveItemsInterface.OnGetElementsListener() {
            @Override
            public void onReceiveItems(List<GalleryItem> items) {
                mNewItems = items;
            }

        });

        return mNewItems;

    }

    public void onGalleryFragmentLoading(NetworkClient.OnGetElementsListener listener) {
        mNetworkClient.getItems(listener);
    }
}
