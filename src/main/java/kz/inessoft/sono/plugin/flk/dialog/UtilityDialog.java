package kz.inessoft.sono.plugin.flk.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import kz.inessoft.sono.plugin.flk.DataHandler;
import kz.inessoft.sono.plugin.flk.FormHandler;
import org.apache.commons.lang.StringUtils;
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

    @Override
    protected ValidationInfo doValidate() {
        if(!DataHandler.fields.containsKey(formHandler.mainXmlField)
                || StringUtils.isBlank(formHandler.mainXmlField)) {
            //javax.swing.JOptionPane.showMessageDialog(null, );
            return new ValidationInfo("Поле для ФЛК обязательно  к заполнению!", null);
        }
        return  null;
    }
}