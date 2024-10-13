package com.acme.insurance.controller;

import com.acme.insurance.model.Health.*;
import io.apiwiz.compliance.config.EnableCompliance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
@RestController
@RequestMapping("v1/health")
@EnableCompliance
public class HealthInsuranceController {
@Value("${api.validate:null}")
private String validate;

@Value("${api.assess:null}")
private String assess;

@Value("${api.payment:null}")
private String payment;

@Value("${api.reject:null}")
private String reject;
@Autowired
private RestTemplate restTemplate;
@PostMapping("/insurance/claim-insurance") //claim
public ResponseEntity<?> claimHealthInsurance(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                              @RequestHeader (value = "deviateResponse",required = false) boolean deviateResponse,
                                              @RequestBody (required = false) HealthClaimRequest healthClaimRequest) throws URISyntaxException {
    if(enableTracing){
        HttpHeaders headers = new HttpHeaders();
        headers.add("enableTracing",String.valueOf(Boolean.TRUE));
        headers.add("deviateResponse",String.valueOf(deviateResponse));
        headers.add("Content-Type","application/json");
        HttpEntity httpEntity = new HttpEntity<>(healthClaimRequest, headers);
        ResponseEntity<PaymentResponse> res =   restTemplate.exchange(new URI(validate), HttpMethod.POST,httpEntity,PaymentResponse.class);
        return new ResponseEntity<>(res.getBody(), HttpStatus.OK);
    }
     return new ResponseEntity<>(HttpStatus.OK);
}

@PostMapping("/validate/validate-claim") //validate
public ResponseEntity<?> validateClaim(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                       @RequestHeader (value = "deviateResponse",required = false) boolean deviateResponse,
                                       @RequestBody (required = false) HealthClaimRequest healthClaimRequest) throws URISyntaxException {
    ValidateResponse validateResponse = new ValidateResponse();
    if(deviateResponse){
        if(enableTracing){
            validateResponse.setValid(false);
            validateResponse.setClaimId(String.valueOf(UUID.randomUUID()));
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            headers.add("deviateResponse",String.valueOf(deviateResponse));
            headers.add("Content-Type","application/json");
            HttpEntity httpEntity = new HttpEntity<>(validateResponse, headers);
            ResponseEntity<RejectClaimResponse> res = restTemplate.exchange(new URI(reject), HttpMethod.POST,httpEntity,RejectClaimResponse.class);
            return new ResponseEntity<>(res.getBody(), HttpStatus.OK);
        }
    }else {
        validateResponse.setValid(true);
        validateResponse.setClaimId(String.valueOf(UUID.randomUUID()));
        if(enableTracing){
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            headers.add("deviateResponse",String.valueOf(deviateResponse));
            headers.add("Content-Type","application/json");
            HttpEntity httpEntity = new HttpEntity<>(validateResponse, headers);
            ResponseEntity<PaymentResponse> res = restTemplate.exchange(new URI(assess), HttpMethod.POST,httpEntity,PaymentResponse.class);
            return new ResponseEntity<>(res.getBody(), HttpStatus.OK);
        }
        return new ResponseEntity<>(validateResponse, HttpStatus.OK);
        
    }
    return new ResponseEntity<>(validateResponse,HttpStatus.OK);
}

@PostMapping("/assess/assess-claim-amount") //assess
public ResponseEntity<?> assessClaimAmount(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                           @RequestHeader (value = "deviateResponse",required = false) boolean deviateResponse,
                                           @RequestBody (required = false) ValidateResponse validateResponse) throws URISyntaxException {
    AssessClaimResponse assessClaimResponse = new AssessClaimResponse();
    if(deviateResponse){
        assessClaimResponse.setClaimID(validateResponse.getClaimId());
        assessClaimResponse.setClaimPercentage("0");
        assessClaimResponse.setStatus("Rejected");
        assessClaimResponse.setPaymentId("----------");
        assessClaimResponse.setPolicyCoverage(String.valueOf(ThreadLocalRandom.current().nextInt(30, 71)));
    }else{
        assessClaimResponse.setClaimID(validateResponse.getClaimId());
        assessClaimResponse.setClaimPercentage(String.valueOf(ThreadLocalRandom.current().nextInt(30, 101)));
        assessClaimResponse.setStatus("Approved");
        assessClaimResponse.setPaymentId(String.valueOf(UUID.randomUUID()));
        assessClaimResponse.setPolicyCoverage(String.valueOf(ThreadLocalRandom.current().nextInt(30, 90)));
        if(enableTracing){
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            headers.add("deviateResponse",String.valueOf(deviateResponse));
            headers.add("Content-Type","application/json");
            HttpEntity httpEntity = new HttpEntity<>(assessClaimResponse, headers);
            ResponseEntity<PaymentResponse> res = restTemplate.exchange(new URI(payment), HttpMethod.POST,httpEntity,PaymentResponse.class);
            return new ResponseEntity<>(res.getBody(), HttpStatus.OK);
        }
    }
    return new ResponseEntity<>(assessClaimResponse,HttpStatus.OK);
}

@PostMapping("/payment/process-payment") //Payment
public ResponseEntity<?> processPayment(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                        @RequestHeader (value = "deviateResponse",required = false) boolean deviateResponse,
                                        @RequestBody (required = false ) AssessClaimResponse assessClaimResponse) throws URISyntaxException {
    PaymentResponse paymentResponse = new PaymentResponse();
    if(deviateResponse){
        paymentResponse.setMessage("INVALID PAYMENT ID");
    }else {
        paymentResponse.setPaymentStatus("COMPLETED");
        paymentResponse.setClaimAmount(String.valueOf(ThreadLocalRandom.current().nextInt(20000, 1000000)));
        paymentResponse.setMessage("Payment for Claim "+ assessClaimResponse.getClaimID() +" has been Processed Successfully");
    }
    return new ResponseEntity<>(paymentResponse,HttpStatus.OK);
}

@PostMapping("insurance/reject-claim")
public ResponseEntity<?> rejectClaim(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                     @RequestHeader (value = "deviateResponse", required = false) boolean deviateResponse,
                                     @RequestBody (required = false) ValidateResponse validateResponse) {
    RejectClaimResponse rejectClaimResponse = new RejectClaimResponse();
        rejectClaimResponse.setClaimId(validateResponse.getClaimId());
        rejectClaimResponse.setMessage("Claim " + validateResponse.getClaimId() + " has been successfully rejected.");
        rejectClaimResponse.setStatus("Rejected");
    return new ResponseEntity<>(rejectClaimResponse, HttpStatus.OK);
}
}
