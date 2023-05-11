package io.jmx;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;
import static java.lang.System.out;

public class JmxDomains {


    static String URL = "service:jmx:rmi:///jndi/rmi://{HOST}:9200/jmxrmi";

    public static void main(String... args) throws Exception {
        if (args.length == 0) {
            out.println("Wrong arguments. " +
                    "Usage : java JmxDomains <host> <user> <pwd>");
            exit(0);
        }
        final String host = args[0];
        JMXServiceURL serviceURL = new JMXServiceURL(URL.replace("{HOST}", host));

        Map<String, Object> env = new HashMap<String, Object>();
        if (args.length == 3) {
            String[] creds = new String[2];
            creds[0] = args[1];
            creds[1] = args[2];
            env.put(JMXConnector.CREDENTIALS, creds);
        }


        System.out.println(serviceURL);
        Arrays.stream(JMXConnectorFactory
                .connect(serviceURL, env).getMBeanServerConnection()
                .getDomains()).forEach(System.out::println);
    }

}