module com.tugalsan.api.tomcat {
    requires javax.servlet.api;
    requires com.tugalsan.api.executable;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.os;
    requires com.tugalsan.api.url;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.file;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.file.txt;
    requires com.tugalsan.api.cast;
    requires com.tugalsan.api.string;
    requires com.tugalsan.api.thread;
    exports com.tugalsan.api.tomcat.server;
}
