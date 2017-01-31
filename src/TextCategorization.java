import java.io.*;
import java.util.ArrayList;
import java.util.Date;
public class TextCategorization {

	final static String outPath = "/home/neetesh/workspace/LearningBased/proc/outFiles";

	public void categorizeText(ArrayList<String> folders) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int done[] = new int[folders.size()];
		String ch = "yes";
		while (ch.equalsIgnoreCase("yes")) {
			System.out.println("List of folders to be categorized");
			for (int i = 0; i < folders.size(); i++) {
				if (done[i] == 0) {
					System.out.println(i + 1 + " " + folders.get(i));
				}
			}
			System.out.print("enter your choice: ");
			int choice = Integer.parseInt(br.readLine());
			if (choice < 1 && choice > folders.size()) {
				System.out.println("Wrong input");
			}
			done[choice - 1] = 1;
			TextProcessor p = new TextProcessor();
			long ini = new Date().getTime();
			// System.out.println(folders.get(choice - 1).toString());
			p.processFolder(folders.get(choice - 1).toString());
			long now = new Date().getTime();
			System.out.println("Time taken by this folder is: ");
			System.out.println((now-ini)/1000 + " sec");
			int count = 0;
			for (int i = 0; i < done.length; i++) {
				if (done[i] == 1)
					count++;
			}
			if (count == done.length) {
				System.out.println();
				System.out.println("All folders are categorized");
				System.out.println("Thank you for using");
				break;
				// System.exit(0);
			}
			System.out.print("do you want to categorize another folder: ");
			ch = br.readLine();
		}
		displayFolders();
	}

	private void displayFolders() {
		System.out.println("Path to output directory: " + outPath);
		File currentDir = new File(outPath);
		displayDirectoryContents(currentDir);

	}

	private void displayDirectoryContents(File currentDir) {
		try {
			File[] files = currentDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					System.out.println("directory:" + file.getCanonicalPath());
					displayDirectoryContents(file);
					System.out.println();
				} else {
					System.out.println("file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
