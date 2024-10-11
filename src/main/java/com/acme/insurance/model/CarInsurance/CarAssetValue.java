package com.acme.insurance.model.CarInsurance;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarAssetValue {
    private String currentAssetValue;
    private String depreciationRate;
    private String assetValueIn5Years;
    
}
