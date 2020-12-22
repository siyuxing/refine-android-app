package com.refine.model;

import java.io.Serializable;
import java.sql.Date;

public class WorkflowDetails implements Serializable {
    private Long id;
    private String workflowId;
    private String owner;
    private Long productId;
    private String productName;
    private Integer numOfTotal;
    private Integer numOfSuccess;
    private Integer numOfFailure;
    private Operation operation;
    private String additionNote;
    private Date startDate;
    private Date finishDate;
    private Boolean finish;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public Integer getNumOfTotal() {
        return numOfTotal;
    }

    public void setNumOfTotal(Integer numOfTotal) {
        this.numOfTotal = numOfTotal;
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

    public Boolean isFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }
}
