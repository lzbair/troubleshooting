package io.jmx;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JmxInvoke {


    static String URL = "service:jmx:rmi:///jndi/rmi://localhost:9300/jmxrmi";

    public static void main(String... args) throws Exception {
        JMXConnectorFactory
                .connect(new JMXServiceURL(URL)).getMBeanServerConnection()
                .getMBeanInfo(new ObjectName(args[0]));
    }

}