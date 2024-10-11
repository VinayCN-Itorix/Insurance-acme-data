package com.acme.insurance.model.Policy;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RenewalRequest {
private String customerId;
private String amount;
private String policyId;
private String paymentMethod;
private String description;
}
