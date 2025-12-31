package core;

import commands.*;

import java.util.Scanner;

public class Shell {

    public static void main(String[] args) throws CommandException {

        VirtualFileSystem vfs = new VirtualFileSystem();
        CommandContext context = new CommandContext(vfs);
        CommandParser parser = new CommandParser();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equals("exit")) break;

            try {
                parser.parseAndExecute(input, context);
            } catch (CommandException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

}
