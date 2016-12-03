# 8 Puzzle

Write a program to solve the 8-puzzle problem (and its natural generalizations)
using the _A* search algorithm_.

The 8-puzzle problem is a puzzle invented and popularized by Noyes Palmer
Chapman in the 1870s. It is played on a 3-by-3 grid with 8 square blocks labeled
1 through 8 and a blank square. Your goal is to rearrange the blocks so that
they are in order, using as few moves as possible. You are permitted to slide
blocks horizontally or vertically into the blank square. The following shows a
sequence of legal moves from an initial board (left) to the goal board (right).

        1  3        1     3        1  2  3        1  2  3        1  2  3
     4  2  5   =>   4  2  5   =>   4     5   =>   4  5      =>   4  5  6
     7  8  6        7  8  6        7  8  6        7  8  6        7  8 
    
     initial        1 left          2 up          5 left          goal

For more informations, check the [official assignment description][1].

## How to compile

Mac OS / Linux / Windows

    javac -cp ../lib/* Board.java Solver.java

[1]: http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
