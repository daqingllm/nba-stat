package me.hailu.crawler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

/**
 * Created by liming_liu on 17/7/9.
 */
public class AwardCrawler {

    public Map<Integer, Integer> getMvps() {
        return getSeasonPlayers("http://www.stat-nba.com/award/item0.html");
    }

    public Map<Integer, Integer> getDpoys() {
        return getSeasonPlayers("http://www.stat-nba.com/award/item2.html");
    }

    public Map<Integer, Integer> getFmvps() {
        return getSeasonPlayers("http://www.stat-nba.com/award/item5.html");
    }

    private Map<Integer, Integer> getSeasonPlayers(String url) {
        Document document = Connections.getDocument(url);
        assert document != null;

        Map<Integer, Integer> result = Maps.newHashMap();
        Elements elements = document.select("table.stat_box").get(0).select("tbody tr");
        for (Element element : elements) {
            Elements tds = element.select("td");
            int season = Integer.parseInt(tds.get(0).text().substring(0, tds.get(0).text().indexOf('-')));
            if (season > 20) {
                break;
            }
            int player = Integer.parseInt(tds.get(1).select("a").attr("href").replaceFirst("../player/","").replaceFirst(".html", ""));
            result.put(season + 2000, player);
        }
        return result;
    }

    public Map<Integer, List<Integer>> getChampions() {
        Map<Integer, List<Integer>> result = Maps.newHashMap();
        for (int i=2000; i<=2016; i++) {
            String url = "http://www.stat-nba.com/award/item15isnba1season" + i + ".html";
            List<Integer> players = getPlayers(url);
            result.put(i, players);
        }
        return result;
    }

    private List<Integer> getPlayers(String url) {
        Document document = Connections.getDocument(url);
        assert document != null;

        List<Integer> result = Lists.newArrayList();
        Elements elements = document.select("table.stat_box tbody tr");
        for (Element element : elements) {
            Elements tds = element.select("td");
            int player = Integer.parseInt(tds.get(1).select("a").attr("href").replaceFirst("../player/","").replaceFirst(".html", ""));
            result.add(player);
        }
        return result;
    }

    public Map<Integer, Map<Integer, List<Integer>>> getTeamPlayers() {
        Map<Integer, Map<Integer, List<Integer>>> result = Maps.newHashMap();
        for (int i=2000; i<= 2016; i++) {
            String url = "http://www.stat-nba.com/award/item8isnba1season" + i + ".html";
            Document document = Connections.getDocument(url);
            assert document != null;
            Elements elements = document.select("table.stat_box");
            Map<Integer, List<Integer>> yearRes = Maps.newHashMap();
            yearRes.put(1, parseTeamTable(elements.get(0)));
            yearRes.put(2, parseTeamTable(elements.get(1)));
            yearRes.put(3, parseTeamTable(elements.get(2)));
            result.put(i, yearRes);
        }
        return result;
    }

    public Map<Integer, Map<Integer, List<Integer>>> getDteamPlayers() {
        Map<Integer, Map<Integer, List<Integer>>> result = Maps.newHashMap();
        for (int i=2000; i<= 2016; i++) {
            String url = "http://www.stat-nba.com/award/item10isnba1season" + i + ".html";
            Document document = Connections.getDocument(url);
            assert document != null;
            Elements elements = document.select("table.stat_box");
            Map<Integer, List<Integer>> yearRes = Maps.newHashMap();
            yearRes.put(1, parseTeamTable(elements.get(0)));
            yearRes.put(2, parseTeamTable(elements.get(1)));
            result.put(i, yearRes);
        }
        return result;
    }

    private List<Integer> parseTeamTable(Element table) {
        List<Integer> result = Lists.newArrayList();
        Elements elements = table.select("tbody tr");
        for (Element element : elements) {
            Elements tds = element.select("td");
            int player = Integer.parseInt(tds.get(1).select("a").attr("href").replaceFirst("../player/","").replaceFirst(".html", ""));
            result.add(player);
        }
        return result;
    }

    public Map<Integer, Map<Integer, Integer>> getRecorders() {
        Map<Integer, Map<Integer, Integer>> result = Maps.newHashMap();
        for (int i=0; i<=4; i++) {
            String url = "http://www.stat-nba.com/award/item14pr" + i + ".html";
            result.put(i, getSeasonPlayers(url));
        }
        return result;
    }
}
