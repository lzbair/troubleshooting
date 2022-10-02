package io.miscellaneous;

import oracle.jdbc.OracleDriver;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import static java.lang.System.exit;
import static java.lang.System.out;

public class ConnectionPoolDBStats {
    public static void main(String[] args) {
        if (args.length != 4) {
            out.println("Wrong arguments. " +
                    "Usage : java -cp  ojdbc8-19.13.0.0.jar:commons-math3-3.6.1.jar:. DBStats <db> <user> <pwd> <samplingSize>");
            exit(0);
        }
        final String db = args[0];
        final String user = args[1];
        final String pwd = args[2];
        final String url = "jdbc:oracle:thin:@ldap://oratns.hostname.com:389/" + db + ",cn=OracleContext,dc=yourDC,dc=com";

        int samplingSize = Integer.parseInt(args[3]);
        DescriptiveStatistics stats = new DescriptiveStatistics();
        try {
            out.println("Setting the Connection Properties at " + (new java.text.SimpleDateFormat("H:mm:ss:SSS")).format(java.util.Calendar.getInstance().getTime()));
            BasicDataSource ds = dataSource(url, user, pwd);
            Properties javaprops = System.getProperties();
            out.println("\nJVM\n===");
            out.println(javaprops.getProperty("java.vm.vendor"));
            out.println(javaprops.getProperty("java.vm.name"));
            out.println(javaprops.getProperty("java.vm.version"));
            out.println(javaprops.getProperty("java.version"));
            out.println(System.getProperties().get("java.class.path"));
            out.println("\nConnectivity to : " + db + "/" + user + "\n==================================");
            for (int i = 0; i < samplingSize; i++) {
                out.println("Initiating new Connection : " + i + " at " + (new java.text.SimpleDateFormat("H:mm:ss:SSS")).format(java.util.Calendar.getInstance().getTime()));
                long startTime = System.currentTimeMillis();
                try (Connection con = ds.getConnection()) {
                    out.println("---> Created at " + (new java.text.SimpleDateFormat("H:mm:ss:SSS")).format(java.util.Calendar.getInstance().getTime()) + " using JDBC Version " + (con.getMetaData()).getDriverVersion());
                    PreparedStatement statement = con.prepareStatement("SELECT * FROM DUAL");
                    try (ResultSet rs = statement.executeQuery()) {
                        out.println("---> Is Valid connection data: " + rs.next());
                    }
                    long endTime = System.currentTimeMillis();
                    long execTime = endTime - startTime;
                    out.println("--->  Execution time: " + execTime);
                    out.println("--->  Closed at " + (new java.text.SimpleDateFormat("H:mm:ss:SSS")).format(java.util.Calendar.getInstance().getTime()) + ". Now waiting for 2 Seconds for next connection\n");
                    stats.addValue(execTime);
                    Thread.sleep(2000);
                }
            }
        } catch (Exception e) {
            out.println("Error occurred : " + e);
        } finally {
            out.println("\nStats summary (in Millis) for : " + db + "/" + user + "\n==================================");
            out.println(stats);
        }


    }

    static BasicDataSource dataSource(String url, String user, String pwd) {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(pwd);
        ds.setDriver(new OracleDriver());
        ds.setInitialSize(12);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
        return ds;
    }
}
