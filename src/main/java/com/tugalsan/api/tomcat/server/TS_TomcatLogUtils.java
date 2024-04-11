package com.tugalsan.api.tomcat.server;

import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncScheduled;
import java.time.Duration;

public class TS_TomcatLogUtils {

    final private static TS_Log d = TS_Log.of(TS_TomcatLogUtils.class);
    final private static boolean PARALLEL = false; //may cause unexpected exception: java.lang.OutOfMemoryError: Java heap space
    public static Duration UNTIL = Duration.ofSeconds(10);

    public static void cleanUpEveryDay(TS_ThreadSyncTrigger killTrigger) {
        d.cr("cleanUpEveryDay");
        TS_ThreadAsyncScheduled.everyDays(killTrigger, UNTIL, true, 1, kt -> {
            var logFolder = TS_TomcatPathUtils.getPathTomcatLogs();
            d.cr("cleanUpEveryDay", "checking...", logFolder);
            TS_DirectoryUtils.createDirectoriesIfNotExists(logFolder);
            var u_subFiles = TS_DirectoryUtils.subFiles(logFolder, null, false, false);
            if (u_subFiles.isExcuse()) {
                return;
            }
            (PARALLEL ? u_subFiles.value().parallelStream() : u_subFiles.value().stream()).forEach(subFile -> {
                var u_delete = TS_FileUtils.deleteFileIfExists(subFile);
                if (u_delete.isExcuse()) {
                    d.ce("cleanUpEveryDay", "cannot delete %s, reason: %s".formatted(subFile.toString(), u_delete.excuse().getMessage()));
                }
            });
        });
    }
}
