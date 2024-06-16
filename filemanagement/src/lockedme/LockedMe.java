package lockedme;

import org.fusesource.jansi.AnsiConsole;
import org.jline.builtins.Completers;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import lockedme.commands.*;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.shell.jline3.PicocliCommands;
import picocli.shell.jline3.PicocliCommands.PicocliCommandsFactory;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

public class LockedMe {
    @Command(name = "",
            subcommands = {
            	AddOperation.class,
            	DeleteOperation.class,
            	ListOperation.class,
            	SearchOperation.class,
                PicocliCommands.ClearScreen.class
            })
    static class CliCommands implements Runnable {
        PrintWriter out;

        CliCommands() {}

        public void setReader(LineReader reader){
            out = reader.getTerminal().writer();
        }

        public void run() {
            out.println(new CommandLine(this).getUsageMessage());
        }
    }
    
    private static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        clearScreen();
        try {
            Supplier<Path> workDir = () -> Paths.get(System.getProperty("user.dir"));
            CliCommands commands = new CliCommands();

            PicocliCommandsFactory factory = new PicocliCommandsFactory();

            CommandLine cmd = new CommandLine(commands, factory);
            PicocliCommands picocliCommands = new PicocliCommands(cmd);

            Parser parser = new DefaultParser();
            try (Terminal terminal = TerminalBuilder.builder().build()) {
                SystemRegistry systemRegistry = new SystemRegistryImpl(parser, terminal, workDir, null);
                systemRegistry.setCommandRegistries(picocliCommands);
                systemRegistry.register("help", picocliCommands);

                LineReader reader = LineReaderBuilder.builder()
                        .terminal(terminal)
                        .completer(new AggregateCompleter(systemRegistry.completer(), new Completers.FileNameCompleter()))
                        .parser(parser)
                        .variable(LineReader.LIST_MAX, 50)
                        .build();
                commands.setReader(reader);
                factory.setTerminal(terminal);

                String prompt = "manage> ";
                String rightPrompt = null;

                // start the shell and process input until the user quits with Ctrl-D
                String line;
                while (true) {
                    try {
                    	terminal.enterRawMode();
                        terminal.writer().println("Welcome to LockedMe!");
                        terminal.writer().println("Developed by: Do Bui Quang Anh.");
                        terminal.writer().println("Email: quanganhdb2510@gmail.com");
                        terminal.writer().println("This application allows you to manage files in a directory.");
                        terminal.writer().println("List of available commands:");
                        terminal.writer().println("add - Create a file by specifying the path.");
                        terminal.writer().println("delete - Delete a file from the input path.");
                        terminal.writer().println("list - List files in a particular directory sorted by name in ascending order.");
                        terminal.writer().println("search - Search for a file in the directory.");
                        terminal.writer().println("clear - Clear the screen.");
                        terminal.writer().println("help - Display help information.");
                        terminal.writer().println("exit - Exit the application.");
                        terminal.writer().println();
                        terminal.writer().flush();
                        systemRegistry.cleanUp();
                        line = reader.readLine(prompt, rightPrompt, (MaskingCallback) null, null);
                        systemRegistry.execute(line);
                    } catch (UserInterruptException e) {
                        // Ignore
                    } catch (EndOfFileException e) {
                        return;
                    } catch (Exception e) {
                        systemRegistry.trace(e);
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }
}