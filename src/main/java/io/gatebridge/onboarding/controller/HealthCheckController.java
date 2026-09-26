package io.gatebridge.onboarding.controller;

import hexacloud.core.server.route.RouteController;
import hexacloud.core.server.route.RouteMapping;

import java.io.PrintWriter;

/**
 * Custom fast-path system controller for GateBridge Onboarding Gateway.
 * Demonstrates how to write custom non-blocking endpoints directly inside GateBridge.
 */
public class HealthCheckController implements RouteController {

    @RouteMapping(value = "/", fastPath = true, isPublic = true)
    public void root(String args, PrintWriter out) {
        long timestamp = System.currentTimeMillis();
        out.println("{\"status\":\"UP\",\"gateway\":\"GateBridge API Gateway\",\"message\":\"Welcome to GateBridge Onboarding Gateway!\",\"timestamp\":" + timestamp + "}");
    }

    @RouteMapping(value = "/v1/health", fastPath = true, isPublic = true)
    public void healthCheck(String args, PrintWriter out) {
        long timestamp = System.currentTimeMillis();
        out.println("{\"status\":\"HEALTHY\",\"service\":\"gatebridge-onboarding\",\"gateway\":\"GateBridge Core 1.5.0-release\",\"timestamp\":" + timestamp + "}");
    }

    @RouteMapping(value = "/v1/ping", fastPath = true, isPublic = true)
    public void ping(String args, PrintWriter out) {
        out.println("PONG");
    }
}
