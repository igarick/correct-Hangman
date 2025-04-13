package org.example;


import java.util.*;

public final class Main {

    public static Scanner scanner = new Scanner(System.in);
    //private static String randomWord;
    private static int numberMistakes = 0;     //sumMistakes

    //private static String[] secretWord;
    private static char[] hiddenWord;

    private static final StringBuilder enteredLetters = new StringBuilder();

    public static final char HIDDEN_LETTER = '*';
    private final static String START = "Н";
    private final static String QUIT = "В";
    private final static String FIRST_LETTER = "А";
    private final static String LAST_LETTER = "Я";
    private final static String COMMAND_REGEX = "[%s%s]".formatted(START, QUIT);
    private final static int MAX_NUMBER_MISTAKES = 6;
    private final static int MIN_NUMBER_HIDDEN_LETTERS = 0;
    private static char[] randomWord;

    private Main() {
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("(Н)ачать игру или (В)ыйти");

            String command = inputCommand(); //startOrEnd -> command
            if (command.equals(QUIT)) {         //startOrEnd -> command
                return;
            }

            startGame();
            System.out.println();
        }
    }

    static String inputCommand() {    //checkStartOrEnd() метод возвращает валидную команду, которую ввел юзер;
        while (true) {
            String command = scanner.next().toUpperCase(); //startOrEnd; название переменной - то что она хранит
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
        System.out.println("Начало игры");

        RandomWordFromDictionary.getDictionary();
        RandomWordFromDictionary.createDictionaryList();
        randomWord = RandomWordFromDictionary.getRandomWord().toCharArray();

        setMaskWord();
        printMaskWord();

        gameLoop();

    }

    public static void setMaskWord() {   //String newWord -> randomWord
        hiddenWord = new char[randomWord.length];
        Arrays.fill(hiddenWord, HIDDEN_LETTER);
    }

    public static void printMaskWord() {
        for (char c : hiddenWord) {
            System.out.print(c);
        }
        System.out.println();
    }

    public static void gameLoop() {
        while (!isGameOver()) {
            char letter = inputLetter();   //1
            boolean isWordLetter = isLetterInRandomWord(letter);    //2

            if (!isWordLetter) {    //3
                countNumberMistakes();      //  4
                printNoLetter();  //8

            } else { //5
                revealMatchedLetters(letter);   //6
                printYesLetter();
            }

            printSecretWord();   //7

            printImageGallows(isWordLetter);    //9

            addLetter(letter);  //10
            printEnteredLetters();  //11

            if (isWin()) {  //12
                printWinMessage();  //13
            } else if (isLose()) {  //14
                printLoseMessage(); //15
            }
        }
        resetAddLettersAndMistakes();   //16
        printRandomWord();  //17
    }

    private static boolean isWin() {
        for (char c : hiddenWord) {
            if (c == HIDDEN_LETTER) {
                return false;
            }
        }
        return true;
    }

    private static boolean isLose() {
        return numberMistakes == MAX_NUMBER_MISTAKES;
    }

    static void printLoseMessage() {
        System.out.println("Увы, мой друг, Вы проиграли");
        System.out.println();
    }

    static void printWinMessage() {
        System.out.println("Вы выйграли");
        System.out.println();
    }

    private static boolean isGameOver() {
        return isWin() || isLose();
    }

    static void printRandomWord() {
        System.out.print("Загаданным было слово: ");
        for (char c : randomWord) {
            System.out.print(c);
        }
        System.out.println();
    }

    public static char inputLetter() {  //checkIsLetter;    метод получает букву от юзера
        System.out.printf("Введите одну букву от %s до %s %n", FIRST_LETTER, LAST_LETTER);

        while (true) {
            String letter = scanner.next().toUpperCase();    //newLetter
            if (!(letter.length() == 1 && letter.matches("[а-яА-Я]"))) {
                System.out.printf("Введите одну букву от %s до %s %n", FIRST_LETTER, LAST_LETTER);
            } else if (enteredLetters.toString().contains(letter)) {
                System.out.printf("Вы уже вводили эту букву. Введите одну букву от %s до %s %n", FIRST_LETTER, LAST_LETTER);
            } else {
                return (letter.toUpperCase()).charAt(0);
            }
        }
    }

    public static void addLetter(char letter) {
        int numberEnteredLetters = enteredLetters.length();
        if (numberEnteredLetters > 0) {
            enteredLetters.append(", ");
        }
        enteredLetters.append(letter);
    }

    private static void printEnteredLetters() {
        System.out.printf("Вы ввели буквы: %s %n%n", enteredLetters);
    }

    public static boolean isLetterInRandomWord(char letter) { //String newLetter, String newWord, boolean checkLetterInWord
        for (char c : randomWord) {
            if (letter == c) {
                return true;
            }
        }
        return false;
    }

    public static void revealMatchedLetters(char letter) {
        for (int i = 0; i < randomWord.length; i++) {
            if (isMatch(letter, i)) {
                hiddenWord[i] = letter;
            }
        }
    }

    private static boolean isMatch(char letter, int index) {
        return randomWord[index] == letter;
    }

    public static void printNoLetter() {
        System.out.println("Такой буквы нет!");
    }

    public static void printYesLetter() {
        System.out.println("Вы угалдали букву!");
    }

    public static void countNumberMistakes() {
        numberMistakes++;
    }

    public static void printImageGallows(boolean checkLetterInWord) {
        if (!checkLetterInWord && numberMistakes == 1) {
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
            System.out.println("У вас осталось попыток: " + (6 - numberMistakes));
        } else if (!checkLetterInWord && numberMistakes == 2) {
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
            System.out.println("У вас осталось попыток: " + (6 - numberMistakes));
        } else if (!checkLetterInWord && numberMistakes == 3) {
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
            System.out.println("У вас осталось попыток: " + (6 - numberMistakes));
        } else if (!checkLetterInWord && numberMistakes == 4) {
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
            System.out.println("У вас осталось попыток: " + (6 - numberMistakes));
        } else if (!checkLetterInWord && numberMistakes == 5) {
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
            System.out.println("У вас осталось попыток: " + (6 - numberMistakes));
        } else if (!checkLetterInWord && numberMistakes == 6) {                             // ПРОИГРАЛ !!!!!!!
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

    public static void printSecretWord() {
        for (char c : hiddenWord) {
            System.out.print(c);
        }
        System.out.println();
    }

    public static void resetAddLettersAndMistakes() {
        numberMistakes = 0;
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
5. Нарушение DRY: ++ 16. Почему буквы тут это строки?
    1. Почему буквы тут это строки?  но private final static !!!!String!!!! START = "Н";

     */




