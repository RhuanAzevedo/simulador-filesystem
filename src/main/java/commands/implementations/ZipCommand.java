package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import commands.CommandUtils;
import inode.Inode;

public class ZipCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length < 2) {
            throw new CommandException("Uso: zip <arquivo> <origem...>");
        }

        String archivePath = args[0];
        if (!archivePath.endsWith(".zip")) {
            archivePath += ".zip";
        }

        // Cria um diretorio que simula o arquivo compactado
        CommandUtils.ParentAndName archiveInfo = CommandUtils.resolveParent(context, archivePath);
        Inode archiveParent = archiveInfo.getParent();
        String archiveName = archiveInfo.getName();

        if (archiveParent.asDirectory().contains(archiveName)) {
            throw new CommandException("Arquivo zip ja existe: " + archiveName);
        }

        Inode archive = Inode.createDirectory(archiveParent);
        archiveParent.asDirectory().addChild(archiveName, archive);

        for (int i = 1; i < args.length; i++) {
            String sourcePath = args[i];
            Inode source = context.resolvePath(sourcePath);
            if (source == null) {
                throw new CommandException("Origem nao encontrada: " + sourcePath);
            }

            String sourceName = CommandUtils.baseName(sourcePath);
            if (archive.asDirectory().contains(sourceName)) {
                throw new CommandException("Entrada duplicada dentro do arquivo zip: " + sourceName);
            }

            // Copia profunda do conteudo para dentro do zip
            Inode copy = CommandUtils.deepCopy(source, archive);
            archive.asDirectory().addChild(sourceName, copy);
        }
    }
}
