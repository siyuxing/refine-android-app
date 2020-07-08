package com.refine.model;

public class OwnerSummary {
    private String owner;
    private String productName;
    private Operation operation;
    private Integer numOfSuccess;
    private Integer numOfFailure;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Integer getNumOfSuccess() {
        return numOfSuccess;
    }

    public void setNumOfSuccess(Integer numOfSuccess) {
        this.numOfSuccess = numOfSuccess;
    }

    public Integer getNumOfFailure() {
        return numOfFailure;
    }

    public void setNumOfFailure(Integer numOfFailure) {
        this.numOfFailure = numOfFailure;
    }
}
