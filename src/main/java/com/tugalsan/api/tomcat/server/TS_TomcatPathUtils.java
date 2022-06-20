package com.tugalsan.api.tomcat.server;

import java.nio.file.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.tugalsan.api.file.server.*;
import com.tugalsan.api.time.client.*;
import com.tugalsan.api.url.client.*;
import com.tugalsan.api.url.server.*;

public class TS_TomcatPathUtils {

    public static Path getPathTomcat() {
        var s = System.getProperty("catalina.base");
        return s == null ? null : Path.of(s);
    }

    public static Path getPathXampp() {
        var s = getPathTomcat();
        return s == null ? null : s.getParent();
    }

    public static Path getPathXamppSibling(CharSequence sibling) {
        var s = getPathXampp();
        return s == null ? null : s.resolveSibling(sibling.toString());
    }

    public static Path getPathTomcatLib() {
        var pathTomcat = getPathTomcat();
        return pathTomcat == null ? null : Path.of(pathTomcat.toString(), "lib");
    }

    public static Path getPathTomcatBin() {
        var pathTomcat = getPathTomcat();
        return pathTomcat == null ? null : Path.of(pathTomcat.toString(), "bin");
    }

    public static Path getPathTomcatWebapps() {
        return Path.of(getPathTomcat().toString(), "webapps");
    }

    public static Path getPathTomcatLogs() {
        return Path.of(getPathTomcat().toString(), "logs");
    }

    public static Path getPathTomcatWebappsChild(CharSequence warFileName) {
        var tomcatWebApps = getPathTomcatWebapps();
        return tomcatWebApps == null ? null : Path.of(tomcatWebApps.toString(), warFileName.toString());
    }

    public static Path getPathApp(ServletContext ctx) {
        var path = ctx.getRealPath("/");
        return Path.of(path);
    }

    public static Path getPathAppRes(ServletContext ctx) {
        var path = getPathApp(ctx);
        return path == null ? null : Path.of(path.toString(), "res");
    }

    public static Path getPathAppWebINFLib(ServletContext ctx) {
        return Path.of(getPathApp(ctx).toString(), "WEB-INF", "lib");
    }

    public static Path getPathWar(ServletContext ctx) {
        var path = Path.of(ctx.getRealPath("/"));//D:\xampp\tomcat\webapps\appName\
        var name = TS_FileUtils.getNameLabel(path);//appName
        return path.resolveSibling(name.concat(".war"));
    }

    public static String getWarNameLabel(ServletContext ctx) {
        return TS_FileUtils.getNameLabel(getPathWar(ctx));
    }

    public static String getWarNameFull(ServletContext ctx) {
        return TS_FileUtils.getNameFull(getPathWar(ctx));
    }

    public static String getWarNameFull(TGS_Url url) {
        return TGS_UrlUtils.getAppName(url).concat(".war");
    }

    public static TGS_Time getWarVersion(ServletContext ctx) {
        return TS_FileUtils.getTimeLastModified(
                TS_TomcatPathUtils.getPathTomcatWebappsChild(
                        TS_TomcatPathUtils.getWarNameFull(ctx)
                )
        );
    }

    public static TGS_Time getWarVersion(HttpServletRequest rq) {
        var url = TS_UrlUtils.toUrl(rq);
        return TS_FileUtils.getTimeLastModified(
                TS_TomcatPathUtils.getPathTomcatWebappsChild(
                        TS_TomcatPathUtils.getWarNameFull(url)
                )
        );
    }
}
