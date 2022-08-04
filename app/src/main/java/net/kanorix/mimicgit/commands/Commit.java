package net.kanorix.mimicgit.commands;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;

import net.kanorix.mimicgit.objects.CommitObject;
import net.kanorix.mimicgit.objects.TreeObject;
import net.kanorix.mimicgit.utils.GitRepositoryUtil;
import net.kanorix.mimicgit.utils.RefUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "commit")
public class Commit implements Callable<Integer> {

    @Parameters(description = "commitのメッセージ")
    private String message;

    @Override
    public Integer call() throws IOException {
        // 現在のブランチを取得する
        final String branch = RefUtil.currentBranchName();

        // 親コミットを取得
        final Optional<String> currentCommit = RefUtil.resolveBranch(branch);

        // 親コミットからツリーを取得（なければ新規ツリー）
        final TreeObject parentTree = (TreeObject) currentCommit
                .map(GitRepositoryUtil::find)
                .map(o -> ((CommitObject) o).tree)
                .map(GitRepositoryUtil::find)
                .orElse(new TreeObject());

        // IndexからTreeを更新し書き込む
        final TreeObject rootTree = GitRepositoryUtil.writeTreeByIndex(parentTree);

        // Commit作成と保存
        final var commit = new CommitObject(rootTree.getHash(), currentCommit.orElse(""));
        GitRepositoryUtil.save(commit);

        // Commitのハッシュを取得
        final var commitHash = commit.getHash();

        // ブランチの参照を更新する
        RefUtil.updateRef(branch, commitHash);

        return 0;
    }
}
