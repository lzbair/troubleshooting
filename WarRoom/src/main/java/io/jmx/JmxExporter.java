package io.jmx;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Usage:
 * > Compile: javac JmxExporter.java
 * > Run : java -cp . JmxExporter [jmXDomain] [filterByName]
 * > e.g. java -cp . JmxExporter tomcat.jdbc:* \"jdbc/nrs-logging-ds\"
 */
class JmxExporter {

    public static final ObjectName ALL_DOMAINS = null;
    public static final String NAME = "name";
    private static final QueryExp NO_QUERY_EXP = null;
    static String URL = "service:jmx:rmi:///jndi/rmi://localhost:9300/jmxrmi";

    public static void main(String[] args) throws Exception {
        ObjectName objectName = (args != null && args.length > 0) ? new ObjectName(args[0]) : ALL_DOMAINS;
        String metric = (args != null && args.length > 1) ? args[1] : "";

        JmxExporterTask task = new JmxExporterTask(objectName, metric);

        new Timer().scheduleAtFixedRate(task, 0, 2000L);
    }


    static class JmxExporterTask extends TimerTask {
        ObjectName objectName;
        String metric;

        JmxExporterTask(ObjectName objectName, String metric) {
            this.objectName = objectName;
            this.metric = metric;
        }

        @Override
        public void run() {

            try (JMXConnector connector = JMXConnectorFactory.connect(new JMXServiceURL(URL))) {
                MBeanServerConnection jmx = connector.getMBeanServerConnection();


                if (metric.isEmpty()) {
                    exportAll(jmx, objectName);
                } else {
                    jmx.queryNames(objectName, NO_QUERY_EXP)
                            .stream()
                            .filter(on -> on.getKeyProperty(NAME).equals(metric))
                            .forEach(on -> export(jmx, on));
                }
            } catch (IOException e) {
                System.out.println("Unable to export jmx data for domain: " + objectName.getDomain() + " (Reason: )" + e.getMessage());
            }

        }
    }


    private static void exportAll(MBeanServerConnection jmx, ObjectName objectName) throws IOException {
        for (ObjectName oName : jmx.queryNames(objectName, NO_QUERY_EXP)) {
            export(jmx, oName);
        }
    }


    static void export(MBeanServerConnection jmx, final ObjectName oName) {

        System.out.println("\n<BEGIN #########################################################################");
        System.out.println("Stats for object: " + oName + ":");
        System.out.println("Domain: " + oName.getDomain());
        System.out.println("Canonical Name: " + oName.getCanonicalName());

        MBeanInfo mbInfo;
        try {
            mbInfo = jmx.getMBeanInfo(oName);
        } catch (Exception e) {
            System.out.println("Unable to get MBean : " + oName);
            return;
        }

        Arrays.stream(mbInfo.getAttributes()).forEach(
                attr -> {
                    try {
                        System.out.println(attr.getName() + " ---> " + jmx.getAttribute(oName, attr.getName()));
                    } catch (Exception e) {
                        System.out.println("Unable to export stats for attribute: " + attr.getName() + " (Reason: )" + e.getMessage());
                    }
                });

        System.out.println("<END #########################################################################");

    }
}