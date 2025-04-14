package org.example;

public class Gallows {
    private static final String[] PICTURES = {"""
                       -------
                       |     |
                       |     O
                       |
                       |
                       |
                       |
                       |_______""",

            """
                       -------
                       |     |
                       |     O
                       |     |
                       |     |
                       |
                       |
                       |_______""",

            """
                       -------
                       |     |
                       |     O
                       |    /|
                       |     |
                       |
                       |
                       |_______""",

            """
                       -------
                       |     |
                       |     O
                       |    /|\\
                       |     |
                       |
                       |
                       |_______""",

            """
                       -------
                       |     |
                       |     O
                       |    /|\\
                       |     |
                       |    /
                       |
                       |_______""",

            """
                       -------
                       |     |
                       |     O
                       |    /|\\
                       |     |
                       |    / \\
                       |
                       |_______"""

    };
    static void printGallows(int numPictures) {
        System.out.println(PICTURES[numPictures]);
    }
}