package MyMeds.App;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class QRcode {

    public static void generateQRForRecipe(Integer recipeID) throws WriterException, IOException {
        String projectDir = System.getProperty("user.dir");
        String qrDir = projectDir + "/src/main/resources/RecipesQrs/";
        String filename = qrDir + recipeID.toString();

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(recipeID.toString(), BarcodeFormat.QR_CODE, 400, 400);
        Path qrPath = Paths.get(filename);
        MatrixToImageWriter.writeToPath(matrix, "jpg", qrPath);
    }
}

