package net.kanorix.mimicgit;

import net.kanorix.mimicgit.commands.Add;
import net.kanorix.mimicgit.commands.Commit;
import net.kanorix.mimicgit.commands.Init;
import net.kanorix.mimicgit.commands.Switch;
import net.kanorix.mimicgit.exceptions.ExceptionHandler;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "mimicgit",
        version = "1.0",
        subcommands = {
                Init.class,
                Switch.class,
                Add.class,
                Commit.class
        })
public class App {

    public static void main(String[] args) {
        System.exit(new CommandLine(new App())
                .setExecutionExceptionHandler(new ExceptionHandler())
                .execute(args));
    }
}
