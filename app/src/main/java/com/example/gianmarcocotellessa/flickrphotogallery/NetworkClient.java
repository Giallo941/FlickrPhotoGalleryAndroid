package com.example.gianmarcocotellessa.flickrphotogallery;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NetworkClient implements RetrieveItemsInterface {

    String mQuery;
    int mPage;
    RequestQueue mRq;

    public NetworkClient(String query, int page, RequestQueue rq) {
        mQuery = query;
        mPage = page;
        mRq = rq;
    }

    @Override
    public void getItems(final OnGetElementsListener listener) {
        String url = UrlManager.getItemUrl(mQuery, mPage);

        JsonObjectRequest request = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<GalleryItem> result = new ArrayList<>();
                        try {
                            JSONObject photos = response.getJSONObject("photos");
                            JSONArray photoArr = photos.getJSONArray("photo");

                            for (int i = 0; i < 99; i++) {
                                JSONObject itemObj = photoArr.getJSONObject(i);
                                GalleryItem item = new GalleryItem(
                                        itemObj.getString("id"),
                                        itemObj.getString("secret"),
                                        itemObj.getString("server"),
                                        itemObj.getString("farm")
                                );
                                result.add(item);
                            }
                            listener.onReceiveItems(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onReceiveItems(null);

                    }

                }
        );

        mRq.add(request);
    }

}
