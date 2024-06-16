package lockedme.commands;

import java.nio.file.Path;
import java.nio.file.Paths;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
	name = "search",
	mixinStandardHelpOptions = true,
	description = {"Search for a file in the directory."},
	subcommands = {CommandLine.HelpCommand.class})
public class SearchOperation implements Runnable {
    @Parameters(index = "0", description = "The path to the file to search.")
    private String file;

    // Optional parameter: the directory to search in or the current directory by default
    @Parameters(index = "1", description = "The directory to search in.")
    private String directory = ".";

    public void run() {
        Path path = Paths.get(directory);

        String[] files = path.toFile().list();

        for (String file : files) {
            if (file.equals(this.file)) {
                System.out.println(path.resolve(file).toString());
                return;
            }
        }

        System.out.println("File not found: " + this.file);
        System.out.println();
    } 
 }
