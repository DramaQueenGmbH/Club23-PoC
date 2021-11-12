package com.dramaqueen.club23;

import com.dramaqueen.club23.model.Document;
import com.dramaqueen.club23.ui.Club23Panel;
import com.dramaqueen.club23.ui.DocumentPanel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class App {
    public static void main( String[] args) {
        Display display = Display.getDefault();

        Document document = new Document();

        Shell shell = createMainWindow(document);
        shell.open();

        while (!shell.isDisposed()) {
            display.readAndDispatch();
        }
    }

    private static Shell createMainWindow(Document document) {
        Shell shell = new Shell();
        shell.setText("Hello Club23");
        createInterface(shell, document);
        return shell;
    }

    private static void createInterface(Shell shell, Document document) {
        Composite leftPanel = new DocumentPanel(shell, document);
        fillGrid(leftPanel).widthHint = 100;
        Composite rightPanel = new Club23Panel(shell, document);
        fillGrid(rightPanel).widthHint = 200;;

        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        shell.setLayout(layout);
    }

    private static GridData fillGrid(Control c) {
        GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
        c.setLayoutData(data);
        return data;
    }
}
