package com.dramaqueen.club23.ui.jface;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import java.net.URL;

public abstract class IconAction extends Action {

    public IconAction(String text, String iconName) {
        this(text, iconName, Action.AS_PUSH_BUTTON);
    }

    public IconAction(String text, String iconName, int style) {
        super(text, style);
        setId(iconName);
        String iconPath = "icons/actions/" + iconName+".png";
        URL iconUrl = getClass().getClassLoader().getResource(iconPath);
        if (iconUrl == null) {
            throw new RuntimeException("No icon URL for '" + iconName + "'");
        }
        setImageDescriptor(ImageDescriptor.createFromURL(iconUrl));
    }
}
