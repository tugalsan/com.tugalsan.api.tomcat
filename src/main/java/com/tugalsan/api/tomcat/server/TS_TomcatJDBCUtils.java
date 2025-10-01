package com.tugalsan.api.tomcat.server;

import module com.tugalsan.api.function;
import module com.tugalsan.api.sql.conn;
import module javax.servlet.api;
import module java.sql;
import java.util.*;

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
