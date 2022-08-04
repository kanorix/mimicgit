package net.kanorix.mimicgit.utils;

import java.nio.file.Path;
import java.util.Optional;

import net.kanorix.mimicgit.objects.GitObject;
import net.kanorix.mimicgit.objects.TreeObject;

public class TreeUtil {

    public static Optional<GitObject> get(final TreeObject tree, final Path path) {
        final int nameCount = path.getNameCount() - 1;
        System.out.println(path);
        if (nameCount == 0) {
            // 末端なので取得する
            final String filename = path.toString();
            return tree.get(filename);
        }
        // 途中であればさらに辿る
        final var dirpath = path.subpath(1, path.getNameCount());
        final var dirname = path.getName(0).toString();
        final var child = tree.get(dirname);
        return child.flatMap(childTree -> get((TreeObject) childTree, dirpath));
    }

    /**
     * 指定されたパスに従うようにTreeを再帰的にたどり、GitObjectを置きます。
     *
     * @param path パス
     * @param object GitObject
     */
    public static void set(final TreeObject tree, final Path path, final GitObject object) {
        final int nameCount = path.getNameCount() - 1;

        System.out.println("dirpath: " + path);

        if (nameCount != 0) {
            // 途中ということはTree
            final var dirpath = path.getName(0);
            final var dirname = dirpath.toString();
            System.out.println("dirpath: " + dirpath);
            final var child = tree.get(dirname)
                    .orElseGet(() -> tree.put(dirname, new TreeObject()));
            // 一つ下の階層でセットする
            final var deep = path.subpath(1, path.getNameCount());
            set((TreeObject) child, deep, object);
            return;
        }
        // 末端なので置く
        final String filename = path.getFileName().toString();
        tree.put(filename, object);
        return;
    }
}
