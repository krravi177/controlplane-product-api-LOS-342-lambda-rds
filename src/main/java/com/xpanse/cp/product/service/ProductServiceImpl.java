package com.xpanse.cp.product.service;

import com.xpanse.cp.product.dto.TenantDTO;
import com.xpanse.cp.product.entity.Publication;
import com.xpanse.cp.product.repository.PublicationRepository;
import com.xpanse.cp.product.entity.Tenant;
import com.xpanse.cp.product.repository.TenantRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xpanse.cp.product.constants.ProductConstant;
import com.xpanse.cp.product.entity.APIResponse;
import com.xpanse.cp.product.entity.Product;
import com.xpanse.cp.product.exception.DuplicateProductException;
import com.xpanse.cp.product.exception.InvalidRequestException;
import com.xpanse.cp.product.exception.ProductNotFoundException;
import com.xpanse.cp.product.dto.ProductDTO;
import com.xpanse.cp.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    /** logger object */
    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private TenantRepository tenantRepository;

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private static final String ACTIVE = "ACTIVE";

    @Transactional
    @Override
    public APIResponse createProduct(ProductDTO request,Long parentProductId) {

        request.setParentProductId(parentProductId);
        if (productRepository.existsByProductCode(request.getProductCode())) {
            throw new DuplicateProductException("Product with code " + request.getProductCode() + " already exists");
        }

       checkTenantId(request);

        if (request.getParentProductId() != null){
            tenantProductCheck(request);
        }

        Product product = populateProduct(request);

        Product response = productRepository.save(product);
        
        logger.info("product-api | Saved product response : {} ", response);

        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage(ProductConstant.SUCCESS_MESSAGE);
        apiResponse.setStatus("Success");
        return apiResponse;
    }


    private void checkTenantId(ProductDTO productDTO) {

        if (productDTO.getProductPublicationRule().equals(ProductConstant.ProductPublicationRule.SPECIFIC)) {
            if (productDTO.getTenants().isEmpty()) {
                throw new InvalidRequestException(ProductConstant.ALLOWED_TENANT_LIST_VALIDATION_MESSAGE);
            }
        }

        if (productDTO.getTenants().isEmpty()) {
            return;
        }
        List<Long> invalidTenants = new ArrayList<>();
        productDTO.getTenants().forEach(tenant -> {
            logger.info("Perform tenants for tenantid {}", tenant.getTenantId());
            Optional<Tenant> tenantDeatils =  getTenantById(tenant.getTenantId());
            if (!tenantDeatils.isPresent()){
                invalidTenants.add(tenant.getTenantId());

            }else{
                logger.info("tenant Code from DB {}", tenantDeatils.get().getTenantCode());
                if (!tenantDeatils.get().getTenantCode().equals(tenant.getTenantCode())) {
                    invalidTenants.add(tenant.getTenantId());
                }
            }
        });
        if(!invalidTenants.isEmpty()){
            throw new InvalidRequestException("Selected tenant(s)"+invalidTenants.toString() +"do not exist");
        }
    }

    private void tenantProductCheck(ProductDTO productDTO) {
        logger.info("Perform tenants for productId {}", productDTO.getParentProductId());

        Product prd =  getByProductId(productDTO.getParentProductId());

        if(prd.getProductPublicationRule().equals(ProductConstant.ProductPublicationRule.SPECIFIC)
            && productDTO.getProductPublicationRule().equals(ProductConstant.ProductPublicationRule.ALL)){
            throw new InvalidRequestException(ProductConstant.ALLOWED_TENANT_SPECIFIC_VALIDATION_MESSAGE);
        }

        List<Long> tenantDBList = new ArrayList<>();
        prd.getPublications().forEach(pub -> {
            tenantDBList.add(pub.getTenantID());
        });

        List<Long> tenantRequestList = new ArrayList<>();
        productDTO.getTenants().forEach(tenant -> {
            tenantRequestList.add(tenant.getTenantId());
        });

        boolean containsAll1 = new HashSet<>(tenantDBList).containsAll(tenantRequestList);
        ArrayList<Long> diff = new ArrayList<>(tenantRequestList);
        diff.removeAll(tenantDBList);
        if(!containsAll1){
            throw new InvalidRequestException("Selected tenant(s) "+ diff.toString() +"is not applicable to the product");
        }

    }

    Product populateProduct(ProductDTO request) {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        logger.info("product-api | Saved product request : {} ", request);
        Product product =new Product();
        product.setParentProductId(request.getParentProductId());
        product.setProductCode(request.getProductCode());
        product.setEnvironment("Dev");
        product.setProductName(request.getProductName());
        product.setProductOwner(request.getProductOwner());
        product.setProductStartDate(request.getProductStartDate());
        product.setProductEndDate(request.getProductEndDate());
        product.setProductPublicationRule(request.getProductPublicationRule());
        product.setRecordStatus(String.valueOf(ProductConstant.RecordStatus.Active));
        product.setCreatedUserId("CurUserID");
        product.setLastupdatedUserId("CurUserID");
        product.setCreatedDttm(new Date());
        product.setLastupdatedDttm(new Date());

        try {
            product.setProductStartDate(request.getProductStartDate() != null ? formatter.parse(String.valueOf(request.getProductStartDate())) : null);
            product.setProductEndDate(request.getProductEndDate() != null ? formatter.parse(String.valueOf(request.getProductEndDate())) : null);
        } catch (ParseException e) {
           // throw new InvalidRequestException("Unable to parse the date format");
        }

        logger.info("product-api | Saved product product : {} ", product);
        populateProductTenant(request,product);
        return product;
    }

    private void populateProductTenant(ProductDTO productDetails, Product product) {
        if (productDetails.getProductPublicationRule() != ProductConstant.ProductPublicationRule.SPECIFIC) {
            product.setPublications(null);
        }
        if (productDetails.getTenants().isEmpty()) {
            return;
        }
        List<Publication> publist = new ArrayList<>();

        productDetails.getTenants().forEach(tenant ->{
            Publication pub = new Publication();

            pub.setTenantID(tenant.getTenantId());
            pub.setPublicationStartDate(new Date());
            pub.setPublicationEndDate(new Date());
            pub.setRecordStatus("Active");
            pub.setCreatedUserId("CurUserID");
            pub.setLastupdatedUserId("CurUserID");
            pub.setCreatedDttm(new Date());
            pub.setLastupdatedDttm(new Date());

            pub.setProduct(product);

            publist.add(pub);
        });
        product.setPublications(publist);
    }


    @Override
    public Product getByProductId(Long productId) throws ProductNotFoundException {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("No Product found with id: | {} " + productId));
    }

    @Override
    public List<ProductDTO> getProducts(Long parentProductId, Long tenantId, String includeInactive) throws ProductNotFoundException {
        List<Product> products = new ArrayList<>();
        if(null != includeInactive && includeInactive.equalsIgnoreCase("Y")){
            logger.info("All Products/SubProducts");
            if(null != parentProductId && null != tenantId){
                logger.info("All Tenant Sub Products");
//                List<Publication> publication = publicationRepository.findByTenantID(tenantId);
//                List<Long> parentProductIds = publication.stream().map(Publication::getProduct).map(Product::getParentProductId).toList();
                products = productRepository.findByParentProductId(parentProductId);
            }else if(null != tenantId){
                logger.info("All Tenant Products");
                List<Publication> publication = publicationRepository.findByTenantID(tenantId);
                List<Long> productIds = publication.stream().map(Publication::getProduct).map(Product::getProductId).toList();
                products = productRepository.findByProductIdIn(productIds);
            }else if(null != parentProductId){
                logger.info("All Sub Products");
                products = productRepository.findByParentProductId(parentProductId);
            }else{
                logger.info("All Products");
                products = productRepository.findAll();
            }
        }else if(null != parentProductId && null != tenantId){
            logger.info("Tenant Active Sub Products");
//            List<Publication> publication = publicationRepository.findByTenantID(tenantId);
//            List<Long> parentProductIds = publication.stream().map(Publication::getProduct).map(Product::getParentProductId).toList();
            products = productRepository.findByParentProductId(parentProductId);
        }else if(null != tenantId){
            logger.info("Active Tenant Products");
            List<Publication> publication = publicationRepository.findByTenantID(tenantId);
            List<Long> parentProductIds = publication.stream().map(Publication::getProduct).map(Product::getParentProductId).toList();
            products = productRepository.findByProductEndDateIsNullOrProductEndDateAfterAndProductIdIn(new Date(), parentProductIds);
        }else if(null != parentProductId){
            logger.info("Active Sub Products");
            products = productRepository.findByProductEndDateIsNullOrProductEndDateAfterAndParentProductId(new Date(), parentProductId);
        }else{
            logger.info("Active Products");
            products = productRepository.findByProductEndDateIsNullOrProductEndDateAfter(new Date());
        }

        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = getProductDTO(product);
            if (product.getProductPublicationRule().equals(ProductConstant.ProductPublicationRule.SPECIFIC)) {
                List<Publication> publications = product.getPublications();
                List<TenantDTO> tenantDTOS = productDTO.getTenants();
                if(null != tenantId) {
                    Optional<Publication> publication = publications.stream()
                            .filter(pub -> tenantId.equals(pub.getTenantID()) && ACTIVE.equalsIgnoreCase(pub.getRecordStatus()))
                            .findFirst();
                    Publication pub = publication.orElse(null);
                    if(ACTIVE.equalsIgnoreCase(Objects.requireNonNull(pub).getRecordStatus())){
                        TenantDTO tenantDTO = getTenantDTO(pub);
                        tenantDTOS.add(tenantDTO);
                    }
                }else{
                    for (Publication publication : publications) {
                        if(ACTIVE.equalsIgnoreCase(publication.getRecordStatus())){
                            TenantDTO tenantDTO = getTenantDTO(publication);
                            tenantDTOS.add(tenantDTO);
                        }
                    }
                }

            }
            productDTOs.add(productDTO);
        }

        return productDTOs;
    }

    private TenantDTO getTenantDTO(Publication publication) {
        TenantDTO tenantDTO = new TenantDTO();
        Optional<Tenant> tenant = getTenantById(publication.getTenantID());
        tenantDTO.setTenantId(publication.getTenantID());
        tenantDTO.setPublicationID(publication.getPublicationID());
        tenantDTO.setTenantCode(tenant.map(Tenant::getTenantCode).orElse(""));
        return tenantDTO;
    }

    private static ProductDTO getProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setProductCode(product.getProductCode());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductOwner(product.getProductOwner());
        productDTO.setProductStartDate(product.getProductStartDate());
        productDTO.setProductEndDate(product.getProductEndDate());
        productDTO.setProductPublicationRule(product.getProductPublicationRule());
        productDTO.setEnvironment(product.getEnvironment());
        productDTO.setParentProductId(product.getParentProductId());
        productDTO.setCreatedDttm(product.getCreatedDttm());
        productDTO.setLastUpdatedDttm(product.getLastupdatedDttm());
        productDTO.setCreatedUserID(product.getCreatedUserId());
        productDTO.setLastUpdatedUserID(product.getLastupdatedUserId());
        productDTO.setRecordStatus(product.getRecordStatus());
        return productDTO;
    }
    
    @Transactional
    @Override
    public String updateProducts(List<ProductDTO> productDetails, Long productId) {
        if(!productDetails.isEmpty() && productDetails.get(0).getParentProductId() != null) {
            List<Product> products = productRepository.findByParentProductId(productId);
            List<Product> toRemove = products.stream().filter(product ->
                    productDetails.stream().map(ProductDTO::getProductId).noneMatch(id -> id.equals(product.getProductId())
                    )).toList();
            toRemove.forEach(product -> {
                deleteProduct(product.getProductId());
            });
        }
        productDetails.forEach(productDetail -> {
            Product product = productRepository.findById(productDetail.getProductId()).orElse(null);
            if (product == null) {
                throw new ProductNotFoundException("No Product found with id: | {} " + productDetail.getProductId());
            }
            if(productRepository.existsByProductCode(productDetail.getProductCode()) && !productRepository.findByProductCode(productDetail.getProductCode()).getProductId().equals(productDetail.getProductId())) {
                throw new DuplicateProductException("Product with code " + product.getProductCode() + " already exists");
            }

           checkTenantId(productDetail);

            if (productDetail.getParentProductId() != null){
                tenantProductCheck(productDetail);
            }

            if (productDetail.getProductPublicationRule().equals(ProductConstant.ProductPublicationRule.SPECIFIC)) {
                List<Publication> publicationlist = new ArrayList<>();

                productDetail.getTenants().forEach(tenant ->{
                    Publication pub = null;
                    if (publicationRepository.existsByProductProductIdAndTenantID(productDetail.getProductId(), tenant.getTenantId())) {
                        pub = publicationRepository.findByProductProductIdAndTenantID(productDetail.getProductId(), tenant.getTenantId());
                    } else {
                        pub = new Publication();
                        pub.setTenantID(tenant.getTenantId());
                        pub.setCreatedUserId("CurUserID");
                        pub.setCreatedDttm(new Date());
                        pub.setProduct(product);
                    }
                    pub.setRecordStatus(String.valueOf(ProductConstant.RecordStatus.Active));
                    pub.setPublicationStartDate(new Date());
                    pub.setPublicationEndDate(new Date());
                    pub.setLastupdatedUserId("CurUserID");
                    pub.setLastupdatedDttm(new Date());
                    publicationlist.add(pub);
                });
                List<Publication> dbPublications = publicationRepository.findByProductProductId(productDetail.getProductId());
                List<Publication> toRemove = dbPublications.stream().filter(dbPublication ->
                        publicationlist.stream().map(Publication::getTenantID).noneMatch(id -> id.equals(dbPublication.getTenantID())
                        )).toList();
                toRemove.forEach(publication -> {
                    publication.setRecordStatus(String.valueOf(ProductConstant.RecordStatus.Deleted));
                    publicationRepository.save(publication);
                });
                product.setPublications(publicationlist);
            } else {
                List<Publication> publications = publicationRepository.findByProductProductId(product.getProductId());
                if(!publications.isEmpty()) {
                    publications.forEach(publication -> {
                        publication.setRecordStatus(String.valueOf(ProductConstant.RecordStatus.Deleted));
                        publicationRepository.save(publication);
                    });
                }
                product.setPublications(null);
            }
            product.setProductPublicationRule(productDetail.getProductPublicationRule());
            product.setEnvironment(productDetail.getEnvironment());
            product.setProductName(productDetail.getProductName());
            product.setProductOwner(productDetail.getProductOwner());
            try {
                product.setProductStartDate(productDetail.getProductStartDate() != null ? formatter.parse(String.valueOf(productDetail.getProductStartDate())) : null);
                product.setProductEndDate(productDetail.getProductEndDate() != null ? formatter.parse(String.valueOf(productDetail.getProductEndDate())) : null);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            product.setRecordStatus(productDetail.getRecordStatus());
            product.setLastupdatedUserId("CurUserID");
            product.setLastupdatedDttm(new Date());
            logger.info("product-api | Updated product : {} ", product);
            productRepository.save(product);
        });

        return "Updated Products Successfully";
    }

    @Transactional
    @Override
    public String deleteProduct(Long productId) throws ProductNotFoundException {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException("No Product found with id: | " + productId);
        }
        if (product.getProductPublicationRule().equals(ProductConstant.ProductPublicationRule.SPECIFIC)) {
            List<Publication> publications = publicationRepository.findByProductProductId(productId);
            publications.forEach(publication -> {
                publication.setRecordStatus(String.valueOf(ProductConstant.RecordStatus.Deleted));
                publicationRepository.save(publication);
            });
        }
        product.setRecordStatus(String.valueOf(ProductConstant.RecordStatus.Deleted));
        productRepository.save(product);
        return "Deleted Product Successfully";
    }

    public Optional<Tenant> getTenantById(final Long tenantId)  {
        return tenantRepository.findById(tenantId);

    }
}

