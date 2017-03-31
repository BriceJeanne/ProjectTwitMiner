package main;

import twitter4j.TwitterException;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Brice on 31/03/2017.
 */

public class ProjectTwitMiner {

    public static void main(String[] args) {
        TwitterMiner miner = new TwitterMiner();
        String tag = '#' + JOptionPane.showInputDialog("Entrez le tag à rechercher sur Twitter : ");

        try {
            CSVWriter.writeCSV(miner.search(tag, 10), "test");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
