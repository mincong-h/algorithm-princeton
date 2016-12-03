# Deques and Randomized Queues

The client program `Subset.java` takes a command-line integer _k_; reads in a
sequence of _N_ strings from standard input using `StdIn.readString()`; and
prints out exactly _k_ of them, uniformly at random. Each item from the sequence
can be printed out at most once. You may assume that _0 ≤ k ≤ n_, where _n_ is
the number of string on standard input. For more information, check [the
official assignment description][1].

## How to compile and run

Mac OS / Linux

    $ javac -cp ../lib/* Deque.java RandomizedQueue.java Subset.java
    $ echo A B C D E F G H I | java -cp ".:../lib/*" Subset 3
    $ echo AA BB BB BB BB BB CC CC | java -cp ".:../lib/*" Subset 8

Windows

    $ javac -cp ../lib/* Deque.java RandomizedQueue.java Subset.java
    $ echo A B C D E F G H I | java -cp ".;../lib/*" Subset 3
    $ echo AA BB BB BB BB BB CC CC | java -cp ".;../lib/*" Subset 8

[1]: http://coursera.cs.princeton.edu/algs4/assignments/queues.html
