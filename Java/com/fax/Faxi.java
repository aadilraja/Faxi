package com.fax;

import com.fax.Lexing.Scanner;
import com.fax.Token.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Faxi {
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            // Too many arguments: show correct usage and exit
            System.err.println("Usage: faxi [script]");
            System.exit(64); // EX_USAGE from UNIX sysexits.h
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    // Run a source file
    private static void runFile(String path) throws IOException {
        if(validateFileType(path))
        {

            byte[] bytes = Files.readAllBytes(Paths.get(path));
            run(new String(bytes, Charset.defaultCharset()));

            // Indicate an error in the exit code
            if (hadError) System.exit(65); // EX_DATAERR


        }
        else
        {
            System.err.println("Wrong file type:"+path+"\n"+"only .fax files are supported");
            System.exit(64);
        }
    }



    // Interactive REPL
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break; // Ctrl-D ends the session
            run(line);
            hadError = false; // a mistake in the REPL shouldn't kill the session
        }
    }

    private static void run(String source) {
           Scanner scanner = new Scanner(source);
           List<Token> tokens = scanner.scanTokens();
           for (Token token : tokens) System.out.println(token);
           System.out.println(source);
    }

    // Error reporting
   public static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }



    private static boolean validateFileType(String path) {
        return path.endsWith(".fax");
    }
}