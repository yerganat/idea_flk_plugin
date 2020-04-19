package kz.inessoft.sono.plugin.flk.dialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.*;
import kz.inessoft.sono.plugin.flk.DataHandler;
import kz.inessoft.sono.plugin.flk.utils.Oper;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JSearchBox mainFieldComboBox;
	private JPanel requirementPanel;
	private JRadioButton requiredRadioButton;
	private JRadioButton conditionRequiredRadioButton;
	private JPanel dependPanel;
	private JPanel pagablePanel;
	private JLabel dependOnLabel;
	private JCheckBox pageViewCheckBox;
	private JPanel addDependPanel;
	private JLabel dependLabel;
	private JSearchBox dependFieldComboBox;
	private JButton addDependButton;
	private JLabel exeptDieldLabel;
	private JSearchBox exeptFieldComboBox;
	private JButton exeptFieldButton;
    private JScrollPane addedDepenedScrollPane;
	private JPanel addedDependPanel;
	private JPanel exprPanel;
	private JCheckBox exprCheckbox;
	private JPanel panel9;
	private JSearchBox fieldComboBox;
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
		mainFieldComboBox = new JSearchBox(DataHandler.fields.keySet().toArray());
		requirementPanel = new JPanel();
		requiredRadioButton = new JRadioButton();
		conditionRequiredRadioButton = new JRadioButton();
		dependPanel = new JPanel();
		pagablePanel = new JPanel();
		dependOnLabel = new JLabel();
		pageViewCheckBox = new JCheckBox();
		addDependPanel = new JPanel();
		dependLabel = new JLabel();
		dependFieldComboBox = new JSearchBox(DataHandler.fields.keySet().toArray());
		addDependButton = new JButton();
		exeptDieldLabel = new JLabel();
		exeptFieldComboBox = new JSearchBox();
		exeptFieldButton = new JButton();
        addedDepenedScrollPane = new JScrollPane();
		addedDependPanel = new JPanel();
		exprPanel = new JPanel();
		exprCheckbox = new JCheckBox();
		panel9 = new JPanel();
		fieldComboBox = new JSearchBox(DataHandler.fields.keySet().toArray());
		operComboBox = new JComboBox(Oper.getValues(false));
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

			//---- pageViewCheckBox ----
				pageViewCheckBox.setText("*показать постранично(проверка заполненности приложении)");
				pageViewCheckBox.setVerticalAlignment(SwingConstants.TOP);
                pageViewCheckBox.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent changeEvent) {
                        if(pageViewCheckBox.isSelected()) {
                            dependFieldComboBox.resetData(DataHandler.pages.keySet().toArray());
                            dependFieldComboBox.setEditable(false);
                        } else {
                            dependFieldComboBox.resetData(DataHandler.fields.keySet().toArray());
                            dependFieldComboBox.setEditable(true);

                            exeptFieldComboBox.resetData(new String[]{});
                        }
                    }
                });

				pagablePanel.add(pageViewCheckBox);
			}
			dependPanel.add(pagablePanel);

			//======== addDependPanel ========
			{
				addDependPanel.setLayout(new BoxLayout(addDependPanel, BoxLayout.X_AXIS));

				//---- dependLabel ----
				dependLabel.setText("Поле");
				addDependPanel.add(dependLabel);

				dependFieldComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String dependValue = (String) dependFieldComboBox.getSelectedItem();
                        if(StringUtils.isNotBlank(dependValue)
                                && pageViewCheckBox.isSelected()
                                && DataHandler.pages.containsKey(dependValue)) {
                            exeptFieldComboBox.resetData(DataHandler.pages.get(dependValue).toArray());
                            exeptFieldComboBox.setEditable(false);
                        }
                    }
                });
				addDependPanel.add(dependFieldComboBox);

				//---- addDependButton ----
				addDependButton.setText("+");
				addDependPanel.add(addDependButton);

				//---- exeptDieldLabel ----
				exeptDieldLabel.setText("исключить поле");
				addDependPanel.add(exeptDieldLabel);
				addDependPanel.add(exeptFieldComboBox);

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
