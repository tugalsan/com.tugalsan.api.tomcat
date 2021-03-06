package com.tugalsan.api.tomcat.server;

import java.nio.file.*;
import javax.servlet.*;
import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.thread.server.*;
import com.tugalsan.api.unsafe.client.*;

public class TS_TomcatUpdateUtils {

    final private static TS_Log d = TS_Log.of(TS_TomcatUpdateUtils.class.getSimpleName());

    public static String NAME_DB_PARAM() {
        return "pathWarUpdate";
    }

    public static void checkNewWarEvery10Secs(ServletContext ctx, Path pathUpdate) {
        if (pathUpdate == null) {
            d.ce("initialize", "pathUpdate == null");
            return;
        }
        d.cr("initialize", "TS_ThreadOnceUtils.addExeEvery10Secs", pathUpdate);
        TS_ThreadRunUtils.everySeconds(true, 10, () -> {
            var warNameFull = TS_TomcatPathUtils.getWarNameFull(ctx);
            var warUpdateFrom = pathUpdate.resolve(warNameFull);
            var cmdUpdateFrom = Path.of(warUpdateFrom.getParent().toString(), TS_FileUtils.getNameLabel(warUpdateFrom) + ".update");
            var warUpdateTo = TS_TomcatPathUtils.getPathTomcatWebappsChild(warUpdateFrom.getFileName().toString());
            d.ci("executeEvery10Secs", "Thread.warStringFrom/to/Cmd:", warUpdateFrom, warUpdateTo, cmdUpdateFrom);

            if (TS_FileUtils.isExistFile(cmdUpdateFrom)) {
                d.cr("executeEvery10Secs", "warUpdateCmd detected!");
                TS_FileUtils.deleteFileIfExists(cmdUpdateFrom);
                if (TS_FileUtils.isExistFile(warUpdateFrom)) {
                    TGS_UnSafe.execute(() -> {
                        d.cr("executeEvery10Secs", "copying... f/t: " + warUpdateFrom + " / " + warUpdateTo);
                        TS_FileUtils.copyFile(warUpdateFrom, warUpdateTo, true);
                    }, e -> d.ce(warNameFull, e));
                    var modFrom = TS_FileUtils.getTimeLastModified(warUpdateFrom);
                    var modTo = TS_FileUtils.getTimeLastModified(warUpdateTo);
                    if (TS_FileUtils.isExistFile(warUpdateTo)) {
                        if (modFrom.hasEqualDateWith(modTo) && modTo.hasEqualTimeWith(modTo)) {
                            d.cr("executeEvery10Secs", "(" + warUpdateTo + ") Successful");
                            TS_FileUtils.deleteFileIfExists(warUpdateFrom);
                        } else {
                            d.ce("executeEvery10Secs", "Failed", modFrom, modTo);
                        }
                    } else {
                        d.ce("executeEvery10Secs", "Failed -> warUpdateTo not exists", warUpdateTo);
                    }
                } else {
                    d.ce("executeEvery10Secs", "Failed -> warUpdateFrom not detected!", warUpdateFrom);
                }
            }
        });
    }
}
