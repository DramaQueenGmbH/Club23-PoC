package com.dramaqueen.club23.ui;

import java.util.HashMap;
import java.util.Map;

public class FocusManager {

    private static final Map<String, Panel> fPanels = new HashMap<>();

    public static void registerPanel(String name, Panel panel) {
        fPanels.put(name, panel);
    }

    public static void switchFocus(String panelName, String field) {
        Panel panel = fPanels.get(panelName);
        if (panel != null) {
            panel.focusField(field);
        }
    }
}
