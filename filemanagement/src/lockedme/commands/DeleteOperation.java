package lockedme.commands;

import java.io.File;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
	name = "delete",
	mixinStandardHelpOptions = true,
	description = {"Delete a file from the input path."},
	subcommands = {CommandLine.HelpCommand.class})
public class DeleteOperation implements Runnable {
    @Parameters(index = "0", description = "The path to the file to delete.")
    private String file;

    public void run() {
        try {
            File fileToBeDeleted = new File(file);

            if (fileToBeDeleted.delete()) {
                System.out.println("File deleted: " + fileToBeDeleted.getName());
            } else {
                System.out.println("File not found.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } finally {
            System.out.println();
        }
    }
 }
