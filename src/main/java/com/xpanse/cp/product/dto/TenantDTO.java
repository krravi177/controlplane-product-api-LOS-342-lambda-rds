package com.xpanse.cp.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantDTO {

	private Long tenantId;

	private String tenantCode;

	private Long publicationID;

	public TenantDTO(Long tenantId) {
		super();
		this.tenantId = tenantId;
	}

}
