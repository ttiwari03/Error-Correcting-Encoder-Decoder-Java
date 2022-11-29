package correcter;

import java.util.ArrayList;

/*
 *  This class
 *      - receive error induced data in byte format
 *      - remove error from data
 *      - convert error free data to text
 *      - show converted data
 *      - save converted data to decoded.txt file.
 */
public class Decode {

    public static ArrayList<Byte> decode(byte[] byteInput) {
        String[] inputArray = convertByteToBinary(byteInput);
        String[] correctCode = removeError(inputArray);
        String decodedData = decodeCode(correctCode);
        String hexCode = convertToHex(decodedData);
        String actualData = convertToText(hexCode);

        printData(inputArray, correctCode, decodedData, hexCode, actualData);

        ArrayList<Byte> list = new ArrayList<>();

        for(String bytes : decodedData.split(" ")) {
            Integer value = Integer.parseInt(bytes, 2);
            list.add(value.byteValue());
        }
        return list;

    }

    private static void printData(String[] inputArray, String[] correctCode, String decodedData, String hexCode, String actualData) {


        String binaryInput = convertToBinary(inputArray);
        String hexInput = convertToHex(binaryInput);

        System.out.println(Main.receivedFileName + ":");
        System.out.println("hex view: " + hexInput);
        System.out.println("bin view: " + binaryInput);
        System.out.println();

        System.out.println(Main.decodeFileName + ":");
        System.out.print("correct: ");
        for (String str : correctCode) {
            System.out.print(str + " ");
        }
        System.out.println();
        System.out.println("decode: " + decodedData);
        System.out.println("hax view: " + hexCode);
        System.out.println("text view: " + actualData);
    }

    private static String[] convertByteToBinary(byte[] byteInput) {

        String[] binaryInput = new String[byteInput.length];

        for (int i = 0; i < byteInput.length; i++) {
            binaryInput[i] = Integer.toBinaryString((byteInput[i] & 0xFF) + 0x100).substring(1);
        }

        return binaryInput;
    }

    private static String convertToText(String hexCode) {
        StringBuilder data = new StringBuilder();

        for (String str : hexCode.split(" ")) {
            data.append((char) Integer.parseInt(str, 16));
        }
        return data.toString();
    }

    private static String decodeCode(String[] correctCode) {
        StringBuilder code = new StringBuilder();

        int bitCount = 0;
        for (String str : correctCode) {
            String subString = str.substring(0, 7);

            for (int i = 0; i < subString.length(); i++) {
                if (i == 2 || i == 4 || i == 5 || i == 6) {
                    code.append(subString.charAt(i));
                    bitCount++;
                }

                if (bitCount == 8) {
                    code.append(" ");
                    bitCount = 0;
                }
            }
        }
        return code.toString();
    }

    private static String convertToBinary(String[] input) {
        StringBuilder binaryData = new StringBuilder();
        for (String b : input) {
            binaryData.append(String.format("%8s", b).replace(" ", "0").concat(" "));
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

    private static String[] removeError(String[] inputArray) {

        StringBuilder correctHammingCode = new StringBuilder();

        for (String str : inputArray) {
            StringBuilder wrongParityPair = new StringBuilder();

            for (int i = 0; i < str.length(); i++) {
                if (i == 0 || i == 1 || i == 3) {
                    if (!isParityCorrect(str, i)) {
                        wrongParityPair.append(i);
                    }
                }
            }

            int errorBitIndex = -1;
            switch (wrongParityPair.toString()) {
                case "01" -> errorBitIndex = 2;
                case "03" -> errorBitIndex = 4;
                case "13" -> errorBitIndex = 5;
                case "013" -> errorBitIndex = 6;
            }

            if (errorBitIndex != -1) {
                String ch = str.substring(errorBitIndex, errorBitIndex + 1);
                ch = ch.equals("0") ? "1" : "0";
                correctHammingCode.append(str.substring(0, errorBitIndex).concat(ch).concat(str.substring(errorBitIndex + 1)).concat(" "));

            } else {
                correctHammingCode.append(str.concat(" "));
            }

        }
        return correctHammingCode.toString().split(" ");
    }

    private static boolean isParityCorrect(String str, int index) {

        switch (index) {
            case 0 -> {
                String firstParity = str.substring(0, 1);
                String calculatedFirstParity = Integer.toBinaryString(
                        (Integer.parseInt(str.substring(2, 3), 2) + Integer.parseInt(str.substring(4, 5), 2) + Integer.parseInt(str.substring(6, 7), 2)) % 2);
                return firstParity.equals(calculatedFirstParity);
            }
            case 1 -> {
                String secondParity = str.substring(1, 2);
                String calculatedSecondParity = Integer.toBinaryString(
                        (Integer.parseInt(str.substring(2, 3), 2) + Integer.parseInt(str.substring(5, 6), 2) + Integer.parseInt(str.substring(6, 7), 2)) % 2);
                return secondParity.equals(calculatedSecondParity);
            }
            case 3 -> {
                String thirdParity = str.substring(3, 4);
                String calculatedThirdParity = Integer.toBinaryString(
                        (Integer.parseInt(str.substring(4, 5), 2) + Integer.parseInt(str.substring(5, 6), 2) + Integer.parseInt(str.substring(6, 7), 2)) % 2);
                return thirdParity.equals(calculatedThirdParity);
            }
        }

        return false;
    }

}