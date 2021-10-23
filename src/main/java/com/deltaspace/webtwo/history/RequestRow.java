package com.deltaspace.webtwo.history;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.Math;

public class RequestRow {
    private final LocalDateTime currentTime;
    private final double executionTime;
    private final double x;
    private final double y;
    private final double r;
    private final boolean hit;
    private int round = 1000;
    private String attribute;

    public RequestRow(LocalDateTime currentTime, double executionTime, double x, double y, double r, boolean hit) {
        this.currentTime = currentTime;
        this.executionTime = executionTime;
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.attribute = "onclick=\"setDot("+x+", "+y+", "+r+", "+hit+");\"";
    }

    @Override
    public String toString() {
        return "<tr " + attribute + " ><td>" + DateTimeFormatter.ofPattern("hh:mm dd.MM.yyyy").format(currentTime) +
                "</td><td>" + executionTime + "</td><td>" + (Math.floor(x*round))/round + "</td><td>" + (Math.floor(y*round))/round + "</td><td>" + (Math.floor(r*round))/round + "</td><td>" +
                (hit?"Есть":"Нет") + "</td></tr>";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public boolean isHit() {
        return hit;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
