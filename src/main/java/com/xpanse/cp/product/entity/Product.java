package com.xpanse.cp.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xpanse.cp.product.constants.ProductConstant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product", schema = "config")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid")
    private Long productId;
    
    @Column(name = "parentproductid")
    private Long parentProductId;
    
    @Column(name = "productcode")
    private String productCode;
    
    @Column(name = "productname")
    private String productName;

    @Column(name = "Environment")
    private String environment;

    @Column(name = "productowner")
    private String productOwner;

    @Enumerated(EnumType.STRING)
    @Column(name = "productpublicationrule")
    private ProductConstant.ProductPublicationRule productPublicationRule;

    @Column(name = "productstartdate")
    private Date productStartDate;

    @Column(name = "productenddate")
    private Date productEndDate;

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

    @JsonIgnore
    @OneToMany(mappedBy="product", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=false)
    @ToString.Exclude
    private List<Publication> publications = new ArrayList<>();

    /*@JsonIgnore
    @OneToMany(mappedBy ="publication", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JsonManagedReference
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("publicationList")
    private List<Publication> publicationList = new ArrayList<>();*/
}


