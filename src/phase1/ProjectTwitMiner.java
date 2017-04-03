package phase1;

import phase1.CSV.CSVToTrans;
import phase1.CSV.CSVWriter;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brice on 31/03/2017.
 */

public class ProjectTwitMiner {

    private static final char DELIMITER = ';';

    private static void writeTweets(boolean loop, String filename) throws TwitterException, InterruptedException {
        String tag = '#' + JOptionPane.showInputDialog("Entrez le tag à rechercher sur Twitter.");

        TwitterMiner miner = new TwitterMiner();
        CSVWriter writer = new CSVWriter(filename, true);

        while (true) {

            /* Doing 180 requests at once before we have to wait 15 mins */
            for (int i = 0; i < 10; ++i) {
                List<String> listLines = new ArrayList<>();
                List<Status> tweets = miner.search(tag, 150);

                for (Status tweet : tweets) {
                    String line = "";

                    line += String.valueOf(tweet.getCreatedAt()) + DELIMITER;
                    line += '@' + tweet.getUser().getScreenName() + DELIMITER;
                    line += tweet.getUser().getLocation() + DELIMITER;

                    String message = tweet.getText();
                    message = message.replace(',', DELIMITER);
                    message = message.replace(' ', DELIMITER);
                    message = message.replace('\n', DELIMITER);
                    message = message.replace('"', DELIMITER);

                    line += message + DELIMITER;

                    listLines.add(line);
                }

                writer.writeLines(listLines);
            }

            if (!loop) break;

            System.out.println("~180 tweets request made, waiting 15 mins...");
            Thread.sleep(600000); // 600000ms = 15 mins
        }
    }

    private static void runApriori(boolean linux, String transPath, Integer freq) throws IOException {
        List<String> commands = new ArrayList<>();

        /* LINUX NOT WORKING YET */
        if (linux) {
            commands.add("/bin/bash");
            commands.add("-c");
            commands.add("./apriori/linux/apriori");
            commands.add(transPath);
            commands.add(freq.toString());
            commands.add(transPath + ".out");
        }

        else {
            commands.add(".\\apriori\\windows\\Apriori.exe");
            commands.add(transPath);
            commands.add(freq.toString());
            commands.add(transPath + ".out");
        }

        ProcessBuilder builder =
                new ProcessBuilder(commands);

        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process = builder.start();
    }

    public static void main(String[] args) {

        String filename = JOptionPane.showInputDialog("Entrez le nom du fichier.");

        try {
            System.out.println("Getting tweets...");
            writeTweets(false, filename);
            System.out.println("Tweets recovered and wrote " + filename + ".csv");

            System.out.println("Getting recurrent patterns..");
            CSVToTrans csvtotrans = new CSVToTrans(filename);
            csvtotrans.writeTrans();
            System.out.println("Recurrent patterns wrote to " + filename + ".trans");

            runApriori(false, filename + ".trans", 3);
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
