package kz.inessoft.sono.plugin.flk.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SampleDialogWrapper extends DialogWrapper {

    public SampleDialogWrapper() {
        super(true); // use current window as parent
        init();
        setTitle("Sono Flk Utility");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return new JFlkConfigPanel();
//        JPanel dialogPanel = new JPanel();
//        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.PAGE_AXIS));
//
//        JLabel label = new JLabel("testing");
//        label.setPreferredSize(new Dimension(100, 100));
//        label.setAlignmentX(LEFT_ALIGNMENT);
//        dialogPanel.add(label);
//
//        JComboBox<String> comboBox = new JComboBox<>();
//        comboBox.setEditable(true);
//        comboBox.setSelectedItem("");
//        comboBox.setAlignmentX(LEFT_ALIGNMENT);;
//        dialogPanel.add(comboBox);
//        for (String filed : SonoFlkAction.pagesInfo.keySet()) {
//            comboBox.addItem(filed);
//        }
//
//        return dialogPanel;
    }
}
