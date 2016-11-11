import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class TextProcessor {
	

	public void processFolder(String path) throws IOException {
		TextProcessor process = new TextProcessor();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		int numberOfFiles = listOfFiles.length;
		/*for (int i = 0; i < numberOfFiles; i++) {
			System.out.println(listOfFiles[i].getName());
		}*/
		for (int i = 0; i < numberOfFiles; i++) {
			if (listOfFiles[i].isFile()) {
				// System.out.println(listOfFiles[i].getName());
				process.removeStopWords(path, listOfFiles[i].getName());
			}
		}
		double intersectionMat[][] = new double[numberOfFiles][numberOfFiles];
		for (int i = 0; i < numberOfFiles; i++) {
			String path1 = path + "/" + listOfFiles[i].getName();
			ArrayList<String> wordList1 = readWords(path1);
			for (int j = i + 1; j < numberOfFiles; j++) {
				String path2 = path + "/" + listOfFiles[j].getName();
				ArrayList<String> wordList2 = readWords(path2);
				intersectionMat[i][j] = calSimilarity(wordList1, wordList2);
			}
		}
		/*for (int i = 0; i < numberOfFiles; i++) {
			for (int j = i + 1; j < numberOfFiles; j++) {
				System.out.print(intersectionMat[i][j] + " ");
			}
			System.out.println();
		}
*/
		createClusters(intersectionMat,listOfFiles,path);

	}

	private void createClusters(double[][] intersectionMat, File[] listOfFiles, String path) {
		int len = listOfFiles.length;
		boolean created = false;
		// ArrayList<String> clusters = new ArrayList<String>();
		double threshold = 15.0;
		for (int i = 0; i < len; i++) {
			for (int j = i + 1; j < len; j++) {
				if (intersectionMat[i][j] > threshold) {
					createFolder(TextCategorization.outPath+"/", path, listOfFiles[j].getName(), i);
					Arrays.fill(intersectionMat[j],0.0);
					if (!created) {
						File source = new File(path + "/" + listOfFiles[i].getName());
						File dest = new File(
								TextCategorization.outPath + "/" + "/cluster" + i + "/" + listOfFiles[i].getName());
						// file.renameTo();
						try {
							copyFile(source, dest);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					created = true;
				}
			}
			created = false;
		}

	}

	private void createFolder(String outpath, String path, String file, int i) {
		File cat = new File(outpath);
		if (!cat.exists() || !cat.isDirectory()) {
			cat.mkdir();
		}
		
		cat = new File(outpath + "/cluster" + i);
		if (!cat.exists() || !cat.isDirectory()) {
			cat.mkdir();
		}
		// Files.move(Paths.get(path+"/"+file_name, null), Paths.get());
		File source = new File(path + "/" + file);
		File dest = new File(outpath + "/cluster" + i + "/" + file);
		// file.renameTo();
		try {
			copyFile(source, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void copyFile(File source, File dest) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destChannel = new FileOutputStream(dest).getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		} finally {
			sourceChannel.close();
			destChannel.close();
		}

	}

	private double calSimilarity(ArrayList<String> wordList1, ArrayList<String> wordList2) {
		int length1 = wordList1.size();
		int length2 = wordList2.size();
		long count = 0;
		for (int i = 0; i < length1; i++) {
			for (int j = 0; j < length2; j++) {
				if (wordList1.get(i).compareToIgnoreCase(wordList2.get(j)) == 0) {
					//System.out.println(wordList1.get(i)+" "+wordList2.get(j));
					count++;
					break;
				}
			}
		}
		double similarity = ((count) * 200) / (length1 + length2);
		//System.out.println(count + "\t" + length2 + "\t" + length1 + "\t" + similarity + "\t");
		return similarity;
	}

	/*
	 * private void processFile(String path, String fileName) throws IOException
	 * { removeStopWords(path, fileName);
	 * 
	 * }
	 */

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

		File oldFile = new File(path + "/" + fileName);
		oldFile.delete();

		// And rename tmp file's name to old file name
		File newFile = new File(path + "/" + tmpFileName);
		newFile.renameTo(oldFile);

	}

	private boolean isStopWord(String line) {
		boolean isStopWord = false;
		int len = FolderSelection.stop.size();
		/// home/teju/Project/inputFiles/iSystem.out.println(len);
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
