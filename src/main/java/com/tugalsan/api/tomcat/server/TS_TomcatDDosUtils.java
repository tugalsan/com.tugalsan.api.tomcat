package com.tugalsan.api.tomcat.server;

import javax.servlet.*;

public class TS_TomcatDDosUtils {

    public static String getJarName(ServletContext ctx) {
        return TS_TomcatInfoUtils.version(ctx) < 10 ? getJarName_Tomcat9AndBelow() : getJarName_Tomcat10AndAbove();
    }

    private static String getJarName_Tomcat9AndBelow() {
        return "anti-dos-valve-1.3.0.jar";
    }

    private static String getJarName_Tomcat10AndAbove() {
        return "anti-dos-valve-1.4.0.jar";
    }
}
