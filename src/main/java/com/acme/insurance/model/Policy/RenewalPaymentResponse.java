package com.acme.insurance.model.Policy;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RenewalPaymentResponse {
private String paymentId;
private String customerId;
private String policyId;
private Boolean paymentStatus;
private String message;
}
