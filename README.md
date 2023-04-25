# Networks Application

Made for CS 4390 at UTD. See [instructions.pdf](instructions.pdf) for the project instructions.

## Build & Execution

Type `make` to build all classes.

1. Run the server on a machine. `java Server <port>`
2. Connect client to server. `java Client <hostname> <port>`
3. (Client side) Enter a calculation. `[OPERAND1] [OPERATION] [OPERAND2]`
4. (Client side) When need to close connection, enter empty line.
5. (Server side) Note log of all conections and requests in real-time.

## Format

```
Connection message format:
[CONNECTION_MESSAGE] [CLIENT_NAME] [NEWLINE]

Connection ACK message format:
[ACK_MESSAGE] [NEWLINE]

Connection ERR message format:
[ERR_MESSAGE] [NEWLINE]

Calculation message format:
[OPERAND1] [OPERATION] [OPERAND2] [NEWLINE]
eg. 1 + 2
eg. 1-2

Calculation result message format:
[RESULT] [NEWLINE]
eg. 3

Close message format:
[CLOSE_MESSAGE] [NEWLINE]
```

See `Client.java` for the `CONNECTION_MESSAGE`, `ACK_MESSAGE`, `ERR_MESSAGE`, and `CLOSE_MESSAGE` strings.