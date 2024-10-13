package com.acme.insurance.controller;

import com.acme.insurance.model.CarInsurance.*;
import io.apiwiz.compliance.config.EnableCompliance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("v1/car")
@EnableCompliance
public class CarInsuranceController {

@Value("${api.variant:null}")
private String variant;

@Value("${api.manufacture}")
private  String manufacture;

@Value("${api.registration:null}")
private String registration;

@Value("${api.history:null}")
private String history;

@Value("${api.asset:null}")
private String asset;

@Value("${api.calculate:null}")
private String calculate;

@Autowired
private RestTemplate restTemplate;
@PostMapping("/request/quote")
public ResponseEntity<?> getCarQuotation(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                         @RequestBody(required = false)CarQuoteRequest carQuoteRequest ) throws URISyntaxException {
    String vehicleNumber = carQuoteRequest.getVehicleNumber();
    
    if(enableTracing){
        CarVariant carVariant;
        CarManufacture carManufacture;
        CarRegistration carRegistration;
        CarHistory carHistory;
        CarAssetValue carAssetValue;
        InsuranceBreakup insuranceBreakup;
        if(true){
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            headers.add("Content-Type","application/json");
            HttpEntity httpEntity = new HttpEntity<>(headers);
            String url = String.format(variant,vehicleNumber);
            ResponseEntity<CarVariant> response  = restTemplate.exchange(new URI(url), HttpMethod.GET,httpEntity,CarVariant.class);
            carVariant = response.getBody();
        }
        if(true){
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            HttpEntity httpEntity = new HttpEntity<>(headers);
            String url = String.format(manufacture,vehicleNumber);
            ResponseEntity<CarManufacture> response  = restTemplate.exchange(new URI(url), HttpMethod.GET,httpEntity,CarManufacture.class);
            carManufacture = response.getBody();
        }
        if(true){
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            HttpEntity httpEntity = new HttpEntity<>(headers);
            String url = String.format(registration,vehicleNumber);
            ResponseEntity<CarRegistration> response  = restTemplate.exchange(new URI(url), HttpMethod.GET,httpEntity,CarRegistration.class);
            carRegistration = response.getBody();
        }
        if(true){
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            HttpEntity httpEntity = new HttpEntity<>(headers);
            String url = String.format(history,vehicleNumber);
            ResponseEntity<CarHistory> response  = restTemplate.exchange(new URI(url), HttpMethod.GET,httpEntity,CarHistory.class);
            carHistory = response.getBody();
        }
        if(true){
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            HttpEntity httpEntity = new HttpEntity<>(headers);
            String url = String.format(asset,vehicleNumber);
            ResponseEntity<CarAssetValue> response  = restTemplate.exchange(new URI(url), HttpMethod.GET,httpEntity,CarAssetValue.class);
            carAssetValue = response.getBody();
        }
        if(true){
            CalRequest calRequest =new CalRequest(carVariant,carManufacture,carRegistration,carHistory,carAssetValue);
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            HttpEntity httpEntity = new HttpEntity<>(calRequest,headers);
            String url = String.format(calculate,vehicleNumber);
            ResponseEntity<InsuranceBreakup> response  = restTemplate.exchange(new URI(url), HttpMethod.POST,httpEntity,InsuranceBreakup.class);
            insuranceBreakup = response.getBody();
        }
        return new ResponseEntity<>(insuranceBreakup,HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.OK);
}

@GetMapping("/variant/{vehicleNumber}")
public ResponseEntity<?> getVariant(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                    @PathVariable (value = "vehicleNumber") String vehicleNumber ){
    CarVariant carVariant = new CarVariant();
    carVariant.setVariant("Range Rover Velar HSE Dynamic 2.0");
    carVariant.setFuel("Petrol");
    carVariant.setManufactureYear(String.valueOf(LocalDate.now().getYear()));
    return new ResponseEntity<>(carVariant,HttpStatus.OK);
}

@GetMapping("/manufacture/{vehicleNumber}")
public ResponseEntity<?> getManufactureDetails(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                    @PathVariable (value = "vehicleNumber") String vehicleNumber ){
    CarManufacture carManufacture = new CarManufacture();
    carManufacture.setManufacture("Jaguar Land Rover Automotive PLC");
    carManufacture.setBrandValue("US$2.23 billion");
    return new ResponseEntity<>(carManufacture,HttpStatus.OK);
}

@GetMapping("/registration/{vehicleNumber}")
public ResponseEntity<?> getCarRegistrationDetails(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                               @PathVariable (value = "vehicleNumber") String vehicleNumber ){
    CarRegistration carRegistration = new CarRegistration();
    carRegistration.setCity("seattle");
    carRegistration.setVIN("1HGCG1659WA029633");
    carRegistration.setRegisterDate(String.valueOf(LocalDate.now()));
    return new ResponseEntity<>(carRegistration,HttpStatus.OK);
}

@GetMapping("/history/{vehicleNumber}")
public ResponseEntity<?> getCarHistory(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                                   @PathVariable (value = "vehicleNumber") String vehicleNumber ){
    CarHistory carHistory = new CarHistory();
    carHistory.setAccidentCounts(4);
    Map<String,String> claimHistory = new HashMap<>();
    claimHistory.put("Accident_001", "Collision with another vehicle, $5000 claim, Approved");
    claimHistory.put("Accident_002", "Rear-end accident, $3000 claim, Pending review");
    claimHistory.put("Accident_003", "Single vehicle crash , $2000 claim, Rejected due to incomplete documents");
    claimHistory.put("Accident_004", "Parking lot accident , $1200 claim, Under investigation");
    return new ResponseEntity<>(carHistory,HttpStatus.OK);
}

@GetMapping("/asset-value/{vehicleNumber}")
public ResponseEntity<?> getCarAssetValue(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                          @PathVariable (value = "vehicleNumber") String vehicleNumber){
    CarAssetValue carAssetValue = new CarAssetValue();
    carAssetValue.setCurrentAssetValue("10900000");
    carAssetValue.setDepreciationRate("0.10");
    carAssetValue.setAssetValueIn5Years("6543826.10");
    return new ResponseEntity<>(carAssetValue,HttpStatus.OK);
}

@PostMapping("/quote/calculate")
public ResponseEntity<?> getCarQuoteCalculations(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                                 @RequestBody (required = false) CalRequest calRequest){
    ODPremium odPremium = new ODPremium();
    odPremium.setBasicOdPremium("40000");
    odPremium.setNoClaimBonus("8000");
    odPremium.setMessage("percentage 20%");
    
    PremiumDetails premiumDetails = new PremiumDetails();
    premiumDetails.setPremium("32000");
    premiumDetails.setGst("18%");
    InsuranceBreakup insuranceBreakup = new InsuranceBreakup();
    insuranceBreakup.setOdPremium(odPremium);
    insuranceBreakup.setPremiumDetails(premiumDetails);
    insuranceBreakup.setNetODPremium("32000");
    insuranceBreakup.setPayableAmount("37760");
    insuranceBreakup.setMessage("Prices are inclusive of GST");
    return new ResponseEntity<>(insuranceBreakup, HttpStatus.OK);
}
}
