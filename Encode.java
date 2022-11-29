package correcter;

import java.util.ArrayList;

/*
 *  This class
 *      - convert received error induced byte formatted input to binary
 *      - convert binary to hamming code for one bit error correction.
 *      - show encoded data
 *      - save encoded data to received.txt file.
 */
public class Encode {
    protected static ArrayList<Byte> encode(byte[] byteInput) {
        String binaryData = convertToBinary(byteInput);

        String encodedData = convertToHammingCode(binaryData);
        printData(byteInput, binaryData, encodedData);

        ArrayList<Byte> list = new ArrayList<>();
        for(String bytes : encodedData.split(" ")) {
            Integer value = Integer.parseInt(bytes, 2);
            list.add(value.byteValue());
        }
        return list;
    }

    private static void printData(byte[] byteInput, String binaryData, String encodedData) {
        System.out.println();
        System.out.println(Main.inputFileName + ":");

        System.out.println("text view: " + convertToText(byteInput));
        System.out.println("hex view: " + convertToHex(binaryData));

        System.out.println("bin view: " + binaryData);
        System.out.println();
        System.out.println(Main.encodeFileName + ":");
        System.out.println("expand: " + dataWithoutParity(encodedData));

        System.out.println("parity: " + encodedData);

        System.out.println("hex view: " + convertToHex(encodedData));

    }

    private static String convertToText(byte[] byteInput) {
        return new String(byteInput);
    }

    private static String dataWithoutParity(String encodedData) {
        StringBuilder dataWithoutParity = new StringBuilder();
        for (String str : encodedData.split(" ")) {
            for (int i = 0; i < str.length(); i++) {
                if (i == 0 || i == 1 || i == 3) {
                    dataWithoutParity.append(".");
                } else {
                    dataWithoutParity.append(str.charAt(i));
                }
            }
            dataWithoutParity.append(" ");
        }
        return dataWithoutParity.toString();
    }

    private static String convertToBinary(byte[] input) {
        StringBuilder binaryData = new StringBuilder();
        for (byte b : input) {
            binaryData.append(String.format("%8s", Integer.toBinaryString(b)).replace(" ", "0"));
            binaryData.append(" ");
        }

        return binaryData.toString();
    }

    private static String convertToHex(String binaryData) {
        StringBuilder hexData = new StringBuilder();

        for (String str : binaryData.split(" ")) {
            int decimal = Integer.parseInt(str,2);
            String hexStr = Integer.toString(decimal,16);
            if (hexStr.length() < 2) {
                hexStr = hexStr.concat("0".repeat(2 - hexStr.length()));
            }
            hexData.append(hexStr.concat(" "));
        }
        return hexData.toString();
    }

    private static String convertToHammingCode(String inputData) {
        StringBuilder hammingCode = new StringBuilder();

        inputData = inputData.replaceAll(" ", "");
        int substringLength = 4;
        int startIndex = 0;
        int endIndex = startIndex + substringLength;

        while (startIndex < inputData.length()) {
            if (endIndex > inputData.length()) {
                endIndex = inputData.length();
            }

            String subString = inputData.substring(startIndex, endIndex);

            String parity = ".";
            String[] tempByte = new String[8];
            int index = 0;
            for (int i = 0; i < tempByte.length; i++) {
                if (i == 2 || i == 4 || i == 5 || i == 6) {
                    //append substring
                    tempByte[i] = subString.substring(index, index + 1);
                    index++;
                } else if (i == 0 || i == 1 || i == 3) {
                    tempByte[i] = parity;
                } else {
                    tempByte[i] = "0";
                }
            }
            calculateParity(tempByte);

            for (String bit : tempByte) {
                hammingCode.append(bit);
            }
            hammingCode.append(" ");

            startIndex += substringLength;
            endIndex = startIndex + substringLength;

        }
        return hammingCode.toString();
    }

    private static void calculateParity(String[] tempByte) {
        tempByte[0] = Integer.toBinaryString(
                (Integer.parseInt(tempByte[2], 2) + Integer.parseInt(tempByte[4], 2) + Integer.parseInt(tempByte[6], 2)) % 2);
        tempByte[1] = Integer.toBinaryString(
                (Integer.parseInt(tempByte[2], 2) + Integer.parseInt(tempByte[5], 2) + Integer.parseInt(tempByte[6], 2)) % 2);
        tempByte[3] = Integer.toBinaryString(
                (Integer.parseInt(tempByte[4], 2) + Integer.parseInt(tempByte[5], 2) + Integer.parseInt(tempByte[6], 2)) % 2);
    }

}
