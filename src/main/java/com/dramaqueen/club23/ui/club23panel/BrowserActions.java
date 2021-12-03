package com.dramaqueen.club23.ui.club23panel;

import com.dramaqueen.club23.ui.jface.IconAction;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.browser.Browser;

import java.util.HashMap;
import java.util.Map;

public class BrowserActions {

    private abstract static class BrowserAction extends IconAction {
        public BrowserAction(String text, String iconName) {
            super(text, iconName);
        }

        public abstract void update();
    }

    private final Browser                      fBrowser;
    private final Map<String, BrowserAction>   fActions;

    public BrowserActions(Browser browser) {
        fBrowser = browser;

        fActions = new HashMap<>();

        addAction(new BrowserAction("Back", "go previous") {
            @Override
            public void run() {
                fBrowser.back();
            }

            @Override
            public void update() {
                setEnabled(fBrowser.isBackEnabled());
            }
        });

        addAction(new BrowserAction("Forward", "go next") {
            @Override
            public void run() {
                fBrowser.forward();
            }

            @Override
            public void update() {
                setEnabled(fBrowser.isForwardEnabled());
            }
        });
    }

    private void addAction(BrowserAction action) {
        fActions.put(action.getId(), action);
    }

    public Action get(String id) {
        return fActions.get(id);
    }

    public void update() {
        for (BrowserAction action : fActions.values()) {
            action.update();
        }
    }

}
