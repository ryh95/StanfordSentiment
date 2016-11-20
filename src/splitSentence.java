import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.util.*;

/**
 * Created by ryh on 2016/11/20.
 */
public class splitSentence {

    private static StanfordCoreNLP pipeline = new StanfordCoreNLP("sentence-split.properties");

    public static void main(String[] args) throws FileNotFoundException {

        Scanner in = new Scanner(new File("input.txt"));

        while (in.hasNext()) {
//            get a piece of news
            String line = in.nextLine();
            // split the news sentences
            splitSentences(line);
        }

        in.close(); // don't forget to close resource leaks

    }

    private static void splitSentences(String text){
        Annotation annotation = new Annotation(text);
        //annotate a piece of news
        pipeline.annotate(annotation);

        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence: sentences){
//            save all the sentence into sentences.txt file

            try(FileWriter fw = new FileWriter("sentences.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {

                out.println(sentence);

            } catch (IOException e) {
                //exception handling left as an exercise for the reader
                e.printStackTrace();
            }

//            System.out.println(sentence);
        }
    }

}
