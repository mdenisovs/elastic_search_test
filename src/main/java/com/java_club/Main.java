package com.java_club;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
        String serverUrl = "http://localhost:9200";
        //String apiKey = "eyJ2ZXIiOiI4LjEzLjQiLCJhZHIiOlsiMTcyLjIzLjAuMjo5MjAwIl0sImZnciI6ImI4MzYyNjI5M2NjNzQ1ZjAwNjZlNTdiYmUwNDQ3ODYxN2UwNjE5NzU5M2JkNjE4YzZlNzM3NGY4ODNiNDBhMDUiLCJrZXkiOiJzbUdocFk4Qm1tSWhrQkp1ZWMtVTowc3BNTEE0dVFvR0RDOVcxOUtXYXB3In0=";

        // Create the low-level client
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
/*                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })*/
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        // Create index
        esClient.indices().create(c -> c
                .index("products")
        );
        Product p1 = new Product();
        p1.setId(UUID.randomUUID().toString());
        p1.setName("guitar");

        // Add element to index
        IndexResponse response1 = esClient.index(i -> i
                .index("products")
                .id(p1.getId())
                .document(p1));
        System.out.println(response1 + "\n");

        // Get element by id
        printGet(esClient, p1);

        SearchResponse<Product> searchResponse = esClient.search(s -> s
                .index("products")
                .query(q -> q.match(t -> t.field("name").query("guitar"))), Product.class);

        System.out.println("Search result: " + searchResponse + "\n");

        p1.setName("electric guitar");
        UpdateResponse<Product> updResponse = esClient.update(u -> u
                        .index("products")
                        .id(p1.getId())
                        .doc(p1)
                        .upsert(p1),
                Product.class
        );

        System.out.println("Updated: " + updResponse + "\n");

        printGet(esClient, p1);

        // delete element
        DeleteResponse delResponse = esClient.delete(d -> d.index("products").id(p1.getId()));
        System.out.println("Delete response: " + delResponse + "\n");

        // delete index
        DeleteIndexResponse delIndexResp = esClient.indices().delete(d -> d
                .index("products")
        );
        System.out.println("Delete Index response: " + delIndexResp);

        esClient._transport().close();
    }

    private static void printGet(ElasticsearchClient esClient, Product p) throws IOException {
        GetResponse<Product> response2 = esClient.get(g -> g
                .index("products")
                .id(p.getId()), Product.class);

        if (response2.found()) {
            Product product = response2.source();
            System.out.println("Get Product: NAME:[" + product.getName() + "] ID:" + product.getId() + "\n");
        } else {
            System.out.println("Product not found");
        }

        System.out.println("Indexed with version " + response2.version() + "\n");
    }
}