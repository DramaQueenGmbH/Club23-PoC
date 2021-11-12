package com.dramaqueen.club23.ui;

import com.dramaqueen.club23.model.Document;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.HashMap;
import java.util.Map;

public class DocumentPanel extends Composite implements Panel {

    private final Document fDocument;
    private final Map<String, Text> fFields;

    public DocumentPanel(Composite parent, Document document) {
        super(parent, SWT.NULL);
        fDocument = document;
        fFields = new HashMap<>();

        String[] fields = {"Title", "Logline", "Summary"};
        for (String f : fields) {
            setupTextField(f);
        }

        FocusManager.registerPanel("document", this);

        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 20;
        layout.marginHeight = 20;
        layout.verticalSpacing = 10;
        setLayout(layout);
    }

    @Override
    public void focusField(String field) {
        Text text = fFields.get(field);
        if (text != null) {
            text.selectAll();
            text.setFocus();
        }
    }

    private void setupTextField(final String field) {
        Label label = new Label(this, SWT.NULL);
        label.setText(field);

        final Text text = new Text(this, SWT.SINGLE | SWT.BORDER);
        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent modifyEvent) {
                fDocument.setValue(field, text.getText());
            }
        });
        text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        fFields.put(field, text);
    }
}
