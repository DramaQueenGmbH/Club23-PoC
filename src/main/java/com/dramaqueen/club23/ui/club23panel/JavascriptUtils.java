package com.dramaqueen.club23.ui.club23panel;

import com.dramaqueen.club23.model.Document;

public class JavascriptUtils {
    public static String toJavascriptObject(Document document) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        document.visitProperties((String name, String value) -> {
            if (builder.length() > 1)
                builder.append(", ");
            builder.append(name);
            builder.append(": '");
            builder.append(value);
            builder.append("'");
        });

        builder.append("}");
        return builder.toString();
    }
}
