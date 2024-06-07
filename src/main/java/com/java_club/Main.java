package com.java_club;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.java_club.client.ElasticNode;
import com.java_club.dto.Currency;
import com.java_club.services.CurrencyService;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ElasticNode node = new ElasticNode();
        node.addTestElements();

        //SearchResponse<Currency> searchResult = node.searchByField(CurrencyService.INDEX, "symbol", "BTCUSD", Currency.class);

        node.close();
    }
}