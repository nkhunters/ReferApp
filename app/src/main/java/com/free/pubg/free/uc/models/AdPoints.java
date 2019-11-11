package com.free.pubg.free.uc.models;

public class AdPoints {

    private String banner, interstitial, reward, daily_bonus;

    public AdPoints(String banner, String interstitial, String reward, String daily_bonus) {
        this.banner = banner;
        this.interstitial = interstitial;
        this.reward = reward;
        this.daily_bonus = daily_bonus;
    }

    public String getBanner() {
        return banner;
    }

    public String getInterstitial() {
        return interstitial;
    }

    public String getReward() {
        return reward;
    }

    public String getDaily_bonus() {
        return daily_bonus;
    }
}
