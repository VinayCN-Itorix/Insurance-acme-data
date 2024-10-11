package com.acme.insurance.model.CarInsurance;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalRequest {
private CarVariant carVariant;
private CarManufacture carManufacture;
private CarRegistration carRegistration;
private CarHistory carHistory;
private CarAssetValue carAssetValue;
}
