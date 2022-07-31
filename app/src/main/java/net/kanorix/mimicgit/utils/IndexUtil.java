package net.kanorix.mimicgit.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

import net.kanorix.mimicgit.objects.BlobObject;
import net.kanorix.mimicgit.objects.GitObject;
import net.kanorix.mimicgit.objects.IndexEntry;

public class IndexUtil {

    public static Path INDEX = GitRepositoryUtil.BASE.resolve("index");

    public static void clearIndex() throws IOException {
        Files.writeString(INDEX, "", StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Indexを更新する
     *
     * @param filename 追加するファイル
     * @throws IOException IO例外
     */
    public static void update(Path filename) throws IOException {

        // ファイルからBlobObjectを作成する
        final GitObject object = new BlobObject(Files.readAllLines(filename));

        // GitObjectを保存する
        GitRepositoryUtil.save(object);

        // ファイルがなければ作成する
        if (Files.notExists(INDEX)) {
            Files.createFile(INDEX);
        }

        // Indexに含めるエントリを作成
        final IndexEntry newEntry = new IndexEntry(object.getHash(), filename.toString());

        // Indexのエントリを取り出す
        final List<IndexEntry> contents = getIndexEntries();

        // Indexに追加し、重複を省く
        final List<String> entries = Stream.concat(contents.stream(), Stream.of(newEntry))
                .distinct()
                .map(IndexEntry::toString)
                .toList();

        // Indexファイルに書き込む
        Files.write(INDEX, entries, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Indexに含まれているファイルの一覧を取り出します。
     *
     * @return Indexに含まれているファイル一覧
     * @throws IOException
     */
    public static List<IndexEntry> getIndexEntries() throws IOException {
        if (Files.notExists(INDEX)) {
            // Indexがなければ作成
            Files.createFile(INDEX);
            return List.of();
        }
        return Files.readAllLines(INDEX).stream()
                .map(IndexEntry::new)
                .toList();
    }
}
