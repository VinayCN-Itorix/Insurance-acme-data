package com.acme.insurance.model.Health;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthClaimRequest {

private String policyId;
private String policyHolderId;
private String claimantName;
private String phoneNo;
private List<?> docs;
}
