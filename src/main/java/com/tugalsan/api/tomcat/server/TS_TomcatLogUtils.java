package com.tugalsan.api.tomcat.server;

import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.thread.server.*;
import com.tugalsan.api.unsafe.client.*;

public class TS_TomcatLogUtils {

    final private static TS_Log d = TS_Log.of(TS_TomcatLogUtils.class);
    final private static boolean PARALLEL = false; //may cause unexpected exception: java.lang.OutOfMemoryError: Java heap space

    public static void cleanUpEveryDay() {
        d.cr("cleanUpEveryDay");
        TS_ThreadRun.everyDays(true, 1, () -> {
            var logFolder = TS_TomcatPathUtils.getPathTomcatLogs();
            d.cr("executeEveryDay", "checking...", logFolder);
            TS_DirectoryUtils.createDirectoriesIfNotExists(logFolder);
            var subFiles = TS_DirectoryUtils.subFiles(logFolder, null, false, false);
            (PARALLEL ? subFiles.parallelStream() : subFiles.stream()).forEach(subFile -> {
                TGS_UnSafe.execute(() -> TS_FileUtils.deleteFileIfExists(subFile, false), e -> TGS_UnSafe.doNothing());
            });
        });
    }
}
