package io.jmx;

import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;

public class JmxDomains {


    static String URL = "service:jmx:rmi:///jndi/rmi://localhost:9300/jmxrmi";

    public static void main(String... args) throws Exception {
        Arrays.stream(JMXConnectorFactory
                .connect(new JMXServiceURL(URL)).getMBeanServerConnection()
                .getDomains()).forEach(System.out::println);
    }

}