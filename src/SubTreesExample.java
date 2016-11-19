import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.*;

import java.io.*;
import java.util.*;

public class SubTreesExample {

    private static void printSubTrees(Tree inputTree,SortedSet<String> words_set) {
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
            printSubTrees(subTree, words_set);
        }
    }

    public static void main(String[] args) {

//        English version properties
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize,ssplit,parse");
//        props.setProperty("parse.binaryTrees", "true");

//        change the parse model
//        props.setProperty("parse.model", "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");

//        Chinese version properties
        StanfordCoreNLP pipeline = new StanfordCoreNLP("CoreNLP-chinese.properties");

//        Chinese version text
//        if properties is English version use English text
        String text = "这部电影有很好的评论。";

        Annotation annotation = new Annotation(text);

        pipeline.annotate(annotation);

        Tree sentenceTree = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0).get(
                TreeCoreAnnotations.BinarizedTreeAnnotation.class);

        System.out.println("Penn tree:");
        sentenceTree.pennPrint(System.out);
        System.out.println();
        System.out.println("Write phrases to output.txt");

        SortedSet<String> words_set = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int len_o1 = o1.length();
                int len_o2 = o2.length();
                return (len_o1>len_o2 ? -1 : (len_o1 == len_o2 ? o1.compareTo(o2): 1));
            }
        });
//        Set<String> words_set = new HashSet<>();

        printSubTrees(sentenceTree,words_set);

//        write phrases to output file
        try(FileWriter fw = new FileWriter("output.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String word:words_set){
                out.println(word);
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            e.printStackTrace();
        }
    }
}