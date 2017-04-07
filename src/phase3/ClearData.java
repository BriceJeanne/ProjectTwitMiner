package phase3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by h15008297 on 07/04/17.
 */
public class ClearData {
    private BufferedReader wordreader;
    private BufferedReader filereader;
    private BufferedWriter writer;
    private String filename;
    private List<String> uselessWords;
    private List<String> logs;

    public ClearData(String filename) {
        try {
            this.filename = filename;
            uselessWords = new ArrayList<>();
            logs = new ArrayList<>();

            wordreader =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream("motsinutiles.txt"), StandardCharsets.UTF_8));
            String line;
            while ((line = wordreader.readLine()) != null)
                uselessWords.add(';' + line + ';');

            wordreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void removeUselessWords(){
        try {
            filereader =
                    new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(filename + ".csv"), StandardCharsets.UTF_8));

            String line;
            while ((line = filereader.readLine()) != null) {
                line = line.toLowerCase();

                for (String word : uselessWords) {
                    if (line.contains(word))
                        line = line.replace(word,"");
                }

                logs.add(line);
            }

            filereader.close();

            writer = new BufferedWriter(
                        new OutputStreamWriter(
                            new FileOutputStream(filename + ".csv", false), StandardCharsets.UTF_8));

            for (String word : logs) {
                writer.write(word);
                writer.newLine();
                writer.flush();
            }

            writer.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void filterRules() {
        try {
            filereader =
                    new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(filename + ".rules.csv"), StandardCharsets.UTF_8));

            String line;
            ArrayList<String> rules = new ArrayList<>();

            while ((line = filereader.readLine()) != null) {
                String[] temp = line.split(";");

                double  x = Double.parseDouble(temp[2]) / Double.parseDouble(temp[3]);

                if (x > 1)
                    rules.add(line + x + ';');
            }

            writer = new BufferedWriter(
                        new OutputStreamWriter(
                            new FileOutputStream(filename + ".rules.csv", false), StandardCharsets.UTF_8));

            for (String rule : rules) {
                writer.write(rule);
                writer.newLine();
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
