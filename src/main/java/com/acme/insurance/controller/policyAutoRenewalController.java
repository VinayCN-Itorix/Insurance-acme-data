package com.acme.insurance.controller;

import com.acme.insurance.model.Health.PaymentResponse;
import com.acme.insurance.model.Policy.*;
import io.apiwiz.compliance.config.EnableCompliance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("v1/policy")
@EnableCompliance
public class policyAutoRenewalController {

@Value("${api.renew.auto:null}")
private String renewAuto;

@Value("${api.card.details:null}")
private String cardDetails;

@Value("${api.payment.debit:null}")
private String paymentDebit;

@Value("${api.payment.retry:null}")
private String paymentRetry;

@Value("${api.profile.update:null}")
private String profileUpdate;

@Value("${api.sms.notification:null}")
private String smsNotification;

@Autowired
private RestTemplate restTemplate;

@PostMapping("/renew/auto") //renew
public ResponseEntity<?> renewPolicy(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                     @RequestHeader (value = "deviateResponse",required = false) boolean deviateResponse,
                                     @RequestBody(required = false) RenewalRequest request) throws URISyntaxException {
    if(enableTracing){
        HttpHeaders headers = new HttpHeaders();
        headers.add("enableTracing",String.valueOf(Boolean.TRUE));
        headers.add("deviateResponse",String.valueOf(deviateResponse));
        headers.add("Content-Type","application/json");
        HttpEntity httpEntity = new HttpEntity<>(request,headers);
        ResponseEntity<PaymentResponse> res =   restTemplate.exchange(new URI(cardDetails), HttpMethod.POST,httpEntity,PaymentResponse.class);
        return new ResponseEntity<>(res.getBody(), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.OK);
}
@PostMapping("/card/details") //card
public ResponseEntity<?> fetchPayment(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                     @RequestHeader (value = "deviateResponse",required = false) boolean deviateResponse,
                                      @RequestBody(required = false) RenewalRequest request) throws URISyntaxException {
    RenewalCard renewalCard = new RenewalCard();
    renewalCard.setCardId(String.valueOf(UUID.randomUUID()));
    renewalCard.setCardNo("4035 6977 4344 2234");
    renewalCard.setCvv("422");
    renewalCard.setNameOnCard("Elon bentley");
    renewalCard.setExpiry("10-10-2030");
    if(enableTracing){
        RenewalPaymentRequest renewalPaymentRequest = new RenewalPaymentRequest();
        renewalPaymentRequest.setPolicyId(request.getPolicyId());
        renewalPaymentRequest.setPaymentId(String.valueOf(UUID.randomUUID()));
        renewalPaymentRequest.setCustomerId(request.getCustomerId());
        renewalPaymentRequest.setCardId(renewalCard.getCardId());
        renewalPaymentRequest.setAmount(request.getAmount());
        renewalPaymentRequest.setMessage(request.getDescription());
        HttpHeaders headers = new HttpHeaders();
        headers.add("enableTracing",String.valueOf(Boolean.TRUE));
        headers.add("deviateResponse",String.valueOf(deviateResponse));
        headers.add("Content-Type","application/json");
        HttpEntity httpEntity = new HttpEntity<>(renewalPaymentRequest, headers);
        ResponseEntity<PaymentResponse> res =   restTemplate.exchange(new URI(paymentDebit), HttpMethod.POST,httpEntity,PaymentResponse.class);
        return new ResponseEntity<>(res.getBody(), HttpStatus.OK);
    }
    return new ResponseEntity<>(renewalCard,HttpStatus.OK);
}

@PostMapping("/payment/debit") //details
public ResponseEntity<?> tryPayment(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                    @RequestHeader (value = "deviateResponse",required = false) boolean deviateResponse,
                                    @RequestBody(required = false) RenewalPaymentRequest request) throws URISyntaxException {
    RenewalPaymentResponse response = new RenewalPaymentResponse();
    if(deviateResponse){
        response.setPaymentId(request.getPaymentId());
        response.setPolicyId(request.getPolicyId());
        response.setPaymentStatus(false);
        response.setCustomerId(request.getCustomerId());
        response.setMessage("Payment Failed Due to Technical Issues");
        if(enableTracing){
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            headers.add("deviateResponse",String.valueOf(deviateResponse));
            headers.add("Content-Type","application/json");
            HttpEntity httpEntity = new HttpEntity<>(request, headers);
            restTemplate.exchange(new URI(paymentRetry), HttpMethod.POST,httpEntity,PaymentResponse.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
    else {
        response.setPaymentId(request.getPaymentId());
        response.setPolicyId(request.getPolicyId());
        response.setPaymentStatus(true);
        response.setCustomerId(request.getCustomerId());
        response.setMessage("Payment Success");
        if(enableTracing){
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            headers.add("deviateResponse",String.valueOf(deviateResponse));
            headers.add("Content-Type","application/json");
            Profile profile = new Profile();
            profile.setPolicyId(request.getPolicyId());
            profile.setPaymentStatus(response.getPaymentStatus());
            profile.setCustomerId(request.getCustomerId());
            HttpEntity httpEntity = new HttpEntity<>(profile, headers);
             restTemplate.exchange(new URI(profileUpdate), HttpMethod.PUT,httpEntity,Object.class);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
    return new ResponseEntity<>(response,HttpStatus.OK);
}

@PostMapping("/payment/re-try") //policy pay
public ResponseEntity<?> retryPayment(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                    @RequestHeader (value = "deviateResponse",required = false) boolean deviateResponse,
                                    @RequestBody(required = false) RenewalPaymentRequest request) throws URISyntaxException {
    RenewalPaymentResponse response = new RenewalPaymentResponse();
    if(deviateResponse){
        response.setPaymentId(request.getPaymentId());
        response.setPolicyId(request.getPolicyId());
        response.setPaymentStatus(false);
        response.setCustomerId(request.getCustomerId());
        response.setMessage("Payment Failed Due to Technical Issues");
        if(enableTracing){
            HttpHeaders headers = new HttpHeaders();
            Profile profile = new Profile();
            profile.setPolicyId(request.getPolicyId());
            profile.setPaymentStatus(response.getPaymentStatus());
            profile.setCustomerId(request.getCustomerId());
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            headers.add("deviateResponse",String.valueOf(deviateResponse));
            headers.add("Content-Type","application/json");
            HttpEntity httpEntity = new HttpEntity<>(profile,headers);
            ResponseEntity<PaymentResponse> res =   restTemplate.exchange(new URI(profileUpdate), HttpMethod.PUT,httpEntity,PaymentResponse.class);
            return new ResponseEntity<>(res.getBody(), HttpStatus.OK);
        }
    }
    else {
        response.setPaymentId(request.getPaymentId());
        response.setPolicyId(request.getPolicyId());
        response.setPaymentStatus(true);
        response.setCustomerId(request.getCustomerId());
        response.setMessage("Payment Success");
        if(enableTracing){
            HttpHeaders headers = new HttpHeaders();
            Profile profile = new Profile();
            profile.setPolicyId(request.getPolicyId());
            profile.setPaymentStatus(response.getPaymentStatus());
            profile.setCustomerId(request.getCustomerId());
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            headers.add("deviateResponse",String.valueOf(deviateResponse));
            headers.add("Content-Type","application/json");
            HttpEntity httpEntity = new HttpEntity<>(profile,headers);
            ResponseEntity<PaymentResponse> res =   restTemplate.exchange(new URI(profileUpdate), HttpMethod.PUT,httpEntity,PaymentResponse.class);
            return new ResponseEntity<>(res.getBody(), HttpStatus.OK);
        }
    }
    return new ResponseEntity<>(response,HttpStatus.OK);
}
@PutMapping("/profile/update")
public ResponseEntity<?> updateProfile(@RequestHeader(value = "enableTracing", required = false) boolean enableTracing,
                                      @RequestHeader (value = "deviateResponse",required = false) boolean deviateResponse,
                                      @RequestBody(required = false) Profile profile) throws URISyntaxException {
    if(enableTracing) {
        Notification notification = new Notification();
        if (profile.getPaymentStatus()) {
            notification.setMessage("Policy renew for " + profile.getPolicyId() + " success ");
            notification.setMsgId(String.valueOf(UUID.randomUUID()));
        } else {
            notification.setMessage("Policy renew for " + profile.getPolicyId() + " is failed");
            notification.setMsgId(String.valueOf(UUID.randomUUID()));
        }
            HttpHeaders headers = new HttpHeaders();
            headers.add("enableTracing",String.valueOf(Boolean.TRUE));
            headers.add("deviateResponse",String.valueOf(deviateResponse));
        headers.add("Content-Type","application/json");
        HttpEntity httpEntity = new HttpEntity<>(notification,headers);
             restTemplate.exchange(new URI(smsNotification), HttpMethod.POST,httpEntity,PaymentResponse.class);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}

@PostMapping("/sms/notification")
public ResponseEntity<?> updateProfile(@RequestBody(required = false) Notification notification){
    NotificationStatus notificationStatus =new NotificationStatus();
    notificationStatus.setMessage("Success");
    notificationStatus.setStatus(true);
    return new ResponseEntity<>(notificationStatus,HttpStatus.OK);
}
}
