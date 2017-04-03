package phase1.CSV;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brice on 03/04/2017.
 */
public class CSVReader {

    private BufferedReader reader;

    public CSVReader(String filepath) {
        try {
            reader =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(filepath), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> getLines() {
        List<String> linesList = new ArrayList<>();

        try {
            for (String line; (line = reader.readLine()) != null; )
                linesList.add(line);
        } catch(IOException e) {
            e.printStackTrace();
        }

        return linesList;
    }
}
