package pl.nort.rat.process;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.raml.model.Raml;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

@Primary
@Component
public class EndpointCallingProcessor implements RamlProcessor {

    private final String endpoint;

    @Inject
    public EndpointCallingProcessor(@Value("${endpoint}") String endpoint) {
        this.endpoint = checkNotNull(endpoint);
    }

    @Override
    public void process(Raml raml) {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(endpoint);

            ResponseHandler<String> responseHandler = response -> {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            };

            System.out.println(httpclient.execute(httpget, responseHandler));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
