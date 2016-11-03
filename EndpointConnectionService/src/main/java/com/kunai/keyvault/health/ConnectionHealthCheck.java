package com.kunai.keyvault.health;

import com.codahale.metrics.health.HealthCheck;

public class ConnectionHealthCheck extends HealthCheck {

    /**
     * A basic ping health check to show the service is up and running.  At the lowest level there's really nothing to
     * check on since the service doesn't need a DB or anything like that by default.
     *
     * @return Healthy
     * @throws Exception
     */
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
