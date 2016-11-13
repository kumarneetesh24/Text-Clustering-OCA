import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class Cluster {
	private static int val  = 0;
	private double threshold = 25.0;
	final static String outPath = TextCategorization.outPath;
	public void create(String path, String file ) throws IOException {
		File folder = new File(outPath);
		File source = new File(path + "/tmp_" + file);
		File dest = null ;
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdir();
		}
		if(folder.list().length <= 0){
			File cl = new File(outPath+"/cluster_"+val);
			cl.mkdir();
			dest = new File(outPath + "/cluster_" + val +"/"+ file);
			copyFile(source,dest);
			return;
		}
		double clu_sim = 0;
		int len = folder.listFiles().length;
		File clusters[] = folder.listFiles();
		for (File cluster : clusters) {
			if (cluster.isDirectory()) {
				double clu = check_cluster(cluster,path,file);
				if( clu > clu_sim){
					clu_sim = clu;
					dest = new File(cluster.getPath() +"/"+ file);
				}
			}
		}
		System.out.println(clu_sim);
		if(clu_sim > threshold){
			copyFile(source,dest);
		} else {
			int i = 0;
			File chfile = null;
			for( i = 0; i<= len; i++){
				chfile = new File(outPath+"/cluster_"+i);
				if(!chfile.exists()|| (chfile.isDirectory() && chfile.list().length == 0 )) {
					System.out.println();
					break;
				}
			}
			if(!chfile.exists()){
				chfile.mkdir();
			}
			dest = new File(outPath + "/cluster_" + i +"/" + file);
			copyFile(source,dest);
		}
	}

	private double check_cluster(File cluster,String path, String file) throws IOException{
		File[] listOfFiles = cluster.listFiles();
		int numberOfFiles = listOfFiles.length;
		/*for (int i = 0; i < numberOfFiles; i++) {
			System.out.println(listOfFiles[i].getName());
		}*/
		double max_similarity = 0;
		ArrayList<String> wordList1 = readWords(path + "/tmp_" + file);
		for (int i = 0; i < numberOfFiles; i++) {
			if (listOfFiles[i].isFile()) {
				String path2 = listOfFiles[i].getPath();
				ArrayList<String> wordList2 = readWords(path2);
				double sim = calSimilarity(wordList1,wordList2);
				max_similarity = Math.max(sim, max_similarity);
			}
		}
		return max_similarity;
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
	
	
	private void copyFile(File source, File dest) throws IOException {
		System.out.println(source.getAbsolutePath());
		System.out.println(dest.getPath());
		System.out.println();
		try {
			Files.copy(source.toPath(), dest.toPath());
		} catch (IOException ex) {
	        throw new IOException("IOException when transferring " + source.getPath() + " to " + dest.getPath());
		}

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