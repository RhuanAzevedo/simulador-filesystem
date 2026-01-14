package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;
import inode.content.DirectoryContent;

public class RmdirCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length < 1) {
            throw new CommandException("Uso: rmdir <diretorio>");
        }

        String path = args[0];

        // Separar o caminho do pai e o nome do alvo
        String parentPath;
        String targetName;

        int lastSlashIndex = path.lastIndexOf('/');

        if (lastSlashIndex == -1) {
            // rmdir 'pasta' (diretório atual)
            parentPath = ".";
            targetName = path;
        } else if (lastSlashIndex == 0) {
            // rmdir '/pasta' (está na raiz)
            parentPath = "/";
            targetName = path.substring(1);
        } else {
            // rmdir 'a/b/c'
            parentPath = path.substring(0, lastSlashIndex);
            targetName = path.substring(lastSlashIndex + 1);
        }

        // Diretórios proibidos
        if (targetName.equals(".") || targetName.equals("..") || targetName.isEmpty()) {
            throw new CommandException("Não é possível remover diretórios especiais (. ou ..)");
        }

        // Encontrar o Inode do Pai
        Inode parentInode = context.resolvePath(parentPath);

        // Se resolvePath retornar null, o caminho não existe
        if (parentInode == null) {
            throw new CommandException("O diretório pai '" + parentPath + "' não foi encontrado.");
        }

        if (!parentInode.isDirectory()) {
            throw new CommandException("O caminho '" + parentPath + "' não é um diretório válido.");
        }

        DirectoryContent parentContent = parentInode.asDirectory();

        // Verificar se o alvo existe dentro do pai
        if (!parentContent.contains(targetName)) {
            throw new CommandException("Diretório não encontrado: " + targetName);
        }

        Inode targetInode = parentContent.getEntries().get(targetName);

        // Validações do Alvo
        if (!targetInode.isDirectory()) {
            throw new CommandException("'" + targetName + "' não é um diretório (use rm para arquivos).");
        }

        DirectoryContent targetContent = targetInode.asDirectory();

        // Verifica se está vazio
        if (targetContent.getEntries().size() > 2) {
            throw new CommandException("Falha ao remover '" + targetName + "': O diretório não está vazio.");
        }

        if (targetInode == context.getCurrentDirectory()) {
            throw new CommandException("Não é possível remover o diretório onde você está agora.");
        }

        if (targetInode == context.getFileSystem().getRoot()) {
            throw new CommandException("Não é possível remover a raiz.");
        }

        // 5. Remover
        parentContent.remove(targetName);
    }
}