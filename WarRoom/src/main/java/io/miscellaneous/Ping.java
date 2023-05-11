package io.miscellaneous;

import javax.net.ssl.SSLContext;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.util.UUID;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

public class Ping {


    static final String header = generate(() -> UUID.randomUUID().toString()).limit(90).collect(joining());
    public static final String URL = "my full url";


    public static void main(String[] args) throws Exception {

        //SSLContext sslContext = SSL.setup();

        ping();



        Toolkit.getDefaultToolkit().beep();

    }

/**
    private static int ping2(SSLContext sslContext) throws Exception {

        System.out.println("################ Start request: ################");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(URL))
                .version(HttpClient.Version.HTTP_2)
                .headers("Xxxx", header)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("HTTP status: " +  response.statusCode());
        System.out.println("HTTP version: " + response.version());
        response.headers().map().forEach((k, v) -> System.out.println(k + " --> " + v));


        System.out.println("################ End request: ################");


        return response.statusCode();
    }


    private static int ping3() throws Exception {

        System.out.println("################ Start request: ################");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(URL))
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("HTTP status: " +  response.statusCode());
        System.out.println("HTTP version: " + response.version());
        response.headers().map().forEach((k, v) -> System.out.println(k + " --> " + v));


        System.out.println("################ End request: ################");


        return response.statusCode();
    }
**/

    private static int ping() throws IOException {

        URL url = new URL(URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();

        System.out.println("HTTP status: " + responseCode);

        System.out.println("HTTP header: ");
        con.getHeaderFields().forEach((k, v) -> System.out.println(k + " --> " + v));

        return responseCode;


    }
}
