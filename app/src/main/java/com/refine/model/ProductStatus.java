package com.refine.model;

public enum ProductStatus {
    待煮滤("poured"),
    待湿加工("zhulved"),
    待干燥("hydro"),
    待切割("dried"),
    待包装入库("cut"),
    成品("finished");

    private String statusCode;

    ProductStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    public static ProductStatus fromStatusCode(String statusCode) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.statusCode.equals(statusCode)) {
                return status;
            }
        }
        return null;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
