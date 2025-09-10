package com.tugalsan.api.tomcat.server;

import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.thread.server.async.scheduled.TS_ThreadAsyncScheduled;

import java.time.Duration;
import com.tugalsan.api.function.client.maythrowexceptions.unchecked.TGS_FuncMTU;
import com.tugalsan.api.function.client.maythrowexceptions.checked.TGS_FuncMTCUtils;

public class TS_TomcatLogUtils {
    
    private TS_TomcatLogUtils(){
        
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
