package net.kanorix.mimicgit.utils;

import java.nio.file.Path;
import java.util.Optional;

import net.kanorix.mimicgit.objects.GitObject;
import net.kanorix.mimicgit.objects.TreeObject;

public class TreeUtil {

    public static Optional<GitObject> get(final TreeObject tree, final Path path) {
        final int nameCount = path.getNameCount() - 1;
        if (nameCount == 0) {
            // 末端なので取得する
            final String filename = path.toString();
            return tree.get(filename);
        }
        // 途中であればさらに辿る
        final var dirpath = path.getName(0);
        final var dirname = dirpath.toString();
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
        if (nameCount == 0) {
            // 末端なので置く
            final String filename = path.toString();
            tree.put(filename, object);
            return;
        }
        // 途中ということはTree
        final var dirpath = path.getName(0);
        final var dirname = dirpath.toString();
        final var child = tree.get(dirname)
                .orElseGet(() -> tree.put(dirname, new TreeObject()));

        // 子Treeに追加
        set((TreeObject) child, dirpath, object);
    }
}
