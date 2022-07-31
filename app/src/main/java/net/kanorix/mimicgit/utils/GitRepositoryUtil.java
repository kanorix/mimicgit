package net.kanorix.mimicgit.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map.Entry;

import net.kanorix.mimicgit.objects.BlobObject;
import net.kanorix.mimicgit.objects.CommitObject;
import net.kanorix.mimicgit.objects.GitObject;
import net.kanorix.mimicgit.objects.GitObject.ObjectType;
import net.kanorix.mimicgit.objects.TreeObject;

public class GitRepositoryUtil {

    public static Path BASE = Paths.get("mimicgit");

    public static Path DIR = BASE.resolve("objects");

    private GitRepositoryUtil() {}

    /**
     * hashに紐づくGitObjectが存在するか
     *
     * @param hash ハッシュ値
     * @return GitObjectが存在する場合、true
     */
    public static boolean exists(final String hash) {
        final Path path = DIR.resolve(hash);
        return Files.exists(path);
    }

    /**
     * hashに紐づくGitObjectを取得します。
     *
     * @param hash ハッシュ値
     * @return GitObject
     */
    public static GitObject find(final String hash) {
        final Path path = DIR.resolve(hash);

        try {
            final List<String> contents = Files.readAllLines(path);
            final String type = contents.remove(0);

            return switch (ObjectType.valueOf(type)) {
                case Blob -> new BlobObject(contents);
                case Tree -> createTreeObject(contents);
                case Commit -> new CommitObject(contents);
            };
        } catch (IOException e) {
            throw new RuntimeException("Not found GitObject [%s]".formatted(hash));
        }
    }

    /**
     * GitObjectを保存します。
     *
     * @param object GitObject
     * @return 保存されたGitObject
     * @throws IOException
     */
    public static GitObject save(final GitObject object) throws IOException {
        final String hash = object.getHash();
        final var path = DIR.resolve(hash);

        // 同じハッシュが既に存在している場合、変更なし
        if (!Files.exists(path)) {
            Files.createFile(path);
            Files.writeString(path, object.toString());
        }

        // Treeの場合は、再帰的に保存
        if (object.getType() == ObjectType.Tree) {
            final var tree = (TreeObject) object;
            for (var entry : tree.entrySet()) {
                save(entry.getValue());
            }
        }
        return object;
    }

    /**
     * コミットの内容を作業ディレクトリに復元します。
     *
     * @param object GitObject
     * @throws IOException IO例外
     */
    public static void restore(final CommitObject object) {
        final var commit = (CommitObject) object;
        final var rootTree = (TreeObject) find(commit.tree);

        // 作業ディレクトリにTreeを書き込む
        restore(Path.of("."), rootTree);
    }

    /**
     * Treeの内容をディレクトリに復元します。
     *
     * @param dir ディレクトリ
     * @param tree TreeObject
     * @throws IOException IO例外
     */
    public static void restore(final Path dir, final TreeObject tree) {
        for (Entry<String, GitObject> entry : tree.entrySet()) {
            final var name = entry.getKey();
            final var object = entry.getValue();
            final var path = dir.resolve(name);

            // Treeなら再帰的に復元
            if (object.getType() == ObjectType.Tree) {
                restore(path, (TreeObject) object);
                break;
            }

            // TreeでなければBlobなので復元
            restore(path, (BlobObject) object);
        }
    }

    /**
     * Blobの内容をディレクトリに復元します。
     *
     * @param dir ディレクトリ
     * @param tree BlobObject
     * @throws IOException IO例外
     */
    public static void restore(final Path dir, final BlobObject blob) {
        try {
            // 該当ファイルに書き込み
            Files.writeString(dir, blob.getContent());
        } catch (IOException e) {
            throw new RuntimeException("Can't restore GitObject [%s]".formatted(dir));
        }
    }

    /**
     * Indexの情報をもとにTreeを上書きします。
     *
     * @param rootTree ルートのTreeObject
     * @return 上書きされたTreeObject
     * @throws IOException IO例外
     */
    public static TreeObject writeTreeByIndex(final TreeObject rootTree) throws IOException {
        // Indexの情報でツリーを上書きする
        for (var ie : IndexUtil.getIndexEntries()) {
            TreeUtil.set(rootTree, ie.getFilePath(), GitRepositoryUtil.find(ie.getHash()));
        }

        // ツリーの状態をリポジトリに保存
        GitRepositoryUtil.save(rootTree);

        // Indexの中身を削除する
        IndexUtil.clearIndex();

        // ルートツリーを返す
        return rootTree;
    }

    /**
     * TreeObjectを作成します。
     *
     * @param entries TreeObjectのエントリ
     * @return TreeObject
     */
    private static TreeObject createTreeObject(final List<String> entries) {
        final TreeObject tree = new TreeObject();

        for (String entry : entries) {
            final String[] parts = entry.split(" ", 2);
            tree.put(parts[0], find(parts[1]));
        }
        return tree;
    }
}
