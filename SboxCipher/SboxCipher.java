package SboxCipher;
//A simple encrption algorithm implemented with given S-box and encription key
//The method to caculate the avalanche effect of the encrption is also provided

public class SboxCipher {

    public static int[][] s1 = new int[][]{{15, 10, 2, 5}, {8, 4, 11 ,6}, {1, 0, 14 ,7}, {9, 3, 12 ,13}};
    public static int[][] s2 = new int[][]{{4, 0, 10, 15}, {9, 8, 7, 13}, {5, 1, 6, 11}, {2, 3, 14, 12}};
    public static String[][] text = new String[][] {
        {"1111", "0101", "0110", "0110"},
        {"0010", "1001", "1100", "0010"},
        {"0101", "1100", "1110", "0010"},
        {"1110", "0111", "1100", "0011"},
        {"0011", "1110", "1111", "0010"}
    };
    public static String[][] key = new String[][] {
        {"1110", "1011", "0011", "1110"},
        {"1011", "1101", "1000", "0011"}
    };

    public static void main(String[] args) {

        //String[] ans = entrpt(text[0], key[0]);

        double avalanceScore = 0;
        double avalanceScoreTwo = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                avalanceScore += avalanche(text[i], key[j], "one");
                avalanceScoreTwo += avalanche(text[i], key[j], "two");
            }
        }
        System.out.println(avalanceScore / (5 * 2 * 16 * 16));
        System.out.println(avalanceScoreTwo / (5 * 2 * 16 * 16));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                String[] result = entrpt(text[i], key[j]);
                for (int k = 0; k < 4; k++) {
                    System.out.print(result[k]);
                }
                System.out.println();
            }
        }
    }

    //Take two arrays of four bytes as text and key
    //Caculate the avalanche effect from one pair of text and key
    public static double avalanche(String[] text, String[] key, String entrpMethod) {
        double sum = 0;
        int count = 0;
        String[] standard = entrpt(text, key);
        String[][] changed = new String[16][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                StringBuilder newKey = new StringBuilder(key[i]);
                if (key[i].charAt(j) == '0') {
                    newKey.setCharAt(j, '1');
                } else {
                    newKey.setCharAt(j, '0');
                }
                key[i] = newKey.toString();

                if (entrpMethod == "one") {
                    changed[count] = entrpt(text, key);
                } else {
                    changed[count] = entrptSecond(text, key);
                }
                count++;
            }
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    if (standard[j].charAt(k) != changed[i][j].charAt(k)) {
                        System.out.println(i + ", " + j + ", " + k);
                        sum++;
                    }
                }
            }
        }
        return sum;
    }

    //Take two arrays of bytes as text and key
    //Return the encrpted arrays of bytes
    public static String[] entrpt(String[] inputText, String[] intputKey) {
        int[] a =  new int[4];
        int[] k = new int[4];
        String[] c = new String[4];

        for (int i = 0; i < 4; i++) {
            a[i] = Integer.parseInt(inputText[i], 2);
            k[i] = Integer.parseInt(intputKey[i], 2);
        }

        c[0] = Integer.toBinaryString(a[1] ^ k[0]);
        c[1] = Integer.toBinaryString(a[3] ^ k[2]);
        c[2] = Integer.toBinaryString(a[0] ^ k[1]);
        c[3] = Integer.toBinaryString(a[2] ^ k[3]);

        for (int j = 0; j < 4; j++) {
            c[j] = appendToFour(c[j]);

            if (j % 2 != 0) {
                c[j] = S2(c[j]);
            } else {
                c[j] = S1(c[j]);
            }

            c[j] = appendToFour(c[j]);
        }

        return c;
    }

    //Second encrption method to enhance avalanche effect
    public static String[] entrptSecond(String[] inputText, String[] intputKey) {
        String[] encrptedKey = new String[4];

        //Encrpt the key
        for (int j = 0; j < 4; j++) {
            if (j % 2 != 0) {
                encrptedKey[j] = S2(intputKey[j]);
            } else {
                encrptedKey[j] = S1(intputKey[j]);
            }
        }

        return entrpt(inputText, encrptedKey);
    }

    //Caculate the output produced by S1 matrix
    //Take a binary string as the input. Return a converted binary string.
    public static String S1(String input) {
        String leftByte = input.substring(0, 2);
        String rightByte = input.substring(2, 4);
        int left = Integer.parseInt(leftByte, 2);
        int right = Integer.parseInt(rightByte, 2);
        return Integer.toBinaryString(s1[right][left]);
    }

    //Caculate the output produced by S2 matrix
    //Take a binary string as the input. Return a converted binary string.
    public static String S2(String input) {
        String leftByte = input.substring(0, 2);
        String rightByte = input.substring(2, 4);
        int left = Integer.parseInt(leftByte, 2);
        int right = Integer.parseInt(rightByte, 2);
        return Integer.toBinaryString(s2[right][left]);
    }

    //Append char '0' infront of bits if its length is less than 4
    public static String appendToFour(String key) {
        int len = key.length();
        if (len < 4) {
            for (int m = len; m < 4; m++) {
                key = "0" + key;
            }
        }
        return key;
    }
}