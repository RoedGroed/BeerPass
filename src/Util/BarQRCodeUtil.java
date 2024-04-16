package Util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BarQRCodeUtil {
    public static Image generateQRCode(String data, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);
        return convertMatrixToImage(matrix);
    }

    public static Image generateBarcode(String data, int width, int height) throws WriterException {
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, width, height);
        return convertMatrixToImage(matrix);
    }

    private static Image convertMatrixToImage(BitMatrix matrix) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            return new Image(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert matrix to image", e);
        }
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}