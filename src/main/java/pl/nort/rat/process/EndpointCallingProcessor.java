package pl.nort.rat.process;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.raml.model.Action;
import org.raml.model.ActionType;
import org.raml.model.Raml;
import org.raml.model.Resource;
import org.raml.model.parameter.QueryParameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

        String url = buildGetUrlFor(endpoint, raml);

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(url);
            System.out.println("Calling: " + url);

            ResponseHandler<String> responseHandler = response -> {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            };

            System.out.println(httpclient.execute(httpget, responseHandler));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildGetUrlFor(String endpoint, Raml raml) {
        StringBuilder stringBuilder = new StringBuilder(endpoint)
                .append("?");

        Resource resource = extractResources(raml).iterator().next();
        Map<String, QueryParameter> queryParameters = extractQueryParametersFor(resource, ActionType.GET);

        for (Map.Entry<String, QueryParameter> queryParameterEntry : queryParameters.entrySet()) {
            stringBuilder
                    .append(queryParameterEntry.getKey())
                    .append("=")
                    .append(queryParameterEntry.getValue().getDefaultValue())
                    .append("&");
        }

        return stringBuilder.toString();
    }

    private Map<String, QueryParameter> extractQueryParametersFor(Resource resource, ActionType actionType) {
        Map<ActionType, Action> actions = resource.getActions();
        Action action = actions.get(actionType);
        return action.getQueryParameters();
    }

    private Set<Resource> extractResources(Raml raml) {
        Map<String, Resource> resources = raml.getResources();
        return new HashSet<>(resources.values());
    }
}
