package com.jhealy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Controller
public class LocalController {

  @Autowired
  RestTemplate restTemplate;

  @Value("${thirdParty.baseUri}")
  public String thirdPartyBaseUri;

  @RequestMapping("/local/helloworld")
  @ResponseBody
  String home() {
    return "hello world";
  }

  @RequestMapping("/local/thirdparty")
  @ResponseBody
  String thirdParty() {

    ResponseEntity<String> exchange = restTemplate.exchange(URI.create(thirdPartyBaseUri + "/thirdpartyrequestpath"), HttpMethod.GET, null, String.class);

    return exchange.getBody();
  }
}
