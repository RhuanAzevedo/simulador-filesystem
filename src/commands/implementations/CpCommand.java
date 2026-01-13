package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import commands.CommandUtils;
import inode.Inode;

public class CpCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length != 2) {
            throw new CommandException("Uso: cp <origem> <destino>");
        }

        String sourcePath = args[0];
        String destinationPath = args[1];

        Inode source = context.resolvePath(sourcePath);
        if (source == null) {
            throw new CommandException("Origem nao encontrada: " + sourcePath);
        }

        Inode destinationNode = context.resolvePath(destinationPath);
        Inode destinationParent;
        String destinationName;

        if (destinationNode != null) {
            if (!destinationNode.isDirectory()) {
                throw new CommandException("Destino existe e nao e um diretorio.");
            }
            // Copia para dentro do diretorio de destino
            destinationParent = destinationNode;
            destinationName = CommandUtils.baseName(sourcePath);
        } else {
            CommandUtils.ParentAndName destInfo = CommandUtils.resolveParent(context, destinationPath);
            destinationParent = destInfo.getParent();
            destinationName = destInfo.getName();
        }

        if (destinationParent.asDirectory().contains(destinationName)) {
            throw new CommandException("Destino ja existe: " + destinationName);
        }

        if (source.isDirectory() && CommandUtils.isDescendant(source, destinationParent)) {
            throw new CommandException("Nao e possivel copiar um diretorio para dentro dele mesmo.");
        }

        // Copia profunda para preservar conteudo e metadados
        Inode copy = CommandUtils.deepCopy(source, destinationParent);
        destinationParent.asDirectory().addChild(destinationName, copy);
    }
}
