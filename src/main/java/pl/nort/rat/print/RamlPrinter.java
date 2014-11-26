package pl.nort.rat.print;

import org.raml.model.*;
import org.raml.model.parameter.QueryParameter;
import org.springframework.stereotype.Component;
import pl.nort.rat.parse.RamlProcessor;

import java.util.Map;

@Component
public class RamlPrinter implements RamlProcessor {

    @Override
    public void parse(Raml raml) {

        Map<String, Resource> resources = raml.getResources();
        for (Map.Entry<String, Resource> resourceEntry : resources.entrySet()) {

            System.out.println("Resource: " + resourceEntry.getKey());

            Map<ActionType, Action> actions = resourceEntry.getValue().getActions();

            for (Map.Entry<ActionType, Action> actionEntry : actions.entrySet()) {

                System.out.println("  Action: " + actionEntry.getKey());

                Map<String, QueryParameter> queryParameters = actionEntry.getValue().getQueryParameters();

                for (Map.Entry<String, QueryParameter> queryParameterEntry : queryParameters.entrySet()) {

                    System.out.println("    Parameter: " + queryParameterEntry.getKey() +
                            (queryParameterEntry.getValue().isRequired() ? " *" : ""));

                }

                Map<String, Response> responses = actionEntry.getValue().getResponses();

                for (Map.Entry<String, Response> responseEntry : responses.entrySet()) {

                    System.out.println("    Response: " + responseEntry.getKey());

                    Map<String, MimeType> mimeTypes = responseEntry.getValue().getBody();

                    for (Map.Entry<String, MimeType> mimeTypeEntry : mimeTypes.entrySet()) {

                        System.out.println("      MimeType: " + mimeTypeEntry.getKey());
                        System.out.println("        Schema: " + mimeTypeEntry.getValue().getSchema());
                    }
                }

            }

        }
    }
}
