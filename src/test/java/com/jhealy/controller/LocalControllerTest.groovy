package com.jhealy.controller

import com.jhealy.spring.CustomException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Unroll

import javax.servlet.http.HttpServletRequest

import static org.springframework.http.HttpMethod.GET

class LocalControllerTest extends BaseIntegrationTest {

  @Autowired
  LocalController controller

  @Autowired
  private RestTemplate restTemplate;

  @LocalServerPort
      port

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
  def "controller third party call - respond with integer status code only - ClientException"() {

    when:
    this.restTemplate.exchange(URI.create("http://localhost:${port}/local/thirdparty"), GET, null, String.class)

    then:
    Exception e = thrown(Exception)
    println e
    1 * this.mockServletHandler.call(GET, "/thirdpartyrequestpath", _ as HttpServletRequest) >> responseStatus

    where:
    responseStatus | _
    404            | _
    500            | _
  }

  @Unroll
  def "controller third party call throws exception"() {

    String exceptionMessage = "This is my exception being thrown"

    when:
    this.restTemplate.exchange(URI.create("http://localhost:${port}/local/thirdparty"), GET, null, String.class)

    then:
    CustomException e = thrown(CustomException)
    e.getStatus() == 404
    e.message == exceptionMessage
    1 * this.mockServletHandler.call(GET, "/thirdpartyrequestpath", _ as HttpServletRequest) >> {
      throw new CustomException(exceptionMessage, 404)
    }
  }

}
