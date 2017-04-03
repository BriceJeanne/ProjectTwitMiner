package phase1;

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
                for (int i = 0; i < 160; ++i)
                    FilesWriter.writeCSV(miner.search(tag, tweets), "data", false);
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
            FilesWriter.writeCSV(miner.search(tag, 10), "test", true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private static void runApriori(String transPath, Integer freq) throws IOException, InterruptedException {
        String cmd = "apriori/linux/apriori";
        ProcessBuilder builder =
                new ProcessBuilder("/bin/bash", "-c", cmd, transPath, freq.toString(), transPath + ".out");

        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process p = builder.start();

        p.waitFor();

    }

    public static void main(String[] args) {
        //ProjectTwitMiner.loop(100);

        try {
            runApriori("test2.trans", 3);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
