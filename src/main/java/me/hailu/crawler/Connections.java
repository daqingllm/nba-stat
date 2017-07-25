package me.hailu.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by liming_liu on 17/7/9.
 */
public class Connections {

    private final static String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
    private final static String cookie = "__cfduid=de5485e9cae506e73dddab47dfead07f31497532732; cf_clearance=18b07af35195e8f3e3b202f6c56e27555d405902-1499600308-28800; Hm_lvt_102e5c22af038a553a8610096bcc8bd4=1497532791,1497720252,1499183049,1499183096; Hm_lpvt_102e5c22af038a553a8610096bcc8bd4=1499600416; blank=goodbetterbest";

    private Connections() {}

    public static Document getDocument(String url) {
        for (int i=0; i<3; i++) {
            try {
                return tryGetDocument(url);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }
        }
        return null;
    }

    private static Document tryGetDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .header("User-Agent", userAgent)
                .header("Cookie", cookie).get();
    }
}
