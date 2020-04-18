package kz.inessoft.sono.plugin.flk.dialog;

import kz.inessoft.sono.plugin.flk.SonoFlkAction;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * Enables efficient searching through a JComboBox.
 * <p>
 * Based on code by Ron (rmlchan@yahoo.com)
 *
 * @author Eric Lindauer
 * @date 2002.9.24
 */

public class JSearchBox extends JComboBox {
    public JSearchBox() {
        super();
        init();
    }

    public JSearchBox(Object[] elements) {
        super(elements);
        init();
    }

    public void setModel(ComboBoxModel model) {
        super.setModel(model);
        init();
    }

    private void init() {
        setEditable(true);
        setEditor(new SearchEditor(this));
    }

    private static class SearchEditor extends BasicComboBoxEditor {
        private List<String> _data = new ArrayList<>();
//        public Object getItem() {
//            return _data.get(super.getItem().toString());
//        }

        public SearchEditor(final JSearchBox cb) {
            // populate the search tree with the items in the list
            ComboBoxModel model = cb.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                Object data = model.getElementAt(i);
                _data.add(data.toString());
            }

            // when the user types, search the data and guess what they want
            KeyAdapter listener = new KeyAdapter() {
                public void keyReleased(KeyEvent ev) {
                    if ((ev.getKeyChar() >= 'a' && ev.getKeyChar() <= 'z') ||
                            (ev.getKeyChar() >= '0' && ev.getKeyChar() <= '9') ||
                            (ev.getKeyChar() >= 'A' && ev.getKeyChar() <= 'Z') ||
                            (ev.getKeyChar() == KeyEvent.VK_SPACE) ||
                            (ev.getKeyChar() == KeyEvent.VK_DELETE) ||
                            (ev.getKeyChar() == KeyEvent.VK_BACK_SPACE) ||
                            (ev.getKeyChar() == KeyEvent.VK_ENTER)) {
                        String startText = editor.getText();
                        cb.showPopup();
                        DefaultComboBoxModel<String> model2 = (DefaultComboBoxModel<String>) model;
                        model2.removeAllElements();
                        String firstElement = "";
                        for (String item : SonoFlkAction.pagesInfo.keySet()) {
                            if (item.toLowerCase().contains(startText.toLowerCase())) {
                                model2.addElement(item);
                                if(firstElement.isEmpty())
                                    firstElement = item;
                            }
                        }

                        if(ev.getKeyChar() == KeyEvent.VK_ENTER && !firstElement.isEmpty()) {
                            cb.setSelectedItem(firstElement);
                            editor.setText(firstElement);
                            cb.hidePopup();
                        } else {
                            cb.setSelectedItem(startText);
                            editor.setText(startText);
                        }
                    }
                }
            };

            editor.addKeyListener(listener);

            // register an action listener to keep the text area always up-to-date
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (cb.getSelectedItem() != null &&
                            !editor.getText().equals(cb.getSelectedItem().toString())) {
                        editor.setText(cb.getSelectedItem().toString());
                    }
                }
            };
            cb.addActionListener(actionListener);
        }
    }
//
//    public static void main(String[] args) {
//        java.awt.Frame f = new java.awt.Frame();
//
//        f.addWindowListener(new java.awt.event.WindowAdapter() {
//                                public void windowClosing(java.awt.event.WindowEvent ev) {
//                                    System.exit(0);
//                                }
//                            }
//        );
//
//        String[] stuff = new String[]{"first", "second", "second and some",
//
//                "third", "three", "three+", "thrice", "four", "four-plus"};
//
//        JSearchableComboBox cb = new JSearchableComboBox(stuff);
//        f.add(cb);
//        f.pack();
//        f.show();
//    }

}

