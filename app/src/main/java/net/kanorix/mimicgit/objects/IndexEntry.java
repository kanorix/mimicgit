package net.kanorix.mimicgit.objects;

import java.nio.file.Path;

public class IndexEntry {

    public IndexEntry(String entry) {
        final String[] parts = entry.split(" ", 2);
        this.hash = parts[0];
        this.filepath = parts[1];
    }

    public IndexEntry(String hash, String filepath) {
        this.hash = hash;
        this.filepath = filepath;
    }

    /** ハッシュ */
    private String hash;

    /** ファイルのパス */
    private String filepath;

    /**
     * ハッシュを取得します。
     *
     * @return ハッシュ
     */
    public String getHash() {
        return hash;
    }

    /**
     * ファイルのパスを取得します。
     *
     * @return ファイルのパス
     */
    public Path getFilePath() {
        return Path.of(filepath);
    }

    @Override
    public String toString() {
        return "%s %s".formatted(hash, filepath);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IndexEntry)) {
            return false;
        }
        final IndexEntry obj = (IndexEntry) object;
        return obj.filepath.equals(filepath) && obj.hash.equals(hash);
    }
}
