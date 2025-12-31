package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

import java.util.HashMap;
import java.util.Map;

public class LsCommand implements Command {
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {

        Inode currentDirectory = context.getCurrentDirectory();

        Map<String, Inode> currentDirectoryContent = currentDirectory.asDirectory().getEntries();

        currentDirectoryContent.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Ordena alfabeticamente pela Chave (String s)
                .forEach(entry -> {
                    String s = entry.getKey();
                    Inode inode = entry.getValue();

                    System.out.println(BLUE + s + (inode.isDirectory() ? "/" : "") + RESET);
                });

    }
}
