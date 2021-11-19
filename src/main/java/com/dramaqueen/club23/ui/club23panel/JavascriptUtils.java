package com.dramaqueen.club23.ui.club23panel;

import com.dramaqueen.club23.model.Document;
import com.dramaqueen.club23.model.DocumentVisitor;

public class JavascriptUtils {
    public static String toJavascriptObject(Document document) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        document.visitProperties((name, value) -> {
            if (builder.length() > 1)
                builder.append(",");
            builder.append(name);
            builder.append(":'");
            builder.append(value);
            builder.append("'");
        });

        builder.append("}");
        return builder.toString();
    }
}
