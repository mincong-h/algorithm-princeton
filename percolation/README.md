# Percolation

The program `PercolationStats` takes two command-line arguments _n_ and _T_,
performs _T_ independent computational experiments on an _n_ * _n_ grid, and
prints the mean, standard deviation, and the 95% confidence interval for the
percolation threshold. For more informations, check [the official assignment
description][1].

## How to compile and run

Mac OS / Linux

    $ javac -cp ../lib/* Percolation.java PercolationStats.java
    $ java -cp "../lib/*:." PercolationStats 200 1000

Windows

    $ javac -cp ../lib/* Percolation.java PercolationStats.java
    $ java -cp "../lib/*;." PercolationStats 200 1000

[1]: http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
