package com.xpanse.cp.product.dto;

import com.xpanse.cp.product.constants.ProductConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name="ProductDTO",
        description = "Schema to hold Product Information"
)
public class ProductDTO {
    private Long productId;


    @Schema(
            description = "Product can have parent product id", example = "1L"
    )
    private Long parentProductId;

    @Schema(
            description = "Product has unique product code", example = "VOIE"
    )
    @NotEmpty(message = ProductConstant.PRODUCT_CODE_VALIDATION_MESSAGE)
    private String productCode;

    @NotEmpty(message = ProductConstant.PRODUCT_NAME_VALIDATION_MESSAGE)
    @Schema(
            description = "Product Name", example = "Verification of Income And Assets"
    )
    private String productName;

    @Schema(
            description = "Environment", example = "DEMO"
    )
    private String environment;

    @Schema(
            description = "Product Start Date", example = "2024-11-01T07:44:16.665Z"
    )
    private Date productStartDate;

    @Schema(
            description = "Product End Date", example = "2024-11-01T07:44:16.665Z"
    )
    private Date productEndDate;

    @Schema(
            description = "Product owner", example = "user@test.com"
    )

    @NotEmpty(message = ProductConstant.OWNER_EMAIL_VALIDATION_MESSAGE)
    @Email(message = ProductConstant.OWNER_EMAIL_INVALID_VALIDATION_MESSAGE)
    //@ConditionalNotEmpty(conditionField = "conditionField", conditionValue = "specificValue", message = ProductConstant.OWNER_EMAIL_VALIDATION_MESSAGE)
    private String productOwner;

    @NotNull(message = ProductConstant.ALLOWED_TENANT_VALIDATION_MESSAGE)
    @Schema(
            description = "Product Publication Rule", example = "ALL, NONE, SPECIFIC"
    )
    private ProductConstant.ProductPublicationRule productPublicationRule;

    @Schema(
            description = "List of Tenants", example = " \"tenants\": [\n" +
            "            {\n" +
            "                \"tenantId\": 10,\n" +
            "                \"tenantCode\": \"Tenant444\",\n" +
            "                \"publicationID\": 11\n" +
            "            }\n" +
            "        ]"
    )
    private List<TenantDTO> tenants = new ArrayList<>();

    @Schema(
            description = "Created User", example = "CurUserID"
    )
    private String createdUserID;

    @Schema(
            description = "Last Updated User", example = "CurUserID"
    )
    private String lastUpdatedUserID;

    @Schema(
            description = "Last Updated User", example = "2024-11-14T12:59:53.931+00:00"
    )
    private Date createdDttm;

    @Schema(
            description = "Created date Time", example = "2024-11-14T12:59:53.931+00:00"
    )
    private Date lastUpdatedDttm;

    @Schema(
            description = "Record Status", example = "Active"
    )
    private String recordStatus;
}
