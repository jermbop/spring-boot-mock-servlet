package com.jhealy.controller;

import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MockServlet extends HttpServlet {

    MockServletHandler servletHandler;

    public void setServletHandler(MockServletHandler servletHandler) {
        this.servletHandler = servletHandler;
    }

    public void doGet(HttpMethod method, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        bindMockResponseFieldsToServletResponse(resp, servletHandler.call(method, req.getRequestURI(), req));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(HttpMethod.GET, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    private void bindMockResponseFieldsToServletResponse(HttpServletResponse resp, Object response) throws IOException {

        //default OK
        resp.setStatus(200);
        if (response instanceof MockServletResponse) {
            MockServletResponse mockServletResponse = (MockServletResponse)response;
            resp.setStatus(mockServletResponse.getStatus());
            for (Map.Entry<String, String> header : mockServletResponse.getHeaders().entrySet()) {
                resp.setHeader(header.getKey(), header.getValue());
            }
            byte[] body = mockServletResponse.getBody();
            if (body != null) {
                resp.setContentType(mockServletResponse.getContentType());
                resp.setContentLength(body.length);
                resp.getOutputStream().write(mockServletResponse.getBody());
            }
        } else if (response instanceof Integer) {
            resp.setStatus((Integer)response);
        } else if (response instanceof String) {
            byte[] byteResponse = ((String) response).getBytes(Charset.forName("UTF-8"));
            resp.setContentLength(byteResponse.length);
            resp.getOutputStream().write(byteResponse);
        }
    }
}
