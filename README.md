# Public Projects

## Git Clone Built in Java

This was the longest and hardest project I have ever
had the pleasure of working on. Requiring multiple months to complete, Gitlet is a near exact clone
of everybody's favorite version control system, Git. Includes the commands [init, add, commit, rm, 
log, global-log, find, status, checkout, branch, rm-branch, reset, merge] running in O(NlogN + D) time,
where N is the total number of commits and D is the total file data for each commit. Although this project was
immensely difficult, coming out of it, I found myself to be a better programmer and attained a familiarity 
with Java that may even surpass Python.

## Random Tile Generator & Interactive Game

This project was a much simpler task than the 
others that came before, however I still managed to learn a great deal from it. Piggybacking off the 
hit indie game, <i>Hollow Knight</i>, this game acts as a very simple rogue-like dungeon where the 
objective is to collect every coin on the screen in less than <i>x</i> moves. This project includes 
relatively advanced features, such as [saving, loading, random tile generation, custom assets, etc] 
all while using a horribly outdated library for rendering!</p>

## Risc V CPU Built in Logism 

Custom RISC V CPU built in Logisim. Has a custom ALU, Register design,
Immediate Generator, Memory Units for instruction fetch and reading/writing data, Branch Comparator, and 
Program Counter. Also features a custom-made control logic unit and support for a 2-stage pipeline! Have 
plans to implement I/O, caching, branch predictors, a 5-stage pipeline, floating-point support, SIMD support, 
written in Verilog.

## NumPy Clone Built in C 

Close copy of NumPy built inside of C. Takes advantage of multiple 
optimizations in order to implement fast matrix operations, including Intel SIMD intrinsics, parallel 
processing using OpenMP, cache blocking, and a repeated squaring algorithm for matrix powering. This 
gives us a 3x overall speedup from a naive implementation, with matrix powering giving us a 800x speedup.
