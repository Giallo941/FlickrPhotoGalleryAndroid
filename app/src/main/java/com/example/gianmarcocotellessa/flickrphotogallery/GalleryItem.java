package com.example.gianmarcocotellessa.flickrphotogallery;

import java.io.Serializable;

public class GalleryItem implements Serializable {

    private String mId;
    private String mSecret;
    private String mServer;
    private String mFarm;

    public GalleryItem(String id, String secret, String server, String farm) {

        mId = id;
        mSecret = secret;
        mServer = server;
        mFarm = farm;

    }

    public String getId() {
        return mId;
    }

    public String getUrl() {

        return "http://farm" + mFarm + ".static.flickr.com/" + mServer + "/" + mId + "_" + mSecret + ".jpg";

    }


}
