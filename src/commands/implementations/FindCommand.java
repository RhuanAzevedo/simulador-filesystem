package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import commands.CommandUtils;
import inode.Inode;
import inode.content.DirectoryContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length != 3 || !args[1].equals("-name")) {
            throw new CommandException("Uso: find <diretorio> -name <nome>");
        }

        String dirPath = args[0];
        String name = args[2];

        Inode start = context.resolvePath(dirPath);
        if (start == null || !start.isDirectory()) {
            throw new CommandException("Diretorio nao encontrado: " + dirPath);
        }

        // Inicia a busca recursiva a partir do diretorio informado
        String basePath = CommandUtils.toAbsolutePath(context, dirPath);
        List<String> matches = new ArrayList<>();
        search(start, basePath, name, matches);

        for (String match : matches) {
            System.out.println(match);
        }
    }

    private void search(Inode current, String currentPath, String name, List<String> matches) {
        DirectoryContent content = current.asDirectory();
        for (Map.Entry<String, Inode> entry : content.getEntries().entrySet()) {
            String entryName = entry.getKey();
            if (entryName.equals(".") || entryName.equals("..")) {
                continue;
            }
            Inode child = entry.getValue();
            String childPath = currentPath.equals("/") ? "/" + entryName : currentPath + "/" + entryName;

            if (entryName.equals(name)) {
                matches.add(childPath);
            }
            if (child.isDirectory()) {
                // Desce na hierarquia para continuar a busca
                search(child, childPath, name, matches);
            }
        }
    }
}
