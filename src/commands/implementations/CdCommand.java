package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;
import inode.content.DirectoryContent;

public class CdCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        // 1. "cd" sozinho volta para a raiz, assim como "cd /"
        if (args.length == 0) {
            goToRoot(context);
            return;
        }

        String pathArg = args[0];

        // 2. "cd /" vai direto para a raiz
        if (pathArg.equals("/")) {
            goToRoot(context);
            return;
        }

        // --- VALIDAÇÃO FÍSICA (Inode) ---
        Inode currentInode = context.getCurrentDirectory();

        // Garante que estamos operando sobre um diretório
        if (!currentInode.isDirectory()) {
            throw new CommandException("Erro interno: Contexto atual não é um diretório.");
        }

        DirectoryContent dirData = currentInode.asDirectory();

        // Verifica se a pasta existe
        if (!dirData.contains(pathArg)) {
            throw new CommandException("Diretório não encontrado: " + pathArg);
        }

        Inode targetInode = dirData.getEntries().get(pathArg);

        // Verifica se o alvo é realmente um diretório (não pode dar cd em arquivo)
        if (!targetInode.isDirectory()) {
            throw new CommandException("Não é um diretório: " + pathArg);
        }

        // --- EXECUÇÃO DO MOVIMENTO ---

        // 1. Atualiza o INODE (Onde o sistema está de verdade)
        context.setCurrentDirectory(targetInode);

        // 2. Atualiza a STRING (O que o usuário vê no terminal)
        updateVisualPath(context, pathArg);
    }

    /**
     * Método auxiliar para ir à raiz e resetar a string
     */
    private void goToRoot(CommandContext context) {
        context.setCurrentDirectory(context.getFileSystem().getRoot());
        context.setCurrentPath("/");
    }

    /**
     * Método auxiliar que calcula a nova String do path
     */
    private void updateVisualPath(CommandContext context, String moveDir) {
        String oldPath = context.getCurrentPath();

        switch (moveDir) {
            case ".":
                // Não muda nada visualmente
                break;

            case "..":
                // Lógica de SUBIR nível
                if (oldPath.equals("/")) {
                    // Já estamos na raiz, visualmente não muda
                    return;
                }

                // Encontra a última barra para cortar a string
                // Ex: "/home/user" (lastSlash index = 5) -> vira "/home"
                int lastSlashIndex = oldPath.lastIndexOf('/');

                if (lastSlashIndex == 0) {
                    // Estava em nível 1 (ex: "/bin"), volta para "/"
                    context.setCurrentPath("/");
                } else {
                    // Estava mais fundo, corta o último segmento
                    context.setCurrentPath(oldPath.substring(0, lastSlashIndex));
                }
                break;

            default:
                // Lógica de DESCER nível (entrar em pasta)
                if (oldPath.equals("/")) {
                    // Evita ficar "//home"
                    context.setCurrentPath("/" + moveDir);
                } else {
                    // Normal: "/home" + "/user"
                    context.setCurrentPath(oldPath + "/" + moveDir);
                }
                break;
        }
    }
}