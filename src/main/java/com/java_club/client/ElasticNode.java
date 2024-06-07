package com.java_club.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.java_club.services.CurrencyService;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

public class ElasticNode {

    private static final String SERVER_URL = "http://localhost:9200";
    private final ElasticsearchClient esClient;
    private final CurrencyService currencyService;

    public ElasticNode() {
        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(SERVER_URL))
                /*   .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })*/
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        this.esClient = new ElasticsearchClient(transport);

        this.currencyService = new CurrencyService();
    }

    public CreateIndexResponse createIndex(String name, int numberOfShards, int numberOfReplicas) throws IOException {
        // Create index (3 shards)
        return esClient.indices().create(c -> c
                .settings(builder -> builder
                        .numberOfShards(String.valueOf(numberOfShards))
                        .numberOfReplicas(String.valueOf(numberOfReplicas)))
                .index(name)
        );
    }

    public <T> UpdateResponse<T> update(String index, String id, T document, Class<T> clazz) throws IOException {
        return esClient.update(u -> u
                .index(index)
                .id(id)
                .doc(document)
                .upsert(document), clazz);
    }

    public <T> GetResponse<T> get(String index, String id, Class<T> clazz) throws IOException {
        GetResponse<T> response = esClient.get(g -> g
                .index(index)
                .id(id), clazz);

        if (response.found()) {
            Object result = response.source();
            System.out.println("Get object: NAME:[" + result + "] ID:" + result + "\n");
        } else {
            System.out.println("object not found");
        }

        System.out.println("Indexed with version " + response.version() + "\n");

        return response;
    }

    public <T> SearchResponse<T> searchByField(String index, String matchField, String query, Class<T> clazz) throws IOException {
        // Works after client close -> looks like it can't search immediately after insert
        SearchResponse<T> searchResponse = esClient.search(s -> s
                .index(index)
                .query(q -> q.match(t -> t.field(matchField).query(query))), clazz);

        System.out.println("search hits total: " + searchResponse.hits().total());
        System.out.println("Search result: " + searchResponse + "\n");

        return searchResponse;
    }

    public <T> IndexResponse addElement(String index, String id, T document) throws IOException {
        // Add element to index
        return esClient.index(i -> i
                .index(index)
                .id(id)
                .document(document));
    }

    public DeleteResponse deleteElement(String index, String id) throws IOException {
        DeleteResponse response = esClient.delete(d -> d.index(index).id(id));
        System.out.println("Delete response: " + response + "\n");

        return response;
    }

    public DeleteIndexResponse deleteIndex(String index) throws IOException {
        DeleteIndexResponse delIndexResp = esClient.indices().delete(d -> d
                .index(index)
        );
        System.out.println("Delete Index response: " + delIndexResp);

        return delIndexResp;
    }

    public void addTestElements() throws IOException, URISyntaxException {
        currencyService.init();
        createIndex(CurrencyService.INDEX, 3, 1);
        currencyService.getCurrencies().forEach(currency -> {
            try {
                addElement("btc_usd_1h", UUID.randomUUID().toString(), currency);
            } catch (IOException e) {
                System.out.println("Unable to add element: " + e);
            }
        });
    }

    public void close() throws IOException {
        esClient._transport().close();
    }
}
