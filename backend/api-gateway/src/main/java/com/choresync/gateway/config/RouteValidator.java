package com.choresync.gateway.config;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

	/*
	 * Exact paths that may be called without a token. Matching is exact on
	 * purpose: a substring match let any path containing e.g. "/auth/login"
	 * skip authentication.
	 */
	public static final List<String> openApiEndpoints = List.of(
			"/api/v1/auth/register",
			"/api/v1/auth/login");

	public Predicate<ServerHttpRequest> isSecured = request -> !openApiEndpoints
			.contains(request.getURI().getPath());
}
