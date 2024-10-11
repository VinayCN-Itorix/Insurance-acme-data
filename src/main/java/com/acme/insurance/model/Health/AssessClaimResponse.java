package com.acme.insurance.model.Health;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssessClaimResponse {
    private String claimID;
    private String policyCoverage;
    private String claimPercentage;
    private String status;
    private String paymentId;
    private String claimAmount;
}
