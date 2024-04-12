package com.tugalsan.api.tomcat.client;

import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.url.client.TGS_Url;
import com.tugalsan.api.url.client.parser.*;

public class TGS_TomcatServletUtils {

    public static String PATH_NAME_MANAGER() {
        return "manager";
    }

    public static String PATH_NAME_MANAGER_HTML() {
        return "html";
    }

    public static String PATH_NAME_MANAGER_JMXPROXY() {
        return "jmxproxy";
    }

    public static TGS_UnionExcuse<TGS_UrlParser> URL_MANAGER_HTML(TGS_Url url) {
        var urlManagerHtml = TGS_UrlParser.of(url);
        if (urlManagerHtml.isExcuse()){
            urlManagerHtml.toExcuse();
        }
        urlManagerHtml.value().path.paths.clear();
        urlManagerHtml.value().path.paths.add("manager");
        urlManagerHtml.value().path.paths.add("html");
        urlManagerHtml.value().path.fileOrServletName = null;
        urlManagerHtml.value().quary.params.clear();
        urlManagerHtml.value().anchor.value = null;
        return urlManagerHtml;
    }

    public static TGS_UnionExcuse<TGS_UrlParser> URL_MANAGER_JMXPROXY(TGS_Url url) {
        var urlManagerJmx = TGS_UrlParser.of(url);
        if (urlManagerJmx.isExcuse()){
            urlManagerJmx.toExcuse();
        }
        urlManagerJmx.value().path.paths.clear();
        urlManagerJmx.value().path.paths.add("manager");
        urlManagerJmx.value().path.fileOrServletName = "jmxproxy";
        urlManagerJmx.value().quary.params.clear();
        urlManagerJmx.value().anchor.value = null;
        return urlManagerJmx;
    }
}
