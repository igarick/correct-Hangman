package org.example;


import java.util.*;

public final class Main {

    public static Scanner scanner = new Scanner(System.in);
    private static String randomWord;
    private static int numberMistakes = 0;     //sumMistakes

    //private static String[] secretWord;
    private static char[] secretWord;

    private static final StringBuilder enteredLetters = new StringBuilder();

    public static final char HIDDEN_LETTER = '*';
    private final static String START = "Н";
    private final static String QUIT = "В";
    private final static String FIRST_LETTER = "А";
    private final static String LAST_LETTER = "Я";
    private final static String COMMAND_REGEX = "[%s%s]".formatted(START, QUIT);

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

        }
    }

    // валидация ввода для start
    public static String inputCommand() {    //checkStartOrEnd() метод возвращает валидную команду, которую ввел юзер;
        while (true) {
            String command = scanner.next().toUpperCase(); //startOrEnd; название переменной - то что она хранит
            if (!((command.length() == 1 && command.matches(COMMAND_REGEX)))) {        // command.matches("[НВ]")
                System.out.printf("Введите букву %s или %s %n", START, QUIT);
            } else {
                return command;
            }
        }
    }

    public static void startGame() {
        System.out.println("Начало игры");

        RandomWordFromDictionary.getDictionary();
        RandomWordFromDictionary.createDictionaryList();
        randomWord = RandomWordFromDictionary.getRandomWord();

        setMaskWord();
        printMaskWord();

        gameLoop();
    }

    //--------------------------------------------igo---------------------------------------------------------------------------------
    // получить новое слово
//    public static String getRandomWord() throws FileNotFoundException {    //getNewWord -> getRandomWord
//        File dictionaryPath = new File("dictionary"); //file -> dictionaryPath
//        Scanner scanner = new Scanner(dictionaryPath);
//
//        List<String> dictionaryList = new ArrayList<>(); //words -> dictionaryList
//        while (scanner.hasNext()) {
//            dictionaryList.add(scanner.next());
//        }
//        scanner.close();
//
//        int indexOfRandomWord = random.nextInt(dictionaryList.size());
//        randomWord = dictionaryList.get(indexOfRandomWord).toUpperCase();    //newWordNew -> randomWord
//
//        return randomWord;
//    }
//-----------------------------------------------------------------------------------------------------------------------------
    // зашифровать новое слово и вывести на экран
    public static void setMaskWord() {   //String newWord -> randomWord


        secretWord = new char[randomWord.length()];
        for (int i = 0; i < secretWord.length; i++) {
            secretWord[i] = HIDDEN_LETTER;
        }

//        secretWord = new String[randomWord.length()];
//
//        for (int i = 0; i < secretWord.length; i++) {
//            secretWord[i] = Character.toString(HIDDEN_LETTER);
//        }
    }

    public static void printMaskWord() {
        for (char c : secretWord) {
            System.out.print(c);
        }
        System.out.println();
    }

    // game
    public static void gameLoop() {

        while (true) {
            char letter = inputLetter();   //newLetter -> letter
            boolean check = isLetterInRandomWord(letter);

            countNumberMistakes(check);
            determineOccurrencesLetter(check, letter);

            printWord();
            printIfNoLetter(check);
            printImageGallows(check);

            addLetter(letter);

            boolean checkState = checkStateWord();

            if (checkState) {
                resetLettersAndMistakes();
                System.out.println("Вы выйграли");
                System.out.println();
                return;
            }
            if (numberMistakes == 6) {
                resetLettersAndMistakes();
                System.out.println("Увы, мой друг, Вы проиграли");
                System.out.println("Загаданное слово: " + randomWord);
                System.out.println();
                return;
            }

        }
    }

    // валидация буквы
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

    // запись введенных букв
    public static void addLetter(char letter) {
        int length = enteredLetters.length();
        if (length == 0) {
            enteredLetters.append(letter);
            System.out.println("Вы ввели буквы: " + enteredLetters);
            System.out.println();
        } else {
            enteredLetters.append(", ");
            enteredLetters.append(letter);
            System.out.println("Вы ввели буквы: " + enteredLetters);
            System.out.println();
        }
    }

    // есть ли буква в слове
    public static boolean isLetterInRandomWord(char letter) { //String newLetter, String newWord, boolean checkLetterInWord
        for (char c : randomWord.toCharArray()) {
            if (letter == c) {
                return true;
            }
        }
        return false;
    }

    //такой буквы нет
    public static void printIfNoLetter(boolean letterInWord) {    //printNoLetter
        if (!letterInWord) {
            System.out.println("Такой буквы нет!");
        }
    }

    // подсчет количества ошибок
    public static void countNumberMistakes(boolean isMistake) {
        if (!isMistake) {
            numberMistakes++;
        }
    }

    // - буквы нет в слове - печатать виселицу
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

    // определить количество вхождений буквы в слово и их индекс
    public static void determineOccurrencesLetter(boolean letterInSecretWord, char letter) {

        if (letterInSecretWord) {
            char[] arrRandomWord = randomWord.toCharArray();
            for (int i = 0; i < arrRandomWord.length; i++) {
                if (letter == arrRandomWord[i] && secretWord[i] == HIDDEN_LETTER) {
                    secretWord[i] = arrRandomWord[i];
                }
            }
        }


//        if (letterInSecretWord) {
//            String[] arrRandomWord = randomWord.split("");
//            for (int j = 0; j < arrRandomWord.length; j++) {
//                if (letter == arrRandomWord[j].charAt(0) && secretWord[j] == '*' {
//                    secretWord[j] = arrRandomWord[j];
//                }
//            }
//        }
    }

    //проверка угаданно ли слово
    public static boolean checkStateWord() {
        int sum = 0;
        for (int j = 0; j < secretWord.length; j++) {
            if (secretWord[j] == HIDDEN_LETTER) {
                sum++;
            }
        }
        return (sum == 0);
    }

    // печатать слово
    public static void printWord() {
        for (char c : secretWord) {
            System.out.print(c);
        }
        System.out.println();
    }

    // обнулить введенные буквы и ошибки
    public static void resetLettersAndMistakes() {
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




