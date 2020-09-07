package com.example.task7;

import com.example.task7.api.ApiFactory;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class APIAvailabilityTest {

    @Test
    public void testAvailability() throws Exception {
        URLConnection connection = new URL("https://newsapi.org/v2/" +
                "everything?q=software&apiKey=" + ApiFactory.API_KEY).openConnection();
        InputStream response = connection.getInputStream();

        StringBuilder buffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response, Charset.defaultCharset()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                buffer.append(line);
            }
        }

        assert buffer.length() > 0;
    }
}
