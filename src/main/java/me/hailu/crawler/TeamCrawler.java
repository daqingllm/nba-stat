package me.hailu.crawler;

import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.List;

/**
 * Created by liming_liu on 17/7/15.
 */
public class TeamCrawler {

    public List<Integer> getTeamPlayers(String team, int year) {
        String url = "http://www.stat-nba.com/team/stat_box_team.php?team=" + team + "&season=" + year + "&col=pts&order=1&isseason=1";
        Document document = Connections.getDocument(url);
        assert document != null;
        Elements table = document.select("table.stat_box");
        if (table.size() == 0) {
            return Collections.emptyList();
        }
        Elements elements = table.get(0).select("tbody tr");

        List<Integer> players = Lists.newArrayList();
        for (Element element : elements) {
            Elements tds = element.select("td");
            if (tds.size() == 0) {
                break;
            }
            int player = Integer.parseInt(tds.get(1).select("a").attr("href").replaceFirst("/player/","").replaceFirst(".html", ""));
            players.add(player);
        }
        return players;
    }
}
