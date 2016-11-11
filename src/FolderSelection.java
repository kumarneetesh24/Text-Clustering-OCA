import java.io.*;
import java.util.*;

public class FolderSelection {
	
	static ArrayList<String> stop;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Main Menu\n");
		ArrayList<String> folders = readFolder();
		if (folders.size() == 0) {
			System.out.println("No folders selected aborting ....");
			System.exit(0);
		}
		int choice = 0;
		stop = readStopWords("stop");
		do {
			System.out.println("Enter your choice:\n1.Text Categorization\n2.Text Summarization\n3.Exit");
			choice = Integer.parseInt(br.readLine());
			if (choice == 2) {
				System.out.println("Text Summarization isn't possible for the time being!");
			} else if (choice == 1) {
				TextCategorization textCat = new TextCategorization();
				textCat.categorizeText(folders);
			} else if (choice == 3) {
				System.exit(0);
			}
		} while (choice == 2);
		

	}

	private static ArrayList<String> readFolder() throws IOException {
		ArrayList<String> folders = new ArrayList<String>();
		String ch = "yes";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (ch.equalsIgnoreCase("yes")) {
			System.out.print("Input Text Folder: ");
			try {
				String path = br.readLine();
				File folder = new File(path);
				if (folder.isDirectory()) {
					if (folder.list().length != 0) {
						folders.add(path);
					} else {
						System.out.println("Folder is empty");
					}
				} else {
					System.out.println("Folder doesnot exists");
				}
				System.out.print("do you like to enter more folders: ");
				ch = br.readLine();
			} catch (InputMismatchException e) {
				System.out.print(e.getMessage());
			}
		}
		return folders;
	}
	
	public static ArrayList<String> readStopWords(String filename) throws IOException {
		ArrayList<String> stopWords = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader("proc/" + filename + ".txt"));
		String word = new String();
		while ((word = br.readLine()) != null) {
			stopWords.add(word);
		}
		br.close();
		//System.out.println(stopWords.size());
		return stopWords;
	}

}
