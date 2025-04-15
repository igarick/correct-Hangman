package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RandomWordFromDictionary {
    final static Random random = new Random();


    private static File DICTIONARY;
    private static List<String> dictionaryList;


    public static void getDictionary(){
        DICTIONARY = new File("dictionary");
    }

    static void createDictionaryList() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(DICTIONARY);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Словарь не найден!" + e);
        }

        dictionaryList = new ArrayList<>();
        while (scanner.hasNext()) {
            dictionaryList.add(scanner.next());
        }
        scanner.close();
    }

    static String getRandomWord(){
        int indexOfRandomWord = random.nextInt(dictionaryList.size());
            return dictionaryList.get(indexOfRandomWord).toUpperCase();

    }
}
