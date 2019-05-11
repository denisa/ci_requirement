package li.antonio.ci_requirement.echo;

import org.junit.jupiter.api.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GreetingResourceIT {
    private static final File PORTS_PROPERTIES = new File("target/ports.properties");

    @Test
    public void testMe() {
        assertThat(getGreetings().stripTrailing(), is("Hello World"));
    }

    private String getGreetings() {
        return ClientBuilder
                .newBuilder()
                .build()
                .target("http://localhost:" + getPort("host.port") + "/server/resources/greetings")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
    }


    private static String getPort(String portPropertyName) {
        Properties properties = loadDockerProperties();
        final String value = properties.getProperty(portPropertyName);
        if (value == null) {
            throw new IllegalArgumentException("Port " + portPropertyName + " has no defined value; defined values: " + properties.keySet());
        }
        return value;
    }

    private static Properties loadDockerProperties() {
        final Properties properties = new Properties();
        try (final FileReader reader = new FileReader(PORTS_PROPERTIES)) {
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Error loading port mapping file " + PORTS_PROPERTIES.getAbsolutePath() + ": " + e, e);
        }
    }

}
