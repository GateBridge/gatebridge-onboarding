package io.gatebridge.onboarding;

import hexacloud.core.model.NodeStatus;
import hexacloud.core.model.PingProtocol;
import hexacloud.core.model.ServerNode;
import hexacloud.core.ports.GatewayBuilderPort;
import hexacloud.core.ports.RunningGatewayPort;
import hexacloud.core.server.PerformanceProfile;
import hexacloud.core.tui.TerminalUiFactory;
import hexacloud.infra.gateway.GatewayFactory;

import io.gatebridge.onboarding.controller.HealthCheckController;

/**
 * GateBridge Onboarding Application — Minimal production-ready Gateway starter template.
 * 
 * Copy and extend this boilerplate project whenever deploying GateBridge in any environment.
 */
public class OnboardingApplication {

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("   GateBridge API Gateway — Minimal Onboarding Starter           ");
        System.out.println("==================================================================");

        // 1. Parse CLI arguments
        boolean enableTui = true;
        for (String arg : args) {
            if ("--headless".equalsIgnoreCase(arg) || "headless".equalsIgnoreCase(arg)) {
                enableTui = false;
                break;
            }
        }

        // 2. Resolve target HTTP ingress port (reads $PORT or $GATEWAY_PORT env var, defaulting to 4000)
        int httpPort = 4000;
        String envPort = System.getenv("PORT");
        if (envPort == null || envPort.trim().isEmpty()) {
            envPort = System.getenv("GATEWAY_PORT");
        }
        if (envPort != null && !envPort.trim().isEmpty()) {
            try {
                httpPort = Integer.parseInt(envPort.trim());
            } catch (NumberFormatException ignored) {}
        }

        // GateBridge port offset: HTTP transport runs on (basePort + HTTP_PORT_OFFSET [1]).
        // To ensure HTTP transport binds EXACTLY to httpPort ($PORT, e.g. 4011), we set basePort = httpPort - 1.
        int basePort = httpPort - 1;
        int adminPort = 9090;

        System.out.println(">>> Bootstrapping GateBridge Gateway (HTTP Ingress Port: " + httpPort + ", Admin: " + adminPort + ")...");

        // 3. Programmatic Bootstrapping using GateBridge Core GatewayFactory API
        GatewayBuilderPort builder = GatewayFactory.createGateway("onboarding-gateway")
                .createCluster("default-cluster")
                .port(basePort)
                .adminPort(adminPort)
                .enableHttp(true)
                .enableTelnet(enableTui)
                .enableWs(true)
                .pingInterval(5)
                .performanceProfile(PerformanceProfile.BALANCED_1GB)
                .registerController(new HealthCheckController());

        // 4. Register upstream target server nodes (if available)
        builder.registerServer(new ServerNode(
                "http://localhost", 8080, NodeStatus.ONLINE, false,
                PingProtocol.HTTP, "/health", null, null
        ));

        // 5. Start listening and schedule backend health checks
        RunningGatewayPort gateway = builder.listen().startPingScheduler();

        System.out.println("\n==================================================================");
        System.out.println(" GATEWAY READY!");
        System.out.println(" • HTTP Ingress Gateway: http://localhost:" + httpPort);
        System.out.println(" • Admin Management API: http://localhost:" + adminPort);
        System.out.println(" • Fast-Path Root Route: http://localhost:" + httpPort + "/");
        System.out.println(" • Fast-Path Health Check: http://localhost:" + httpPort + "/v1/health");
        System.out.println(" • Fast-Path Ping Endpoint: http://localhost:" + httpPort + "/v1/ping");
        System.out.println("==================================================================\n");

        // 6. Optional interactive DevOps TUI
        if (enableTui) {
            TerminalUiFactory.createTui("GateBridge DevOps Console")
                    .seedGateway(gateway)
                    .redirectSystemOut(true)
                    .startToggleMode();
        }

        // 7. Shutdown Hook for Graceful Teardown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n>>> Shutting down GateBridge Gateway...");
            gateway.stop();
            System.out.println(">>> GateBridge Gateway stopped.");
        }));
    }
}
