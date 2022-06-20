package com.tugalsan.api.tomcat.server;

import java.nio.file.*;
import com.tugalsan.api.file.txt.server.*;

public class TS_TomcatXMLUtils { 

    final public static Path server_xml = Path.of("D:\\xampp\\tomcat\\conf\\server.xml");

    @Deprecated
    public static void writeDefaultServerXml() {
        TS_FileTxtUtils.toFile(getDefaultServerXmlContent(), server_xml, false);
    }

    @Deprecated
    public static String getDefaultServerXmlContent() {//TODO TS_SQLConnectAnchor.DATAFOLDER
        return """
               <?xml version='1.0' encoding='utf-8'?>
               <Server port="8005" shutdown="SHUTDOWN">
                 <Listener className="org.apache.catalina.startup.VersionLoggerListener" />
                 <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />
                 <Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener" />
                 <Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener" />
                 <Listener className="org.apache.catalina.core.ThreadLocalLeakPreventionListener" />
                 <GlobalNamingResources>
                   <Resource name="UserDatabase" auth="Container" type="org.apache.catalina.UserDatabase" description="User database that can be updated and saved" factory="org.apache.catalina.users.MemoryUserDatabaseFactory" pathname="conf/tomcat-users.xml" />
                 </GlobalNamingResources>
                 <Service name="Catalina">
                 <Connector port="8443" maxHttpHeaderSize="65536" maxThreads="150" minSpareThreads="25" enableLookups="false" acceptCount="100" connectionTimeout="20000" disableUploadTimeout="true" compression="on" compressionMinSize="128" noCompressionUserAgents="gozilla, traviata" URIEncoding="UTF-8" scheme="https" secure="true" SSLEnabled="true" clientAuth="false" sslProtocol="TLS" keyAlias="server" keystoreFile="../tomcatFileData/SSL/www.tugalsan.com.jks" keystorePass="MebosaExport0" />
                   <Engine name="Catalina" defaultHost="localhost">
                     <Realm className="org.apache.catalina.realm.LockOutRealm"><Realm className="org.apache.catalina.realm.UserDatabaseRealm" resourceName="UserDatabase"/></Realm>
                     <Host name="localhost"  appBase="webapps" unpackWARs="true" autoDeploy="true">
                       <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" prefix="localhost_access_log." suffix=".txt" pattern="%h %l %u %t &quot;%r&quot; %s %b" />
                     </Host>
                   </Engine>
                 </Service>
               </Server>
               """;
    }
}
