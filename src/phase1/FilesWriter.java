package phase1;

import twitter4j.Status;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Brice on 31/03/2017.
 */

public class FilesWriter {

    private static final String FILE_HEADER = "Date;Utilisateur;Pays;Message";
    private static final char DELIMITER = ';';

    private static int getIndex(List<String> dictionnary, String word) {
        int index = 0;
        boolean found = false;

        for (int i = 0; i < dictionnary.size(); ++i) {
            if (dictionnary.get(i).equals(word)) {
                index = i;
                found = true;
            }
        }

        if (!found) {
            dictionnary.add(word);
            index = dictionnary.size() - 1;
        }

        return index;
    }

    public static void writeTrans(String filename) throws IOException {

        List<String> dictionnary = new ArrayList<>();

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(filename + ".csv"), StandardCharsets.UTF_8));

        Writer writer =
                new OutputStreamWriter(
                        new FileOutputStream(filename + ".trans", false), StandardCharsets.UTF_8);

        for(String line; (line = reader.readLine()) != null; ) {

            String lineToWrite = "";
            int index;
            List<String> words = new ArrayList<>(Arrays.asList((line.split(DELIMITER + ""))));

            for (String word : words) {
                index = FilesWriter.getIndex(dictionnary, word);
                lineToWrite += index + " ";
            }

            writer.write(lineToWrite + "\n");
            writer.flush();
        }

        writer.close();
        reader.close();
    }

    public static void writeCSV(List<Status> listTweets, String filename, boolean header) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename +".csv", true);
        Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);

        if (header) writer.append(FILE_HEADER);

        for (Status tweet : listTweets) {

            String username = '@' + tweet.getUser().getScreenName();
            Date date = tweet.getCreatedAt();
            String location = tweet.getUser().getLocation();

            String message = tweet.getText();
            message = message.replace(',', DELIMITER);
            message = message.replace(' ', DELIMITER);
            message = message.replace('\n', DELIMITER);
            message = message.replace('"', DELIMITER);

            writer.append('\n');
            writer.append(String.valueOf(date));
            writer.append(DELIMITER);
            writer.append(username);
            writer.append(DELIMITER);
            writer.append(location);
            writer.append(DELIMITER);
            writer.append(message);
            writer.append(DELIMITER);
            writer.flush();
        }

        writer.close();
    }
}
