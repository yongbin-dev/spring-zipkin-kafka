package com.yb.controller;

import com.yb.dto.request.CourseRequestDto;
import com.yb.dto.response.CourseResponseDto;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class CourseControllerTest {

  RestTemplate restTemplate = new RestTemplate();
  Random random = new Random();
  private final Logger log = LoggerFactory.getLogger(CourseControllerTest.class);

  @Test
  void create_many_student() {
    for (int i = 0; i < 100; i++) {
      String professor = "교수" + (random.nextInt(1000) + 1);
      int year = 2024;
      String courseName = "수강" + (random.nextInt(1000) + 1);
      var resp = restTemplate.postForEntity(
          "http://localhost:8085/course",
          new CourseRequestDto(professor, year, courseName, 1000),
          CourseResponseDto.class
      );

      if (resp.getStatusCode().isError()) {
        log.info("error: {}", resp.getStatusCode());
      }
    }
  }

}
