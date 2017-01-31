import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class TextProcessor {
	
	public void processFolder(String path) throws IOException {
		TextProcessor process = new TextProcessor();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		int numberOfFiles = listOfFiles.length;
		/*for (int i = 0; i < numberOfFiles; i++) {
			System.out.println(listOfFiles[i].getName());
		}*/
		Cluster cluster = new Cluster();
		for (int i = 0; i < numberOfFiles; i++) {
			if (listOfFiles[i].isFile()) {
				// System.out.println(listOfFiles[i].getName());
				process.removeStopWords(path, listOfFiles[i].getName());
				long ini = new Date().getTime();
				cluster.create(path,listOfFiles[i].getName());
				long now = new Date().getTime();
				System.out.println("Time taken by file "+ listOfFiles[i].getName() +" is: ");
				System.out.println((now-ini)/1000 + " sec");
				File tmp_file = new File(path+"/tmp_"+listOfFiles[i].getName());
				tmp_file.delete();
			}
		}
	}
	private void removeStopWords(String path, String fileName) throws IOException {
		String tmpFileName = "tmp_" + fileName;

		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(path + "/" + fileName));
			bw = new BufferedWriter(new FileWriter(path + "/" + tmpFileName));
			String line;
			while ((line = br.readLine()) != null) {
				String words[] = line.split("[ \n\r\t,.;:?!\"()]+");
				int len = words.length;
				for (int i = 0; i < len; i++) {
					if (!isStopWord(words[i])) {
						bw.write(words[i] + " ");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

//		File oldFile = new File(path + "/" + fileName);
//		oldFile.delete();

		// And rename tmp file's name to old file name
//		File newFile = new File(path + "/" + tmpFileName);
//		newFile.renameTo(oldFile);

	}

	private boolean isStopWord(String line) {
		boolean isStopWord = false;
		int len = FolderSelection.stop.size();
		for (int i = 0; i < len; i++) {
			if (line.compareToIgnoreCase(FolderSelection.stop.get(i)) == 0) {
				// System.out.println("[" + FolderSelection.stop.get(i) + "]");
				return true;
			}
		}
		return isStopWord;
	}

	public static ArrayList<String> readWords(String path) throws IOException {
		ArrayList<String> words = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(path));
		String word = new String();
		while ((word = br.readLine()) != null) {
			String arr[] = word.split(" ");
			for (int i = 0; i < arr.length; i++) {
				words.add(arr[i]);
			}
		}
		br.close();
		// System.out.println(stopWords.size());
		return words;
	}

}
