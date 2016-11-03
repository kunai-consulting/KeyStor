package com.kunai.keyvault.health;

import com.codahale.metrics.health.HealthCheck;
import com.kunai.keyvault.crypto.voltage.VoltageEncryptor;
import com.kunai.keyvault.crypto.voltage.vibesimple.VibeSimple_Service;

import java.net.URL;

public class VoltageHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        new VibeSimple_Service(new URL(VoltageEncryptor.WSDL)).getVibeSimpleSOAP();

        // We didn't throw anything so...
        return Result.healthy();
    }
}
