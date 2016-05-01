package com.example.grace.bilingualspellcheck;

/**
 * Created by Jeannelle on 4/30/2016.
 */
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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


    public static String check(String lang1, String lang2, String originalText, Context c) {

        String result = "";


        //gets words from the users and puts them into arrays
        String[] originalWords = originalText.split(" ");

        //puts lang 1 and 2 to lowercase
        lang1 = lang1.toLowerCase();
        lang2 = lang2.toLowerCase();

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
        BufferedReader reader;

        for (int i = 0; i < originalWords.length; i ++){
            try {
                InputStream langFile1 = c.getAssets().open(lang1 + ".txt");
                reader = new BufferedReader(new InputStreamReader(langFile1));
                String line = reader.readLine();
                while (line != null){
                    if (originalWords[i].compareTo(line) == 0) {
                        isCorrect[i] = true;
                    }
                    line = reader.readLine();
                }
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
            try {
                InputStream langFile2 = c.getAssets().open(lang2 + ".txt");
                reader = new BufferedReader(new InputStreamReader(langFile2));
                String line = reader.readLine();
                while (line != null){
                    if (originalWords[i].compareTo(line) == 0) {
                        isCorrect[i] = true;
                    }
                    line = reader.readLine();
                }
            }catch(IOException ioe){
                ioe.printStackTrace();
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
        if(misspelled.length>0)
            result += "Misspelled words: ";
        for(int i=0; i<misspelled.length; i++) {
            result += misspelled[i];
            if(i<misspelled.length-1)
                result+=", ";
        }

		/* suggestions for each misspelled word
		 * suggestions are stored in an array of arrays
		 */
        String[][] suggestions = new String[badWords][10];
        for (int i = 0; i < badWords; i ++) {
            int j = 0;
            try {
                InputStream langFile1 = c.getAssets().open(lang1 + ".txt");
                reader = new BufferedReader(new InputStreamReader(langFile1));
                String line = reader.readLine();
                while (line != null) {
                    if (minDistance(misspelled[i], line) < 2) {
                        if (misspelledIsCap[i]) {
                            char newChar = (char) (line.charAt(0) - 32);
                            suggestions[i][j] = newChar + line.substring(1, line.length());
                        } else {
                            suggestions[i][j] = line;
                        }
                        j++;
                    }
                    // first five suggestions are from first language
                    if (j == 5) break;
                    line = reader.readLine();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
            for (int i = 0; i < badWords; i ++) {
                int j = 0;
                try {
                    InputStream langFile2 = c.getAssets().open(lang2 + ".txt");
                    reader = new BufferedReader(new InputStreamReader(langFile2));
                    String line = reader.readLine();
                    while (line != null) {
                        if (minDistance(misspelled[i], line) < 2) {
                            if (misspelledIsCap[i]) {
                                char newChar = (char) (line.charAt(0) - 32);
                                suggestions[i][j] = newChar + line.substring(1, line.length());
                            } else {
                                suggestions[i][j] = line;
                            }
                            j++;
                        }
                        // second five suggestions are from second language
                        if (j == 10) break;
                        line = reader.readLine();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

        result += "\nSuggestions:";
        for (int i = 0; i < badWords; i ++){
            result += "\n" + misspelled[i] + ":";
            for(int j=-0; j<suggestions[i].length; j++){
                if(suggestions[i][j]==null)
                    break;
                else result += " " + suggestions[i][j];
            }
        }
        return result;
    }
}