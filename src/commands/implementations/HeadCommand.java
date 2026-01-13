package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;
import inode.Inode;

import java.util.List;

public class HeadCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        // verifica quantidade de argumentos do comando
        if(args.length < 1){
            throw new CommandException("Too few arguments.");
        } else if (args.length > 2) {
            throw new CommandException("Too many arguments.");
        }
        int lines = 10;
        Inode current = context.getCurrentDirectory();
        String name = args[0];
        if(context.resolvePath(name) != null){
            // Verifica se o caminho é válido, se é um arquivo e se o argumento de qtd de linhas é inteiro
            if (context.resolvePath(name).isFile()) {
                try{
                    current = context.resolvePath(name);
                    if(args.length == 2){
                        lines = Integer.parseInt(args[1]);
                    }
                    List<String> output = current.asFile().getLines();
                    // exibe "lines" linhas ou todas, caso "lines" seja maior que a quantidade de linhas disponiveis
                    for(int i = 0; i < Math.min(lines, output.size()); i++){
                        System.out.println(output.get(i));
                    }

                }catch(NumberFormatException e){
                    throw new CommandException("Invalid arguments.");
                }
            }else {
                throw new CommandException(name + " is not a file.");
            }
        }else {
            throw new CommandException(name + " not found.");
        }

    }
}
