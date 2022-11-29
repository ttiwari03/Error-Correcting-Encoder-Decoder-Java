package correcter;

import java.util.ArrayList;

/*
 *  This class
 *      - convert received byte format input to binary
 *      - induce one bit error in binary data
 *      - show error induced data
 *      - save error induced data to encoded.txt file.
 */
public class Send {
    public static ArrayList<Byte> send(byte[] byteInput) {
        String[] inputArray = convertByteToBinary(byteInput);
        String[] errorInducedData = simulateError(inputArray);
        String binaryErrorData = convertToBinary(errorInducedData);

        printData(inputArray, binaryErrorData);
        ArrayList<Byte> list = new ArrayList<>();

        for(String bytes : binaryErrorData.split(" ")) {
            Integer value = Integer.parseInt(bytes, 2);
            list.add(value.byteValue());
        }
        return list;

    }

    private static String[] convertByteToBinary(byte[] byteInput) {

        String[] binaryInput = new String[byteInput.length];

        for (int i = 0; i < byteInput.length; i++) {
            binaryInput[i] = Integer.toBinaryString((byteInput[i] & 0xFF) + 0x100).substring(1);
        }

        return binaryInput;
    }

    private static void printData(String[] input, String binaryErrorData) {

        String binaryInput = convertToBinary(input);
        String hexInput = convertToHex(binaryInput);
        String hexErrorData = convertToHex(binaryErrorData);

        System.out.println();
        System.out.println(Main.encodeFileName + ":");
        System.out.println("hex view: " + hexInput);
        System.out.println("bin view: " + binaryInput);
        System.out.println();


        System.out.println(Main.receivedFileName + ":");

        System.out.println("bin view: " + binaryErrorData);
        System.out.println("hex view: " + hexErrorData);

    }

    private static String[] simulateError(String[] data) {
        int length = 7;
        String[] errorInducedData = new String[data.length];

        for (int i = 0; i < data.length; i++) {
            int randomPos;
            do {
                randomPos = (int) (Math.random() * length);
            } while (randomPos == 0 || randomPos == 1 || randomPos == 3 || randomPos == 7);

            String bitAtRandomPos = data[i].substring(randomPos, randomPos + 1);
            bitAtRandomPos = bitAtRandomPos.equals("0") ? "1" : "0";
            String tempByte = data[i].substring(0, randomPos) + bitAtRandomPos + data[i].substring(randomPos + 1);
            errorInducedData[i] = tempByte;
        }

        return errorInducedData;
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
}
