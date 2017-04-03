package phase1.CSV;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by Brice on 03/04/2017.
 */
public class CSVWriter {

    private Writer writer;

    public CSVWriter(String filename, boolean append) {
        try {
            writer =
                new OutputStreamWriter(
                    new FileOutputStream(filename +".csv", append), StandardCharsets.UTF_8);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeLines(List<String> lines) {
        try {
            for (String line : lines) {
                writer.write(line + "\n");
                writer.flush();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
