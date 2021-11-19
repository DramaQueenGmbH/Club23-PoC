package com.dramaqueen.club23.ui;

import com.dramaqueen.club23.model.Document;
import com.dramaqueen.club23.model.DocumentListener;
import com.dramaqueen.club23.ui.jface.IconAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Club23Panel extends Composite {

    private final Document   fDocument;
    private final Browser    fBrowser;
    private final Action     fBackAction;
    private final Action     fForwardAction;
    private BrowserFunction fEventCallback;

    public Club23Panel(Composite parent, Document document) {
        super(parent, SWT.NULL);
        fDocument = document;
        final DOMPropertyUpdater domUpdater = new DOMPropertyUpdater();
        fDocument.addListener(domUpdater);
        addDisposeListener(disposeEvent -> {
            fDocument.removeListener(domUpdater);
            fEventCallback.dispose();
        });

        fBrowser = new Browser(this, SWT.NULL);
        fBrowser.addLocationListener(new LocationListener() {
            @Override
            public void changing(LocationEvent locationEvent) {
                if ("about:blank".equals(locationEvent.location)) {
                    loadBrowserContentsFromResources();
                } else if (locationEvent.location.startsWith("dq://focus")) {
                    try {
                        String query = locationEvent.location.split("\\?")[1];
                        Map<String, String> params = splitQuery(query);
                        String focusPanel = params.get("panel");
                        String focusField = params.get("field");
                        if (focusPanel != null && focusField != null) {
                            FocusManager.switchFocus(focusPanel, focusField);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    locationEvent.doit = false;
                }
            }

            @Override
            public void changed(LocationEvent locationEvent) {
                updateActions();
            }
        });

        fBrowser.addProgressListener( new ProgressListener() {
            public void changed(ProgressEvent event) {
                // @TODO: implement loading progress bar
            }

            public void completed(ProgressEvent event) {
                fBrowser.execute("initDq()");
            }
        });

        fBackAction = new IconAction("Back", "go previous") {
            @Override
            public void run() {
                fBrowser.back();
            }
        };

        fForwardAction = new IconAction("Forward", "go next") {
            @Override
            public void run() {
                fBrowser.forward();
            }
        };

        fEventCallback = new EventCallback(fBrowser, fDocument);

        String url = document.getValue("url");
        if (url.length() > 0) {
            fBrowser.setUrl(url);
        } else {
            loadBrowserContentsFromResources();
        }

        updateActions();

        setLayout(new FillLayout());
    }

    public Action getNavigateBackAction() {
        return fBackAction;
    }

    public Action getNavigateForwardAction() {
        return fForwardAction;
    }

    private void updateActions() {
        fBackAction.setEnabled(fBrowser.isBackEnabled());
        fForwardAction.setEnabled(fBrowser.isForwardEnabled());
    }

    private static Map<String, String> splitQuery(String query) {
        Map<String, String> paramMap = new LinkedHashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            paramMap.put(
                    URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8),
                    URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return paramMap;
    }

    private void loadBrowserContentsFromResources() {
        InputStream resource = getClass().getResourceAsStream("index.html");
        try {
            if (resource != null) {
                String html = new String(resource.readAllBytes());
                fBrowser.setText(html);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resource.close();
            } catch (Exception ignored) {
            }
        }
    }

    private class DOMPropertyUpdater implements DocumentListener {
        @Override
        public void propertyChanged(String name, String value) {
            fBrowser.execute("onPropChanged(`" + name + "`,`" + value + "`);");
        }
    }

    private static class PropertyEvent {
        public String name;
        public String value;
    }

    private static class EventCallback extends BrowserFunction {

        private final Document fDocument;

        EventCallback(Browser browser, Document document) {
            super(browser, "eventCallbackJava");
            fDocument = document;
        }

        @Override
        public Object function(Object[] arguments) {
            try {
                System.out.println("Received event: " + arguments[0]);
                PropertyEvent event = new ObjectMapper().readValue((String) arguments[0], PropertyEvent.class);
                fDocument.setValue(event.name, event.value);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
