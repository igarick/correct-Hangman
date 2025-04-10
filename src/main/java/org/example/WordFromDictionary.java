package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordFromDictionary {
    public static Random random = new Random();
    public static String randomWord;

    private static File DICTIONARY;


    public static void getDictionary(){
        DICTIONARY = new File("dictionary");
    }

    static List<String> createDictionaryList() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(DICTIONARY);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Файл словоря не найден");
        }

        List<String> dictionaryList = new ArrayList<>(); //words -> dictionaryList
        while (scanner.hasNext()) {
            dictionaryList.add(scanner.next());
        }
        scanner.close();
            return dictionaryList;
    }
    static void getRandomWord(List<String> dictionaryList){
        int indexOfRandomWord = random.nextInt(dictionaryList.size());
        randomWord = dictionaryList.get(indexOfRandomWord).toUpperCase();    //newWordNew -> randomWord
        //    return randomWord;
    }
}
