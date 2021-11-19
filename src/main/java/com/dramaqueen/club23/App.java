package com.dramaqueen.club23;

import com.dramaqueen.club23.model.Document;
import com.dramaqueen.club23.ui.MainWindow;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class App {
    public static void main(String[] args) {
        Display display = Display.getDefault();

        Document document = new Document();
        storeArgsInDocument(document, args);

        MainWindow window = new MainWindow(document);
        Shell shell = window.open();

        while (!shell.isDisposed()) {
            display.readAndDispatch();
        }
    }

    private static void storeArgsInDocument(Document document, String[] args) {
        for (String arg : args) {
            String[] keyValue = arg.split("=");
            if (keyValue.length == 2) {
                document.setValue(keyValue[0], keyValue[1]);
            }
        }
    }
}
