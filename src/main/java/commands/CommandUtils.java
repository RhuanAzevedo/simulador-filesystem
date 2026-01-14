package commands;

import inode.Inode;
import inode.Permissions;
import inode.content.DirectoryContent;

import java.util.List;
import java.util.Map;

public final class CommandUtils {

    private CommandUtils() {
    }

    public static class ParentAndName {
        private final Inode parent;
        private final String name;
        private final String parentPath;

        public ParentAndName(Inode parent, String name, String parentPath) {
            this.parent = parent;
            this.name = name;
            this.parentPath = parentPath;
        }

        public Inode getParent() {
            return parent;
        }

        public String getName() {
            return name;
        }

        public String getParentPath() {
            return parentPath;
        }
    }

    public static ParentAndName resolveParent(CommandContext context, String path) throws CommandException {
        if (path == null || path.isBlank()) {
            throw new CommandException("Caminho invalido.");
        }

        // Resolve o diretorio pai e o nome do alvo
        int lastSlashIndex = path.lastIndexOf('/');
        String parentPath;
        String name;

        if (lastSlashIndex == -1) {
            parentPath = ".";
            name = path;
        } else if (lastSlashIndex == 0) {
            parentPath = "/";
            name = path.substring(1);
        } else {
            parentPath = path.substring(0, lastSlashIndex);
            name = path.substring(lastSlashIndex + 1);
        }

        if (name.isEmpty() || name.equals(".") || name.equals("..")) {
            throw new CommandException("Nome de destino invalido: " + name);
        }

        Inode parent = context.resolvePath(parentPath);
        if (parent == null || !parent.isDirectory()) {
            throw new CommandException("Diretorio pai nao encontrado: " + parentPath);
        }

        return new ParentAndName(parent, name, parentPath);
    }

    public static String baseName(String path) throws CommandException {
        if (path == null || path.isBlank()) {
            throw new CommandException("Caminho invalido.");
        }
        int lastSlashIndex = path.lastIndexOf('/');
        String name = lastSlashIndex == -1 ? path : path.substring(lastSlashIndex + 1);
        if (name.isEmpty() || name.equals(".") || name.equals("..")) {
            throw new CommandException("Nome de destino invalido: " + name);
        }
        return name;
    }

    public static String toAbsolutePath(CommandContext context, String path) {
        // Monta um caminho absoluto a partir do contexto atual
        String absolute;
        if (path == null || path.isBlank()) {
            return context.getCurrentPath();
        }
        if (path.startsWith("/")) {
            absolute = path;
        } else if (path.equals(".")) {
            absolute = context.getCurrentPath();
        } else if (context.getCurrentPath().equals("/")) {
            absolute = "/" + path;
        } else {
            absolute = context.getCurrentPath() + "/" + path;
        }
        if (absolute.length() > 1 && absolute.endsWith("/")) {
            absolute = absolute.substring(0, absolute.length() - 1);
        }
        return absolute;
    }

    public static Inode deepCopy(Inode source, Inode newParent) {
        Inode copy;
        if (source.isFile()) {
            copy = Inode.createFile();
            List<String> lines = source.asFile().getLines();
            for (String line : lines) {
                copy.asFile().write(line, true);
            }
        } else {
            // Copia recursiva da arvore de diretorios
            copy = Inode.createDirectory(newParent);
            DirectoryContent srcDir = source.asDirectory();
            DirectoryContent destDir = copy.asDirectory();
            for (Map.Entry<String, Inode> entry : srcDir.getEntries().entrySet()) {
                String name = entry.getKey();
                if (name.equals(".") || name.equals("..")) {
                    continue;
                }
                Inode childCopy = deepCopy(entry.getValue(), copy);
                destDir.addChild(name, childCopy);
            }
        }

        Permissions perms = source.getPermissions();
        copy.setPermissions(new Permissions(perms.owner(), perms.group(), perms.others()));
        copy.setOwner(source.getOwner());
        return copy;
    }

    public static boolean isDescendant(Inode ancestor, Inode node) {
        // Verifica se um inode esta dentro da subarvore de outro
        if (ancestor == node) {
            return true;
        }
        if (!ancestor.isDirectory()) {
            return false;
        }
        DirectoryContent content = ancestor.asDirectory();
        for (Map.Entry<String, Inode> entry : content.getEntries().entrySet()) {
            String name = entry.getKey();
            if (name.equals(".") || name.equals("..")) {
                continue;
            }
            if (isDescendant(entry.getValue(), node)) {
                return true;
            }
        }
        return false;
    }
}
