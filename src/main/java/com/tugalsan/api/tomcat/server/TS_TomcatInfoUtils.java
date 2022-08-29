package com.tugalsan.api.tomcat.server;

import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.log.server.TS_Log;
import java.util.Arrays;
import javax.servlet.*;

public class TS_TomcatInfoUtils {

    final private static TS_Log d = TS_Log.of(TS_TomcatInfoUtils.class);

    public static boolean isTOMCAT() {
        var r = Arrays.stream(new Throwable().getStackTrace())
                .filter(s -> s.getClassName().equals("org.apache.catalina.core.StandardEngineValve"))
                .findAny().isPresent();
        d.ci("isTOMCAT", r);
        return r;
    }

    public static int version(ServletContext ctx) {
        var info = ctx.getServerInfo();
        var versionDetailed = info.substring("Apache Tomcat/".length());
        var idx = versionDetailed.indexOf(".");
        if (idx == -1) {
            return -1;
        }
        var versionSimple = versionDetailed.substring(0, idx);
        var versionSimpleInt = TGS_CastUtils.toInteger(versionSimple);
        if (versionSimpleInt == null) {
            return -1;
        }
        d.ci("version", versionSimpleInt);
        return versionSimpleInt;
    }
}
