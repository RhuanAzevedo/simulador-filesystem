package core;

import commands.*;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.EndOfFileException;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Scanner;

public class Shell {

    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    public static void main(String[] args) throws CommandException, IOException {

        VirtualFileSystem vfs = new VirtualFileSystem();
        CommandContext context = new CommandContext(vfs);
        CommandParser parser = new CommandParser();

        // Scanner scanner = new Scanner(System.in);

        // Ao inves de scanner, usando terminal JLine
        Terminal terminal = TerminalBuilder.builder()
                .dumb(true)
                .system(true)
                .build();

        // Implementa linereader com histórico (biblioteca JLine)
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .history(new DefaultHistory())
                .build();

        // inicia o usuário no diretório "/home/user"
        String startup = "cd home/user";
        parser.parseAndExecute(startup, context);

        // Inicia a captação dos comandos
        while (true) {
            // System.out.print(GREEN + context.getCurrentPath() + RESET + " > ");
            // String input = scanner.nextLine();

            String input;
            try {
                input = lineReader.readLine(GREEN + context.getCurrentPath() + RESET + " > ");
            } catch (UserInterruptException e) {
                continue; // Ignora o Ctrl+C
            } catch (EndOfFileException e) {
                break; // Ctrl+D para sair
            }

            if (input.trim().equals("exit")) break;
            context.addHistory(input);

            try {
                parser.parseAndExecute(input, context);
            } catch (CommandException e) {
                System.out.println("Erro: " + e.getMessage());
            }
            System.out.println();
        }
    }

}
