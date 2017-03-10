package com.jhealy.controller

import com.jhealy.spring.CustomException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Unroll

import javax.servlet.http.HttpServletRequest

import static org.springframework.http.HttpMethod.GET

class LocalControllerTest extends BaseIntegrationTest {

  @Autowired
  LocalController controller

  @Autowired
  private RestTemplate restTemplate;

  @LocalServerPort port

  def "hello world"() {

    expect:
    this.restTemplate.getForEntity(URI.create("http://localhost:${port}/local/helloworld"), String.class).getBody() == 'hello world'
  }

  def "controller third party call - respond with 200 and MockServletResponse"() {

    when:
    ResponseEntity<String> response = this.restTemplate.getForEntity(URI.create("http://localhost:${port}/local/thirdparty"), String.class)

    then:
    1 * this.mockServletHandler.call(GET, "/thirdpartyrequestpath", _ as HttpServletRequest) >> { HttpMethod method, String uri, HttpServletRequest servletRequest ->
      new MockServletResponse.Builder().status(200).body("hello world").build()
    }
    response.getBody() == "hello world"
  }

  @Unroll
  def "controller third party call - respond with integer status code only, empty body"() {

    when:
    ResponseEntity<String> response = restTemplate.exchange(URI.create("http://localhost:${port}/local/thirdparty"), GET, null, String.class)

    then:
    response.getStatusCode().value() == 200
    response.getBody() == null
    1 * this.mockServletHandler.call(GET, "/thirdpartyrequestpath", _ as HttpServletRequest) >> 200
  }

  @Unroll
  def "controller third party call - third party throws Client Side Exception, turns into HttpServerErrorException"() {

    when:
    this.restTemplate.exchange(URI.create("http://localhost:${port}/local/thirdparty"), GET, null, String.class)

    then:
    def e = thrown(HttpServerErrorException)
    e.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
    1 * this.mockServletHandler.call(GET, "/thirdpartyrequestpath", _ as HttpServletRequest) >> 404
  }

  @Unroll
  def "controller third party call - third party throws HttpServerErrorException"() {

    when:
    this.restTemplate.exchange(URI.create("http://localhost:${port}/local/thirdparty"), GET, null, String.class)

    then:
    def e = thrown(HttpServerErrorException)
    e.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
    1 * this.mockServletHandler.call(GET, "/thirdpartyrequestpath", _ as HttpServletRequest) >> 500
  }


//  @Unroll
//  def "controller third party call throws exception"() {
//
//    String exceptionMessage = "This is my exception being thrown"
//
//    when:
//    this.restTemplate.exchange(URI.create("http://localhost:${port}/local/thirdparty"), GET, null, String.class)
//
//    then:
//    CustomException e = thrown(CustomException)
//    e.getStatus() == 404
//    e.message == exceptionMessage
//    1 * this.mockServletHandler.call(GET, "/thirdpartyrequestpath", _ as HttpServletRequest) >> {
//      throw new CustomException(exceptionMessage, 404)
//    }
//  }

}
