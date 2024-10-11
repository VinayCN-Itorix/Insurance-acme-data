package com.acme.insurance.model.Policy;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RenewalPaymentRequest {
private String customerId;
private String policyId;
private String cardId;
private String paymentId;
private String amount;
private String message;
}
