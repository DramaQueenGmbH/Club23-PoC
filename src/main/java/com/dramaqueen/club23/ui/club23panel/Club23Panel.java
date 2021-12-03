package com.dramaqueen.club23.ui.club23panel;

import com.dramaqueen.club23.model.Document;
import com.dramaqueen.club23.model.DocumentListener;
import com.dramaqueen.club23.ui.FocusManager;
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

    private final Document          fDocument;
    private final Browser           fBrowser;
    private final BrowserFunction   fEventCallback;
    private final BrowserActions    fActions;

    public Club23Panel(Composite parent, Document document) {
        super(parent, SWT.NULL);
        fDocument = document;
        final DOMPropertyUpdater domUpdater = new DOMPropertyUpdater();
        fDocument.addListener(domUpdater);

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
                fActions.update();
            }
        });

        fBrowser.addProgressListener(new ProgressListener() {
            public void changed(ProgressEvent event) {
                // @TODO: implement loading progress bar
            }

            public void completed(ProgressEvent event) {
                String documentAsJSObject = JavascriptUtils.toJavascriptObject(fDocument);
                String script = "initDq(" + documentAsJSObject + ");";
                fBrowser.execute(script);
            }
        });

        fEventCallback = new EventCallback(fBrowser, fDocument);
        fActions = new BrowserActions(fBrowser);
        fActions.update();

        initContents();

        addDisposeListener(disposeEvent -> {
            fDocument.removeListener(domUpdater);
            fEventCallback.dispose();
        });

        setLayout(new FillLayout());
    }

    public BrowserActions getActions() {
        return fActions;
    }

    private void initContents() {
        String url = fDocument.getValue("url");
        if (url.length() > 0) {
            fBrowser.setUrl(url);
        } else {
            loadBrowserContentsFromResources();
        }
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

}
