package phase2;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brice on 05/04/2017.
 */

public class RulesExtractor {

    private BufferedReader reader;
    private BufferedWriter writer;

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

        this.minConf = minConf;
    }

    public void extract() throws IOException {
        /*
            On crée une liste contenant toutes les lignes (motifs)
         */
        String line;
        List<String> listLines = new ArrayList<>();
        while ( (line = reader.readLine()) != null) {
            listLines.add(line);
        }

        /*
            Recherche de la position du premier motif fréquent avec attributs > 1
         */
        int firstItem = 0;

        for (int i = 0; i < listLines.size(); ++i) {
            String[] attributes = listLines.get(i).split(" ");

            if (attributes.length > 2) {
                firstItem = i;
                break;
            }
        }

        /*
            On parcours les lignes depuis la position du premier itemset pertinent
            puis on traite chaque ligne
         */
        for (int i = firstItem; i < listLines.size(); ++i) {
            String item = listLines.get(i);

            List<String> itemset = fetchSub(item);
            double freq = getFrequency(item);

            /*
                On parcours les lignes jusqu'à la position courante pour trouver un sous ensemble appartenant à itemset
             */
            for (int j = 0; j < i; ++j) {
                String subitem = listLines.get(j);
                List<String> subitemset = fetchSub(subitem);

                if (itemset.containsAll(subitemset)) {
                    double subFreq = getFrequency(subitem);
                    double confiance = (freq / subFreq);

                    if (confiance >= minConf) {
                        /*
                            On enregistre la règle trouvée
                         */
                        System.out.println("Règle d'association trouvée.");
                        itemset.removeAll(subitemset);
                        writeRule(itemset, subitemset, confiance, freq);
                    }
                }
            }
        }
    }

    private void writeRule(List<String> itemset, List<String> subitemset, double conf, double freq) throws IOException {
        String rule = "{";

        for (String item : subitemset)
            rule += item + ",";
        rule = rule.substring(0, rule.length() - 1); // on retire la dernière virgule

        rule += "} -> {";

        for (String item : itemset)
            rule += item + ",";
        rule = rule.substring(0, rule.length() - 1); // on retire la dernière virgule

        rule += "} : Conf = " + conf + ", Freq = " + freq;

        try {
            writer.write(rule);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getFrequency(String set) {
        List<String> subSets = new ArrayList<>(Arrays.asList(set.split(" ")));
        String strFreq = subSets.get(subSets.size() - 1);
        strFreq = strFreq.substring(1, strFreq.length() - 1);

        return Integer.parseInt(strFreq);
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
