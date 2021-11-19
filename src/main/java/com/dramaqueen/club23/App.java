package com.dramaqueen.club23;

import com.dramaqueen.club23.model.Document;
import com.dramaqueen.club23.ui.MainWindow;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class App {
    public static void main( String[] args) {
        Display display = Display.getDefault();

        Document document = new Document();
        MainWindow window = new MainWindow(document);
        Shell shell = window.open();

        while (!shell.isDisposed()) {
            display.readAndDispatch();
        }
    }
}
