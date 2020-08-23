package com.refine.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Operation {
    创建任务单("add_workflow", 1, null, null),
    浇注("jiaozhu", 1, null, ProductStatus.待醛化),
    醛化("quanhua", 2, ProductStatus.待醛化, ProductStatus.待干燥),
    干燥("dry", 3, ProductStatus.待干燥, ProductStatus.待切割),
    干加工切割("cut", 4, ProductStatus.待切割, ProductStatus.待包装入库),
    包装入库("pack", 5, ProductStatus.待包装入库, ProductStatus.成品),
    出库("stock", 6, ProductStatus.成品, null);

    public static final String ADMIN_PERMISSION_NAME = "管理员";

    private final String operationCode;
    private final int rank;
    private final ProductStatus fromStatus;
    private final ProductStatus toStatus;

    Operation(String operationCode, int rank,
              ProductStatus fromStatus,
              ProductStatus toStatus) {
        this.operationCode = operationCode;
        this.rank = rank;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public int getRank() {
        return rank;
    }

    public static Operation fromOperationCode(String operationCode) {
        for (Operation operation : Operation.values()) {
            if (operation.getOperationCode().equals(operationCode)) {
                return operation;
            }
        }
        return null;
    }

    public static List<Operation> fromPermissionCodes(List<String> permissionCodes) {
        List<Operation> operations = new ArrayList<>();
        for (Operation operation : Operation.values()) {
            if (permissionCodes.contains(operation.operationCode)) {
                operations.add(operation);
            }
        }
        return operations;
    }

    public static List<Operation> fromNames(List<String> permissionNames) {
        List<Operation> roles = new ArrayList<>();
        for (Operation role : Operation.values()) {
            if (permissionNames.contains(role.name())) {
                roles.add(role);
            }
        }
        return roles;
    }

    public static List<String> getPermissionCodes(List<String> permissionNames) {
        List<Operation> roles = fromNames(permissionNames);
        List<String> permissionCodes = new ArrayList<>();
        for (Operation role : roles) {
            permissionCodes.add(role.operationCode);
        }
        return permissionCodes;
    }

    public static String[] extractPermissions() {
        String[] permissions = new String[Operation.values().length + 1];
        permissions[0] = ADMIN_PERMISSION_NAME;

        int pos = 1;
        for (Operation role : Operation.values()) {
            permissions[pos] = role.name();
            pos++;
        }
        return permissions;
    }

    public static List<String> getAllOperations() {
        List<String> operations = new ArrayList<>();
        for (Operation role : Operation.values()) {
            operations.add(role.name());
        }
        return operations;
    }

    public static List<String> generateUserOperations(List<Operation> roles) {
        Collections.sort(roles, (o1, o2) -> o1.getRank() - o2.getRank());

        List<String> operations = new ArrayList<>();
        for (Operation role : roles) {
            operations.add(role.name());
        }
        return operations;
    }

    public ProductStatus getFromStatus() {
        return fromStatus;
    }

    public ProductStatus getToStatus() {
        return toStatus;
    }
}
