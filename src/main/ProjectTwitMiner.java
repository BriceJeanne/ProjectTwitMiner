package main;

import twitter4j.TwitterException;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Brice on 31/03/2017.
 */

public class ProjectTwitMiner {

    private static void loop(int tweets) {
        while (true) {
            TwitterMiner miner = new TwitterMiner();
            String tag = "#Presidentielle2017";

            try {
                CSVWriter.write(miner.search(tag, tweets), "test", false);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            System.out.println("Mined " + tweets + " tweets... Waiting 15 mins.");

            try {
                for (int i = 1; i <= 15; ++i) {
                    Thread.sleep(60000);
                    System.out.println(i + " min");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Wait is over ! Time to mine :)");
        }
    }

    private static void once() {
        TwitterMiner miner = new TwitterMiner();
        String tag = '#' + JOptionPane.showInputDialog("Entrez le tag à rechercher sur Twitter : ");

        try {
            CSVWriter.write(miner.search(tag, 10), "test", true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ProjectTwitMiner.loop(100);
    }
}
