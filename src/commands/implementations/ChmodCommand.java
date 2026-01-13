package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;
import inode.Permissions;

public class ChmodCommand implements Command {

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        // Validação básica
        if (args.length < 2) {
            throw new CommandException("Uso: chmod <permissao_octal> <arquivo_ou_diretorio>");
        }

        String modeStr = args[0];
        String path = args[1];

        // Validar se o modo é um número octal válido (ex: 777, 644)
        int mode;
        try {
            mode = Integer.parseInt(modeStr);
            if (mode < 0 || mode > 777) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new CommandException("Permissão inválida: " + modeStr + ". Use formato octal (ex: 755).");
        }

        // Encontrar o alvo
        Inode target = context.resolvePath(path);

        if (target == null) {
            throw new CommandException("Arquivo ou diretório não encontrado: " + path);
        }

        // Alterar a permissão
        target.setPermissions(Permissions.fromOctal(mode));

        System.out.println("Permissões de '" + path + "' alteradas para " + modeStr);
    }
}