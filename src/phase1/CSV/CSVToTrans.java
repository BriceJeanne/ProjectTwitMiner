package phase1.CSV;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brice on 03/04/2017.
 */
public class CSVToTrans {

    private CSVReader reader;
    private Writer writer;

    private List<String> dictionnary;

    public CSVToTrans(String filename) {
        reader = new CSVReader(filename + ".csv");

        try {
            writer =
                new OutputStreamWriter(
                    new FileOutputStream(filename + ".trans"), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        dictionnary = new ArrayList<>();
    }

    private int getWordIndex(String word) {
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

    public void writeTrans() {
        List<String> linesList = reader.getLines();

        for (String line : linesList) {
            StringBuilder lineBuilder = new StringBuilder();
            int index;

            List<String> words = new ArrayList<>(Arrays.asList((line.split(";"))));
            for (String word : words) {
                index = getWordIndex(word);
                lineBuilder.append(index + " ");
            }

            lineBuilder.append('\n');

            try {
                writer.write(lineBuilder.toString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
