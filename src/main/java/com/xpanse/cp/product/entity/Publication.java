package com.xpanse.cp.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "publication", schema = "config")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publicationid")
    private Long publicationID;

    @Column(name = "tenantid")
    private Long tenantID;

    @Column(name = "publicationstartdate")
    private Date publicationStartDate;

    @Column(name = "publicatiionenddate")
    private Date publicationEndDate;

    @Column(name = "createduserid")
    private String createdUserId;

    @Column(name = "lastupdateduserid")
    private String lastupdatedUserId;

    @Column(name = "createddttm")
    private Date createdDttm;

    @Column(name = "lastupdateddttm")
    private Date lastupdatedDttm;

    @Column(name = "recordstatus")
    private String recordStatus;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productid")
    private Product product;

}


