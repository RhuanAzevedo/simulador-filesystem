package commands;

import core.VirtualFileSystem;
import inode.Inode;
import inode.content.DirectoryContent;

import java.util.LinkedList;

public class CommandContext {

    private final VirtualFileSystem vfs;
    private Inode currentDirectory;
    private final LinkedList<String> pathStack;

    public CommandContext(VirtualFileSystem vfs) {
        this.vfs = vfs;
        this.currentDirectory = vfs.getRoot();
        this.pathStack = new LinkedList<>();
    }

    // --- Getters básicos ---

    public VirtualFileSystem getFileSystem() {
        return vfs;
    }

    public Inode getCurrentDirectory() {
        return currentDirectory;
    }

    public String getCurrentPath() {
        if (pathStack.isEmpty()) return "/";
        return "/" + String.join("/", pathStack);
    }

    // --- Métodos de Alteração de Estado (Usados pelo CD) ---

    public void enterDirectory(String name, Inode nextDir) {
        this.currentDirectory = nextDir;
        this.pathStack.add(name);
    }

    public void leaveDirectory(Inode parentDir) {
        this.currentDirectory = parentDir;
        if (!pathStack.isEmpty()) {
            this.pathStack.removeLast();
        }
    }

    public void goToRoot() {
        this.currentDirectory = vfs.getRoot();
        this.pathStack.clear();
    }

    public void setCurrentDirectory(Inode dir) {
        this.currentDirectory = dir;
    }

    // Metodo Utilitário para achar caminhos.
    // Recebe uma string (ex: "home/user/../docs") e retorna o Inode correspondente
    public Inode resolvePath(String path) {
        if (path == null || path.isBlank()) return null;

        Inode node = this.currentDirectory;

        // Se for absoluto, começa da raiz
        if (path.startsWith("/")) {
            node = vfs.getRoot();
        }

        String[] steps = path.split("/");

        for (String step : steps) {
            // Ignora vazios e o próprio diretório
            if (step.isEmpty() || step.equals(".")) {
                continue;
            }

            // Se no meio do caminho achar algo que não é diretório, falhou
            if (!node.isDirectory()) {
                return null;
            }

            DirectoryContent dir = node.asDirectory();

            if (step.equals("..")) {
                // Navega para o pai
                Inode parent = dir.getEntries().get("..");
                if (parent != null) {
                    node = parent;
                }
            } else {
                // Navega para o filho
                Inode next = dir.getEntries().get(step);
                if (next == null) {
                    return null; // Não existe
                }
                node = next;
            }
        }

        return node;
    }
}