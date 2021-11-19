package com.dramaqueen.club23.ui;

import com.dramaqueen.club23.model.Document;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;

public class MainWindow {

    private final Document fDocument;

    public MainWindow(Document document) {
        fDocument = document;
    }

    public Shell open() {
        Shell shell = createShell();
        createInterface(shell, fDocument);
        shell.open();
        return shell;
    }

    private static Shell createShell() {
        Shell shell = new Shell();
        shell.setText("SWT native image demo");
        return shell;
    }

    private static void createInterface(Shell shell, Document document) {
        ToolBarManager toolBarManager = createToolBar(shell, document);

        Club23Panel browserPanel = new Club23Panel(shell, document);
        fillGrid(browserPanel).widthHint = 200;
        Composite rightPanel = new DocumentPanel(shell, document);
        fillGrid(rightPanel).widthHint = 100;

        toolBarManager.add(browserPanel.getNavigateBackAction());
        toolBarManager.add(browserPanel.getNavigateForwardAction());
        toolBarManager.update(false);

        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        shell.setLayout(layout);
    }

    private static ToolBarManager createToolBar(Shell shell, Document document) {
        ToolBar toolBar = shell.getToolBar();
        if (toolBar == null) {
            toolBar = new ToolBar(shell, SWT.FLAT);
        }
        return new ToolBarManager(toolBar);
    }

    private static GridData fillGrid(Control c) {
        GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
        c.setLayoutData(data);
        return data;
    }
}
