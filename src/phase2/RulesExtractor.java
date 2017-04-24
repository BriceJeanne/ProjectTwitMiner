package phase2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brice on 05/04/2017.
 */

public class RulesExtractor {

    private BufferedReader reader;
    private BufferedWriter writer;

    private List<String> dictionary;
    private Double minConf;

    public RulesExtractor(String aprioriFile, String outFile, double minConf) {
        try {
            reader = new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(aprioriFile)));

            writer = new BufferedWriter(
                        new OutputStreamWriter(
                            new FileOutputStream(outFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        dictionary = new ArrayList<>();
        getDictionary();
        this.minConf = minConf;
    }

    private void getDictionary() {
        try {
            BufferedReader reader =
                    new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream("dictionary.txt"), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void extract() throws IOException {
        /*
            On crée une liste contenant toutes les lignes (motifs)
         */
        String line;
        List<String> aprioriLines = new ArrayList<>();
        while ( (line = reader.readLine()) != null) {
            aprioriLines.add(line);
        }

        double globalFreq = getGlobalFreq(aprioriLines);

        /*
            Recherche de la position du premier motif fréquent avec attributs > 1
         */
        int firstItem = 0;

        for (int i = 0; i < aprioriLines.size(); ++i) {
            String[] attributes = aprioriLines.get(i).split(" ");

            if (attributes.length > 2) {
                firstItem = i;
                break;
            }
        }

        /*
            On parcours les lignes depuis la position du premier itemset pertinent
            puis on traite chaque ligne
         */
        for (int i = firstItem; i < aprioriLines.size(); ++i) {
            String item = aprioriLines.get(i);

            List<String> itemset = fetchSub(item);
            double freq = getFrequency(item) / globalFreq;

            /*
                On parcours les lignes jusqu'à la position courante pour trouver un sous ensemble appartenant à itemset
             */
            for (int j = 0; j < i; ++j) {
                String subitem = aprioriLines.get(j);
                List<String> subitemset = fetchSub(subitem);

                if (itemset.containsAll(subitemset)) {
                    double subFreq = getFrequency(subitem) / globalFreq;
                    double confiance = (subFreq / freq);

                    if (confiance >= minConf) {
                        /*
                            On enregistre la règle trouvée
                         */
                        itemset.removeAll(subitemset);
                        if (!itemset.isEmpty() && !subitemset.isEmpty()) {
                            System.out.println("Règle d'association trouvée.");
                            writeRule(itemset, subitemset, confiance, freq);
                        }
                    }
                }
            }
        }
    }

    private void writeRule(List<String> itemset, List<String> subitemset, double conf, double freq) throws IOException {
        String rule = "{";

        for (String item : subitemset) {
            String name = dictionary.get(Integer.parseInt(item));
            rule += name + ",";
        }
        rule = rule.substring(0, rule.length() - 1); // on retire la dernière virgule

        rule += "};{";

        for (String item : itemset) {
            String name = dictionary.get(Integer.parseInt(item));
            rule += name + ",";
        }
        rule = rule.substring(0, rule.length() - 1); // on retire la dernière virgule

        rule += "};" + conf + ';' + freq + ';';

        try {
            writer.write(rule);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double getFrequency(String set) {
        List<String> subSets = new ArrayList<>(Arrays.asList(set.split(" ")));
        String strFreq = subSets.get(subSets.size() - 1);
        strFreq = strFreq.substring(1, strFreq.length() - 1);

        return Double.parseDouble(strFreq);
    }

    private double getGlobalFreq(List<String> aprioriLines) {
        double freq = 0;

        for (String line : aprioriLines)
            freq += getFrequency(line);

        return freq;
    }

    private List<String> fetchSub(String set) {
        List<String> subSets = new ArrayList<>(Arrays.asList(set.split(" ")));
        subSets.remove(subSets.size() - 1);

        return subSets;
    }

    /* TEST FUNCTION */
    public static void main(String[] args) {
        RulesExtractor extractor = new RulesExtractor("test2.trans.out", "rules.txt", 0.9);

        try {
            extractor.extract();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
