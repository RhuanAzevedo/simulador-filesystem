package core;

import inode.Inode;

public class VirtualFileSystem {

    private final Inode root;

    public VirtualFileSystem() {
        this.root = Inode.createRoot();
        initDefaultStructure();
    }

    private void initDefaultStructure() {
        // /
        Inode home = Inode.createDirectory(root);
        Inode bin  = Inode.createDirectory(root);
        Inode etc  = Inode.createDirectory(root);
        Inode tmp  = Inode.createDirectory(root);
        Inode var  = Inode.createDirectory(root);

        root.asDirectory().addChild("home", home);
        root.asDirectory().addChild("bin", bin);
        root.asDirectory().addChild("etc", etc);
        root.asDirectory().addChild("tmp", tmp);
        root.asDirectory().addChild("var", var);

        // /home/user
        Inode user = Inode.createDirectory(home);
        user.setOwner("user");

        home.asDirectory().addChild("user", user);
    }

    public Inode getRoot() {
        return root;
    }

}
