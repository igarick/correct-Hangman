package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RandomWordFromDictionary {
    final static Random random = new Random();

    private static List<String> dictionaryList;


    public static void createDictionaryList(){
        File dictionary = new File("dictionary");

        Scanner scanner = null;
        try {
            scanner = new Scanner(dictionary);
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
