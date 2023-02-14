package org.opensearch.ad.rest;

import static org.opensearch.rest.RestRequest.Method.GET;
import static org.opensearch.rest.RestStatus.NOT_FOUND;
import static org.opensearch.rest.RestStatus.OK;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.ad.constant.CommonErrorMessages;
import org.opensearch.ad.settings.EnabledSetting;
import org.opensearch.extensions.rest.ExtensionRestRequest;
import org.opensearch.extensions.rest.ExtensionRestResponse;
import org.opensearch.rest.RestHandler.Route;
import org.opensearch.rest.RestRequest.Method;
import org.opensearch.sdk.ExtensionRestHandler;

public class RestGetDetectorAction implements ExtensionRestHandler {
    private final Logger logger = LogManager.getLogger(RestGetDetectorAction.class);

    @Override
    public List<Route> routes() {
        return List.of(new Route(GET, "/detectors"));
    }

    @Override
    public ExtensionRestResponse handleRequest(ExtensionRestRequest request) {
        if (!EnabledSetting.isADPluginEnabled()) {
            throw new IllegalStateException(CommonErrorMessages.DISABLED_ERR_MSG);
        }
        Method method = request.method();

        if (!Method.GET.equals(method)) {
            return new ExtensionRestResponse(
                request,
                NOT_FOUND,
                "Extension REST action improperly configured to handle " + request.toString()
            );
        }
        // do things with request
        return new ExtensionRestResponse(request, OK, "placeholder");
    }

}