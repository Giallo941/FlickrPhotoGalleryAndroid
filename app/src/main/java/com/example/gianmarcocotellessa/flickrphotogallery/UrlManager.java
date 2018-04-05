package com.example.gianmarcocotellessa.flickrphotogallery;

import android.net.Uri;

public class UrlManager {

    public static final String API_KEY = "51885ef41dff54033e1de374307f110c";
    public static final String PREF_SEARCH_QUERY ="searchQuery";

    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String METHOD_GETRECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";

    public static String getItemUrl(String query, int page) {

        String url;

        if (query != null) {

            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_SEARCH)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("text", query)
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();

        } else {

            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GETRECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();

        }
        return url;

    }
    
}
