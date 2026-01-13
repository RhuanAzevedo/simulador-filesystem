package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import commands.CommandUtils;
import inode.Inode;

public class MvCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length != 2) {
            throw new CommandException("Uso: mv <origem> <destino>");
        }

        String sourcePath = args[0];
        String destinationPath = args[1];

        CommandUtils.ParentAndName sourceInfo = CommandUtils.resolveParent(context, sourcePath);
        Inode sourceParent = sourceInfo.getParent();
        String sourceName = sourceInfo.getName();
        Inode source = sourceParent.asDirectory().getEntries().get(sourceName);

        if (source == null) {
            throw new CommandException("Origem nao encontrada: " + sourcePath);
        }

        if (source == context.getFileSystem().getRoot()) {
            throw new CommandException("Nao e possivel mover o diretorio raiz.");
        }

        if (source == context.getCurrentDirectory()) {
            throw new CommandException("Nao e possivel mover o diretorio atual.");
        }

        if (source.isDirectory() && CommandUtils.isDescendant(source, context.getCurrentDirectory())) {
            throw new CommandException("Nao e possivel mover um diretorio que contem o diretorio atual.");
        }

        Inode destinationNode = context.resolvePath(destinationPath);
        Inode destinationParent;
        String destinationName;

        if (destinationNode != null) {
            if (!destinationNode.isDirectory()) {
                throw new CommandException("Destino existe e nao e um diretorio.");
            }
            destinationParent = destinationNode;
            destinationName = sourceName;
        } else {
            CommandUtils.ParentAndName destInfo = CommandUtils.resolveParent(context, destinationPath);
            destinationParent = destInfo.getParent();
            destinationName = destInfo.getName();
        }

        if (destinationParent.asDirectory().contains(destinationName)) {
            throw new CommandException("Destino ja existe: " + destinationName);
        }

        if (source.isDirectory() && CommandUtils.isDescendant(source, destinationParent)) {
            throw new CommandException("Nao e possivel mover um diretorio para dentro dele mesmo.");
        }

        sourceParent.asDirectory().remove(sourceName);
        destinationParent.asDirectory().addChild(destinationName, source);

        if (source.isDirectory()) {
            // Atualiza o ponteiro de pai ("..") ao mover diretorios
            source.asDirectory().getEntries().put("..", destinationParent);
        }
    }
}
