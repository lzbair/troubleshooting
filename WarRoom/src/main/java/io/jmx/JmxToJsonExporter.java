package io.jmx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Usage:
 * > Compile: javac -d target\classes\ -cp src\main\resources\* src\main\java\io\jmx\JmxExporter.java
 * > Run : java -Dlog4j.configurationFile=src\main\resources\log4j2.xml -cp src\main\resources\*;target\classes\;. io.jmx.JmxExporter hybris:*
 */
class JmxToJsonExporter {

    static final ObjectName ALL_DOMAINS = null;
    static final String NAME = "name";
    static final QueryExp NO_QUERY_EXP = null;
    static String URL = "service:jmx:rmi:///jndi/rmi://localhost:9024/jmxrmi";


    static Logger LOG = LogManager.getLogger(JmxToJsonExporter.class);


    public static void main(String[] args) throws Exception {
        ObjectName objectName = (args != null && args.length > 0) ? new ObjectName(args[0]) : ALL_DOMAINS;
        String metric = (args != null && args.length > 1) ? args[1] : "";

        JmxExporterTask task = new JmxExporterTask(objectName, metric);

        new Timer().scheduleAtFixedRate(task, 0, 10000L);
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
                LOG.debug("Unable to export jmx data for domain. Reason: " + e.getMessage());
            }

        }
    }


    private static void exportAll(MBeanServerConnection jmx, ObjectName objectName) throws IOException {
        for (ObjectName oName : jmx.queryNames(objectName, NO_QUERY_EXP)) {
            export(jmx, oName);
        }
    }


    static void export(MBeanServerConnection jmx, final ObjectName oName) {


        MBeanInfo mbInfo;
        try {
            mbInfo = jmx.getMBeanInfo(oName);
        } catch (Exception e) {
            LOG.debug("Unable to get MBean : " + oName);
            return;
        }
        LOG.debug("############################# Stats for object: " + oName);

        final JMXReport jmxReport = new JMXReport();
        Arrays.stream(mbInfo.getAttributes()).forEach(
                attr -> {
                    try {
                        jmxReport.put(oName.getCanonicalName(), attr.getName(), jmx.getAttribute(oName, attr.getName()).toString());
                    } catch (Exception e) {
                        LOG.debug("Unable to export stats for attribute: " + attr.getName() + " (Reason: )" + e.getMessage());
                    }
                });


        LOG.info(jmxReport.toJson());
    }

    static class JMXReport {
        Map<String, Map<String, String>> stats = new HashMap<>();

        void put(String mbean, String attributeKey, String attributeValue) {
            if (this.stats.get(mbean) != null) {
                this.stats.get(mbean).put(attributeKey, attributeValue);
            } else {
                Map<String, String> newStats = new HashMap<>();
                newStats.put(attributeKey, attributeValue);
                this.stats.put(mbean, newStats);
            }
        }

        JSONObject toJson() {
            return new JSONObject(stats);
        }

    }
}