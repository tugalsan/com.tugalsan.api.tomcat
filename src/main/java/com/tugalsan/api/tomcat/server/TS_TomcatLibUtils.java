package com.tugalsan.api.tomcat.server;

import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import java.nio.file.*;
import javax.servlet.*;

public class TS_TomcatLibUtils {

    final private static TS_Log d = TS_Log.of(TS_TomcatLibUtils.class);

    public static Path jarInRes(ServletContext ctx, CharSequence jarFileName) {
        var pathApp = TS_TomcatPathUtils.getPathApp(ctx);
        d.ci("jarInRes", "pathApp", pathApp);
        var jarInRes = Path.of(pathApp.toString(), "res", "jar", jarFileName.toString());
        d.ci("jarInRes", "jarInRes", jarInRes);
        return jarInRes;
    }

    public static Path jarInTomcatLib(CharSequence jarFileName) {
        var pathTomcatLib = TS_TomcatPathUtils.getPathTomcatLib();
        d.ci("jarInTomcatLib", "pathTomcatLib", pathTomcatLib);
        var jarInTomcatLib = Path.of(pathTomcatLib.toString(), jarFileName.toString());
        d.ci("jarInTomcatLib", "jarInTomcatLib", jarInTomcatLib);
        return jarInTomcatLib;
    }

    public static Path jarInAppWebINFLib(ServletContext ctx, CharSequence jarFileName) {
        var pathTomcatLib = TS_TomcatPathUtils.getPathAppWebINFLib(ctx);
        d.ci("jarInAppWebINFLib", "PathAppWebINFLib", pathTomcatLib);
        var jarInAppWebINFLib = Path.of(pathTomcatLib.toString(), jarFileName.toString());
        d.ci("jarInAppWebINFLib", "jarInAppWebINFLib", jarInAppWebINFLib);
        return jarInAppWebINFLib;
    }

    public static boolean copyFromResToTomcatLib(ServletContext ctx, CharSequence jarFileName) {
        var jarInTomcatLib = jarInTomcatLib(jarFileName);
        var jarInRes = jarInRes(ctx, jarFileName);
        if (!TS_FileUtils.isExistFile(jarInRes)) {
            d.ce("copyFromRes", "resource file does not exists; cannot copy to fix it", jarInRes);
            return false;
        }
        TS_FileUtils.copyAs(jarInRes, jarInTomcatLib, false);
        if (!TS_FileUtils.isExistFile(jarInTomcatLib)) {
            d.ce("copyFromRes", "resource cannot be copied to lib. manual fix needed", jarInRes, jarInTomcatLib);
            return false;
        }
        d.ce("copyFromRes", "resource copied to lib. tomcat restart needed", jarInRes, jarInTomcatLib);
        return true;
    }

    public static boolean isExistInTomcatLib(ServletContext ctx, CharSequence jarFileName) {
        var jarInLib = jarInTomcatLib(jarFileName);
        return TS_FileUtils.isExistFile(jarInLib);
    }

    public static boolean isExistInAppWebINFLib(ServletContext ctx, CharSequence jarFileName) {
        var jarInAppWebINFLib = jarInAppWebINFLib(ctx, jarFileName);
        return TS_FileUtils.isExistFile(jarInAppWebINFLib);
    }

    public static boolean checkTomcatLibOnlyJars(ServletContextEvent evt, CharSequence... jarNames) {
        return checkTomcatLibOnlyJars(evt.getServletContext(), jarNames);
    }

    public static boolean checkTomcatLibOnlyJars(ServletContext ctx, CharSequence... jarNames) {
        var result = true;
        var files_at_tomcat_lib = TGS_StreamUtils.toLst(
                TS_DirectoryUtils.subFiles(TS_TomcatPathUtils.getPathTomcatLib(), "*.jar", false, false)
                        .stream().map(j -> TS_FileUtils.getNameFull(j))
        );
        var files_at_war_lib = TGS_StreamUtils.toLst(
                TS_DirectoryUtils.subFiles(TS_TomcatPathUtils.getPathAppWebINFLib(ctx), "*.jar", false, false)
                        .stream().map(j -> TS_FileUtils.getNameFull(j))
        );
        for (var jarName : jarNames) {
            var files_at_tomcat_lib_exits = files_at_tomcat_lib.stream().anyMatch(j -> j.startsWith(jarName.toString()));
            if (!files_at_tomcat_lib_exits) {
                files_at_tomcat_lib.forEach(jar -> {
                    d.ce("checkTomcatLibOnlyJars", "files_at_tomcat_lib", jar);
                });
                d.ce("checkTomcatLibOnlyJars", "This file should exists!", jarName, "trying to copying from resource directory");
                result = result && copyFromResToTomcatLib(ctx, jarName);
            }
            var files_at_war_lib_exits = files_at_war_lib.stream().anyMatch(j -> j.startsWith(jarName.toString()));
            if (files_at_war_lib_exits) {
                files_at_war_lib.forEach(jar -> {
                    d.ce("checkTomcatLibOnlyJars", "files_at_war_lib", jar);
                });
                d.ce("checkTomcatLibOnlyJars", "This file should not exists; use <scope>provided</scope> and rebuild the app!", jarName);
                result = false;
            }
        }
        return result;
    }
}
