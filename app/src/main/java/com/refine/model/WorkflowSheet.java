package com.refine.model;

import java.io.Serializable;
import java.util.Date;

public class WorkflowSheet implements Serializable {
    private String sheetId;
    private Date startDate;
    private Date finishDate;
    private String material;
    private Integer numOfMaterial;
    private Long productId;
    private String productName;
    private Integer numOfRequested;
    private Integer numOfFinal;
    private String requester;

    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Integer getNumOfMaterial() {
        return numOfMaterial;
    }

    public void setNumOfMaterial(Integer numOfMaterial) {
        this.numOfMaterial = numOfMaterial;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getNumOfRequested() {
        return numOfRequested;
    }

    public void setNumOfRequested(Integer numOfRequested) {
        this.numOfRequested = numOfRequested;
    }

    public Integer getNumOfFinal() {
        return numOfFinal;
    }

    public void setNumOfFinal(Integer numOfFinal) {
        this.numOfFinal = numOfFinal;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }
}
