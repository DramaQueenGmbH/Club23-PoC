package com.dramaqueen.club23.ui;

import com.dramaqueen.club23.model.Document;
import com.dramaqueen.club23.model.DocumentListener;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Club23Panel extends Composite {

    private final Document  fDocument;
    private final Browser   fBrowser;

    public Club23Panel(Composite parent, Document document) {
        super(parent, SWT.NULL);
        fDocument = document;
        final DOMPropertyUpdater domUpdater = new DOMPropertyUpdater();
        fDocument.addListener(domUpdater);
        addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent disposeEvent) {
                fDocument.removeListener(domUpdater);
            }
        });

        fBrowser = new Browser(this, SWT.NULL);
        fBrowser.addLocationListener(new LocationAdapter() {
            @Override
            public void changing(LocationEvent locationEvent) {
                if (locationEvent.location.startsWith("dq://focus")) {
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
        });

        fBrowser.setUrl("https://ba5k8vx.myraidbox.de/schreibtisch");

        fBrowser.addProgressListener( new ProgressListener() {
            public void changed(ProgressEvent event) {
                // @TODO: implement loading progress bar
            }

            public void completed(ProgressEvent event) {
                fBrowser.execute("initDq()");
            }
        });

        setLayout(new FillLayout());
    }

    private static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> paramMap = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            paramMap.put(
                    URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8),
                    URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return paramMap;
    }

    private class DOMPropertyUpdater implements DocumentListener {
        @Override
        public void propertyChanged(String name, String value) {
            fBrowser.execute("onPropChanged(`" + name + "`,`" + value + "`);");
        }
    }
}
