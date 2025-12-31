package inode;

import java.time.LocalDateTime;

import inode.enums.ContentType;
import inode.content.Content;
import inode.content.DirectoryContent;
import inode.content.FileContent;

public class Inode {

    private static int nextId = 1;

    private final int id;
    private final Content content;

    private String owner;
    private Permissions permissions;
    private int linkCount = 1;

    private final LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private Inode(Content content) {
        this.id = nextId++;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = this.createdAt;

        // permissões padrão conforme tipo
        if (content.getType() == ContentType.DIRECTORY) {
            this.permissions = Permissions.defaultDirectory();
        } else {
            this.permissions = Permissions.defaultFile();
        }

        this.owner = "user"; // default simples
    }

    /* =========================
       FACTORY METHODS
       ========================= */

    public static Inode createFile() {
        return new Inode(new FileContent());
    }

    public static Inode createDirectory(Inode parent) {
        Inode dir = new Inode(new DirectoryContent());
        dir.asDirectory().init(parent, dir);
        return dir;
    }

    public static Inode createRoot() {
        Inode root = new Inode(new DirectoryContent());
        DirectoryContent dir = root.asDirectory();

        dir.init(root, root); // "." e ".." apontam para ele mesmo

        root.owner = "root";
        root.permissions = Permissions.fromOctal(755);

        return root;
    }

    /* =========================
       TYPE CHECKS
       ========================= */

    public boolean isFile() {
        return content.getType() == ContentType.FILE;
    }

    public boolean isDirectory() {
        return content.getType() == ContentType.DIRECTORY;
    }

    public FileContent asFile() {
        return (FileContent) content;
    }

    public DirectoryContent asDirectory() {
        return (DirectoryContent) content;
    }

    /* =========================
       METADATA
       ========================= */

    public long getSize() {
        return content.getSize();
    }

    public int getId() {
        return id;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
        touch();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getLinkCount() {
        return linkCount;
    }

    public void incrementLinkCount() {
        linkCount++;
    }

    public void decrementLinkCount() {
        linkCount--;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    private void touch() {
        this.modifiedAt = LocalDateTime.now();
    }
}
