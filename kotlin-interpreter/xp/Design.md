# xp

A fully turing complete expression language

## Operations

### Unary

Acts at the right operand with the lowest precedence.

```
-(3 + 1)
> -4

!true
> false

fact 4
> 12
```

### Binary

Acts in both left and right operand

```
result : 1 + 1
> 2

[1, 2, 3] . 1
> 2
```

### Defining Operations

Using the left and right references.

```
fact: {[true: 1, false: right * (this (right - 1))] . right <= 1}
fact 3
[true: 1, false: 3 * this (3 - 1)] . 3 <= 1
[true: 1, false: 3 * this (2)] . false
3 * [true: 1, false: 2 * this (2 - 1)] . 2 <= 1
3 * [true: 1, false: 2 * this (1)] . false
3 * 2 * this (1)
3 * 2 * [true: 1, false: 1 * this (1 - 1)] . 1 <= 1
3 * 2 * [true: 1, false: 1 * this (0)] . true
3 * 2 * 1
6

fact

succ: right + 1
succ 1
2
```

## Basic Syntax

```
Arithm: + - * / ^ % ( ) 
Comp: < > <= >= = <> # lt gt le ge eq ne
Logic: | || & && ~ true false # or or! and and! not
List: [ ] : " { } .. # Llist Rlist Def/key String LIndex RIndex Intersect Range
Reference: left right this null :=
Comment: # 
IO: @ # @in @out @err @/file

[0 1] + 1 = [1 2]
[0 1] + [1] = [1 1]
[0 1] + [1 2] = [1 3]

[0 1] < [5] = true
[0 1] < 5 = true

[1 2] & 1 = 2
3 & [1 2] = null
[1 2] | 1 = [1 2 2]
[1 2] & [] = [1 2]
[1 2] | [2] = [1 2 2]

"string" = ['s' 't' 'r' 'i' 'n' 'g']
's' + 1 = 't'
's' * 2 = ?

[1 2 3] & {1 2} = [2 3]
[1 2 3] & [1 2] = [1 2]
[1 2 3] & 2 = 3

[1 2 3] & {1 ...} = [2 3]
[1 2 3] & {... 1} = [1 2]
[1 2 3] & [2 ... 5] = [2 3]
[1 2 3] & 4 = null
[1] & [2 3] = []

[a: 1 b: 2 c: 4] & {a b} = [1 2]
[a: 1 b: 2 c: 4] & [a b] = []
[a: 1 b: 2 c: 4] & [1 2] = [a: 1 b: 2] ???

a: 1; b: 2; c: 4 = [a: 1 b: 2 c: 4]
'0; ...; '5 = {0 ... 5}
```

## Grammar

program        → statement* EOF ;
statement      → exprStmt | printStmt ;
exprStmt       → expression ";" ;
printStmt      → "print" expression ";" ;
expression     → assignment ;
assignment     → IDENTIFIER ":" assignment | equality ;
equality       → comparison ( ( "!=" | "==" ) comparison )* ;
comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term           → factor ( ( "-" | "+" ) factor )* ;
factor         → unary ( ( "/" | "*" ) unary )* ;
unary          → ( "!" | "-" ) unary | primary ;
primary        → NUMBER | STRING | "true" | "false" | "null" | "(" expression ")" | IDENTIFIER ;

---

file        : statement* EOF ;
statement   : exprStmt | ioStmt ;
exprStmt    : expression ";" ;
ioStmt      : "@" channel expression ";" ;
channel     : "in" | "out" | "err" | "now" | "xp" STRING | STRING 
expression  : assignment ;
assignment  : IDENTIFIER ":" assignment | equality ;
equality    : comparison ( ( "!=" | "==" ) comparison )* ;
comparison  : term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term        : factor ( ( "-" | "+" ) factor )* ;
factor      : unary ( ( "/" | "*" ) unary )* ;
unary       : ( "~" | "-" ) unary | primary ;
primary     : NUMBER | STRING | "true" | "false" | "null" | "this" | "right" | "left" | "(" expression ")" | IDENTIFIER ;

file        : expression* EOF ;
expression  : binary ;
binary      : unary (binaryOp unary)* ;
binaryOp    : "!=" | "==" | ">" | ">=" | "<" | "<=" | "-" | "+" | "/" | "*" | "@" | ":" | ";" | "|" | "||" | "&" | "&&" | "^" | "%" | IDENTIFIER
unary       : unaryOp unary | primary ;
unaryOp     : "~" | "-" | IDENTIFIER
primary     : "true" | "false" | "null" | "this" | "right" | "left" | NUMBER | STRING | "(" expression ")" | IDENTIFIER ;