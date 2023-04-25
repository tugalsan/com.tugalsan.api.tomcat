package com.tugalsan.api.tomcat.server;

import java.nio.file.*;
import javax.servlet.*;
import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.unsafe.client.*;

public class TS_TomcatUpdateUtils {

    final private static TS_Log d = TS_Log.of(TS_TomcatUpdateUtils.class);

    public static String NAME_DB_PARAM() {
        return "pathWarUpdate";
    }

    private static void checkNewWar_do(ServletContext ctx, Path pathUpdate) {
        var warNameFull = TS_TomcatPathUtils.getWarNameFull(ctx);
        var warUpdateFrom = pathUpdate.resolve(warNameFull);
        var cmdUpdateFrom = Path.of(warUpdateFrom.getParent().toString(), TS_FileUtils.getNameLabel(warUpdateFrom) + ".update");
        var warUpdateTo = TS_TomcatPathUtils.getPathTomcatWebappsChild(warUpdateFrom.getFileName().toString());
        d.ci("checkNewWar_do", "Thread.warStringFrom/to/Cmd:", warUpdateFrom, warUpdateTo, cmdUpdateFrom);

        if (TS_FileUtils.isExistFile(cmdUpdateFrom)) {
            d.cr("checkNewWar_do", "warUpdateCmd detected!");
            TS_FileUtils.deleteFileIfExists(cmdUpdateFrom);
            if (TS_FileUtils.isExistFile(warUpdateFrom)) {
                TGS_UnSafe.run(() -> {
                    d.cr("checkNewWar_do", "copying... f/t: " + warUpdateFrom + " / " + warUpdateTo);
                    TS_FileUtils.copyAs(warUpdateFrom, warUpdateTo, true);
                }, e -> d.ce(warNameFull, e));
                var modFrom = TS_FileUtils.getTimeLastModified(warUpdateFrom);
                var modTo = TS_FileUtils.getTimeLastModified(warUpdateTo);
                if (TS_FileUtils.isExistFile(warUpdateTo)) {
                    if (modFrom.hasEqualDateWith(modTo) && modTo.hasEqualTimeWith(modTo)) {
                        d.cr("checkNewWar_do", "(" + warUpdateTo + ") Successful");
                        TS_FileUtils.deleteFileIfExists(warUpdateFrom);
                    } else {
                        d.ce("checkNewWar_do", "Failed", modFrom, modTo);
                    }
                } else {
                    d.ce("checkNewWar_do", "Failed -> warUpdateTo not exists", warUpdateTo);
                }
            } else {
                d.ce("checkNewWar_do", "Failed -> warUpdateFrom not detected!", warUpdateFrom);
            }
        }
    }

    public static void checkNewWar(ServletContext ctx, Path pathUpdate) {
        if (pathUpdate == null) {
            d.ce("checkNewWar", "pathUpdate == null");
            return;
        }
        d.cr("checkNewWar", pathUpdate);
        var warNameFull = TS_TomcatPathUtils.getWarNameFull(ctx);
        var warUpdateFrom = pathUpdate.resolve(warNameFull);
        TGS_UnSafe.run(() -> {
            TS_FileWatchUtils.file(warUpdateFrom, () -> checkNewWar_do(ctx, pathUpdate),
                    TS_FileWatchUtils.Triggers.CREATE, TS_FileWatchUtils.Triggers.MODIFY);
        }, e -> d.ct("checkNewWar", e));

    }
}
