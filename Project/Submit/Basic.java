import java.io.*;
import java.util.*;

public class Basic {
    public static ArrayList<String> In = new ArrayList<>();
    public static String s1ForGenerate;
    public static String s2ForGenerate;
    public static String align1;
    public static String align2;
    public static int delta = 30;
    public static int[][] alphas = new int[4][4];
    public static Map<Character, Integer> mapDNA = new HashMap<>();
    public static ArrayList<Integer> indices1 = new ArrayList<>();
    public static ArrayList<Integer> indices2 = new ArrayList<>();

    public static void getIndices(){
        boolean flag = false;
        for (String s : In) {
            flag = getIndicesHelper(flag, s);
        }
    }

    private static boolean getIndicesHelper(boolean flag, String s) {
        if (!Character.isDigit(s.charAt(0))) {
            flag = getIndicesHelperFirBranch(flag, s);
        } else {
            getIndicesHelperSecBranch(flag, s);
        }
        return flag;
    }

    private static boolean getIndicesHelperFirBranch(boolean flag, String s) {
        if (!flag) {
            flag = isFlagisFalse(s);
        } else {
            flag = ifFlagisTrue(s);
        }
        return flag;
    }

    private static boolean ifFlagisTrue(String s) {
        boolean flag;
        s2ForGenerate = s;
        flag = false;
        return flag;
    }

    private static boolean isFlagisFalse(String s) {
        boolean flag;
        s1ForGenerate = s;
        flag = true;
        return flag;
    }

    private static void getIndicesHelperSecBranch(boolean flag, String s) {
        if (flag) {
            indices1.add(Integer.valueOf(s));
        } else {
            indices2.add(Integer.valueOf(s));
        }
    }

    public static String generateString(String str, ArrayList<Integer> indices){
        StringBuilder newStr = new StringBuilder(str);
        newStr = getStringBuilderHelper(indices, newStr);
        return newStr.toString();
    }

    private static StringBuilder getStringBuilderHelper(ArrayList<Integer> indices, StringBuilder newStr) {
        for (Integer index : indices) {
            newStr = getStringBuilderByAddLeftAndRight(newStr, index);
        }
        return newStr;
    }

    private static StringBuilder getStringBuilderByAddLeftAndRight(StringBuilder newStr, Integer index) {
        String left = newStr.substring(0, index + 1);
        String right = newStr.substring(index + 1, newStr.length());
        newStr = new StringBuilder(left + newStr + right);
        return newStr;
    }

    private static double getMemory() {
        double total = Runtime.getRuntime().totalMemory();
        double ret = (total-Runtime.getRuntime().freeMemory())/10e3;
        return ret;
    }

    private static double getTime() {
        return System.nanoTime()/10e6;
    }

    public static int getCost(String str1, String str2){

        int l1 = str1.length();
        int l2 = str2.length();
        int[][] dp = new int[l1 + 1][l2 + 1];
        extracted(l1, l2, dp);
        extractedByStr1AndStr2(str1, str2, l1, l2, dp);

        int cost = dp[l1][l2];
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int i = l1;
        int j = l2;
        extractedByStringBuilder(str1, str2, dp, sb1, sb2, i, j);
        
        align1 = sb1.reverse().toString();
        align2 = sb2.reverse().toString();
        return cost;
    }

    private static void extractedByStringBuilder(String str1, String str2, int[][] dp, StringBuilder sb1, StringBuilder sb2, int i, int j) {
        while (i > 0 || j > 0){
            if (i >= 1 && j >= 1 && dp[i][j] == alphas[mapDNA.get(str1.charAt(i - 1))][mapDNA.get(str2.charAt(j - 1))] + dp[i - 1][j - 1]){
                firstBranchInEBSB(sb1, str1.charAt(i - 1), sb2, str2.charAt(j - 1));
                i--;
                j--;
            }
            else if (i >= 1 && dp[i][j] == dp[i - 1][j] + delta){
                firstBranchInEBSB(sb1, str1.charAt(i - 1), sb2, '_');
                i--;
            }
            else{
                firstBranchInEBSB(sb1, '_', sb2, str2.charAt(j - 1));
                j--;
            }
        }
    }

    private static void firstBranchInEBSB(StringBuilder sb1, char str1, StringBuilder sb2, char str2) {
        sb1.append(str1);
        sb2.append(str2);
    }

    private static void extractedByStr1AndStr2(String str1, String str2, int l1, int l2, int[][] dp) {
        for (int i = 1; i <= l1; i++){
            for (int j = 1; j <= l2; j++){
                dp[i][j] = Math.min(alphas[mapDNA.get(str1.charAt(i - 1))][mapDNA.get(str2.charAt(j - 1))] + dp[i - 1][j - 1],
                        Math.min(dp[i - 1][j], dp[i][j - 1]) + delta);
            }
        }
    }

    private static void extracted(int l1, int l2, int[][] dp) {
        for (int i = 1; i <= l1; i++){
            dp[i][0] = dp[i - 1][0] + delta;
        }
        for (int i = 1; i <= l2; i++){
            dp[0][i] = dp[0][i - 1] + delta;
        }
    }

    public static void readFile(String filePath){
        File file = new File(filePath);
        String tempString = null;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while((tempString = reader.readLine()) != null){
                In.add(tempString);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String[] strArr, String filePath){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            for (String s : strArr) {
                out.write(s);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void initial_alphas(int[][] alphas){
        alphas[0][0] = 0;
        alphas[0][1] = 110;
        alphas[0][2] = 48;
        alphas[0][3] = 94;

        alphas[1][0] = 110;
        alphas[1][1] = 0;
        alphas[1][2] = 118;
        alphas[1][3] = 48;

        alphas[2][0] = 48;
        alphas[2][1] = 118;
        alphas[2][2] = 0;
        alphas[2][3] = 110;

        alphas[3][0] = 94;
        alphas[3][1] = 48;
        alphas[3][2] = 110;
        alphas[3][3] = 0;
    }

    public static void initial_map(Map<Character, Integer> mapDNA){
        mapDNA.put('A', 0);
        mapDNA.put('C', 1);
        mapDNA.put('G', 2);
        mapDNA.put('T', 3);
    }

    public static void main(String[] args){
        initial_alphas(alphas);
        initial_map(mapDNA);
        double startMemory = getMemory();
        double startTime = getTime();
        
        String filePath = args[0];
        readFile(filePath);
        getIndices();
        
        int cost = getCost(generateString(s1ForGenerate, indices1), generateString(s2ForGenerate, indices2));
        
        double endMemory = getMemory();
        double endTime = getTime();
        
        double totalMemory = endMemory-startMemory;
        double totalTime = endTime - startTime;
        String[] strArr = new String[5];
        strArr[0] = String.valueOf(cost);
        strArr[1] = align1;
        strArr[2] = align2;
        strArr[3] = String.valueOf(totalTime);
        strArr[4] = String.valueOf(totalMemory);
        writeFile(strArr, args[1]);
    }
}
