package MyMeds.App;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;


public class QRcode {

    public static void genereteQRForRecipe(Integer recipeID) throws WriterException, IOException {
        String save = "C:\\Users\\marco\\Projects\\MyMeds\\src\\main\\resources\\RecipesQrs\\" +
                    String.valueOf(recipeID);
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(String.valueOf(recipeID),
                                        BarcodeFormat.QR_CODE, 400, 400);
        MatrixToImageWriter.writeToPath(matrix, "jpg", Paths.get(save));
    }

}

