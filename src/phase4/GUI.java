package phase4;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brice on 07/04/2017.
 */
public class GUI {

    private JPanel mainPanel;
    private JButton searchBtn;
    private JTextField searchField;
    private JTable rulesTable;

    private String filename;
    private List<String> rules;

    public GUI(String filename) {
        this.filename = filename;
        rules = new ArrayList<>();
    }

    private void createJTable() {
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
        String[] header = {"X", "Y", "Freq", "Conf", "Lift"};
        rulesTable = new JTable(null, header);

        for (String rule : rules) {
            List<String> ruleList = Arrays.asList(rule.split(";"));
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
