package phase4;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Brice on 07/04/2017.
 */
public class GUI {

    private JPanel mainPanel;
    private JButton searchBtn;
    private JTextField searchField;
    private JTable rulesTable;
    private TableRowSorter<DefaultTableModel> sorter;

    private String filename;

    public GUI(String filename) {
        this.filename = filename;
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterRows();
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    filterRows();
                }
            }
        });
    }

    private void filterRows() {
        try {
            RowFilter<DefaultTableModel, Integer> rf =
                    RowFilter.regexFilter(searchField.getText(), 0, 1);
            sorter.setRowFilter(rf);
        } catch (PatternSyntaxException pse) {
            return;
        }
    }

    private void createJTable() {
        List<String> rules = new ArrayList<>();
        try {
            BufferedReader reader =
                    new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(filename + ".rules" + ".csv")));

            String line;
            while ((line = reader.readLine()) != null)
                rules.add(line);

        } catch (IOException e) {
            e.printStackTrace();
        }

        DefaultTableModel model = new DefaultTableModel();
        sorter = new TableRowSorter<>(model);
        rulesTable = new JTable(model);
        rulesTable.setRowSorter(sorter);

        // Create a couple of columns
        model.addColumn("X");
        model.addColumn("Y");
        model.addColumn("Conf");
        model.addColumn("Freq");
        model.addColumn("Lift");

        for (String rule : rules) {
            String[] ruleList = rule.split(";");
            model.addRow(ruleList);
        }
    }

    private void createUIComponents() {
        createJTable();
    }

    public static void start(String filename) {
        JFrame frame = new JFrame("Project TwitMiner : A simple tweeter data miner");
        frame.setContentPane(new GUI(filename).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
