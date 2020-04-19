package kz.inessoft.sono.plugin.flk.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import kz.inessoft.sono.plugin.flk.FormHandler;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class UtilityDialog extends DialogWrapper {
    public FormHandler formHandler = new FormHandler();

    public UtilityDialog() {
        super(true); // use current window as parent
        init();
        setTitle("Sono Flk Utility");
        setOKButtonText("Сформировать");
        setCancelButtonText("Отменить");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return new JFlkConfigPanel(formHandler);
    }
}