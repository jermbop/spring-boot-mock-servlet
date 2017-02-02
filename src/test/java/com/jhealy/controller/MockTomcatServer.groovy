package com.jhealy.controller

import org.apache.catalina.LifecycleException
import org.apache.catalina.startup.Tomcat

class MockTomcatServer {

    Tomcat tomcat;
    MockServlet mockServlet = new MockServlet()

    void startTomcat() {
        tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.setBaseDir(createTempContextDirectory());
        tomcat.addContext("/", createTempContextDirectory());
        tomcat.addServlet("/", "mockServlet", mockServlet).addMapping("/");
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    private static String createTempContextDirectory() {
        try {
            File dir = File.createTempFile("tomcat-integration-test-context", "");
            if (dir.delete() && dir.mkdirs()) {
                return dir.getAbsolutePath();
            } else {
                throw new RuntimeException("unable to create tomcat context directory: " + dir.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void stopTomcat() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    void setMockServletHandler(MockServletHandler mockServletHandler) {
        mockServlet.setServletHandler(mockServletHandler)
    }
}
