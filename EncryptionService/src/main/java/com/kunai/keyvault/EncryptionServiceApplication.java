package com.kunai.keyvault;


/**
 * The encryption service application initializer
 */

import com.kunai.keyvault.crypto.voltage.VoltageEncryptorFactory;
import com.kunai.keyvault.health.EncryptionHealthCheck;
import com.kunai.keyvault.health.VoltageHealthCheck;
import com.kunai.keyvault.hsm.TestHSM;
import com.kunai.keyvault.resources.DUKPTEncryptionResource;
import com.kunai.keyvault.resources.EncryptionResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionServiceApplication extends Application<EncryptionServiceConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionServiceApplication.class);

    /**
     * This is the entry point of the Encryption Service.
     * @param args A String array that takes initial parameters.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new EncryptionServiceApplication().run(args);
    }

    /**
     *
     * @return String describing the module
     */
    @Override
    public String getName() {
        return "encryption-service";
    }

    /**
     *
     * @param bootstrap The pre-start application environment, containing everything required to bootstrap a Dropwizard command.
     *                  It wires up everything being used in the Environment, including the Configuration and the Application
     */
    @Override
    public void initialize(Bootstrap<EncryptionServiceConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new AssetsBundle());
    }

    /**
     *
     * This is the first method executed after the entry point, it performs the Health Checks and register the EncryptionResource.
     * @param configuration Passes an instance of EncryptionServiceConfiguration
     * @param environment This is the Dropwizard Environment container, it has properties that are to the Dropwizard
     *                    framework such as the jersey web container.
     */
    @Override
    public void run(EncryptionServiceConfiguration configuration, Environment environment) {

        // HSM integration for DUKPT may be supported in the future...
        if (configuration.hsmType != null) {
            // TODO: there's a better way to do this.  Refactor it to work like the Encryptor
            switch (configuration.hsmType.toLowerCase().trim()) {
                case "test":
                default:
                    LOGGER.info("Using test HSM");
                    DUKPTEncryptionResource.hsm = new TestHSM();
                    break;
            }
            environment.jersey().register(new DUKPTEncryptionResource(configuration.encryptor));
        }

        if (configuration.encryptor instanceof VoltageEncryptorFactory) {
            environment.healthChecks().register("voltage", new VoltageHealthCheck());
        };
        environment.healthChecks().register("encryptor", new EncryptionHealthCheck());
        environment.jersey().register(new EncryptionResource(configuration.encryptor));
    }
}
