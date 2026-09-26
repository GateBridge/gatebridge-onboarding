#!/usr/bin/env bash
set -e

echo "=== Launching GateBridge Onboarding Gateway (Headless Mode) ==="
mvn exec:java -Dexec.args="--headless"
