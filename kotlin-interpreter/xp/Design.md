# xp

A fully turing complete expression language

## Types

* Numeric
  * Integer
  * Real
  * Rational (tbd)
  * Complex (tbd)
  * Char
  * Boolean
* List
  * String (List of Char)
  * Linked
  * Array
  * Map
* Operator
  * Unary
  * Binary
* Special
  * Index

## Reserved words

* true
* false
* this
* right
* left

## Operators

### Unary

Acts at the right operand with the lowest precedence.

```
-(3 + 1)
> -4

~true
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
> 1
```

### Defining Operations

Using the left and right references.

```
fact: {[true: 1, false: right * (this (right - 1))] . right <= 1}
fact 3
> [true: 1, false: 3 * this (3 - 1)] . 3 <= 1
> [true: 1, false: 3 * this (2)] . false
> 3 * [true: 1, false: 2 * this (2 - 1)] . 2 <= 1
> 3 * [true: 1, false: 2 * this (1)] . false
> 3 * 2 * this (1)
> 3 * 2 * [true: 1, false: 1 * this (1 - 1)] . 1 <= 1
> 3 * 2 * [true: 1, false: 1 * this (0)] . true
> 3 * 2 * 1
> 6

succ: right + 1
succ 1
> 1 + 1
> 2
```

## Basic Syntax

```
Arithm: + - * / ^ % ( ) 
Comp: < > <= >= = <> # lt gt le ge eq ne
Logic: | || & && ~ true false # or or! and and! not
List: [ ] ; " { } . .. # Llist Rlist Cons String LIndex RIndex Intersect Range
Reference: left right this null :
Comment: # 
IO: @ # @in @out @err @file

[0 1] + 1 = 3
[0 1] + [1] = 3
[0 1] + [1 2] = 4

[0 1] < [5] = false
[0 1] < 5 = true

[1 2] & 1 = true
3 & [1 2] = true
[1 2] | 0 = true
[1 2] & [] = false
[1 2] | [2] = true

"string" = ['s' 't' 'r' 'i' 'n' 'g']
's' + 1 = 't'
's' * 2 = ?

a: 1; b: 2; c: 4 = [a: 1 b: 2 c: 4]
```

## Grammar
program     : expression* EOF ;
expression  : binary ;
binary      : unary (binaryOp unary)* ;
binaryOp    : "!=" | "==" | ">" | ">=" | "<" | "<=" | "-" | "+" | "/" | "*" | "@" | ":" | ";" | "|" | "||" | "&" | "&&" | "^" | "%" | IDENTIFIER
unary       : unaryOp unary | primary ;
unaryOp     : "~" | "-" | IDENTIFIER
primary     : "true" | "false" | "null" | "this" | "right" | "left" | NUMBER | STRING | "(" expression ")" | IDENTIFIER ;

---

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

