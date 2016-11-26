import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.util.*;

public class parsePhrases {

    private static StanfordCoreNLP pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties");

    private static void getPhrases(Tree inputTree, SortedSet<String> words_set) {
        if (inputTree.isLeaf()) {
            return;
        }
        ArrayList<Word> words = new ArrayList<Word>();
        for (Tree leaf : inputTree.getLeaves()) {
            words.addAll(leaf.yieldWords());
        }
//        attach labels
//        System.out.print(spacing+inputTree.label()+"\t");
        String phrase = "" ;
        for (Word w : words) {
            phrase += w.word()+" ";
        }
        words_set.add(phrase);
        for (Tree subTree : inputTree.children()) {
            getPhrases(subTree, words_set);
        }
    }

    private static void parseAndSavePhrases(String text){
        Annotation annotation = new Annotation(text);

        pipeline.annotate(annotation);

        CoreMap sentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
        Tree sentenceTree = sentence.get(TreeCoreAnnotations.BinarizedTreeAnnotation.class);

        System.out.println("Write phrases to output.txt");

        SortedSet<String> phrases_set = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int len_o1 = o1.length();
                int len_o2 = o2.length();
                return (len_o1>len_o2 ? -1 : (len_o1 == len_o2 ? o1.compareTo(o2): 1));
            }
        });

        //save phrases of a sentence into phrases_set
        getPhrases(sentenceTree,phrases_set);

        //        write phrases to output file
        try(FileWriter fw = new FileWriter("data/phrases.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String phrase:phrases_set){
                out.println(phrase);
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner in = new Scanner(new File("data/sentences.txt"));

        while (in.hasNext()) { // iterates each line in the file
//            get a sentence
            String line = in.nextLine();
            // parse the sentence and save the phrases
            parseAndSavePhrases(line);
        }

        in.close(); // don't forget to close resource leaks


    }
}