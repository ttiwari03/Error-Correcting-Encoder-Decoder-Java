package correcter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
 *  This program
 *      - encode text in send.txt file,
 *      - introduce one bit error,
 *      - send error induced encoded text
 *      - remove error and decode text
 *      - final decoded text is saved in decoded.txt file.
 */
public class Main {
    public static final Scanner readIp = new Scanner(System.in);
    public static final String inputFileName = "send.txt";
    public static final String encodeFileName = "encoded.txt";
    public static final String decodeFileName = "decoded.txt";
    public static final String receivedFileName = "received.txt";
    public static ArrayList<Byte> byteOutput = new ArrayList<>();
    public static byte[] byteInput = new byte[0];


    public static void main(String[] args) {
        System.out.println("Write a mode: (encode/send/decode)");
        String operationMode = readIp.nextLine();

        switch (operationMode) {
            case "encode" -> {
                readDataFromFile(inputFileName);
                byteOutput = Encode.encode(byteInput);
                writeDataToFile(encodeFileName);
            }
            case "send" -> {
                readDataFromFile(encodeFileName);
                byteOutput = Send.send(byteInput);
                writeDataToFile(receivedFileName);
            }
            case "decode" -> {
                readDataFromFile(receivedFileName);
                byteOutput = Decode.decode(byteInput);
                writeDataToFile(decodeFileName);
            }
        }


    }

    private static void writeDataToFile(String fileName) {
        byte[] output = new byte[byteOutput.size()];

        for (int i= 0;  i < byteOutput.size(); i++) {
            output[i] = byteOutput.get(i);
        }

        File file = new File(fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            fileOutputStream.write(output);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private static void readDataFromFile(String fileName) {
        File file = new File(fileName);

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byteInput = fileInputStream.readAllBytes();
        } catch (Exception e) {
            System.out.println("File not found.");
        }
    }

}

