package Util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class QRCodeGenerator {

    public static Image generateQRCodeImage(String text, UUID uuid, int width, int height) throws IOException {
        byte[] qrCodeByteArray = generateQRCodeByteArray(text, uuid, width, height);
        return new Image(new ByteArrayInputStream(qrCodeByteArray));
    }

    public static Image generateBarcodeImage(String text, UUID uuid, int width, int height) throws IOException {
        System.out.println("UUID used for generating barcode image: " + uuid); // Print UUID
        byte[] barcodeByteArray = generateBarcodeByteArray(text, uuid, width, height);
        return new Image(new ByteArrayInputStream(barcodeByteArray));
    }

    private static byte[] generateQRCodeByteArray(String text, UUID uuid, int width, int height) throws IOException {
        String qrData = generateQRData(text, uuid);
        return generateCodeByteArray(qrData, width, height, BarcodeFormat.QR_CODE);
    }

    private static byte[] generateBarcodeByteArray(String text, UUID uuid, int width, int height) throws IOException {
        String barcodeData = generateBarcodeData(text, uuid);
        return generateCodeByteArray(barcodeData, width, height, BarcodeFormat.CODE_128);
    }

    private static byte[] generateCodeByteArray(String data, int width, int height, BarcodeFormat format) throws IOException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(data, format, width, height);
        } catch (WriterException e) {
            throw new IOException("Error generating code matrix", e);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static String generateQRData(String data, UUID uuid) {
        // Combine data with UUID for the QR code
        return uuid + "_" + data;
    }

    private static String generateBarcodeData(String data, UUID uuid) {
        // Combine data with UUID for the barcode
        String latinData = data.replaceAll("[^\\x00-\\x7F]", ""); // Filter out non-Latin characters
        return uuid + "_" + latinData;
    }
}
