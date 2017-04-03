package main;

import twitter4j.Status;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * Created by Brice on 31/03/2017.
 */

public class CSVWriter {

    private static final String FILE_HEADER = "Date;Utilisateur;Pays;Message";
    private static final char DELIMITER = ';';

    public static void write(List<Status> listTweets, String filename, boolean header) throws IOException {
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
