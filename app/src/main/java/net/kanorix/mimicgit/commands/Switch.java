package net.kanorix.mimicgit.commands;

import java.util.concurrent.Callable;

import net.kanorix.mimicgit.objects.CommitObject;
import net.kanorix.mimicgit.utils.GitRepositoryUtil;
import net.kanorix.mimicgit.utils.RefUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "switch", helpCommand = true)
public class Switch implements Callable<Integer> {

    @Option(names = { "-c" }, description = "ブランチを作る")
    private boolean createBranch;

    @Parameters(description = "ブランチ名")
    private String branch;

    @Override
    public Integer call() throws Exception {
        // Commitのハッシュを取得する
        final String commitHash = createBranch
                ? RefUtil.resolveHead().orElseThrow()
                : RefUtil.resolveBranch(branch).orElseThrow();

        // CommitのハッシュからObjectを取得する
        final var commit = (CommitObject) GitRepositoryUtil.find(commitHash);

        // ワーキングディレクトリを復元する
        GitRepositoryUtil.restore(commit);

        // ブランチを作成する
        if (createBranch) {
            RefUtil.createBranch(branch, commit.getHash());
        }

        // HEADを更新する
        RefUtil.updateHead(branch);

        return 0;
    }
}
