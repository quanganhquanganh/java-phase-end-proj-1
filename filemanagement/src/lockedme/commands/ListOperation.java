package lockedme.commands;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
	name = "list",
	mixinStandardHelpOptions = true,
	description = {"List files in a particular directory sorted by name in ascending order."},
	subcommands = {CommandLine.HelpCommand.class})
public class ListOperation implements Runnable {
    @Parameters(index = "0", description = "The directory to list.")
    private String directory;

    public void run() {
        Path path = Paths.get(directory);

        String[] files = path.toFile().list();

        Arrays.sort(files);

        for (String file : files) {
            System.out.println(file);
        }

        System.out.println("Total files and folders: " + files.length);
        System.out.println();
    }
 }
