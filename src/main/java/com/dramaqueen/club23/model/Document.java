package com.dramaqueen.club23.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Document {
    private final Map<String, Property> fProperties;
    private final List<DocumentListener> fListeners;

    public Document() {
        fProperties = new HashMap<>();
        fListeners = new ArrayList<>();
    }

    public void setValue(String name, String value) {
        Property p = fProperties.get(name);
        if (p == null) {
            p = new Property();
            fProperties.put(name, p);
        }
        if (!value.equals(p.getValue())) {
            p.setValue(value);
            notifyValueChanged(name, p);
        }
    }

    public String getValue(String name) {
        Property p = fProperties.get(name);
        if (p != null) {
            return p.getValue();
        }
        return "";
    }

    public void addListener(DocumentListener l) {
        if (!fListeners.contains(l)) {
            fListeners.add(l);
        }
    }

    public void removeListener(DocumentListener l) {
        fListeners.remove(l);
    }

    private void notifyValueChanged(String name, Property p) {
        List<DocumentListener> listeners = new ArrayList<>(fListeners);
        for (DocumentListener l : listeners) {
            l.propertyChanged(name, p.getValue());
        }
    }
}
