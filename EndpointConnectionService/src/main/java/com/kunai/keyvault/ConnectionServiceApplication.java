package com.kunai.keyvault;

import com.kunai.keyvault.crypto.voltage.VoltageEncryptorFactory;
import com.kunai.keyvault.health.ConnectionHealthCheck;
import com.kunai.keyvault.health.VoltageHealthCheck;
import com.kunai.keyvault.resources.ConnectionResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;


/**
 *
 */
public class ConnectionServiceApplication extends Application<ConnectionServiceConfiguration> {

    /**
     * This is the entry point of the Encryption Service.
     * @param args A String array that takes initial parameters.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new ConnectionServiceApplication().run(args);
    }

    /**
     *
     * @return String describing the module
     */
    @Override
    public String getName() {
        return "connection-service";
    }


    /**
     *
     * @param bootstrap The pre-start application environment, containing everything required to bootstrap a Dropwizard command.
     *                  It wires up everything being used in the Environment, including the Configuration and the Application
     */
    @Override
    public void initialize(Bootstrap<ConnectionServiceConfiguration> bootstrap) {
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
    public void run(ConnectionServiceConfiguration configuration, Environment environment) {
        final HttpClient httpClient = new HttpClientBuilder(environment).using(configuration.getHttpClientConfiguration()).using(new RedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
                return false;
            }

            @Override
            public HttpUriRequest getRedirect(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
                return null;
            }
        })
                .build(getName() + "-proxy");
        if (configuration.encryptor instanceof VoltageEncryptorFactory) {
            environment.healthChecks().register("voltage", new VoltageHealthCheck());
        };
        environment.healthChecks().register("connection", new ConnectionHealthCheck());
        environment.jersey().register(new ConnectionResource(configuration.encryptor, configuration.decryptor, httpClient));
    }
}
