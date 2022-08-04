package net.kanorix.mimicgit;

import net.kanorix.mimicgit.commands.Add;
import net.kanorix.mimicgit.commands.Commit;
import net.kanorix.mimicgit.commands.Init;
import net.kanorix.mimicgit.commands.Restore;
import net.kanorix.mimicgit.commands.Switch;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "mimicgit",
        version = "1.0",
        subcommands = {
                Init.class,
                Add.class,
                Commit.class,
                Switch.class,
                Restore.class,
        })
public class App {

    public static void main(String[] args) {
        System.exit(new CommandLine(new App())
                // .setExecutionExceptionHandler(new ExceptionHandler())
                .execute(args));
    }
}
