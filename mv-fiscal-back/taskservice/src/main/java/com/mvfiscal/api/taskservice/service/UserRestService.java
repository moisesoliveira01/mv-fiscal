package com.mvfiscal.api.taskservice.service;

import com.mvfiscal.api.taskservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserRestService {

    private final RestTemplate restTemplate;
    @Value("${mvfiscal.api.userservice.url}")
    private String userServiceUrl;

    public UserRestService() {
        this.restTemplate = new RestTemplate();
    }

    public UserDTO getUserById(Long userId) {
        String url = String.format("%s/usuario/%d", this.userServiceUrl, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UserDTO.class
                );

        return response.getBody();
    }
}
