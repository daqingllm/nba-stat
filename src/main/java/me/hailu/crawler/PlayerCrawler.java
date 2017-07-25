package me.hailu.crawler;

import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liming_liu on 17/7/6.
 */
public class PlayerCrawler {

    private final String url;
    private final Document document;

    public PlayerCrawler(String url) {
        this.url = url;
        document = Connections.getDocument(url);
    }

    public String getName() {
        return document.title().substring(0, document.title().indexOf('|'));
    }

    public List<List<String>> getStatBoxTot() {
        List<List<String>> result = Lists.newArrayList();
        //document.getElementById("stat_box_tot").select("tbody").select("tr").get(0).select("td").get(3).attr("rank")
        Elements elements = document.getElementById("stat_box_tot").select("tbody tr");
        for (Element element : elements) {
            List<String> row = element.select("td").stream().map(ele -> ele.attr("rank")).collect(Collectors.toList());
            result.add(row);
        }
        return result;
    }

    public List<List<String>> getPlayoffStatBoxTot() {
        List<List<String>> result = Lists.newArrayList();
        String playoffUrl = url.replaceFirst("\\.html", "_playoff.html");
        playoffUrl = playoffUrl.replaceFirst("/player/", "/player/stat_box/");
        Document playoffDoc = Connections.getDocument(playoffUrl);
        assert playoffDoc != null;
        Element table = playoffDoc.getElementById("stat_box_tot");
        if (table == null) {
            return result;
        }
        Elements elements = table.select("tbody tr");
        for (Element element : elements) {
            List<String> row = element.select("td").stream().map(ele -> ele.attr("rank")).collect(Collectors.toList());
            result.add(row);
        }
        return result;
    }
}
