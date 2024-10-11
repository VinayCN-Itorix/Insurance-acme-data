package com.acme.insurance.model.CarInsurance;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ODPremium {
private String BasicOdPremium;
private String noClaimBonus;
private String message;
}
