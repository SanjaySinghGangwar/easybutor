package com.thedramaticcolumnist.app.Model;

public class DistributorListModel {
    String id;
    DistributorListDetails details;

    public DistributorListModel(String id, DistributorListDetails details) {
        this.id = id;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DistributorListDetails getDetails() {
        return details;
    }


    public void setDetails(DistributorListDetails details) {
        this.details = details;
    }
}
