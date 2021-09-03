package com.thedramaticcolumnist.app.Model;

public class CartListModel {
    String id;
    CartListDetailsModel details;

    public CartListModel(String id, CartListDetailsModel details) {
        this.id = id;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CartListDetailsModel getDetails() {
        return details;
    }

    public void setDetails(CartListDetailsModel details) {
        this.details = details;
    }
}
