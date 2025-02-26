package opencv;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class FreeTTSDemo {
    public static void main(String[] args) {
        // Set system properties for FreeTTS
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        // Get the voice instance
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");

        if (voice != null) {
            voice.allocate();
            voice.speak("Bonjour Mariam! Ceci est un test de FreeTTS.");
            voice.deallocate();
        } else {
            System.out.println("Erreur : Voix introuvable.");
        }
    }
}
