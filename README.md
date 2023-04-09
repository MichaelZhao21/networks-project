# Networks Application

Made for CS 4390 at UTD. See [instructions.pdf](instructions.pdf) for the project instructions.

## Build & Execution

Type `make` to build all classes.

Run server using `java Server <port>`. Run client using `java Client <hostname> <port>`.

## Format

```
Connection message format:
[CONNECTION_MESSAGE] [CLIENT_NAME] [NEWLINE]

Connection ACK message format:
[ACK_MESSAGE] [NEWLINE]

Connection ERR message format:
[ERR_MESSAGE] [NEWLINE]

Calculation message format:
[OPERATION] [OPERAND1] [OPERAND2] [NEWLINE]
eg. ADD 1 2

Calculation result message format:
[RESULT] [NEWLINE]
eg. 3

Close message format:
[CLOSE_MESSAGE] [NEWLINE]
```

See `Client.java` for the `CONNECTION_MESSAGE`, `ACK_MESSAGE`, `ERR_MESSAGE`, and `CLOSE_MESSAGE` strings.