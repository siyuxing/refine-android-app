package com.refine.model;

public class ProductStock {
    private String productName;
    private ProductStatus status;
    private Long numOfProduct;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Long getNumOfProduct() {
        return numOfProduct;
    }

    public void setNumOfProduct(Long numOfProduct) {
        this.numOfProduct = numOfProduct;
    }
}
