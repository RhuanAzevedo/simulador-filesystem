package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import commands.CommandUtils;
import inode.Inode;
import inode.content.DirectoryContent;

import java.util.Map;

public class UnzipCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length < 1 || args.length > 2) {
            throw new CommandException("Uso: unzip <arquivo> [destino]");
        }

        String archivePath = args[0];
        Inode archive = context.resolvePath(archivePath);
        if (archive == null) {
            throw new CommandException("Arquivo zip nao encontrado: " + archivePath);
        }
        if (!archive.isDirectory()) {
            throw new CommandException("Arquivo zip nao e um diretorio.");
        }

        Inode destination = context.getCurrentDirectory();
        if (args.length == 2) {
            destination = context.resolvePath(args[1]);
            if (destination == null || !destination.isDirectory()) {
                throw new CommandException("Diretorio de destino nao encontrado: " + args[1]);
            }
        }

        // Copia cada entrada do zip para o destino
        DirectoryContent archiveContent = archive.asDirectory();
        for (Map.Entry<String, Inode> entry : archiveContent.getEntries().entrySet()) {
            String name = entry.getKey();
            if (name.equals(".") || name.equals("..")) {
                continue;
            }
            if (destination.asDirectory().contains(name)) {
                throw new CommandException("Destino ja contem: " + name);
            }
            Inode copy = CommandUtils.deepCopy(entry.getValue(), destination);
            destination.asDirectory().addChild(name, copy);
        }
    }
}
