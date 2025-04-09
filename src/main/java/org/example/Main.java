package org.example;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static Random random = new Random();
    public static Scanner scanner = new Scanner(System.in);
    private static String randomWord;
    private static int sumMistakes = 0;
    private static String[] secretWord;
    private static final StringBuilder enteredLetters = new StringBuilder();


    public static void main(String[] args) throws FileNotFoundException {
        do {
            System.out.println("(Н)ачать игру или (В)ыйти");

            String command = inputCommand(); //startOrEnd -> command
            if (command.equals("В"))         //startOrEnd -> command
                return;

            startGame();

        } while (true);
    }

    // валидация ввода для start
    public static String inputCommand() {    //checkStartOrEnd() метод возвращает валидную команду, которую ввел юзер;
        do {
            String command = scanner.next().toUpperCase(); //startOrEnd; название переменной - то что она хранит
            if (!((command.length() == 1 && command.matches("[НВ]")))) {
                System.out.println("Введите букву Н или В");
            } else
                return command;
        } while (true);
    }

    public static void startGame() throws FileNotFoundException {
        System.out.println("Начало игры");
        String word = getRandomWord();   //String newWord -> randomWord
        maskWord(word);

        gameLoop(word);
    }

    // получить новое слово
    public static String getRandomWord() throws FileNotFoundException {    //getNewWord -> getRandomWord
        File dictionaryPath = new File("dictionary"); //file -> dictionaryPath
        Scanner scanner = new Scanner(dictionaryPath);

        List<String> dictionary = new ArrayList<>(); //words -> dictionary
        while (scanner.hasNext()) {
            dictionary.add(scanner.next());
        }
        scanner.close();

        int indexOfRandomWord = random.nextInt(dictionary.size());
        randomWord = dictionary.get(indexOfRandomWord).toUpperCase();    //newWordNew -> randomWord

      //  newWordNew = randomWord;
        return randomWord;
    }

    // зашифровать новое слово и вывести на экран
    public static void maskWord(String randomWord) {   //String newWord -> randomWord

        String maskWord = "*".repeat(randomWord.length());  //maskNewWord -> maskWord

        secretWord = new String[randomWord.length()];
        for (int i = 0; i < secretWord.length; i++) {
            secretWord[i] = "*";
        }
        System.out.println(maskWord);
    }

    // game
    public static void gameLoop(String newWord) {

        do {
            String letter = inputLetter();   //newLetter -> letter
            boolean check = checkLetterInWord(letter, newWord);

            countNumberMistakes(check);
            determineOccurrencesLetter(check, letter);

            printWord();
            printNoLetter(check);
            printImageVisel(check);

            addLetter(letter);

            boolean checkState = checkStateWord();

            if (checkState) {
                resetLettersAndMistakes();
                System.out.println("Вы выйграли");
                System.out.println("");
                return;
            }
            if (sumMistakes == 6) {
                resetLettersAndMistakes();
                System.out.println("Увы, мой друг, Вы проиграли");
                System.out.println("Загаданное слово: " + randomWord);
                System.out.println("");
                return;
            }

        } while (true);
    }

    // валидация буквы
    public static String inputLetter() {  //checkIsLetter;    метод получает букву от юзера
        System.out.println("Введите одну букву от А до Я");

        do {
            String letter = scanner.next().toUpperCase();    //newLetter
            if (!(letter.length() == 1 && letter.matches("[а-яА-Я]"))) {
                System.out.println("Введите одну букву от А до Я");
            } else if (enteredLetters.toString().contains(letter)) {
                System.out.println("Вы уже вводили эту букву. Введите одну букву от А до Я");
            } else
                return letter.toUpperCase();
        } while (true);
    }

    // запись введенных букв
    public static void addLetter(String newLetter) {
        int length = enteredLetters.length();
        if (length == 0) {
            enteredLetters.append(newLetter);
            System.out.println("Вы ввели буквы: " + enteredLetters);
            System.out.println("");
        } else {
            enteredLetters.append(", ");
            enteredLetters.append(newLetter);
            System.out.println("Вы ввели буквы: " + enteredLetters);
            System.out.println("");
        }
    }

    // есть ли буква в слове
    public static boolean checkLetterInWord(String newLetter, String newWord) {
        char a = newLetter.charAt(0);
        for (char c : newWord.toCharArray()) {
            if (a == c) return true;
        }
        return false;
    }

    //такой буквы нет
    public static void printNoLetter(boolean letterInWord) {
        if (!letterInWord)
            System.out.println("Такой буквы нет!");
    }

    // подсчет количества ошибок
    public static void countNumberMistakes(boolean isMistake) {
        if (!isMistake) {
            sumMistakes++;
        }
    }

    // - буквы нет в слове - печатать виселицу
    public static void printImageVisel(boolean checkLetterInWord) {
        if (!checkLetterInWord && sumMistakes == 1) {
            char[][] missOne = {{'-', '-', '-', '-', '-', ' '},
                    {'|', '/', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', ' ', 'O', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '}};
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    System.out.print(missOne[i][j]);
                }
                System.out.println("");
            }
            System.out.println("У вас осталось попыток: " + (6 - sumMistakes));
        } else if (!checkLetterInWord && sumMistakes == 2) {
            char[][] missOne = {{'-', '-', '-', '-', '-', ' '},
                    {'|', '/', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', ' ', 'O', ' '},
                    {'|', ' ', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '}};
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    System.out.print(missOne[i][j]);
                }
                System.out.println("");
            }
            System.out.println("У вас осталось попыток: " + (6 - sumMistakes));
        } else if (!checkLetterInWord && sumMistakes == 3) {
            char[][] missOne = {{'-', '-', '-', '-', '-', ' '},
                    {'|', '/', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', ' ', 'O', ' '},
                    {'|', ' ', ' ', '/', '|', ' '},
                    {'|', ' ', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '}};
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    System.out.print(missOne[i][j]);
                }
                System.out.println("");
            }
            System.out.println("У вас осталось попыток: " + (6 - sumMistakes));
        } else if (!checkLetterInWord && sumMistakes == 4) {
            char[][] missOne = {{'-', '-', '-', '-', '-', ' '},
                    {'|', '/', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', ' ', 'O', ' '},
                    {'|', ' ', ' ', '/', '|', '\\'},
                    {'|', ' ', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '}};
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    System.out.print(missOne[i][j]);
                }
                System.out.println("");
            }
            System.out.println("У вас осталось попыток: " + (6 - sumMistakes));
        } else if (!checkLetterInWord && sumMistakes == 5) {
            char[][] missOne = {{'-', '-', '-', '-', '-', ' '},
                    {'|', '/', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', ' ', 'O', ' '},
                    {'|', ' ', ' ', '/', '|', '\\'},
                    {'|', ' ', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', '/', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '}};
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    System.out.print(missOne[i][j]);
                }
                System.out.println("");
            }
            System.out.println("У вас осталось попыток: " + (6 - sumMistakes));
        } else if (!checkLetterInWord && sumMistakes == 6) {                             // ПРОИГРАЛ !!!!!!!
            char[][] missOne = {{'-', '-', '-', '-', '-', ' '},
                    {'|', '/', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', ' ', 'O', ' '},
                    {'|', ' ', ' ', '/', '|', '\\'},
                    {'|', ' ', ' ', ' ', '|', ' '},
                    {'|', ' ', ' ', '/', ' ', '\\'},
                    {'|', ' ', ' ', ' ', ' ', ' '},
                    {'|', ' ', ' ', ' ', ' ', ' '}};
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 6; j++) {
                    System.out.print(missOne[i][j]);
                }
                System.out.println("");
            }
        }
    }

    // определить количество вхождений буквы в слово и их индекс
    public static void determineOccurrencesLetter(boolean letterInWord, String enteredLetter) {
        if (letterInWord) {
            String[] arr = randomWord.split("");
            for (int j = 0; j < arr.length; j++) {
                if (enteredLetter.equals(arr[j]) && secretWord[j].equals("*")) {
                    secretWord[j] = arr[j];
                }
            }
        }
    }

    //проверка угаданно ли слово
    public static boolean checkStateWord() {
        int sum = 0;
        for (int j = 0; j < secretWord.length; j++) {
            if (secretWord[j].equals("*"))
                sum++;
        }
        return (sum == 0);
    }

    // печатать слово
    public static void printWord() {
        for (String x : secretWord) {
            System.out.print(x);
        }
        System.out.println("");
    }

    // обнулить введенные буквы и ошибки
    public static void resetLettersAndMistakes() {
        sumMistakes = 0;
        enteredLetters.delete(0, enteredLetters.length());
    }
}

    /*
 1  inputCommand()
    1. название return можно оставить или может
 быть любым, но осмысленным?

2. Нарушение инкапсуляции, публичным должен быть только метод main.
    1. Т.е. все методы должны быть только static? в моем случае
    2.


     */




