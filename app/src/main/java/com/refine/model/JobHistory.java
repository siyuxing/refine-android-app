package com.refine.model;

import java.sql.Date;

public class JobHistory {
    private Long id;
    private Date date;
    private String owner;
    private Integer numOfMaterial;
    private Integer numOfSuccess;
    private Integer numOfFailure;
    private String productName;
    private Operation operation;
    private String additionNote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getNumOfMaterial() {
        return numOfMaterial;
    }

    public void setNumOfMaterial(Integer numOfMaterial) {
        this.numOfMaterial = numOfMaterial;
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

    public String getAdditionNote() {
        return additionNote;
    }

    public void setAdditionNote(String additionNote) {
        this.additionNote = additionNote;
    }

}
