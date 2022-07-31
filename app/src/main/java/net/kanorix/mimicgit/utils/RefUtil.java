package net.kanorix.mimicgit.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class RefUtil {

    public static Path HEAD = GitRepositoryUtil.BASE.resolve("HEAD");

    public static Path REFS = GitRepositoryUtil.BASE.resolve("refs/heads");

    /**
     * ブランチを作成します。
     *
     * @param branchName ブランチ名
     * @throws IOException IO例外
     */
    public static void createBranch(final String branchName, final String ref) throws IOException {
        final var branch = REFS.resolve(branchName);
        if (Files.notExists(branch)) {
            // ファイルを作成する
            Files.createFile(branch);
            // 参照を追加する
            updateRef(branchName, ref);
        } else {
            throw new RuntimeException("Exists branch name [%s]".formatted(branchName));
        }
    }

    /**
     * 現在のブランチを取得します。
     *
     * @return 現在のブランチ
     */
    public static String currentBranchName() {
        try {
            final var head = Files.readString(HEAD);
            if (head.isEmpty()) {
                // なければ「main」ブランチを作成する
                RefUtil.updateHead("main");
                return "main";
            }
            return head.replaceAll("ref: refs/heads/", "");
        } catch (IOException e) {
            throw new RuntimeException("Can't read HEAD");
        }
    }

    /**
     * 現在のHEADが指しているCommitのハッシュ値を取得します。
     *
     * @return Commitのハッシュ値
     */
    public static Optional<String> resolveHead() {
        return resolveBranch(currentBranchName());
    }

    /**
     * ブランチ名からCommitのハッシュ値を取得します。
     *
     * @param branchName ブランチ名
     * @return Commitのハッシュ値
     */
    public static Optional<String> resolveBranch(final String branchName) {
        try {
            final String commitHash = Files.readString(REFS.resolve(branchName));
            return Optional.ofNullable(commitHash.isEmpty() || commitHash.isBlank()
                    ? null
                    : commitHash);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * 何らかの識別子からCommitのハッシュ値を取得します。
     *
     * @param ident 何らかの識別子（hash / branchNameなど）
     * @return Commitのハッシュ値
     */
    public static Optional<String> resolve(final String ident) {
        // hashだった場合
        if (GitRepositoryUtil.exists(ident)) {
            return Optional.of(ident);
        }

        // 参照だった場合
        if (ident.startsWith("ref:")) {
            return resolve(ident.replaceAll("ref: refs/heads/", ""));
        }

        // branchとして処理する
        return resolveBranch(ident);
    }

    /**
     * 参照を更新する
     *
     * @param branchName ブランチ名
     * @param filename 追加するファイル
     * @throws IOException IO例外
     */
    public static void updateRef(final String branchName, final String hash) throws IOException {
        // ブランチが参照しているコミットを更新
        Files.writeString(REFS.resolve(branchName), hash, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * HEADの参照を更新する
     *
     * @param branchName HEADに指定するブランチ名
     * @return ブランチ名
     */
    public static String updateHead(final String branchName) {
        try {
            final String head = "ref: refs/heads/%s".formatted(branchName);
            // HEADが参照しているブランチを更新
            Files.writeString(HEAD, head, StandardOpenOption.TRUNCATE_EXISTING);
            return branchName;
        } catch (IOException e) {
            throw new RuntimeException("Can't update HEAD by [%s]".formatted(branchName));
        }
    }
}
