package imgpro_GLCM;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.trishiaanne.skincheckr.Camera;

/**
 *
 * @author Kirsten A. Malcaba
 */
public class Main extends AppCompatActivity{
    int days;
    int itch;
    int scale;
    int burn;
    int sweat;
    int crust;
    int bleed;
    int disease;
    String path="";


    private void displayMessage(Context context, String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        path = i.getStringExtra("path_value");
        displayMessage(getBaseContext(),"PATH: " + path);
        Log.i("PATH: ",path);

    }
    /*
    try {
        String path_value = Intent.getIntentOld("")
        //img pro
        //median filter - noise reduction
        BufferedImage med = MedianFilter.filter(img);
        //otsu's method thresholding
        Otsu o = new Otsu(med, img); //if mask
//      Otsu o = new Otsu(med); //if binary img only
        int threshold = o.getThreshold();
        System.out.println("Threshold: " + threshold);
        BufferedImage thresh = o.applyThreshold();
        BufferedImage dilate = o.dilateImage(thresh);BufferedImage mask = o.applyMask(dilate); //replace white with orig img

        //show pic
//      Picture pic = new Picture(f);
//      pic.show();
//      Picture after = new Picture(med);
//      after.show();
//      Picture dilated = new Picture(dilate);
//      dilated.show();
        Picture masked = new Picture(mask);
//      masked.show();
        masked.save("test/t/" + f.getName());

            //GLCM
            FeatureExtraction fe = new FeatureExtraction(mask, 8);
            fe.extract();

            System.out.println("Contrast: " + fe.getContrast());
            System.out.println("Correlation: " + fe.getCorrelation());
            System.out.println("Energy: " + fe.getEnergy());
            System.out.println("Entropy: " + fe.getEntropy());
            System.out.println("Homogeneity: " + fe.getHomogeneity());
            System.out.println("Mean: " + fe.getMean());
            System.out.println("Variance: " + fe.getVariance());


            Scanner sc = new Scanner(System.in);
            System.out.print("No. of days? ");
            days = (int)(Math.random()*((100-1)+1)) + 1; //generate random number
            //sc.nextLine();
            System.out.print("Itchiness (1-10): ");
            itch = (int)(Math.random()*((10-5)+1)) + 5;
            //sc.nextLine();
            System.out.print("Scaling/Peeling (1-10): ");
            scale = (int)(Math.random()*((5-2)+1)) + 2;
            //sc.nextLine()
            System.out.print("Burning sensation (1-10): ");
            burn = (int)(Math.random()*((10-7)+1)) + 7;
            //sc.nextLine();
            System.out.print("Sweating (1-10): ");
            sweat = (int)(Math.random()*((10-7)+1)) + 7;
            //sc.nextLine();
            System.out.print("Crusting (1-10): ");
            crust = (int)(Math.random()*((8-5)+1)) + 5;
            //sc.nextLine();
            System.out.print("Bleeding (0/1): ");
            bleed = 0;
            //sc.nextLine();
            System.out.println("Disease:\n"
                    + "0 = Atopic Dermatitis\n"
                    + "1 = Contact Dermatitis\n"
                    + "2 = Dyshidrotic Eczema\n"
                    + "3 = Intertrigo\n"
                    + "4 = Melanoma\n"
                    + "5 = Pityriasis Versicolor\n"
                    + "6 = Psoriasis\n"
                    + "7 = Tinea Corporis\n"
                    + "8 = Tinea Pedis\n"
                    + "9 = Benign Mole\n"
                    + "10 = No Disease/Lesion");
            disease = 3;
            //sc.nextLine();
            
            String [] record = {Double.toString(fe.getContrast()), Double.toString(fe.getCorrelation()), 
                Double.toString(fe.getEnergy()), Double.toString(fe.getEntropy()), 
                Double.toString(fe.getHomogeneity()), Double.toString(fe.getMean()), 
                Double.toString(fe.getVariance()), Integer.toString(days), 
                Integer.toString(itch), Integer.toString(scale), Integer.toString(burn), 
                Integer.toString(sweat), Integer.toString(crust), Integer.toString(bleed), 
                Integer.toString(disease)
            };
            
             writeFile(record); //write row to csv
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }*/
}
