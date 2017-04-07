import phase0.TwitterMiner;
import phase1.CSV.CSVToTrans;
import phase1.CSV.CSVWriter;
import phase2.RulesExtractor;
import phase3.ClearData;
import phase4.GUI;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brice on 31/03/2017.
 */

public class ProjectTwitMiner {

    private static final char DELIMITER = ';';

    private static void writeTweets(boolean loop, String filename) throws TwitterException, InterruptedException {
        TwitterMiner miner = new TwitterMiner();
        CSVWriter writer = new CSVWriter(filename, true);

        while (true) {

            /* Doing 1 request to get 100 tweets */
            /* Reduced requests to reduce duplicates */
            for (int i = 0; i < 1; ++i) {
                List<String> listLines = new ArrayList<>();
                List<Status> tweets = miner.search('#' + filename, 100);

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

    private static void runApriori(String transPath, Integer freq) throws IOException {
        List<String> commands = new ArrayList<>();

        String OS = System.getProperty("os.name").toLowerCase();
        boolean linux = OS.indexOf("nix") < 0;

        if (linux) {
            if (System.getProperty("os.arch").equals("i386"))
                commands.add("./apriori/linux/32/apriori");
            else
                commands.add("./apriori/linux/64/apriori");

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

        try {
            process.waitFor();
            process.destroy();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            String filename;

            int choice = JOptionPane.showOptionDialog(null,
                    "Do you want to open a .csv file containing your tweets ?",
                    "Open file",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, null);

            if (choice == JOptionPane.YES_OPTION) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "CSV files", "csv");

                chooser.setFileFilter(filter);

                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    filename = chooser.getSelectedFile().getName();
                    filename = filename.substring(0, filename.lastIndexOf("."));
                } else return;

            } else {
                /* PHASE 0 */
                System.out.println("Getting tweets...");
                filename = JOptionPane.showInputDialog("Entrez le tag à rechercher.");
                writeTweets(false, filename);
                System.out.println("Tweets recovered and wrote " + filename + ".csv");
            }

            /* PHASE 3 (1) */

            ClearData cd =  new ClearData(filename);
            System.out.println("Clearing tweets...");
            cd.removeUselessWords();
            System.out.println("Done !");

            /* PHASE 1 */
            System.out.println("Getting recurrent patterns..");
            CSVToTrans csvtotrans = new CSVToTrans(filename);
            csvtotrans.writeTrans();
            System.out.println("Frequent itemsets wrote to " + filename + ".trans");

            int aprioriFreq = Integer.parseInt(JOptionPane.showInputDialog("Entrez la fréquence minimale de l'algorithme apriori."));

            runApriori(filename + ".trans", aprioriFreq);
            System.out.println("Done !");

            /* PHASE 2 */
            System.out.println("Starting rules extraction...");

            double minConf = Double.parseDouble(JOptionPane.showInputDialog("Entrez la confiance minimale pour les règles d'associations. (double)"));

            RulesExtractor extractor = new RulesExtractor(filename + ".trans.out", filename + ".rules.csv", minConf);
            extractor.extract();
            System.out.println("Done !");

            /* PHASE 3 (2) */
            System.out.println("Clearing rules...");
            cd.filterRules();
            System.out.println("Done !");

            /* PHASE 4 */
            //GUI.start(filename);
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
