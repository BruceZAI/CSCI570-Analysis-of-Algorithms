import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class Efficient {
    public static ArrayList<String> In = new ArrayList<>();
    public static String s1ForGenerate;
    public static String s2ForGenerate;
    public static String align1;
    public static String align2;
    public static int delta = 30;
    public static int[][] alphas = new int[4][4];
    public static int[][] dp1;
    public static Map<Character, Integer> mapDNA = new HashMap<>();
    public static ArrayList<Integer> indices1 = new ArrayList<>();
    public static ArrayList<Integer> indices2 = new ArrayList<>();

    public static void getIndices(){
        int size = In.size();
        boolean flag = false;
        for (int i = 0; i < size; i++){
            flag = isDigit(flag, i);
        }
    }

    private static boolean isDigit(boolean flag, int i) {
        if (!Character.isDigit(In.get(i).charAt(0))){
            flag = notDigit(flag, i);
        }
        else {
            Digit(flag, i);
        }
        return flag;
    }

    private static void Digit(boolean flag, int i) {
        if (flag){
            indices1.add(Integer.valueOf(In.get(i)));
        } else {
            indices2.add(Integer.valueOf(In.get(i)));
        }
    }

    private static boolean notDigit(boolean flag, int i) {
        if (!flag){
            s1ForGenerate = In.get(i);
            flag = true;
        } else {
            s2ForGenerate = In.get(i);
            flag = false;
        }
        return flag;
    }

    public static String generateString(String str, ArrayList<Integer> indices){
        StringBuilder newStr = new StringBuilder(str);
        for (Integer index : indices) {
            newStr = generateStringHelper(newStr, index);
        }
        return newStr.toString();
    }

    private static StringBuilder generateStringHelper(StringBuilder newStr, Integer index) {
      return new StringBuilder(newStr.substring(0, index + 1) + newStr + newStr.substring(index + 1, newStr.length()));
    }

    private static double getMemory() {
        double total = Runtime.getRuntime().totalMemory();
        double ret = (total-Runtime.getRuntime().freeMemory())/10e3;
        return ret;
    }

    private static double getTime() {
        return System.nanoTime()/10e6;
    }

    public static String[] getCost(String str1, String str2){
        int l1 = str1.length();
        int l2 = str2.length();
        StringBuilder temp1 = new StringBuilder(str1);
        StringBuilder temp2 = new StringBuilder(str2);
        String reverse1 = temp1.reverse().toString();
        String reverse2 = temp2.reverse().toString();
        String[] cost1 = getStrings(str1, str2, l1, l2);
        if (cost1 != null) {
            return cost1;
        }
        String left = str2.substring(0, l2/2);
        getCostEfficient(left, str1);
        int[][] leftdp = dp1;
        getCostEfficient(reverse2.substring(0, l2 - (l2/2)), reverse1);
        int[][] rightdp = dp1;
        int cost = Integer.MAX_VALUE;
        int slow = -1;
        slow = getSlow(l1, leftdp, rightdp, cost, slow);
        String[] array1 = getCost(str2.substring(0, l2/2), str1.substring(0, slow));
        String[] array2 = getCost(str2.substring(l2/2), str1.substring(slow));
        String[] merge = makeMerge(3, String.valueOf(Integer.parseInt(array1[0]) + Integer.parseInt(array2[0])), array1[1] + array2[1], array1[2] + array2[2]);
        return merge;
    }

    private static String[] makeMerge(int x, String array1, String array11, String array12) {
        String[] merge = new String[x];
        merge[0] = array1;
        merge[1] = array11;
        merge[2] = array12;
        return merge;
    }

    private static String[] getStrings(String str1, String str2, int l1, int l2) {
        if (l1 <= 2 || l2 <= 2){
            int cost = getCostBasic(str1, str2);
            return new String[]{String.valueOf(cost), align1, align2};
        }
        return null;
    }

    private static int getSlow(int l1, int[][] leftdp, int[][] rightdp, int cost, int slow) {
        for (int fast = 0; fast <= l1; fast++){
            int sum = leftdp[fast][0] + rightdp[l1 - fast][0];
            if (sum < cost){
                cost = sum;
                slow = fast;
            }
        }
        return slow;
    }

    public static void getCostEfficient(String str1, String str2){
        int l1 = str1.length();
        int l2 = str2.length();
        dp1 = new int[l2 + 1][2];
        generateDPArrFirLoop(l2);
        generateDPArrSecLoop(str1, str2, l1, l2);
    }

    private static void generateDPArrSecLoop(String str1, String str2, int l1, int l2) {
        for (int i = 1; i <= l1; i++){
            dp1[0][1] = i * delta;
            for (int j = 1; j <= l2; j++){
                difSwap(str1, str2, i, j);
            }
            for (int j = 0; j <= l2; j++){
                easSwap(j);
            }
        }
    }

    private static void easSwap(int j) {
        dp1[j][0] = dp1[j][1];
    }

    private static void difSwap(String str1, String str2, int i, int j) {
        int a = alphas[mapDNA.get(str2.charAt(j - 1))][mapDNA.get(str1.charAt(i - 1))];
        dp1[j][1] = Math.min(dp1[j][0] + delta, Math.min(a + dp1[j - 1][0], delta + dp1[j - 1][1]));
    }

    private static void generateDPArrFirLoop(int l2) {
        extractedDPFirPos(l2, dp1);
    }

    public static int getCostBasic(String str1, String str2){
        int l1 = str1.length();
        int l2 = str2.length();
        Integer x = ifEquals(str1, str2);
        if (x != null) {
            return x;
        }
        int[][] dp = new int[l1 + 1][l2 + 1];
        extractedDPFirPos(l1, dp);
        extractedDPSecPos(l2, dp);
        extractedDPByMin(str1, str2, l1, l2, dp);
        int cost = makeCost(str1, str2, l1, l2, dp);
        return cost;
    }

    private static int makeCost(String str1, String str2, int l1, int l2, int[][] dp) {
        int cost = dp[l1][l2];
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int i = l1;
        int j = l2;
        ifIAndJBiggerThanZero(str1, str2, dp, sb1, sb2, i, j);
        align1 = sb1.reverse().toString();
        align2 = sb2.reverse().toString();
        return cost;
    }

    private static void ifIAndJBiggerThanZero(String str1, String str2, int[][] dp, StringBuilder sb1, StringBuilder sb2, int i, int j) {
        while (i > 0 || j > 0){
            if (i >= 1 && j >= 1 && dp[i][j] == alphas[mapDNA.get(str1.charAt(i - 1))][mapDNA.get(str2.charAt(j - 1))] + dp[i - 1][j - 1]){
                extractedByStrAndSb(sb1, str1.charAt(i - 1), sb2, str2.charAt(j - 1));
                i--;
                j--;
            } else if (i >= 1 && dp[i][j] == dp[i - 1][j] + delta){
                extractedByStrAndSb(sb1, str1.charAt(i - 1), sb2, '_');
                i--;
            } else{
                extractedByStrAndSb(sb1, '_', sb2, str2.charAt(j - 1));
                j--;
            }
        }
    }

    private static Integer ifEquals(String str1, String str2) {
        if (str1.equals(str2)){
            align1 = str1;
            align2 = str2;
            return 0;
        }
        return null;
    }

    private static void extractedByStrAndSb(StringBuilder sb1, char str1, StringBuilder sb2, char str2) {
        sb1.append(str1);
        sb2.append(str2);
    }

    private static void extractedDPByMin(String str1, String str2, int l1, int l2, int[][] dp) {
        for (int i = 1; i <= l1; i++){
            for (int j = 1; j <= l2; j++){
                dp[i][j] = Math.min(alphas[mapDNA.get(str1.charAt(i - 1))][mapDNA.get(str2.charAt(j - 1))] + dp[i - 1][j - 1],
                        Math.min(dp[i - 1][j], dp[i][j - 1]) + delta);
            }
        }
    }

    private static void extractedDPSecPos(int l2, int[][] dp) {
        for (int i = 1; i <= l2; i++){
            dp[0][i] = dp[0][i - 1] + delta;
        }
    }

    private static void extractedDPFirPos(int l1, int[][] dp) {
        for (int i = 1; i <= l1; i++){
            dp[i][0] = dp[i - 1][0] + delta;
        }
    }

    public static void readFile(String filePath){
        File file = new File(filePath);
        String tempString;
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
        double startMemory=getMemory();
        double startTime = getTime();
        String filePath = args[0];
        readFile(filePath);
        getIndices();
        String str1 = generateString(s1ForGenerate, indices1);
        String str2 = generateString(s2ForGenerate, indices2);
        String[] res = getCost(str1, str2);
        double endMemory = getMemory();
        double endTime = getTime();
        double totalUsage = endMemory-startMemory;
        double totalTime = endTime - startTime;
        String[] strArr = makeMerge(5, res[0], res[1], res[2]);
        strArr[3] = String.valueOf(totalTime);
        strArr[4] = String.valueOf(totalUsage);
        writeFile(strArr, args[1]);
    }
}
