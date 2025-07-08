package com.tugalsan.api.tomcat.server;

import java.sql.Driver;
import java.util.ServiceLoader;
import javax.servlet.ServletContext;
import com.tugalsan.api.function.client.maythrowexceptions.checked.TGS_FuncMTCUtils;
import com.tugalsan.api.sql.conn.server.TS_SQLConnConfig;
import com.tugalsan.api.sql.conn.server.TS_SQLConnMethodUtils;

public class TS_TomcatJDBCUtils {

    private TS_TomcatJDBCUtils() {

    }

    public static void registerJdbcDrivers(ServletContext context, TS_SQLConnConfig config) {
        TGS_FuncMTCUtils.run(() -> {
            var iter = ServiceLoader.load(Driver.class, context.getClassLoader()).iterator();
            while (iter.hasNext()) {
                // Just for the side-effect of loading the class and registering the driver
                iter.next();
            }
        }, e -> e.printStackTrace());
        if (config != null) {
            TGS_FuncMTCUtils.run(() -> {
                TS_SQLConnMethodUtils.getDriver(config);
            }, e -> e.printStackTrace());
        }
    }

}
