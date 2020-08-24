package com.refine.database;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.refine.account.AccountProfileLocator;
import com.refine.model.JobHistory;
import com.refine.model.Operation;
import com.refine.model.OwnerSummary;
import com.refine.model.ProductStatus;
import com.refine.model.ProductStock;
import com.refine.model.User;
import com.refine.model.WorkflowDetails;
import com.refine.model.WorkflowSheet;

public class DatabaseHelper {
    private static final String DROP_USER_QUERY = "DROP USER IF EXISTS ?@'%'";
    private static final String CREATE_USER_QUERY = "CREATE USER IF NOT EXISTS ?@'%' IDENTIFIED BY ?";
    private static final String GRANT_ADMIN_PERMISSION_QUERY = "GRANT ALL PRIVILEGES ON *.* TO ?@'%'";
    private static final String GRANT_GRANT_OPTION_QUERY = "GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP," +
                                                                   " RELOAD, PROCESS, REFERENCES, INDEX, ALTER, CREATE TEMPORARY TABLES," +
                                                                   " LOCK TABLES, EXECUTE, REPLICATION SLAVE, REPLICATION CLIENT, CREATE VIEW," +
                                                                   " SHOW VIEW, CREATE ROUTINE, ALTER ROUTINE, CREATE USER, EVENT, TRIGGER" +
                                                                   " ON *.* TO ?@'%'";
    private static final String GRANT_NORMAL_PERMISSION_QUERY = "GRANT SELECT, INSERT, UPDATE, DELETE ON refine.* TO ?@'%'";
    private static final String FLUSH_PRIVILEGES = "FLUSH PRIVILEGES";
    private static final String LIST_ALL_USERS_QUERY = "SELECT User from mysql.User where Host='%'";

    private static final String CHECK_USER_INFO_QUERY = "SELECT count(*) FROM users WHERE username = ?";
    private static final String INSERT_USER_INFO_QUERY = "INSERT INTO users (username, display_name, is_admin) VALUES (?, ?, ?)";
    private static final String MODIFY_USER_INFO_QUERY = "UPDATE users SET display_name = ?, is_admin = ? WHERE username = ?";
    private static final String INSERT_USER_PERMISSION_QUERY = "INSERT INTO user_permissions (username, permission) VALUES (?, ?)";
    private static final String REMOVE_USER_PERMISSIONS_QUERY = "DELETE FROM user_permissions WHERE username = ?";
    private static final String SELECT_USER_INFO_QUERY = "SELECT * FROM users WHERE username = ?";

    private static final String CHECK_PRODUCT_QUERY = "SELECT count(*) FROM products WHERE product_name = ?";
    private static final String ADD_PRODUCT_QUERY = "INSERT INTO products (product_name) VALUES (?)";
    private static final String MODIFY_PRODUCT_QUERY = "UPDATE products SET product_name = ? WHERE product_name = ?";
    private static final String LIST_PRODUCTS_QUERY = "SELECT product_name from products";
    private static final String DELETE_JOB_HISTORY_FOR_PRODUCT_QUERY = "DELETE FROM job_history WHERE product_name = ?";
    private static final String DELETE_PRODUCT_STOCK_FOR_PRODUCT_QUERY = "DELETE FROM products_stock WHERE product_name = ?";
    private static final String DELETE_PRODUCT_QUERY = "DELETE FROM products WHERE product_name = ?";
    private static final String SELECT_PRODUCT_ID_QUERY = "SELECT id FROM products WHERE product_name = ?";

    private static final String CHECK_PRODUCT_COUNT_QUERY = "SELECT number from product_stock WHERE product_id = ? AND status = ?";
    private static final String ADD_PRODUCTS_TO_STOCK = "INSERT INTO product_stock (product_id, number, status) values (?, ?, ?)";
    private static final String MODIFY_PRODUCT_COUNT_IN_STOCK = "UPDATE product_stock SET number = ? WHERE product_id = ? AND status = ?";

    private static final String ADD_JOB_HISTORY_QUERY = "INSERT INTO job_history (product_name, date, owner_username, operation, additional_note," +
                                                          " success, failure, material_num) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SEARCH_JOB_HISTORY = "SELECT * FROM job_history WHERE date >= ? AND date <= ?";
    private static final String SEARCH_JOB_HISTORY_ORDER_BY = " ORDER BY owner_username, product_name, date";
    private static final String GET_JOB_HISTORY_QUERY = "SELECT * FROM job_history WHERE id = ?";
    private static final String DELETE_JOB_HISTORY_QUERY = "DELETE FROM job_history WHERE id = ?";
    private static final String MODIFY_JOB_HISTORY_QUERY = "UPDATE job_history SET date = ?, additional_note = ?, success = ?, failure = ?" +
                                                                   " WHERE id = ?";

    private static final String ADD_WORKFLOW_SHEET_QUERY = "INSERT INTO workflow_sheet (product_id, id, start_date, material, material_num," +
                                                                " expected_product_count, requester) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String COMPLETE_WORKFLOW_SHEET_QUERY = "UPDATE workflow_sheet SET end_date = ?, final_product_count = ? WHERE id = ?";
    private static final String SEARCH_WORKFLOW_SHEET_QUERY = "SELECT ws.*, product_name FROM workflow_sheet ws join products p on ws.product_id = p.id " +
                                                                      "WHERE start_date >= ? AND start_date <= ?";
    private static final String SEARCH_WORKFLOW_SHEET_ORDER_BY = " ORDER BY ws.id, product_id";

    private static final String ADD_WORKFLOW_DETAILS_QUERY = "INSERT INTO workflow_details (workflow_id, product_id, start_date, total, operation)"
                                                                   + " VALUES (?, ?, ?, ?, ?)";
    private static final String CONFIRM_WORKFLOW_DETAILS_QUERY = "UPDATE workflow_details SET owner_username = ?, success = ?, failure = ?, finish_date = ?,"
                                                                         + " additional_note = ?, is_finish = true WHERE id = ?";
    private static final String SEARCH_PENDING_WORKFLOW_DETAILS_QUERY = "SELECT wd.*, product_name FROM workflow_details wd join products p on wd.product_id = p.id" +
                                                                                " WHERE operation = ? AND is_finish = '0'";
    private static final String GET_WORKFLOW_DETAILS_QUERY = "SELECT wd.*, product_name FROM workflow_details wd join products p on wd.product_id = p.id WHERE wd.id = ?";
    private static final String GET_WORKFLOW_DETAILS_BY_SHEET_ID_QUERY = "SELECT wd.*, product_name FROM workflow_details wd join products p on wd.product_id = p.id WHERE wd.workflow_id = ?";


    private static final String ADD_DISCHARGE_HISTORY_QUERY = "INSERT INTO discharge_history (date, owner_username, count, product_id, additional_note)"
                                                                     + " VALUES (?, ?, ?, ?, ?)";

    private static final String SEARCH_OWNER_SUMMARY = "select owner_username, product_id, product_name, operation, SUM(success) as total_success, SUM(failure) as total_failure" +
                                                               " FROM workflow_details wd join products p on wd.product_id = p.id WHERE finish_date >= ? AND finish_date <= ?";
    private static final String SEARCH_OWNER_GROUP_BY = " GROUP BY owner_username, product_name, operation";
    private static final String SEARCH_OWNER_ORDER_BY = " ORDER BY owner_username, product_name";

    private static final String SEARCH_PRODUCT_STOCK = "select ps.*, product_name FROM product_stock ps join products p on ps.product_id = p.id WHERE product_name = ?";
    private static final String SEARCH_PRODUCT_STOCK_STATUS_FILTER = " AND status = ?";

    private static final String REQUESTER_FILTER = " AND requester = ?";
    private static final String OWNER_USERNAME_FILTER = " AND owner_username = ?";
    private static final String PRODUCT_NAME_FILTER = " AND product_name = ?";
    private static final String PRODUCT_ID_FILTER = " AND product_id = ?";
    private static final String OPERATION_FILTER = " AND operation = ?";

    // User related queries
    public static void createUser(String username, String password, boolean adminUser) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();
        // Recreate user to reset password and permissions
        dbAccessor.execute(DROP_USER_QUERY, new String[] {username});
        dbAccessor.execute(CREATE_USER_QUERY, new String[] {username, password});

        if (adminUser) {
            dbAccessor.execute(GRANT_ADMIN_PERMISSION_QUERY, new String[] {username});
            dbAccessor.execute(GRANT_GRANT_OPTION_QUERY, new String[] {username});
        } else {
            dbAccessor.execute(GRANT_NORMAL_PERMISSION_QUERY, new String[] {username});
        }
        dbAccessor.execute(FLUSH_PRIVILEGES, null);
    }

    public static void updateUser(String username, String password, boolean adminUser) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        if (password != null) {
            // Recreate user to reset password and permissions
            dbAccessor.execute(DROP_USER_QUERY, new String[]{username});
            dbAccessor.execute(CREATE_USER_QUERY, new String[]{username, password});
        }

        if (adminUser) {
            dbAccessor.execute(GRANT_ADMIN_PERMISSION_QUERY, new String[] {username});
            dbAccessor.execute(GRANT_GRANT_OPTION_QUERY, new String[] {username});
        } else {
            dbAccessor.execute(GRANT_NORMAL_PERMISSION_QUERY, new String[] {username});
        }
        dbAccessor.execute(FLUSH_PRIVILEGES, null);
    }

    public static void dropUser(String username) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.execute(REMOVE_USER_PERMISSIONS_QUERY, new String[] {username});
        SingleNumberCallback checkCallback = new SingleNumberCallback();
        dbAccessor.query(CHECK_USER_INFO_QUERY, new String[] {username}, checkCallback);
        if (checkCallback.getResult() == 1L) {
            dbAccessor.execute(MODIFY_USER_INFO_QUERY, new Object[]{"已删除", 0, username});
        }
        // Delete database user
        dbAccessor.execute(DROP_USER_QUERY, new String[] {username});
    }

    public static List<String> listAllUserNames() throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        SingleStringListCallback callback = new SingleStringListCallback();
        dbAccessor.query(LIST_ALL_USERS_QUERY, callback);

        return callback.getResult();
    }

    public static List<User> listAllUsers() throws Exception {
        List<User> users = new ArrayList<>();

        for (String userName : listAllUserNames()) {
            users.add(getUser(userName));
        }
        return users;
    }

    public static List<String> listAllOtherUserNames() throws Exception {
        List<String> allUsers = listAllUserNames();
        allUsers.remove(fetchDBAccessor().getUsername());

        return allUsers;
    }

    public static List<User> listAllOtherUsers() throws Exception {
        List<User> users = new ArrayList<>();

        for (String userName : listAllOtherUserNames()) {
            users.add(getUser(userName));
        }
        return users;
    }

    public static void updateUserInformation(String username, String displayName, boolean adminUser, List<String> permissions) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        SingleNumberCallback checkCallback = new SingleNumberCallback();
        dbAccessor.query(CHECK_USER_INFO_QUERY, new String[] {username}, checkCallback);

        if (checkCallback.getResult() == 1L) {
            dbAccessor.execute(MODIFY_USER_INFO_QUERY, new Object[] {displayName, adminUser, username});
        } else {
            dbAccessor.execute(INSERT_USER_INFO_QUERY, new Object[] {username, displayName, adminUser});
        }

        for (String permission : permissions) {
            try {
                dbAccessor.execute(INSERT_USER_PERMISSION_QUERY, new String[]{username, permission});
            } catch (Exception e) {
                // permission already exists, move on
                e.printStackTrace();
            }
        }
    }

    // Product related queries
    public static void addProduct(String productName) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        SingleNumberCallback checkCallback = new SingleNumberCallback();
        dbAccessor.query(CHECK_PRODUCT_QUERY, new String[] {productName}, checkCallback);
        if (checkCallback.getResult() == 1L) {
            throw new ResourceAlreadyExistException("Product already exist!");
        } else {
            dbAccessor.execute(ADD_PRODUCT_QUERY, new String[] {productName});
        }
    }

    public static void modifyProduct(String productName, String newProductName) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        SingleNumberCallback checkCallback = new SingleNumberCallback();
        dbAccessor.query(CHECK_PRODUCT_QUERY, new String[] {newProductName}, checkCallback);
        if (checkCallback.getResult() == 1L) {
            throw new ResourceAlreadyExistException("Product already exist!");
        } else {
            dbAccessor.execute(MODIFY_PRODUCT_QUERY, new String[] {newProductName, productName});
        }
    }

    public static List<String> listProducts() throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        SingleStringListCallback callback = new SingleStringListCallback();
        dbAccessor.query(LIST_PRODUCTS_QUERY, callback);

        return callback.getResult();
    }

    public static void deleteProduct(String productName) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        //dbAccessor.execute(DELETE_JOB_HISTORY_FOR_PRODUCT_QUERY, new String[] {productName});
        //dbAccessor.execute(DELETE_PRODUCT_STOCK_FOR_PRODUCT_QUERY, new String[] {productName});
        dbAccessor.execute(DELETE_PRODUCT_QUERY, new String[] {productName});
    }

    public static Long getProductId(String productName) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        SingleNumberCallback callback = new SingleNumberCallback();
        dbAccessor.query(SELECT_PRODUCT_ID_QUERY, new String[] {productName}, callback);

        return callback.getResult();
    }

    // Product stock related queries
    public static void mutateProductCountInStock(Long productId, String productStatus, int increment) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        if (increment == 0) {
            return;
        }

        SingleNumberCallback checkCallback = new SingleNumberCallback();
        dbAccessor.query(CHECK_PRODUCT_COUNT_QUERY, new Object[] {productId, productStatus}, checkCallback);
        Long productCount = checkCallback.getResult();
        if (productCount == null) {
            if (increment >= 0) {
                dbAccessor.execute(ADD_PRODUCTS_TO_STOCK, new Object[] {productId, (long) increment, productStatus});
            } else {
                throw new InsuffcientProductException(String.format("%s的产品数量不足(%d), 现有数量(%d).",
                                                                    ProductStatus.fromStatusCode(productStatus), Math.abs(increment), 0));
            }
        } else {
            if (productCount + increment >= 0) {
                dbAccessor.execute(MODIFY_PRODUCT_COUNT_IN_STOCK, new Object[] {productCount + increment, productId, productStatus});
            } else {
                throw new InsuffcientProductException(String.format("%s的产品数量不足(%d), 现有数量(%d).",
                                                                    ProductStatus.fromStatusCode(productStatus), increment, productCount));
            }
        }
    }

    public static void updateProductCountInStock(Long productId, String productStatus, long count) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        SingleNumberCallback checkCallback = new SingleNumberCallback();
        dbAccessor.query(CHECK_PRODUCT_COUNT_QUERY, new Object[] {productId, productStatus}, checkCallback);
        Long productCount = checkCallback.getResult();
        if (productCount == null) {
            dbAccessor.execute(ADD_PRODUCTS_TO_STOCK, new Object[] {productId, count, productStatus});
        } else {
            dbAccessor.execute(MODIFY_PRODUCT_COUNT_IN_STOCK, new Object[] {count, productId, productStatus});
        }
    }

    // Job History related queries
    @Deprecated
    public static void addJobHistory(String productName, Date date, String ownerName, String operation, String note,
                                     Integer successCount, Integer failCount, Integer materialCount) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.execute(ADD_JOB_HISTORY_QUERY, new Object[] {productName, date, ownerName, operation, note, successCount, failCount, materialCount});
    }

    @Deprecated
    public static void updateJobHistory(Long jobId, Date date, String note, Integer successCount, Integer failCount) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.execute(MODIFY_JOB_HISTORY_QUERY, new Object[] {date, note, successCount, failCount, jobId});
    }

    @Deprecated
    public static List<JobHistory> searchJobHistory(Date startDate, Date endDate, String owner, String productName, String operation) throws Exception {
        String query = SEARCH_JOB_HISTORY;
        List<Object> params = Lists.newArrayList(startDate, endDate);
        if (owner != null) {
            query += OWNER_USERNAME_FILTER;
            params.add(owner);
        }
        if (productName != null) {
            query += PRODUCT_NAME_FILTER;
            params.add(productName);
        }
        if (operation != null) {
            query += OPERATION_FILTER;
            params.add(operation);
        }
        query += SEARCH_JOB_HISTORY_ORDER_BY;
        JobHistoriesCallback callback = new JobHistoriesCallback();
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.query(query, params.toArray(), callback);

        return callback.getResult();
    }

    @Deprecated
    public static JobHistory getJobHistory(Long jobId) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        JobHistoriesCallback callback = new JobHistoriesCallback();
        dbAccessor.query(GET_JOB_HISTORY_QUERY, new Object[] {jobId}, callback);

        if (callback.getResult().isEmpty()) {
            return null;
        } else {
            return Iterables.getOnlyElement(callback.getResult());
        }
    }

    @Deprecated
    public static void deleteJobHistory(Long jobId) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.execute(DELETE_JOB_HISTORY_QUERY, new Object[] {jobId});
    }

    // Workflow Sheet related queries
    public static void addWorkflowSheet(Long productId, String sheetId, Date startDate, String material, Integer materialCount, Integer expectedProductCount, String requester) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.execute(ADD_WORKFLOW_SHEET_QUERY, new Object[] {productId, sheetId, startDate, material, materialCount, expectedProductCount, requester});
    }

    public static void completeWorkflowSheet(String sheetId, Date endDate, Integer finalCount) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.execute(COMPLETE_WORKFLOW_SHEET_QUERY, new Object[] {endDate, finalCount, sheetId});
    }

    public static List<WorkflowSheet> searchWorkflowSheets(Date startDate, Date endDate, String requester, String productName) throws Exception {
        String query = SEARCH_WORKFLOW_SHEET_QUERY;
        List<Object> params = Lists.newArrayList(startDate, endDate);
        if (requester != null) {
            query += REQUESTER_FILTER;
            params.add(requester);
        }
        if (productName != null) {
            query += PRODUCT_NAME_FILTER;
            params.add(productName);
        }
        query += SEARCH_WORKFLOW_SHEET_ORDER_BY;

        WorkflowSheetCallback callback = new WorkflowSheetCallback();
        DatabaseAccessor dbAccessor = fetchDBAccessor();
        dbAccessor.query(query, params.toArray(), callback);

        return callback.getResult();
    }

    // Workflow Details related queries
    public static void createWorkflowDetails(Long productId, String sheetId, Date startDate, Integer totalCount, Operation operation) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.execute(ADD_WORKFLOW_DETAILS_QUERY, new Object[] {sheetId, productId, startDate, totalCount, operation.getOperationCode()});
    }

    public static List<WorkflowDetails> searchPendingWorkflowDetails(Operation operation) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        WorkflowDetailsCallback callback = new WorkflowDetailsCallback();
        dbAccessor.query(SEARCH_PENDING_WORKFLOW_DETAILS_QUERY, new Object[] {operation.getOperationCode()}, callback);
        return callback.getResult();
    }

    public static WorkflowDetails getWorkflowDetails(Long workflowDetailsId) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        WorkflowDetailsCallback callback = new WorkflowDetailsCallback();
        dbAccessor.query(GET_WORKFLOW_DETAILS_QUERY, new Object[] {workflowDetailsId}, callback);
        return callback.getResult().isEmpty() ? null : Iterables.getOnlyElement(callback.getResult());
    }

    public static List<WorkflowDetails> getWorkflowDetailsBySheetId(String sheetId) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        WorkflowDetailsCallback callback = new WorkflowDetailsCallback();
        dbAccessor.query(GET_WORKFLOW_DETAILS_BY_SHEET_ID_QUERY, new Object[] {sheetId}, callback);
        return callback.getResult();
    }

    public static void confirmWorkflowDetail(Long workflowDetailId, String owner, Date finishDate,
                                             Integer successCount, Integer failedCount, String note) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.execute(CONFIRM_WORKFLOW_DETAILS_QUERY, new Object[] {owner, successCount, failedCount, finishDate, note, workflowDetailId});
    }

    // Discharge history queries
    public static void addDischargeHistory(Long productId, Date date, String owner, Integer count, String note) throws Exception {
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.execute(ADD_DISCHARGE_HISTORY_QUERY, new Object[] {date, owner, count, productId, note});
    }

    // OwnerSummary related queries
    public static List<OwnerSummary> searchOwnerSummary(Date startDate, Date endDate, String owner, String productName) throws Exception {
        String query = SEARCH_OWNER_SUMMARY;
        List<Object> params = Lists.newArrayList(startDate, endDate);
        if (owner != null) {
            query += OWNER_USERNAME_FILTER;
            params.add(owner);
        }
        if (productName != null) {
            query += PRODUCT_NAME_FILTER;
            params.add(productName);
        }
        query += SEARCH_OWNER_GROUP_BY;
        query += SEARCH_OWNER_ORDER_BY;
        OwnerSummaryCallback callback = new OwnerSummaryCallback();
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.query(query, params.toArray(), callback);

        return callback.getResult();
    }

    // ProductStock related queries
    public static List<ProductStock> searchProductStock(String productName) throws Exception {
        return searchProductStock(productName, null);
    }

    public static List<ProductStock> searchProductStock(String productName, String status) throws Exception {
        ProductStockCallback callback = new ProductStockCallback();
        DatabaseAccessor dbAccessor = fetchDBAccessor();
        String query = SEARCH_PRODUCT_STOCK;
        List<Object> params = Lists.newArrayList(productName);
        if (status != null) {
            query += SEARCH_PRODUCT_STOCK_STATUS_FILTER;
            params.add(status);
        }

        dbAccessor.query(query, params.toArray(), callback);

        return callback.getResult();
    }

    public static User getUser(String username) throws Exception {
        UserCallback callback = new UserCallback();
        DatabaseAccessor dbAccessor = fetchDBAccessor();

        dbAccessor.query(SELECT_USER_INFO_QUERY, new Object[]{username}, callback);

        User user = callback.getResult();

        if (user != null) {
            SingleStringListCallback permissionCallback = new SingleStringListCallback();
            dbAccessor.query("SELECT permission from user_permissions where username = ?", new String[] {username},
                           permissionCallback);

            user.addPermissions(Operation.generateUserOperations(
                    Operation.fromPermissionCodes(permissionCallback.getResult())));
        }

        return user;
    }

    // private methods
    private static DatabaseAccessor fetchDBAccessor() {
        AccountProfileLocator profile = AccountProfileLocator.getProfile();
        if (profile == null) {
            // TODO: throw specific Exception and activity should move back to login page.
            throw new RuntimeException("No profile information");
        }

        return profile.getDbAccessor();
    }
}
