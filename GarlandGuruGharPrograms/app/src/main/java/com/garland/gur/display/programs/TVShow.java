package com.garland.gur.display.programs;

import java.util.List;

public class TVShow {

            public  String dy;
            public  String dt;
            public  String title;
            public  String fullDt;

    public TVShow() {
    }

    public TVShow(String dy, String title, String dt) {
                this.dy = dy;
                this.dt = dt;
                this.title = title;
            }

            public String getDy() {
                return dy;
            }

            public String getDt() {
                return dt;
            }

            public String getTitle() {
                return title;
            }

            public String getFullDt() {
                return fullDt;
            }

            public void setFullDt(String fullDt) {
                this.fullDt = fullDt;
            }
    }