module com.tugalsan.api.tomcat {
    requires java.sql;
    requires javax.servlet.api;
    requires com.tugalsan.api.function;
    requires com.tugalsan.api.sql.conn;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.os;
    requires com.tugalsan.api.stream;
    requires com.tugalsan.api.url;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.file;
    
    requires com.tugalsan.api.file.txt;
    requires com.tugalsan.api.cast;
    requires com.tugalsan.api.string;
    requires com.tugalsan.api.thread;
    exports com.tugalsan.api.tomcat.server;
}
