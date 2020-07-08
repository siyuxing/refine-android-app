package com.refine.database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.refine.model.ProductStatus;
import com.refine.model.ProductStock;

public class ProductStockCallback implements ResultSetCallback<List<ProductStock>> {
    private static final String PRODUCT_NAME_COLUMN = "product_name";
    private static final String STATUS_COLUMN = "status";
    private static final String NUM_OF_PRODUCT_COLUMN = "number";

    private List<ProductStock> productStocks = new ArrayList<>();


    @Override
    public void processResultSet(ResultSet rs) throws Exception {
        do {
            ProductStock ownerSummary = new ProductStock();
            ownerSummary.setProductName(rs.getString(PRODUCT_NAME_COLUMN));
            ownerSummary.setStatus(ProductStatus.fromStatusCode(rs.getString(STATUS_COLUMN)));
            ownerSummary.setNumOfProduct(rs.getLong(NUM_OF_PRODUCT_COLUMN));

            productStocks.add(ownerSummary);
        } while(rs.next());
    }

    @Override
    public List<ProductStock> getResult() {
        return productStocks;
    }
}