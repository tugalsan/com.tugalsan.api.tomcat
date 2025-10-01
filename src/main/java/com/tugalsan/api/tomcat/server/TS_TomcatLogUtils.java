package com.tugalsan.api.tomcat.server;

import module com.tugalsan.api.function;
import module com.tugalsan.api.file;
import module com.tugalsan.api.log;
import module com.tugalsan.api.thread;
import java.time.*;

public class TS_TomcatLogUtils {

    private TS_TomcatLogUtils() {

    }

    final private static TS_Log d = TS_Log.of(TS_TomcatLogUtils.class);
    public static Duration UNTIL = Duration.ofSeconds(10);

    public static void cleanUpEveryDay(TS_ThreadSyncTrigger killTrigger) {
        d.cr("cleanUpEveryDay");
        TS_ThreadAsyncScheduled.everyDays("cleanup_tomcatlogs_everyday", killTrigger.newChild(d.className()), UNTIL, true, 1, kt -> {
            var logFolder = TS_TomcatPathUtils.getPathTomcatLogs();
            d.cr("cleanUpEveryDay", "checking...", logFolder);
            TS_DirectoryUtils.createDirectoriesIfNotExists(logFolder);
            var subFiles = TS_DirectoryUtils.subFiles(logFolder, null, false, false);
            subFiles.parallelStream().forEach(subFile -> {
                TGS_FuncMTCUtils.run(() -> TS_FileUtils.deleteFileIfExists(subFile), e -> TGS_FuncMTU.empty.run());
            });
        });
    }
}
