package org.example;


import java.util.*;

public final class Main {

    public static Scanner scanner = new Scanner(System.in);
    private static int numberMistakes = 0;
    private final static int MAX_NUMBER_MISTAKES = 6;
    private static char[] randomWord;
    private static char[] hiddenWord;
    private static final StringBuilder enteredLetters = new StringBuilder();
    private static final char HIDDEN_LETTER = '*';
    private final static String START = "Н";
    private final static String QUIT = "В";
    private final static String COMMAND_REGEX = "[%s%s]".formatted(START, QUIT);
    private final static String FIRST_LETTER = "А";
    private final static String LAST_LETTER = "Я";
    private final static String ALPHABET_REGEX = "[%s-%s]".formatted(FIRST_LETTER, LAST_LETTER);


    private Main() {
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("(Н)ачать игру или (В)ыйти");

            String command = inputCommand();
            if (command.equals(QUIT)) {
                return;
            }

            startGame();
            System.out.println();
        }
    }

    static String inputCommand() {
        while (true) {
            String command = scanner.next().toUpperCase();
            if (!isValidCommand(command)) {
                System.out.printf("Введите букву %s или %s %n", START, QUIT);
            } else {
                return command;
            }
        }
    }

    private static boolean isValidCommand(String command){
        return command.length() == 1 && command.matches(COMMAND_REGEX);
    }

    static void startGame() {
        System.out.println("Начало игры \n");

        RandomWordFromDictionary.getDictionary();
        RandomWordFromDictionary.createDictionaryList();
        randomWord = RandomWordFromDictionary.getRandomWord().toCharArray();

        setMaskWord();
        printMaskWord();

        gameLoop();

    }

    static void setMaskWord() {
        hiddenWord = new char[randomWord.length];
        Arrays.fill(hiddenWord, HIDDEN_LETTER);
    }

    static void printMaskWord() {
        for (char c : hiddenWord) {
            System.out.print(c);
        }
        System.out.println();
    }

    static void gameLoop() {
        while (!isGameOver()) {
            char letter = inputLetter();
            boolean isWordLetter = isLetterInRandomWord(letter);
            writeInputLetter(letter);

            if (!isWordLetter) {
                countNumberMistakes();
                printNoLetter();
                Gallows.printGallows(numberMistakes);
                printNumberRemainingAttempts();

            } else {
                revealMatchedLetters(letter);
                printYesLetter();
            }

            printHiddenWord();
            printEnteredLetters();

            if (isWin()) {
                printWinMessage();
            } else if (isLose()) {
                printLoseMessage();
            }
        }
        printRandomWord();
        resetEnteredLettersAndMistakes();
    }

    private static boolean isGameOver() {
        return isWin() || isLose();
    }

    static char inputLetter() {
        System.out.printf("Введите одну букву от %s до %s %n", FIRST_LETTER, LAST_LETTER);

        while (true) {
            String letter = scanner.next().toUpperCase();
            if (!isSingleLetterAlphabet(letter)) {
                System.out.printf("Введите одну букву от %s до %s %n", FIRST_LETTER, LAST_LETTER);
            } else if (isLetterRepeat(letter)) {
                System.out.printf("Вы уже вводили эту букву. Введите одну букву от %s до %s %n", FIRST_LETTER, LAST_LETTER);
            } else {
                return letter.charAt(0);
            }
        }
    }

    private static boolean isSingleLetterAlphabet(String letter) {
        return letter.length() == 1 && letter.matches(ALPHABET_REGEX);
    }

    private static boolean isLetterRepeat(String letter) {
        return enteredLetters.toString().contains(letter);
    }

    private static boolean isLetterInRandomWord(char letter) {
        for (char c : randomWord) {
            if (letter == c) {
                return true;
            }
        }
        return false;
    }

    static void countNumberMistakes() {
        numberMistakes++;
    }

    static void printNoLetter() {
        System.out.println("Такой буквы нет! \n");
    }

    static void printNumberRemainingAttempts() {
        System.out.println("У Вас осталось попыток: " + countUnusedAttempts());
    }

    static int countUnusedAttempts() {
        return MAX_NUMBER_MISTAKES - numberMistakes;
    }

    static void revealMatchedLetters(char letter) {
        for (int i = 0; i < randomWord.length; i++) {
            if (isMatch(letter, i)) {
                hiddenWord[i] = letter;
            }
        }
    }

    private static boolean isMatch(char letter, int index) {
        return randomWord[index] == letter;
    }

    static void printYesLetter() {
        System.out.println("Вы угадали букву!");
    }

    static void printHiddenWord() {
        for (char c : hiddenWord) {
            System.out.print(c);
        }
        System.out.println();
    }

    static void writeInputLetter(char letter) {
        int numberEnteredLetters = enteredLetters.length();
        if (numberEnteredLetters > 0) {
            enteredLetters.append(", ");
        }
        enteredLetters.append(letter);
    }

    static void printEnteredLetters() {
        System.out.printf("Вы ввели буквы: %s %n%n", enteredLetters);
    }

    private static boolean isWin() {
        for (char c : hiddenWord) {
            if (c == HIDDEN_LETTER) {
                return false;
            }
        }
        return true;
    }

    static void printWinMessage() {
        System.out.println("Вы выйграли!");
        System.out.println();
    }

    private static boolean isLose() {
        return numberMistakes == MAX_NUMBER_MISTAKES;
    }


    static void printLoseMessage() {
        System.out.println("Увы, мой друг, Вы проиграли");
        System.out.println();
    }

    static void printRandomWord() {
        System.out.print("Загаданным было слово: " + representStrRandomWord());
        System.out.println();
    }

    static String representStrRandomWord() {
        StringBuilder strRandomWord = new StringBuilder();
        for (char c : randomWord) {
            strRandomWord.append(c);
        }
        return strRandomWord.toString();
    }

    static void resetEnteredLettersAndMistakes() {
        numberMistakes = 0;
        enteredLetters.delete(0, enteredLetters.length());
    }
}



