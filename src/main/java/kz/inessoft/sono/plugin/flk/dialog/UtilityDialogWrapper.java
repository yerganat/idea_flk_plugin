package kz.inessoft.sono.plugin.flk.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class UtilityDialogWrapper extends DialogWrapper {

    public UtilityDialogWrapper() {
        super(true); // use current window as parent
        init();
        setTitle("Sono Flk Utility");
        setOKButtonText("Сформировать");
        setCancelButtonText("Отменить");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return new JFlkConfigPanel();
    }
}