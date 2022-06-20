package com.tugalsan.api.tomcat.client;

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

    public static TGS_UrlParser URL_MANAGER_HTML(CharSequence url) {
        var urlManagerHtml = TGS_UrlParser.of(url);
        urlManagerHtml.path.paths.clear();
        urlManagerHtml.path.paths.add("manager");
        urlManagerHtml.path.paths.add("html");
        urlManagerHtml.path.fileOrServletName = null;
        urlManagerHtml.quary.params.clear();
        urlManagerHtml.anchor.value = null;
        return urlManagerHtml;
    }

    public static TGS_UrlParser URL_MANAGER_JMXPROXY(CharSequence url) {
        var urlManagerJmx = TGS_UrlParser.of(url);
        urlManagerJmx.path.paths.clear();
        urlManagerJmx.path.paths.add("manager");
        urlManagerJmx.path.fileOrServletName = "jmxproxy";
        urlManagerJmx.quary.params.clear();
        urlManagerJmx.anchor.value = null;
        return urlManagerJmx;
    }
}
