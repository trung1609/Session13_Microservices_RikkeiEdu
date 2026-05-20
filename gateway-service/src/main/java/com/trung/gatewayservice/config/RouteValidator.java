package com.trung.gatewayservice.config;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints =
            List.of("/identity/api/auth/**");

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints.stream().anyMatch(openApi -> pathMatcher.match(openApi, request.getURI().getPath()));
}
