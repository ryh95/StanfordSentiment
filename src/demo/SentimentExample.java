package demo;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
//import edu.stanford.nlp.pipeline.CoreNLPProtos;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

//import java.util.Properties;

/**
 * Created by ryh on 2016/11/4.
 */
public class SentimentExample {
    public static void main(String args[]){
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
//        for English
//        props.setProperty("annotators", "tokenize, ssplit,parse,sentiment");
//        for Chinese
        props.setProperty("annotators", "segment, ssplit,parse,sentiment");
        props.setProperty("customAnnotatorClass.segment","edu.stanford.nlp.pipeline.ChineseSegmenterAnnotator");
        props.setProperty("segment.model","edu/stanford/nlp/models/segmenter/chinese/pku.gz");
        props.setProperty("segment.sighanCorporaDict","edu/stanford/nlp/models/segmenter/chinese");
        props.setProperty("segment.serDictionary","edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz");
        props.setProperty("segment.sighanPostProcessing","true");
        props.setProperty("ssplit.boundaryTokenRegex","[.]|[!?]+|[\\u3002]|[\\uFF01\\uFF1F]+");
        props.setProperty("parse.model","edu/stanford/nlp/models/lexparser/chineseFactored.ser.gz");
        props.setProperty("parse.binaryTrees","true");
        props.setProperty("sentiment.model","C:\\Users\\ryh\\IdeaProjects\\StanfordSentiment\\model\\sentiment_Chinese.ser.gz");
//        props.setProperty("sentiment.model","C:\\Users\\ryh\\IdeaProjects\\StanfordSentiment\\model\\model.ser.gz");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable

//        String text = "这个股票要跌";// Add your text here!
        String text = "这是个好电影！";// Add your text here!

        // create an empty Annotation just with the given text
        Annotation annotation = new Annotation(text);

        // run all Annotators on this text

        pipeline.annotate(annotation);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

         int sentiment = 0;
        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            System.out.println(tree);
            // System.out.println(tree.yield());
            tree.pennPrint(System.out);
//            get sentiment
             Tree tree_sentiment = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
             sentiment = RNNCoreAnnotations.getPredictedClass(tree_sentiment);
        }

         System.out.print(sentiment);
    }
}
