package org.opensearch.ad;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.CreateIndexResponse;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;

public class SDKClient {
    private final Logger logger = LogManager.getLogger(SDKClient.class);

    public void createIndex() throws IOException{
        String endpoint = "localhost";
        String username = "admin";
        String password = "admin";
        String protocol = "http";
        int port = 9200;
        RestClient restClient = null;
        try {
            RestClientBuilder builder = RestClient.builder(new HttpHost(endpoint, port, protocol));
            builder.setStrictDeprecationMode(true);
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
                try {
                    return httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .setSSLContext(SSLContextBuilder.create().loadTrustMaterial(null, (chains, authType) -> true).build());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            restClient = builder.build();
            String index = "test-index-extensibility";

            // Create Client
            OpenSearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            OpenSearchClient client = new OpenSearchClient(transport);

            logger.info("Creating Index on OpenSearch");
            // Create Index
            CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder().index(index).build();
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest);
            logger.info("Created Index on OpenSearch", createIndexResponse);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (restClient != null) {
                restClient.close();
            }
        }
    }
}

// public SDKClient(Settings settings) {
// super(settings, null);
// }
//
// @Override
// protected <Request extends ActionRequest, Response extends ActionResponse> void doExecute(
// ActionType<Response> action,
// Request request,
// ActionListener<Response> listener
// ) {
// listener.onResponse(null);
// }
//
// @Override
// public void close() {
// // nothing really to do
// }
