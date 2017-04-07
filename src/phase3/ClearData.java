package phase3;

import com.sun.xml.internal.bind.api.impl.NameConverter;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by h15008297 on 07/04/17.
 */
public class ClearData {
    private BufferedReader wordreader;
    private BufferedReader filereader;
    private Writer writer;
    private String filename;
    private ArrayList<String> uselessWords;
    private ArrayList<String> logs;
    private File tempfile;

    public ClearData(String filename) {
        try {
            this.filename = filename;
            uselessWords = new ArrayList<>();
            logs = new ArrayList<>();
            tempfile = new File("temp.csv");

            wordreader =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream("motsinutiles.txt"), StandardCharsets.UTF_8));
            String line;
            while ((line = wordreader.readLine()) != null) {
                uselessWords.add(';' + line + ';');
            }
            wordreader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void removeUselessWords(){
        try {
            filereader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filename+".csv"), StandardCharsets.UTF_8));

            writer = new OutputStreamWriter( // tempfile
                    new FileOutputStream(tempfile, false), StandardCharsets.UTF_8);

            String line;
            while ((line = filereader.readLine()) != null) {
                for (int i = 0 ; i < uselessWords.size() ; i++ ) {
                    String w = uselessWords.get(i);
                    if (line.contains(w)){
                        line = line.replace(w,";");
                    }
                }
                logs.add(line);
            }

            filereader.close();

            for (int j = 0 ; j < logs.size(); j++ ) {
                writer.write(logs.get(j) + "\n");
                writer.flush();
            }

            writer.close();

            tempfile.renameTo(new File(filename+".csv"));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void filterRules() {
        try {
            filereader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filename +".rules.csv"), StandardCharsets.UTF_8));

            tempfile = new File("temp.rules.csv");
            writer = new OutputStreamWriter(
                    new FileOutputStream(tempfile, false), StandardCharsets.UTF_8);

            String line;
            ArrayList<String> rules = new ArrayList<>();

            while ((line = filereader.readLine()) != null) {
                String[] temp = line.split(";");

                double  x = Double.parseDouble(temp[2]) / Double.parseDouble(temp[3]);

                if (x > 1) {
                    line = line +x+';';
                    writer.write(line + "\n");
                    writer.flush();
                }
            }
            tempfile.renameTo(new File(filename+".rules.csv"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
