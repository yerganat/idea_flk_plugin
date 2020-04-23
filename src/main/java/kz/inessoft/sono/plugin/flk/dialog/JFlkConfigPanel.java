package kz.inessoft.sono.plugin.flk.dialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.intellij.ui.JBColor;
import com.jgoodies.forms.factories.*;
import kz.inessoft.sono.plugin.flk.DataHandler;
import kz.inessoft.sono.plugin.flk.FormHandler;
import kz.inessoft.sono.plugin.flk.utils.Logic;
import kz.inessoft.sono.plugin.flk.utils.Oper;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
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
	private JCheckBox formIdxCheckBox;
	private JPanel addDependPanel;
	private JLabel dependLabel;
	private JSearchBox dependFieldComboBox;
	private JComboBox logicComboBox;
	private JButton addDependButton;
	private JLabel exeptFieldLabel;
	private JSearchBox exeptFieldComboBox;
	private JButton exeptFieldButton;
    private JScrollPane addedDepenedScrollPane;
	private JPanel addedDependPanel;
	private JPanel exprPanel;
	private JCheckBox exprCheckbox;
	private JPanel exprInnerPanel;
	private JSearchBox exprComboBox;
	private JComboBox operComboBox;
	private JButton exprAddButton;
    private JScrollPane addedExprScrollPane1;
	private JPanel addedExprPanel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	FormHandler formHandler;
	public JFlkConfigPanel(FormHandler formHandler) {
		this.formHandler = formHandler;
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
        formIdxCheckBox = new JCheckBox();
		addDependPanel = new JPanel();
		dependLabel = new JLabel();
		dependFieldComboBox = new JSearchBox(DataHandler.fields.keySet().toArray());
		logicComboBox = new JComboBox(Logic.getValues(false));
		addDependButton = new JButton();
		exeptFieldLabel = new JLabel();
		exeptFieldComboBox = new JSearchBox();
		exeptFieldButton = new JButton();
        addedDepenedScrollPane = new JScrollPane();
		addedDependPanel = new JPanel();
		exprPanel = new JPanel();
		exprCheckbox = new JCheckBox();
		exprInnerPanel = new JPanel();
		exprComboBox = new JSearchBox();
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
			mainFieldLabel.setText("ФЛК для ");
			fieldPanel.add(mainFieldLabel);

            mainFieldComboBox.setEditable(true);
            mainFieldComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    formHandler.mainXmlField = (String) mainFieldComboBox.getSelectedItem();
					//fieldComboBox.resetData(ArrayUtils.addAll(new String[]{formHandler.mainXmlField}, formHandler.dependOnXmlFieldList.toArray()));
                }
            });
            fieldPanel.add(mainFieldComboBox);
            //AutoCompleteDecorator.decorate(mainFieldComboBox);
		}
		add(fieldPanel);

		//======== requirementPanel ========
		{
			requirementPanel.setLayout(new BoxLayout(requirementPanel, BoxLayout.X_AXIS));

			//---- requiredRadioButton ----
			requiredRadioButton.setText("обязательный реквезит");
			requiredRadioButton.setHorizontalAlignment(SwingConstants.LEFT);
			requiredRadioButton.setSelected(true);
			requiredRadioButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					formHandler.isMainOnlyRequired = requiredRadioButton.isSelected();
					setPanelEnabled(dependPanel, !formHandler.isMainOnlyRequired);
				}
			});
			requirementPanel.add(requiredRadioButton);

			//---- conditionRequiredRadioButton ----
			conditionRequiredRadioButton.setText("обязательно-зависимый реквизит");
			conditionRequiredRadioButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					formHandler.isMainOnlyRequired = requiredRadioButton.isSelected();
					setPanelEnabled(dependPanel, !formHandler.isMainOnlyRequired);
				}
			});

			requirementPanel.add(conditionRequiredRadioButton);

            ButtonGroup bg=new ButtonGroup();
            bg.add(requiredRadioButton);
            bg.add(conditionRequiredRadioButton);
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
				pageViewCheckBox.setText("*показать страницы(приложении)");
				pageViewCheckBox.setVerticalAlignment(SwingConstants.TOP);
				pageViewCheckBox.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent changeEvent) {
                        if(pageViewCheckBox.isSelected()) {
                            dependFieldComboBox.resetData(DataHandler.pages.keySet().toArray());
                            dependFieldComboBox.setEditable(false);

							if(StringUtils.isNotBlank((String) dependFieldComboBox.getSelectedItem()))
								exeptFieldComboBox.resetData(DataHandler.pages.get((String) dependFieldComboBox.getSelectedItem()).toArray());
						} else {
                            dependFieldComboBox.resetData(DataHandler.fields.keySet().toArray());
                            dependFieldComboBox.setEditable(true);

                            exeptFieldComboBox.resetData(new String[]{});
                        }

						exeptFieldComboBox.setEditable(false);
                    }
                });
                pagablePanel.add(pageViewCheckBox);

                formIdxCheckBox.setText("formIdx(i)");
                formIdxCheckBox.setVerticalAlignment(SwingConstants.TOP);
                formIdxCheckBox.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent changeEvent) {
                        DataHandler.formIdx = formIdxCheckBox.isSelected();
                    }
                });
                pagablePanel.add(formIdxCheckBox);

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
				//addDependPanel.add(logicComboBox); TODO раскоменить  когда понадобиться

				//---- addDependButton ----
				addDependButton.setText("+");
				addDependButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						addDependedField((String) dependFieldComboBox.getSelectedItem(), formHandler.dependOnXmlFieldList, false);

						exprComboBox.resetData( formHandler.dependOnXmlFieldList.stream().filter(p-> !p.startsWith("page")).toArray());

                        addExprPanel("+", (String) dependFieldComboBox.getSelectedItem());
                        setPanelEnabled(addedExprPanel, formHandler.isHasCalculation);
                        setPanelEnabled(exprInnerPanel, formHandler.isHasCalculation);
					}
				});
				addDependPanel.add(addDependButton);


				//---- exeptDieldLabel ----
				exeptFieldLabel.setText("исключить поле");
				addDependPanel.add(exeptFieldLabel);

				exeptFieldComboBox.setEditable(false);
				addDependPanel.add(exeptFieldComboBox);

				//---- exeptFieldButton ----
				exeptFieldButton.setText("+");
                exeptFieldButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
						addDependedField((String) exeptFieldComboBox.getSelectedItem(), formHandler.excludeXmlFieldList, true);
                    }
                });
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
            }

			dependPanel.add(addedDepenedScrollPane);
		}
		add(dependPanel);

		//======== exprPanel ========
		{
			exprPanel.setLayout(new BoxLayout(exprPanel, BoxLayout.Y_AXIS));

			//---- exprCheckbox ----
			exprCheckbox.setText("проверка выражения(если поле заполнено)");
			exprCheckbox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					if(exprCheckbox.isSelected()) {
						formHandler.isHasCalculation = true;
					} else {
						formHandler.isHasCalculation = false;
					}
					setPanelEnabled(addedExprPanel, formHandler.isHasCalculation);
					setPanelEnabled(exprInnerPanel, formHandler.isHasCalculation);
				}
			});

			exprPanel.add(exprCheckbox);

			//======== panel9 ========
			{
				exprInnerPanel.setLayout(new BoxLayout(exprInnerPanel, BoxLayout.X_AXIS));

				//---- operComboBox ----
				operComboBox.setToolTipText("операторы");
				exprInnerPanel.add(operComboBox);

				//---- fieldComboBox ----
				exprComboBox.setToolTipText("поле");
				exprComboBox.setEditable(false);
				exprInnerPanel.add(exprComboBox);

				//---- exprAddButton ----
				exprAddButton.setText("+");
				exprAddButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					addExprPanel((String)operComboBox.getSelectedItem(), (String) exprComboBox.getSelectedItem());
				}
			});
				exprInnerPanel.add(exprAddButton);
			}
			exprPanel.add(exprInnerPanel);

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

		setPanelEnabled(dependPanel, false);
		setPanelEnabled(exprInnerPanel, false);
	}

	private void addDependedField(String fieldName, Set<String> containerList, boolean exclude) {
		if(containerList.contains(fieldName)) return;
		containerList.add(fieldName);

		JPanel jPanelTmp = new JPanel();
		jPanelTmp.setLayout(new BoxLayout(jPanelTmp, BoxLayout.X_AXIS));
		jPanelTmp.add(new JLabel((exclude?"исключить ":"зависит от ") + fieldName));
		jPanelTmp.setName(fieldName);

		JButton jRemoveButtonTmp = new JButton("-");
		jRemoveButtonTmp.setForeground(JBColor.RED);
		jRemoveButtonTmp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				containerList.remove(jPanelTmp.getName());

				addedDependPanel.remove(jPanelTmp);
				addedDependPanel.revalidate();
				addedDependPanel.repaint();

				exprComboBox.resetData(formHandler.dependOnXmlFieldList.toArray());
			}
		});
		jPanelTmp.add(jRemoveButtonTmp);

		addedDependPanel.add(jPanelTmp);
		addedDependPanel.revalidate();
		addedDependPanel.repaint();

		JScrollBar toBottom = addedDepenedScrollPane.getVerticalScrollBar();
		toBottom.setValue(toBottom.getMaximum() );
	}

	private void addExprPanel(String oper, String field) {
		if(formHandler.calcXmlFieldMap.containsKey(field)
				&& formHandler.calcXmlFieldMap.containsValue(oper)) return;

		formHandler.calcXmlFieldMap.put(field, oper);
		JPanel jPanelTmp = new JPanel();
		jPanelTmp.setLayout(new BoxLayout(jPanelTmp, BoxLayout.X_AXIS));
		jPanelTmp.add(new JLabel(oper + " " + field));
		jPanelTmp.setName(field);

		JButton jRemoveButtonTmp = new JButton("-");
		jRemoveButtonTmp.setForeground(JBColor.RED);
		jRemoveButtonTmp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				formHandler.calcXmlFieldMap.remove(jPanelTmp.getName());

				addedExprPanel.remove(jPanelTmp);
				addedExprPanel.revalidate();
				addedExprPanel.repaint();
			}
		});
		jPanelTmp.add(jRemoveButtonTmp);

		addedExprPanel.add(jPanelTmp);
		addedExprPanel.revalidate();
		addedExprPanel.repaint();

		JScrollBar toBottom = addedExprScrollPane1.getVerticalScrollBar();
		toBottom.setValue(toBottom.getMaximum() );
	}
	private  void setPanelEnabled(Container container, Boolean isEnabled) {
		container.setEnabled(isEnabled);

		Component[] components = container.getComponents();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof java.awt.Container) {
				setPanelEnabled((java.awt.Container) components[i], isEnabled);
			}
			components[i].setEnabled(isEnabled);
		}
	}

	private void actions() {

	}
}
