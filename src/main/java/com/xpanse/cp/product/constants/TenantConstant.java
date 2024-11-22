package com.xpanse.cp.product.constants;

public class TenantConstant {
    //Restrict Initialization
    private TenantConstant(){

    }

    public enum Status{
        PotentialCustomer,Testing, Customer, Inactive
    }
    public enum TenancyTier{
        SILO,POOL;
    }
    public enum SupportTier{
        BASIC,PREMIUM;
    }

    public enum Role {
        BUSINESS, ADMIN, OPERATION, FINANCE;
    }

    public enum ContactType {
        PRIMARY, SECONDARY;
    }

    public enum RecordStatus {
        Active, Deleted;
    }

}
