# Hack-Assembler
An implementation of the Hack machine language assembler in java. Followed from the book "The Elements of Computing Systems."

The implementaion follows the one given in project 6. It has:
 - Parser module: a parser that splits the instruction into it's mnemonics. This works similar to a state machine. As for each call of the advance method, it reads the next instruction and adjusts it's state.
 - Code module: this is responsible for translating the mnemonics produced by the parser module to generate machine code. It takes the current state of the parser, and produces machine code based on it. 
 - Symbol table: a table for storing predefined, label, and variable symbols.
 - Main module: the module that controls the flow of the parser and code modules.

# Issues
 - Bad Exception names.
 - Needs better parsing. The assembler can translate a correctly written asm file with no issues, but it may translate a file with syntax errors without throwing an error.
