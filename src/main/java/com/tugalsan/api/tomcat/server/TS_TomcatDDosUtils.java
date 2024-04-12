package com.tugalsan.api.tomcat.server;

import com.tugalsan.api.union.client.TGS_UnionExcuse;
import javax.servlet.*;

public class TS_TomcatDDosUtils {

    public static TGS_UnionExcuse<String>  getJarName(ServletContextEvent evt) {
        return getJarName(evt.getServletContext());
    }

    public static TGS_UnionExcuse<String> getJarName(ServletContext ctx) {
        var version = TS_TomcatInfoUtils.version(ctx);
        if (version.isExcuse()) {
            return version.toExcuse();
        }
        return TGS_UnionExcuse.of(
                version.value() < 10
                ? getJarName_Tomcat9AndBelow()
                : getJarName_Tomcat10AndAbove()
        );
    }

    private static String getJarName_Tomcat9AndBelow() {
        return "anti-dos-valve-1.3.0.jar";
    }

    private static String getJarName_Tomcat10AndAbove() {
        return "anti-dos-valve-1.4.0.jar";
    }
}
