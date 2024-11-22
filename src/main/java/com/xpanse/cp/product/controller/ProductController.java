package com.xpanse.cp.product.controller;

import com.xpanse.cp.product.entity.APIResponse;
import com.xpanse.cp.product.entity.Product;
import com.xpanse.cp.product.exception.InvalidRequestException;
import com.xpanse.cp.product.exception.ProductNotFoundException;
import com.xpanse.cp.product.service.ProductService;
import com.xpanse.cp.product.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.List;

/**
 * @author authorName
 */

@Tag(
        name="CRUD REST API's for Product in Control Plane",
        description="CRUD REST API's in Control Plane to CREATE, FETCH, UPDATE, DELETE Product and Sub-Product"
)

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

@Operation(
        summary="Create Product REST API",
        description="REST API to create new Product"
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "HTTP Status Created"
        ),
        @ApiResponse(responseCode = "400",
                description = "Http status Bad Request",
                content=@Content(
                        schema=@Schema(implementation = InvalidRequestException.class)
                )
        )
}
)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse createProduct(@Valid @RequestBody final ProductDTO request,@RequestParam(value = "parentProductId", required = false) Long parentProductId) {

        return productService.createProduct(request,parentProductId);
    }

@Operation(
        summary="Get Product REST API",
        description="REST API to get Product using Product Id"
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "HTTP Status Created"
        ),
        @ApiResponse(responseCode = "400",
                description = "Http status Bad Request",
                content=@Content(
                        schema=@Schema(implementation = ProductNotFoundException.class)
                )
        )
}
)
    @GetMapping(value = "/{productId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable Long productId) {

        return productService.getByProductId(productId);
    }


    @Operation(
            summary="Get Products/Sub Products REST API",
            description="REST API to get Products/Sub Products using parent Product Id, tenant Id and includeInActive parameters"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Success"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Http status Bad Request",
                    content=@Content(
                            schema=@Schema(implementation = ProductNotFoundException.class)
                    )
            )
    }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getProducts(@RequestParam(value = "parentProductId", required = false) Long parentProductId, @RequestParam(value = "tenantId", required = false) Long tenantId, @RequestParam(value = "includeInactive", required = false) String includeInactive) {
            return productService.getProducts(parentProductId, tenantId, includeInactive);
    }

    @Operation(
            summary="Update Product REST API",
            description="REST API to update list of products"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Created"
            ),
            @ApiResponse(responseCode = "400",
                    description = "Http status Bad Request",
                    content=@Content(
                            schema=@Schema(implementation = ProductNotFoundException.class)
                    )
            )
    }
    )
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String updateProduct(@Valid @RequestBody List<ProductDTO> products, @RequestParam Long productId) {
        return productService.updateProducts(products, productId);
    }

    @Operation(
            summary="delete Product REST API",
            description="REST API to delete product based on productId"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Product has been deleted"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Product does not exist",
                    content=@Content(
                            schema=@Schema(implementation = ProductNotFoundException.class)
                    )
            )
    }
    )
    @DeleteMapping(value="/{productId}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }

}
