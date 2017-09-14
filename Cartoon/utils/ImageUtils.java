package Cartoon.utils;

import javafx.scene.image.Image;


import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;

import java.util.Base64.Decoder;

/**
 * Image loading utilities to allow encoding of images in text format.
 * */
public class ImageUtils {
    public static String encodeImage(Image img) {
        Encoder enc = Base64.getEncoder();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] data = new byte[0];
        try {
            ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(img, null), "jpg", b);
            data = b.toByteArray();
            b.close();
        } catch (IOException e) {
            // This never happens (hopefully), since we're using a ByteArrayOutputStream
            e.printStackTrace();
        }
        return enc.encodeToString(data);
    }
    public static String encodeImage(File f) throws IOException {
        byte[] data = Files.readAllBytes(f.toPath());
        Encoder enc = Base64.getEncoder();
        return enc.encodeToString(data);
    }
    public static void encodeImageToFile(File source, File destination) throws IOException {
        BufferedWriter pw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(destination)));
        pw.write(ImageUtils.encodeImage(source));
        pw.close();
    }
    public static void encodeImageToFile(String source, String destination) throws IOException {
        ImageUtils.encodeImageToFile(new File(source), new File(destination));
    }
    public static Image decodeImage(String imageString) {
        byte[] data = Base64.getDecoder().decode(imageString);
        ByteArrayInputStream b = new ByteArrayInputStream(data);
        try {
            return javafx.embed.swing.SwingFXUtils.toFXImage(ImageIO.read(b), null);
        } catch (IOException e) {
            // Never happens because we're reading from a ByteArrayInputStream
            e.printStackTrace();
        }
        return null;
    }
}
