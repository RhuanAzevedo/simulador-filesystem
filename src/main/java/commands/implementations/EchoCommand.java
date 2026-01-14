package commands.implementations;

import commands.Command;
import commands.CommandContext;
import commands.CommandException;

import static core.Shell.GREEN;

public class EchoCommand implements Command {
    @Override
    public void execute(String[] args, CommandContext context) throws CommandException {
        if(args.length == 0){
            System.out.println(GREEN + "echo");
        }else if(args.length == 1){
            System.out.println(GREEN + args[0]);
        }else if(args.length == 2){
            throw new CommandException("Missing file path.");
        }else if(args.length > 3){
            throw new CommandException("Too many arguments");
        }else{
            if (context.resolvePath(args[2])!=null) {
                if(args[1].equals(">")){
                        if(context.resolvePath(args[2]).isFile()){
                            context.resolvePath(args[2]).asFile().write(args[0], false);
                            System.out.println(GREEN + "Overwrite successful.");
                        }else {
                            System.out.println(GREEN + args[0] + " does not exist.");
                        }
                } else if (args[1].equals(">>")) {
                    if(context.resolvePath(args[2]).isFile()){
                        context.resolvePath(args[2]).asFile().write(args[0], true);
                        System.out.println(GREEN + "Append successful.");
                    }else  {
                        System.out.println(GREEN + args[0] + " does not exist.");
                    }
                }else{
                    throw new CommandException("Invalid argument");
                }
            }else {
                throw new CommandException("Invalid path/file");
            }
        }
    }
}
