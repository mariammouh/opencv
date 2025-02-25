package opencv;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;

import java.util.ArrayList;
import java.util.List;

public class GestureTranslator {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // Fonction de détection des gestes et traduction en texte
    public static String detectGesture(Mat frame) {
        Mat gray = new Mat();
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(gray, gray, 60, 255, Imgproc.THRESH_BINARY);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(gray, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > 5000) {
                int fingers = countFingers(contour);
                switch (fingers) {
                    case 0:
                        return "Merci";
                    case 1:
                        return "Oui";
                    case 2:
                        return "Bonjour";
                    case 5:
                        return "Au revoir";
                    default:
                        return "Geste inconnu";
                }
            }
        }

        return null;
    }

    // Compter le nombre de doigts visibles
    public static int countFingers(MatOfPoint contour) {
        return (int) (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) / 100); // Approximatif
    }

    public static void main(String[] args) {
        VideoCapture capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Erreur : Impossible d’ouvrir la webcam !");
            return;
        }

        Mat frame = new Mat();
        while (true) {
            capture.read(frame);
            if (frame.empty()) {
                continue;
            }

            String gestureText = detectGesture(frame);
            if (gestureText != null) {
                System.out.println("Traduction : " + gestureText);
            }

            HighGui.imshow("Assistant Virtuel - Détection en Temps Réel", frame);
            if (HighGui.waitKey(30) == 'q') {
                break;
            }
        }

        capture.release();
        HighGui.destroyAllWindows();
    }
}
