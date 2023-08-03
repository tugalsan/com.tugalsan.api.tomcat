package com.tugalsan.api.tomcat.server;

import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.thread.server.safe.TS_ThreadSafeTrigger;
import com.tugalsan.api.thread.server.async.TS_ThreadAsync;
import com.tugalsan.api.unsafe.client.*;

public class TS_TomcatLogUtils {

    final private static TS_Log d = TS_Log.of(TS_TomcatLogUtils.class);
    final private static boolean PARALLEL = false; //may cause unexpected exception: java.lang.OutOfMemoryError: Java heap space

    public static void cleanUpEveryDay(TS_ThreadSafeTrigger killTrigger) {
        d.cr("cleanUpEveryDay");
        TS_ThreadAsync.everyDays(killTrigger, true, 1, kt -> {
            var logFolder = TS_TomcatPathUtils.getPathTomcatLogs();
            d.cr("cleanUpEveryDay", "checking...", logFolder);
            TS_DirectoryUtils.createDirectoriesIfNotExists(logFolder);
            var subFiles = TS_DirectoryUtils.subFiles(logFolder, null, false, false);
            (PARALLEL ? subFiles.parallelStream() : subFiles.stream()).forEach(subFile -> {
                TGS_UnSafe.run(() -> TS_FileUtils.deleteFileIfExists(subFile, false), e -> TGS_UnSafe.runNothing());
            });
        });
    }
}
