package com.jhealy.controller;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MockServletResponse {

    private int status = 200;
    private byte[] body = new byte[]{};
    private String contentType = "application/octet-stream";
    private Map<String,String> headers = new HashMap<String,String>();

    public MockServletResponse(int status, byte[] body, String contentType, Map<String, String> headers) {
        this.status = status;
        this.body = body;
        this.contentType = contentType;
        this.headers = headers;
    }

    public static MockServletResponse.Builder status(int status) {
        return new MockServletResponse.Builder().status(status);
    }

    public int getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }

    public Map<String,String> getHeaders() {
        return headers;
    }

    public String getContentType() {
        return contentType;
    }

    public static class Builder {

        private int status = 200;
        private byte[] body;
        private String contentType = "application/octet-stream";
        private Map<String,String> headers = new HashMap<String,String>();

        public MockServletResponse build() {
            return new MockServletResponse(status, body, contentType, headers);
        }

        public MockServletResponse.Builder status(int status) {
            this.status = status;
            return this;
        }

        public MockServletResponse.Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public MockServletResponse.Builder body(InputStream body) throws IOException {
            this.body = IOUtils.toByteArray(body);
            return this;
        }

        public MockServletResponse.Builder body(String body) {
            this.body = body.getBytes();
            return this;
        }

        public MockServletResponse.Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public MockServletResponse.Builder header(String name, String value) {
            headers.put(name, value);
            return this;
        }
    }
}
