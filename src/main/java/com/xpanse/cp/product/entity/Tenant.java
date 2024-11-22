package com.xpanse.cp.product.entity;

import com.xpanse.cp.product.constants.TenantConstant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "tenant", schema = "config")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenantid") 
    private Long tenantId;


    @Column(name = "tenantcode")
    private String tenantCode;
    
    @Column(name = "tenantname")
    private String tenantName;
    
    @Column(name = "shortname")
    private String shortName;

    @Column(name = "feinnumber")
    private String federalId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tenantstatus")
    private TenantConstant.Status status;

    @Column(name = "tenanttype")
    private String tenantType;

    @Column(name = "tenancytierlevel")
    private String tenancyTier;

    @Column(name = "supporttierlevel")
    private String supportTier;

    @Column(name = "termstartdate")
    private Date termStartDate;

    @Column(name = "termenddate")
    private Date termEndDate;

    @Column(name = "createduserid")
    private String createdUserId;

    @Column(name = "lastupdateduserid")
    private String lastUpdatedUserId;

    @Column(name = "createddttm")
    private LocalDateTime createdDttm;

    @Column(name = "lastupdateddttm")
    private LocalDateTime lastUpdatedDttm;

    @Column(name = "recordstatus")
    private String recordStatus;
    
}


