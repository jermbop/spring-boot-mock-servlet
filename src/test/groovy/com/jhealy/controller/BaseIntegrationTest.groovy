package com.jhealy.controller

import com.jhealy.MainClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(classes = MainClass.class, webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseIntegrationTest extends Specification {

    @Shared
    MockTomcatServer mockTomcatServer

    MockServletHandler mockServletHandler

    def setupSpec() {
        mockTomcatServer = new MockTomcatServer()
        mockTomcatServer.startTomcat()
    }

    def setup() {
        mockServletHandler = Mock(MockServletHandler)
        mockTomcatServer.setMockServletHandler(mockServletHandler)
    }

    def cleanupSpec() {
        mockTomcatServer.stopTomcat()
    }

}
