package com.mvfiscal.api.userservice.service;

import com.mvfiscal.api.userservice.dto.SetTaskDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TaskRestService {

    private final RestTemplate restTemplate;
    @Value("${mvfiscal.api.taskservice.url}")
    private String taskServiceUrl;

    public TaskRestService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean userHasAssociatedTasks(Long userId) {
        String url = String.format("%s/tarefa?userId=%d&page=0&size=1", this.taskServiceUrl, userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );

        return response.getStatusCode().equals(HttpStatus.OK);
    }
}
