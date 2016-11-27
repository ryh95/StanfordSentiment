import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by ryh on 2016/11/27.
 */
public class BuildTrainData {
    public static void main(String args[]) throws FileNotFoundException {

        Map dict = init();

        Scanner in = new Scanner(new File("data/sentences.txt"));

        SortedSet<String> phrases_set = null;

        while (in.hasNext()) { // iterates each line in the file
//            get a sentence
            String line = in.nextLine();
            // parse the sentence and save the phrases to a set
            phrases_set = parsePhrases.parseAndSavePhrases(line);

            String labeled_phrases = "" ;
            for (String phrase : phrases_set){
//                if the phrase has a blank at final position
//                remove it
                if (phrase.charAt(phrase.length()-1)==' '){
                    phrase = phrase.substring(0, phrase.length()-1);
                }
                String value = (String)dict.get(phrase);
                labeled_phrases += value+" "+phrase+"\r\n";
            }

            BuildBinarizedDataset.buildTrainData(labeled_phrases);

        }
    }

    private static Map init() throws FileNotFoundException {
        //      read dict into set
        Scanner in = new Scanner(new File("data/dict.txt"));
        Map<String,String> dict = new HashMap<>();

        while (in.hasNext()) { // iterates each line in the file
//            get a sentence
            String line = in.nextLine();
            // parse the sentence and save the phrases
            String value = line.substring(0,1);
            String key = line.substring(2,line.length());
            dict.put(key,value);
        }
        System.out.println("dict has been loaded");
        return dict;
    }
}
