package lockedme.commands;

import java.io.File;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
	name = "add",
	mixinStandardHelpOptions = true,
	description = {"Create a file by specifying the path."},
	subcommands = {CommandLine.HelpCommand.class})
public class AddOperation implements Runnable {
    @Parameters(index = "0", description = "The path to the file to create.")
    private String file;

    public void run() {
        try {
            File newFile = new File(file);
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } finally {
            System.out.println();
        }
    }
}
