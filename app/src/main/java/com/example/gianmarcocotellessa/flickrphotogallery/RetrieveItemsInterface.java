package com.example.gianmarcocotellessa.flickrphotogallery;

import java.util.List;

public interface RetrieveItemsInterface {

    void getItems(OnGetElementsListener listener);

    interface OnGetElementsListener {

        void onReceiveItems(List<GalleryItem> items);

    }

}
