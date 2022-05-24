## Non-Recursive-Predictive-Parser and SLR-Parser

---

**this repo is "monorepo" meaning it will combine a independent projects
under "one repo", Non-recursive-predictive-parser and slr-parser**

## Table of contents

-   [Introduction](#introduction)
-   [NRPP](#non-recursive-predictive-parser)
-   [SP](#slr-parser)
-   [Conclusion](#conclusion)

### Introduction

this project was a part of a course that I took titled "compiler construction".

the main approach that was used to teach the student in this course
was the mainly explanation of doctor and the slides, it was sufficient but I have a habit of reading books, because I believe if you are doing things people aren't doing then you will discover/learn things people around you will never know, because it's not like everybody reads books and it's normalize no one got time really especially when are taking 4 courses besides compiler, it's hard thing and it's draining really, but I'm lucky that I got time so I love investing in myself, so anyhow the book titled "Compilers, Principles, Techniques, & Tools by Alfred V. Aho" and it was 1000+ page! I read about 300 page, it covered much of what we took in the course.

reading the book helped me so much with the building of the project, you will read the code will basically understand nothing if you didn't have any clue about java recent feature like streams, also Regular Expressions is used heavily on this project, using it was crucial really, knowing Regex saved a ton of time so thanks for Regular Expressions.

### Non-Recursive-Predictive-Parser

a non-recursive predictive parser is table-driven parser
meaning that it needs a table for parsing what input you supply to it.

non-recursive predictive parser can be built by maintaining a stack explicitly rather than implicitly via recursive calls.

here is a model of table-driven predictive parser.
![model-NRPP](./README%20pics/NRPP%20pics/model-of-table-driven-predictive-parser.png)

you will see that it's using stack data structure explicitly and it's pointing at the top of the stack also it's maintaining a input buffer of the string that is trying to parse.

you will notice that both input buffer and the stack end with a dollar sign.

if we ever get to the point where the top element of the stack is the dollar sign and pointer of the input buffer points to its dollar sign then and only then we know that this string is accepted by the grammar that generated the table that we used to check the validity of the string.

initial values are as follows:

-   stack will contain the start symbol of the grammar above the dollar sign
-   input buffer will hold the complete string

#### algorithm:

![algorithm](./README%20pics/NRPP%20pics/algorithm.png)

#### Error Recovery in Predictive Parsing

an error is detected during predictive parsing when the terminal on top of the stack does not match the next input symbol or when the nonTerminal is on the top of that stack and the next input symbol their lookup in the table does yield an empty entry.

there is error recovery modes, "Panic Mode" and "Phrase-level Recovery".

Panic Mode is based on the idea of skipping over symbols on the input until a token in selected set of synchronizing tokens appears.

if the parser looks up entry M[A,a] and finds that it is blank, then input symbol "a" is skipped. if the entry is "synch" then the nonTerminal on the top of the stack is popped, in an attempt to resume parsing. if a token on top of the stack does not match the input symbol, then we pop the token from the stack.

Phrase-level Recovery is implemented by filling in the blank entries in the predictive parsing table with pointers to error routines.

These routines may change, insert, or delete symbols on the input and issue appropriate error messages. They may also pop from the stack. Alteration of stack symbols or the pushing of new symbols onto the stack is questionable for several reasons.

#### Example output

![example-one](./README%20pics/NRPP%20pics/NRPP-result.png)

### Slr-Parser

the most prevalent type of bottom-up parsers today is based on a concept called LR(k) parsing, the "L" is for left-to-right scanning of the input,
the "R" for constructing a rightmost derivation in reverse, and the k for the number of input symbols of lookahead that are used in making parsing decisions.

LR parsers are table-driven much like non-recursive predictive parser.

for a grammar to be LR it is sufficient that left-to-right shift-reduce parser be able to recognize handles of right-sentential forms when they appear on top of the stack.

here is aa model of an LR parser:
![model-SP](./README%20pics/SP%20pics/model-of-LR-parser.png)

#### algorithm:

![algorithm](./README%20pics/SP%20pics/SLR%20Algorithm.png)

#### Example output

![example-two](./README%20pics/SP%20pics/slr-result.png)

### Conclusion

I successfully implemented a non-recursive predictive parser and slr-parser
using java, it was such an intense experience due to the level required to read the content of the book, even the doctor who taught us admitted that
this book is not on our level it's for someone who at least has a background on compilers or he is on graduate level, despite of that it was such a great experience.
