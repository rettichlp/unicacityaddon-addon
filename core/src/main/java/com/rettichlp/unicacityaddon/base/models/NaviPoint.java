package com.rettichlp.unicacityaddon.base.models;

import com.rettichlp.unicacityaddon.base.api.request.APIConverter;
import net.labymod.api.util.math.vector.FloatVector3;

public class NaviPoint {

    private final String name;
    private final int x;
    private final int y;
    private final int z;
    private final String article;

    public NaviPoint(String name, int x, int y, int z, String article) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getArticle() {
        return article;
    }

    public String getTabName() {
        return name.replace(" ", "-");
    }

    public String getArticleFourthCase() {
        return article.replace("der", "den");
    }

    public FloatVector3 getBlockPos() {
        return new FloatVector3(x, y, z);
    }

    public static NaviPoint getNaviPointByTabName(String tabName) {
        return APIConverter.NAVIPOINTLIST.stream()
                .filter(naviPointEntry -> naviPointEntry.getTabName().equals(tabName))
                .findFirst()
                .orElse(null);
    }
}