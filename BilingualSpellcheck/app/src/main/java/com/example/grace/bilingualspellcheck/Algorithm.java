package com.example.grace.bilingualspellcheck;

/**
 * Created by Jeannelle on 4/30/2016.
 */
import java.util.Arrays;

public class Algorithm {

	/*
	 have a boolean for if the first letter is capitalized so suggestion can also be capitalized
	 */

    //calculates the distance between two words using edit distance
    public static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }


    public static String check(String lang1, String lang2, String originalText) {

        String result = "";


        //gets words from the users and puts them into arrays
        String[] originalWords = originalText.split(" ");

        //checks if words are capitalized or not
        boolean[] isCapital = new boolean[originalWords.length];
        for (int i = 0; i < isCapital.length; i ++){
            isCapital[i] = Character.isUpperCase(originalWords[i].charAt(0));
        }

        //puts everything to lowercase
        for (int i = 0; i < originalWords.length; i ++){
            originalWords[i] = originalWords[i].toLowerCase();
        }

        //removes punctuation marks
        for (int i = 0; i < originalWords.length; i ++){
            String justWord = originalWords[i].replaceAll("[\\W]", "");
            originalWords[i] = justWord;
        }

        //creates boolean array to see if words appear in dictionaries or not
        boolean[] isCorrect = new boolean[originalWords.length];
        for (int i = 0; i < isCorrect.length; i ++){
            isCorrect[i] = false;
        }

        //checks if each word is in the dictionary or not
        for (int i = 0; i < originalWords.length; i ++){
            TextIO.readFile("app/res/dictionaries/" + lang1 + ".txt");
            while (!TextIO.eof()){
                String line = TextIO.getln();
                if (originalWords[i].compareTo(line) == 0){
                    isCorrect[i] = true;
                }
            }
            TextIO.readFile("app/res/dictionaries/" +lang2 + ".txt");
            while (!TextIO.eof()){
                String line = TextIO.getln();
                if (originalWords[i].compareTo(line) == 0){
                    isCorrect[i] = true;
                }
            }
        }

        //counts the number of misspelled words
        int badWords = 0;
        for (int i = 0; i < isCorrect.length; i ++){
            if (isCorrect[i] == false)
                badWords++;
        }

        //creates new arrays of misspelled words and capitalization status
        String[] misspelled = new String[badWords];
        boolean[] misspelledIsCap = new boolean[badWords];
        //mCount keeps track of indices in misspelled array
        int mCount = 0;
        for (int i = 0; i < isCorrect.length; i ++){
            if (isCorrect[i] == false){
                misspelled[mCount] = originalWords[i];
                misspelledIsCap[mCount] = isCapital[i];
                mCount++;
            }
        }

        result += Arrays.toString(misspelled);

		/* suggestions for each misspelled word
		 * suggestions are stored in an array of arrays
		 */
        String[][] suggestions = new String[badWords][10];
        for (int i = 0; i < badWords; i ++){
            int j = 0;
            TextIO.readFile(lang1 + ".txt");
            while (!TextIO.eof()){
                String line = TextIO.getln();
                if (minDistance(misspelled[i],line) < 2){
                    if (misspelledIsCap[i]){
                        char newChar = (char) (line.charAt(0)-32);
                        suggestions[i][j] = newChar + line.substring(1,line.length());
                    }
                    else{
                        suggestions[i][j] = line;
                    }
                    j ++;
                }
                // first five suggestions are from first language
                if (j == 5) break;
            }
            TextIO.readFile(lang2 + ".txt");
            while (!TextIO.eof()){
                String line = TextIO.getln();
                if (minDistance(misspelled[i],line) < 2){
                    if (misspelledIsCap[i]){
                        char newChar = (char) (line.charAt(0)-32);
                        suggestions[i][j] = newChar + line.substring(1,line.length());
                    }
                    else{
                        suggestions[i][j] = line;
                    }
                    j++;
                }
                //second five suggestions are from second language
                if (j == 10) break;
            }
        }

        for (int i = 0; i < badWords; i ++){
            result += Arrays.toString(suggestions[i]);
        }
        return result;
    }
}