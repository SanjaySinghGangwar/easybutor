package com.thedramaticcolumnist.app.Model;

public class WishListModel {
    String id;
    WishListDetailsModel details;

    public WishListModel(String id, WishListDetailsModel details) {
        this.id = id;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WishListDetailsModel getDetails() {
        return details;
    }

    public void setDetails(WishListDetailsModel details) {
        this.details = details;
    }
}
