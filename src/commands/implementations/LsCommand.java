package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

import java.time.format.DateTimeFormatter;
import java.util.Map;

public class LsCommand implements Command {
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMM dd HH:mm");

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        // Verifica se tem a flag -l
        boolean longFormat = args.length > 0 && args[0].equals("-l");

        Inode currentDirectory = context.getCurrentDirectory();
        Map<String, Inode> content = currentDirectory.asDirectory().getEntries();

        content.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String name = entry.getKey();
                    Inode inode = entry.getValue();

                    if (longFormat) {
                        printLong(inode, name);
                    } else {
                        printShort(inode, name);
                    }
                });
    }

    private void printShort(Inode inode, String name) {
        String color = inode.isDirectory() ? BLUE : RESET;
        System.out.println(color + name + (inode.isDirectory() ? "/" : "") + RESET);
    }

    private void printLong(Inode inode, String name) {
        String perms = (inode.isDirectory() ? "d" : "-") + inode.getPermissions().toSymbol();
        String user = inode.getOwner();
        long size = inode.getSize();
        String date = inode.getModifiedAt().format(DATE_FMT);
        String color = inode.isDirectory() ? BLUE : RESET;

        // Formatação alinhada similar ao Linux
        // %-10s : Permissões (alinhado esquerda)
        // %-8s  : Usuário
        // %6d   : Tamanho
        System.out.printf("%s %-8s %6d %s %s%s%s%n",
                perms, user, size, date, color, name, RESET);
    }
}