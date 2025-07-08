package com.tugalsan.api.tomcat.server;

import java.sql.Driver;
import java.util.ServiceLoader;
import javax.servlet.ServletContext;

public class TS_TomcatJDBCUtils {

    private TS_TomcatJDBCUtils() {

    }

    public static void registerJdbcDrivers(ServletContext context) {
        var iter = ServiceLoader.load(Driver.class, context.getClassLoader()).iterator();
        while (iter.hasNext()) {
            // Just for the side-effect of loading the class and registering the driver
            iter.next();
        }
    }
}
