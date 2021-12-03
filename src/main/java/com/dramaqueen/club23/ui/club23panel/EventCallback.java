package com.dramaqueen.club23.ui.club23panel;

import com.dramaqueen.club23.model.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

class EventCallback extends BrowserFunction {

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

    private static class PropertyEvent {
        public String name;
        public String value;
    }
}
