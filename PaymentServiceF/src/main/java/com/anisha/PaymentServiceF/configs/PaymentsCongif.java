package com.anisha.PaymentServiceF.configs;


import com.anisha.PaymentServiceF.dtos.OrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Configuration
public class PaymentsCongif{

    @Value("${auth.server.token.url}")
    private String authServerTokenUrl;

    @Value("${payment.service.client-id}")
    private String clientId;

    @Value("${payment.service.client-secret}")
    private String clientSecret;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    @Autowired
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("scope", "order.read");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        RestTemplate restTemplate = restTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(authServerTokenUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        } else {
            throw new RuntimeException("Failed here here here here to get access token from Auth Server");
        }
    }


    //post to Userservice to get bearer tokens
     //* Make a POST request to Order Service with Bearer token
    public ResponseEntity<OrderDto> callOrderServiceGetOrder(String path) {
        System.out.println("Anisha i am about to get token");
        String token = getAccessToken();
        System.out.println("Anisha i got the token");


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate().exchange(orderServiceUrl + "/order/" + path, HttpMethod.GET, entity, OrderDto.class);
    }

    public ResponseEntity<OrderDto> callOrderServicePlaceOrder(String orderId) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = orderServiceUrl + "/order/" + orderId + "/place";
        return restTemplate().exchange(url, HttpMethod.PUT, entity, OrderDto.class );
    }
    public ResponseEntity<OrderDto> callOrderServiceFailedOrder(String orderId) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = orderServiceUrl + "/order/" + orderId + "/cancel";
        return restTemplate().exchange(url, HttpMethod.PUT, entity, OrderDto.class );
    }
}