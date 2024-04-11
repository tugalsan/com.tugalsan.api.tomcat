package com.tugalsan.api.tomcat.server;

import java.nio.file.*;
import javax.servlet.*;
import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.union.client.TGS_UnionExcuse;

public class TS_TomcatUpdateUtils {

    final private static TS_Log d = TS_Log.of(TS_TomcatUpdateUtils.class);

    public static String NAME_DB_PARAM() {
        return "pathWarUpdate";
    }

    private static TGS_UnionExcuse<Path> checkNewWar_do(ServletContext ctx, Path pathUpdate) {
        var warNameFull = TS_TomcatPathUtils.getWarNameFull(ctx);
        var warUpdateFrom = pathUpdate.resolve(warNameFull);
        var cmdUpdateFrom = Path.of(warUpdateFrom.getParent().toString(), TS_FileUtils.getNameLabel(warUpdateFrom) + ".update");
        var warUpdateTo = TS_TomcatPathUtils.getPathTomcatWebappsChild(warUpdateFrom.getFileName().toString());
        d.ci("checkNewWar_do", "Thread.warStringFrom/to/Cmd:", warUpdateFrom, warUpdateTo, cmdUpdateFrom);
        if (!TS_FileUtils.isExistFile(cmdUpdateFrom)) {
            return TGS_UnionExcuse.ofExcuse(d.className, "checkNewWar_do", "file not exists: cmdUpdateFrom");
        }
        d.ci("checkNewWar_do", "warUpdateCmd detected!");
        var u_delete = TS_FileUtils.deleteFileIfExists(cmdUpdateFrom);
        if (u_delete.isExcuse()) {
            return u_delete.toExcuse();
        }
        if (!TS_FileUtils.isExistFile(warUpdateFrom)) {
            return TGS_UnionExcuse.ofExcuse(d.className, "checkNewWar_do", "file not exists: warUpdateFrom");
        }
        d.ci("checkNewWar_do", "copying... f/t: " + warUpdateFrom + " / " + warUpdateTo);
        var u_copyAs = TS_FileUtils.copyAs(warUpdateFrom, warUpdateTo, true);
        if (u_copyAs.isExcuse()) {
            return u_copyAs.toExcuse();
        }
        var u_modFrom = TS_FileUtils.getTimeLastModified(warUpdateFrom);
        if (u_modFrom.isExcuse()) {
            return u_modFrom.toExcuse();
        }
        var u_modTo = TS_FileUtils.getTimeLastModified(warUpdateTo);
        if (u_modTo.isExcuse()) {
            return u_modFrom.toExcuse();
        }
        if (!TS_FileUtils.isExistFile(warUpdateTo)) {
            return TGS_UnionExcuse.ofExcuse(d.className, "checkNewWar_do", "file not exists: warUpdateTo");
        }
        if (u_modFrom.value().hasEqualDateWith(u_modTo.value()) && u_modTo.value().hasEqualTimeWith(u_modTo.value())) {
            d.ci("checkNewWar_do", "(" + warUpdateTo + ") Successful");
            var u_delete2 = TS_FileUtils.deleteFileIfExists(warUpdateFrom);
            if (u_delete2.isExcuse()) {
                return u_delete2.toExcuse();
            }
            return TGS_UnionExcuse.of(warUpdateTo);
        } else {
            return TGS_UnionExcuse.ofExcuse(d.className, "checkNewWar_do", "failed");
        }
    }

    public static boolean checkNewWar(TS_ThreadSyncTrigger killTrigger, ServletContextEvent evt, Path pathUpdate) {
        return checkNewWar(killTrigger, evt.getServletContext(), pathUpdate);
    }

    public static boolean checkNewWar(TS_ThreadSyncTrigger killTrigger, ServletContext ctx, Path pathUpdate) {
        if (pathUpdate == null) {
            d.ce("checkNewWar", "pathUpdate == null");
            return false;
        }
        d.cr("checkNewWar", pathUpdate);
        var warNameFull = TS_TomcatPathUtils.getWarNameFull(ctx);
        var warUpdateFrom = pathUpdate.resolve(warNameFull);
        return TS_FileWatchUtils.file(killTrigger, warUpdateFrom, () -> {
            var u = checkNewWar_do(ctx, pathUpdate);
            if (u.isExcuse()) {
                d.ct("checkNewWar", u.excuse());
            } else {
                d.cr("checkNewWar", "update success %s".formatted(u.value().toString()));
            }
        }, TS_FileWatchUtils.Triggers.CREATE, TS_FileWatchUtils.Triggers.MODIFY);
    }
}
