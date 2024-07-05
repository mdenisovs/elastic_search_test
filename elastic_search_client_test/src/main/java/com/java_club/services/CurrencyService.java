package com.java_club.services;

import com.java_club.dto.Currency;
import lombok.Getter;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CurrencyService {

    public static final String INDEX = "btc_usd_1h";
    private static final String FILE_PATH = "Bitstamp_BTCUSD_1h_1000.csv";

    @Getter
    private List<Currency> currencies;

    public void init() throws IOException, URISyntaxException {
        currencies = new ArrayList<>();
        try (ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(Paths.get(
                Objects.requireNonNull(getClass().getClassLoader().getResource(FILE_PATH)).toURI()).toFile()), CsvPreference.STANDARD_PREFERENCE)) {
            // the header elements are used to map the values to the bean
            //final String[] headers = beanReader.getHeader(true);
            final String[] headers = new String[]{"date", "symbol", "open", "high", "low", "close", "volumeBase", "volumeQuote"};
            final CellProcessor[] processors = getProcessors();

            Currency currency;
            while ((currency = beanReader.read(Currency.class, headers, processors)) != null) {
                assert currencies != null;
                currencies.add(currency);
            }
        }
    }

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[]{
                new NotNull(),
                new NotNull(),
                new NotNull(new ParseDouble()),
                new NotNull(new ParseDouble()),
                new NotNull(new ParseDouble()),
                new NotNull(new ParseDouble()),
                new NotNull(new ParseDouble()),
                new NotNull(new ParseDouble())
        };
        return processors;
    }
}
