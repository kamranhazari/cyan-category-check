package com.cyanapp.api.util;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtil {
    public static String getDomainFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getHost();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
