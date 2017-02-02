package com.jhealy.controller;

import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

public interface MockServletHandler {

    Object call(HttpMethod httpMethod, String uri, HttpServletRequest request);

}
