package com.deltaspace.webtwo.history;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class RequestHistory {
    public final HashSet<RequestRow> rows = new LinkedHashSet<>();

    public void addRow(RequestRow row) {
        rows.add(row);
    }

    public void clear() {
        rows.clear();
    }
}
