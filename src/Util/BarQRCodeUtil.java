package Util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class BarQRCodeUtil {

    public static Image generateQRCodeImage(String data) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200);
        return createImageFromBitMatrix(bitMatrix);
    }

    public static Image generateBarcodeImage(String data) throws WriterException {
        Code128Writer writer = new Code128Writer();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.CODE_128, 200, 200);
        return createImageFromBitMatrix(bitMatrix);
    }

    private static Image createImageFromBitMatrix(BitMatrix bitMatrix) {
        try {
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            return SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(pngData)), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }
}
