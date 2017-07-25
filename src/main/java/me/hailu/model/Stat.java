package me.hailu.model;

import lombok.Data;
import me.hailu.param.Coefficient;

import java.io.Serializable;

@Data
public class Stat implements Serializable {

    private String team;
    private int match;
    private int start;
    private int minute;
    private int hit;
    private int shoot;
    private int threePointHit;
    private int threePointShoot;
    private int freeThrowHit;
    private int freeThrowShoot;
    private int reb;
    private int offReb;
    private int defReb;
    private int assist;
    private int steal;
    private int block;
    private int turnover;
    private int foul;
    private int score;
    private int win;
    private int loss;

    public int quality() {
        int quality = 0;
        quality += (score * Coefficient.stat_score) / (match * Coefficient.score_step);
        quality += (reb * Coefficient.stat_score) / (match * Coefficient.reb_step);
        quality += (assist * Coefficient.stat_score) / (match * Coefficient.assist_step);
        quality += (block * Coefficient.stat_score) / (match * Coefficient.block_step);
        quality += (steal * Coefficient.stat_score) / (match * Coefficient.steal_step);
        return quality;
    }

    public String stats() {
        return String.format("%s分 + %s板 + %s助 + %s帽 + %s断"
                , String.format("%.1f", score * 1.0 / match)
                , String.format("%.1f", reb * 1.0 / match)
                , String.format("%.1f", assist * 1.0 / match)
                , String.format("%.1f", block * 1.0 / match)
                , String.format("%.1f", steal * 1.0 / match));
    }
}
