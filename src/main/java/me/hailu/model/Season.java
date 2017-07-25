package me.hailu.model;

import lombok.Data;
import me.hailu.param.Coefficient;

import java.io.Serializable;

/**
 * Created by liming_liu on 17/6/18.
 */
@Data
public class Season implements Serializable {

    private int season;
    private Stat regular;
    private Stat playoff;
    private boolean mvp;
    private boolean dpoy;
    private boolean champion;
    private boolean fmvp;
    private int records;
    private int team;
    private int dteam;

    public int quality() {
        if (regular.getMatch() < 42) {
            return 0;
        }
        int quality = Coefficient.season_score + regular.quality();
        if (playoff != null) {
            quality += Coefficient.playoff_co;
        }
        if (mvp) {
            quality += Coefficient.mvp_co;
        }
        if (dpoy) {
            quality += Coefficient.dpoy_co;
        }
        if (champion) {
            quality += Coefficient.champion_co;
        }
        if (fmvp) {
            quality += Coefficient.fmvp_co;
        }
        quality += records * Coefficient.record_co;
        if (team == 1) {
            quality += Coefficient.team1_co;
        }
        if (team == 2) {
            quality += Coefficient.team2_co;
        }
        if (team == 3) {
            quality += Coefficient.team3_co;
        }
        if (dteam == 1) {
            quality += Coefficient.dteam1_co;
        }
        if (dteam == 2) {
            quality += Coefficient.dteam2_co;
        }
        return quality;
    }
}
