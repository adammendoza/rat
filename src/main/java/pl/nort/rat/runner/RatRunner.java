package pl.nort.rat.runner;

import org.raml.model.Raml;
import org.raml.parser.visitor.RamlDocumentBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.nort.rat.parse.RamlProcessor;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class RatRunner implements CommandLineRunner {

    private final RamlProcessor ramlProcessor;
    private final String endpoint;
    private final String testPath;

    @Inject
    public RatRunner(RamlProcessor ramlProcessor, @Value("${endpoint}") String endpoint, @Value("${testPath}") String testPath) {
        this.ramlProcessor = checkNotNull(ramlProcessor);
        this.endpoint = checkNotNull(endpoint);
        this.testPath = checkNotNull(testPath);
    }

    @Override
    public void run(String... args) throws Exception {
        Raml raml = new RamlDocumentBuilder().build(testPath);

        ramlProcessor.process(raml);

        System.out.println("running test: " + testPath + " against: " + endpoint);
    }
}
