package io.miscellaneous;

import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

public class Ping {

    public static void main(String[] args) throws Exception {

        SSL.setup();

        int status = 200;

        while (status == 200) {
            status = ping();
        }


        Toolkit.getDefaultToolkit().beep();

    }



    private static int ping() throws IOException {

        URL url = new URL("https://nc2.nespresso.com/pro/ch/sitemap.xml/?hgjhhjh");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        String header = generate(() -> UUID.randomUUID().toString()).limit(900).collect(joining());

        con.setRequestProperty("Xxxx", header);

        int responseCode = con.getResponseCode();

        System.out.println("HTTP status: " + responseCode);

        System.out.println("HTTP header: ");
        con.getHeaderFields().forEach((k, v) -> System.out.println(k + " --> " + v));

        return responseCode;


    }
}
