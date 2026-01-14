package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import commands.CommandUtils;
import inode.Inode;

import java.time.format.DateTimeFormatter;

public class StatCommand implements Command {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if (args.length != 1) {
            throw new CommandException("Uso: stat <nome>");
        }

        String path = args[0];
        Inode target = context.resolvePath(path);

        if (target == null) {
            throw new CommandException("Arquivo ou diretorio nao encontrado: " + path);
        }

        // Exibe informacoes detalhadas do inode
        String absolutePath = CommandUtils.toAbsolutePath(context, path);
        long size = target.getSize();
        long blocks = size == 0 ? 0 : (size + 511) / 512;

        System.out.println("Arquivo: " + absolutePath);
        System.out.println("Tipo: " + (target.isDirectory() ? "diretorio" : "arquivo"));
        System.out.println("Tamanho: " + size);
        System.out.println("Blocos: " + blocks);
        System.out.println("Inode: " + target.getId());
        System.out.println("Links: " + target.getLinkCount());
        System.out.println("Permissoes: " + target.getPermissions().toSymbol());
        System.out.println("Dono: " + target.getOwner());
        System.out.println("Criado: " + target.getCreatedAt().format(DATE_FMT));
        System.out.println("Modificado: " + target.getModifiedAt().format(DATE_FMT));
    }
}
