package edu.cmu.sphinx.demo;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.util.TimeFrame;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Listens and translates to text
 * using the highest available decoding settings
 *
 */
public class VoiceToText {

    public VoiceToText() throws IOException {
        Configuration configuration = new Configuration();
        configuration.setSampleRate(441000);

// Set path to acoustic model.
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
// Set path to dictionary.
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
// Set language model.

        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        //LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        recognizer.startRecognition(new FileInputStream("/tmp/t.wav"), new TimeFrame(201000));


        System.out.println("START");

        //recognizer.startRecognition(true);

        //while (true) {
            SpeechResult result = recognizer.getResult();
            //while (( result = recognizer.getResult() ) != null) {
                System.out.println(result.getWords());
                System.out.println(result.getHypothesis());
                System.out.println();
            //}

            recognizer.stopRecognition();
            // Pause recognition process. It can be resumed then with startRecognition(false).
            //recognizer.startRecognition(false);
        //}

    }

    public static void main(String[] args) throws IOException {
        new VoiceToText();
    }

}
