import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by ryh on 2016/11/20.
 */
public class phrasesToDict {

    public static void main(String args[]) throws FileNotFoundException {


        Scanner in = new Scanner(new File("data/phrases.txt"));

//
        Set<String> phrases = new HashSet<>();

        while (in.hasNext()) { // iterates each line in the file
//            get a sentence
            String line = in.nextLine();
            // parse the sentence and save the phrases
            phrases.add(line);
        }

        in.close(); // don't forget to close resource leaks

        //        write phrases to output file
        try(FileWriter fw = new FileWriter("data/dict.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String phrase:phrases){
                out.println(phrase);
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            e.printStackTrace();
        }
    }

}
