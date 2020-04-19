package kz.inessoft.sono.plugin.flk.dialog;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.jgoodies.forms.factories.*;
import kz.inessoft.sono.plugin.flk.SonoFlkAction;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
/*
 * Created by JFormDesigner on Sat Apr 18 15:57:46 ALMT 2020
 */



/**
 * @author MERZENTAY YERGANAT
 */
public class JFlkConfigPanel extends JPanel {
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - MERZENTAY YERGANAT
	private JPanel fieldPanel;
	private JLabel mainFieldLabel;
	private JComboBox mainFieldComboBox;
	private JPanel requirementPanel;
	private JRadioButton requiredRadioButton;
	private JRadioButton conditionRequiredRadioButton;
	private JPanel dependPanel;
	private JPanel pagablePanel;
	private JLabel dependOnLabel;
	private JCheckBox oageViewCheckBox;
	private JPanel addDependPanel;
	private JLabel dependLabel;
	private JComboBox dependFieldComboBox;
	private JButton addDependButton;
	private JLabel exeptDieldLabel;
	private JComboBox expetFieldComboBox;
	private JButton exeptFieldButton;
    private JScrollPane addedDepenedScrollPane;
	private JPanel addedDependPanel;
	private JPanel exprPanel;
	private JCheckBox exprCheckbox;
	private JPanel panel9;
	private JComboBox fieldComboBox;
	private JComboBox operComboBox;
	private JButton exprAddButton;
    private JScrollPane addedExprScrollPane1;
	private JPanel addedExprPanel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	public JFlkConfigPanel() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - MERZENTAY YERGANAT
		fieldPanel = new JPanel();
		mainFieldLabel = new JLabel();
		mainFieldComboBox = new JSearchBox();
		requirementPanel = new JPanel();
		requiredRadioButton = new JRadioButton();
		conditionRequiredRadioButton = new JRadioButton();
		dependPanel = new JPanel();
		pagablePanel = new JPanel();
		dependOnLabel = new JLabel();
		oageViewCheckBox = new JCheckBox();
		addDependPanel = new JPanel();
		dependLabel = new JLabel();
		dependFieldComboBox = new JSearchBox();
		addDependButton = new JButton();
		exeptDieldLabel = new JLabel();
		expetFieldComboBox = new JSearchBox();
		exeptFieldButton = new JButton();
        addedDepenedScrollPane = new JScrollPane();
		addedDependPanel = new JPanel();
		exprPanel = new JPanel();
		exprCheckbox = new JCheckBox();
		panel9 = new JPanel();
		fieldComboBox = new JSearchBox();
		operComboBox = new JSearchBox();
		exprAddButton = new JButton();
        addedExprScrollPane1 = new JScrollPane();
		addedExprPanel = new JPanel();

		//======== this ========
		setBorder(Borders.DLU2_BORDER);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		//======== fieldPanel ========
		{
			fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.X_AXIS));

			//---- mainFieldLabel ----
			mainFieldLabel.setText("\u0424\u041b\u041a \u0434\u043b\u044f \u043f\u043e\u043b\u0435");
			fieldPanel.add(mainFieldLabel);
			fieldPanel.add(mainFieldComboBox);

			for (String filed : SonoFlkAction.pagesInfo.keySet()) {
				mainFieldComboBox.addItem(filed);
			}
			mainFieldComboBox.setEditable(true);
			//AutoCompleteDecorator.decorate(mainFieldComboBox);
		}
		add(fieldPanel);

		//======== requirementPanel ========
		{
			requirementPanel.setLayout(new BoxLayout(requirementPanel, BoxLayout.X_AXIS));

			//---- requiredRadioButton ----
			requiredRadioButton.setText("обязательный реквезит");
			requiredRadioButton.setHorizontalAlignment(SwingConstants.LEFT);
			requirementPanel.add(requiredRadioButton);

			//---- conditionRequiredRadioButton ----
			conditionRequiredRadioButton.setText("условно обязательный реквезит");
			requirementPanel.add(conditionRequiredRadioButton);

            ButtonGroup bg=new ButtonGroup();
            bg.add(requiredRadioButton);bg.add(conditionRequiredRadioButton);
        }
		add(requirementPanel);

		//======== dependPanel ========
		{
			dependPanel.setLayout(new BoxLayout(dependPanel, BoxLayout.Y_AXIS));

			//======== pagablePanel ========
			{
				pagablePanel.setLayout(new BoxLayout(pagablePanel, BoxLayout.X_AXIS));

				//---- dependOnLabel ----
				dependOnLabel.setText("Зависит от  полей:                 ");
				pagablePanel.add(dependOnLabel);

			//---- oageViewCheckBox ----
				oageViewCheckBox.setText("*показать постранично(проверка заполненности приложении)");
				oageViewCheckBox.setVerticalAlignment(SwingConstants.TOP);
				pagablePanel.add(oageViewCheckBox);
			}
			dependPanel.add(pagablePanel);

			//======== addDependPanel ========
			{
				addDependPanel.setLayout(new BoxLayout(addDependPanel, BoxLayout.X_AXIS));

				//---- dependLabel ----
				dependLabel.setText("Поле");
				addDependPanel.add(dependLabel);
				addDependPanel.add(dependFieldComboBox);

				//---- addDependButton ----
				addDependButton.setText("+");
				addDependPanel.add(addDependButton);

				//---- exeptDieldLabel ----
				exeptDieldLabel.setText("исключить поле");
				addDependPanel.add(exeptDieldLabel);
				addDependPanel.add(expetFieldComboBox);

				//---- exeptFieldButton ----
				exeptFieldButton.setText("-");
				addDependPanel.add(exeptFieldButton);
			}
			dependPanel.add(addDependPanel);

            //======== addedDepenedScrollPane ========
            {
                //======== addedDepenedPanel ========
                {
                    addedDependPanel.setLayout(new BoxLayout(addedDependPanel, BoxLayout.Y_AXIS));
                }
                addedDepenedScrollPane.setViewportView(addedDependPanel);
                addedDepenedScrollPane.setPreferredSize(new Dimension(300, 100));

                addedDependPanel.add(getJButton(addedDependPanel));
            }

			dependPanel.add(addedDepenedScrollPane);
		}
		add(dependPanel);

		//======== exprPanel ========
		{
			exprPanel.setLayout(new BoxLayout(exprPanel, BoxLayout.Y_AXIS));

			//---- exprCheckbox ----
			exprCheckbox.setText("проверка выражения(если поле заполнено)");
			exprPanel.add(exprCheckbox);

			//======== panel9 ========
			{
				panel9.setLayout(new BoxLayout(panel9, BoxLayout.X_AXIS));

				//---- fieldComboBox ----
				fieldComboBox.setToolTipText("поле");
				panel9.add(fieldComboBox);

				//---- operComboBox ----
				operComboBox.setToolTipText("операторы");
				panel9.add(operComboBox);

				//---- exprAddButton ----
				exprAddButton.setText("+");
				panel9.add(exprAddButton);
			}
			exprPanel.add(panel9);

            //======== addedExprScrollPane1 ========
            {

                //======== addedExprPanel ========
                {
                    addedExprPanel.setLayout(new BoxLayout(addedExprPanel, BoxLayout.Y_AXIS));
                }
                addedExprScrollPane1.setViewportView(addedExprPanel);
                addedExprScrollPane1.setPreferredSize(new Dimension(300, 100));
            }

			exprPanel.add(addedExprScrollPane1);
		}
		add(exprPanel);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents

	}

    static public JButton getJButton(JPanel p){
        JButton b = new JButton("more");
        b.addActionListener(evt->{
            p.add(getJButton(p));
            p.revalidate();
            p.repaint();
        });
        return b;
    }
}
