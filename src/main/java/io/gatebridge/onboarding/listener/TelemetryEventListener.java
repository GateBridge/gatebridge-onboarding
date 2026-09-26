package io.gatebridge.onboarding.listener;

import hexacloud.core.event.EventController;
import hexacloud.core.event.Subscribe;
import hexacloud.core.cluster.event.ClusterEvent;

/**
 * Custom event listener for GateBridge EventBus.
 * Intercepts node status changes, telemetry updates, and cluster events in real-time.
 */
public class TelemetryEventListener implements EventController {

    @Subscribe
    public void onNodeStatusChanged(ClusterEvent.NodeStatusChanged event) {
        System.out.printf("[ONBOARDING-EVENT-BUS] Node Status Changed -> Host: %s | Status: %s\n",
                event.host(), event.status());
    }

    @Subscribe
    public void onTelemetryUpdated(ClusterEvent.NodeTelemetryUpdated event) {
        System.out.printf("[ONBOARDING-EVENT-BUS] Telemetry Received -> Host: %s\n", event.host());
    }
}
