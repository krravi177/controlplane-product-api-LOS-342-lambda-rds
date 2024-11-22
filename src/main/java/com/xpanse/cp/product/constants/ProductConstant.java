package com.xpanse.cp.product.constants;

public class ProductConstant {
    //Restrict Initialization
    private ProductConstant(){

    }

    public enum ProductPublicationRule {
        ALL,
        NONE,
        SPECIFIC
    }

    public enum RecordStatus {
        Active,
        Deleted
    }

    public static final String SUCCESS_MESSAGE = "Product details saved successfully";

    public static final String UPDATE_MESSAGE = "Updated Product details successfully";

    public static final String DUPLICATE_PRODUCT_MESSAGE = "Duplicate product code found.";

    public static final String PRODUCT_CODE_VALIDATION_MESSAGE = "Product Code cannot be null or empty.";

    public static final String PRODUCT_NAME_VALIDATION_MESSAGE = "Product Name cannot be null or empty.";

    public static final String OWNER_EMAIL_VALIDATION_MESSAGE = "Owner Email cannot be null or empty.";

    public static final String OWNER_EMAIL_INVALID_VALIDATION_MESSAGE = "Owner Email should be a valid value";

    public static final String ALLOWED_TENANT_VALIDATION_MESSAGE = "Allowed Tenant cannot be null or empty.";

    public static final String ALLOWED_TENANT_LIST_VALIDATION_MESSAGE = "Product should have Tenant list when allowed tenant is SPECIFIC";

    public static final String ALLOWED_TENANT_SPECIFIC_VALIDATION_MESSAGE = "Product is not applicable for ALL tenants, so sub product cannot be available for ALL tenant";
}
