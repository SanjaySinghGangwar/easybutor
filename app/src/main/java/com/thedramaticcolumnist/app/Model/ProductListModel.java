package com.thedramaticcolumnist.app.Model;

public class ProductListModel {
    String id;
    ProductdetailModel detail;

    public ProductListModel(String id, ProductdetailModel detail) {
        this.id = id;
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductdetailModel getDetail() {
        return detail;
    }

    public void setDetail(ProductdetailModel detail) {
        this.detail = detail;
    }
}
