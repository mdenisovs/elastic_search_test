package com.java_club;

import com.java_club.client.ElasticNode;

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