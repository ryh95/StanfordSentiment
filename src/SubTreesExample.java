import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class SubTreesExample {

    private static void printSubTrees(Tree inputTree, String spacing, PrintWriter out) {
        if (inputTree.isLeaf()) {
            return;
        }
        ArrayList<Word> words = new ArrayList<Word>();
        for (Tree leaf : inputTree.getLeaves()) {
            words.addAll(leaf.yieldWords());
        }
//        attach labels
//        System.out.print(spacing+inputTree.label()+"\t");

        for (Word w : words) {
            out.print(w.word()+ " ");
        }
        out.println();
        for (Tree subTree : inputTree.children()) {
            printSubTrees(subTree, spacing + " ", out);
        }
    }

    public static void main(String[] args) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,parse");
        props.setProperty("parse.binaryTrees", "true");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String text = "Yet the act is still charming here.";

        Annotation annotation = new Annotation(text);

        pipeline.annotate(annotation);

        Tree sentenceTree = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0).get(
                TreeCoreAnnotations.BinarizedTreeAnnotation.class);

        System.out.println("Penn tree:");
        sentenceTree.pennPrint(System.out);
        System.out.println();
        System.out.println("Write phrases to output.txt");

//        Todo:remove duplicate phrases
//        write phrases to the output file
        try(FileWriter fw = new FileWriter("output.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            printSubTrees(sentenceTree, "",out);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            e.printStackTrace();
        }
        System.out.println("done");

    }
}