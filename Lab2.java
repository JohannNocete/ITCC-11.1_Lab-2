import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class FileNode {
    String name;
    boolean isDirectory;
    List<FileNode> children;

    FileNode(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.children = new ArrayList<>();
    }

    void addChild(FileNode child) {
        if (this.isDirectory) {
            this.children.add(child);
        }
    }
}

interface FileFoundListener {
    void onFileFound(String filePath);
}

class FileSearcher {
    private FileFoundListener listener;
    private BufferedWriter writer;

    FileSearcher(FileFoundListener listener) throws IOException {
        this.listener = listener;
        this.writer = new BufferedWriter(new FileWriter("search_results.txt"));
    }

    void search(File directory, String extension) throws IOException {
        if (!directory.isDirectory()) {
            return;
        }
        
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                search(file, extension);
            } else if (file.getName().endsWith(extension)) {
                listener.onFileFound(file.getAbsolutePath());
                writer.write("File found: " + file.getAbsolutePath() + "\n");
            }
        }
    }

    void closeWriter() throws IOException {
        writer.close();
    }
}

public class Lab2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter directory path: ");
        String dirPath = scanner.nextLine();
        System.out.print("Enter file extension to search for: ");
        String extension = scanner.nextLine();
        
        File startDirectory = new File(dirPath);
        if (!startDirectory.exists() || !startDirectory.isDirectory()) {
            System.out.println("Invalid directory path.");
            return;
        }

        try {
            FileSearcher searcher = new FileSearcher(filePath -> System.out.println("File found: " + filePath));
            System.out.println("Searching...");
            searcher.search(startDirectory, extension);
            searcher.closeWriter();
            System.out.println("Search completed. Results saved to search_results.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
