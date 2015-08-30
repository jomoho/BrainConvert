import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class Config {
	public File[] files;
	public String[] emotions;
	private HashSet<String> classes;
	private String prefix;
	public static final String[] attributes= {
			"excitement",
			"engagementBoredom",
			"frustration",
			"smile",
			"laugh",
			"clench"};
	
	public String getPrefix() {
		return prefix;
	}
	public HashSet<String> getClasses() {
		return classes;
	}
	Config(String filename){
		BufferedReader f;
		try {
			f = new BufferedReader(new FileReader(filename));
			prefix = f.readLine();
			File dir = new File(prefix);
			//String[] files = dir.list();
			
			files = dir.listFiles(new FilenameFilter() {
			        @Override
			        public boolean accept(File dir, String name) {
			            return name.matches("log_[^_]+_[^_]+.csv");
			    }
			});
			Arrays.sort(files);
			
			File tmp = files[0];
			
			for (int i = 1; i <files.length-1; i++){
				files[i-1] = files[i];
			}
			files[files.length-2] = tmp;
			
			emotions = new String[files.length];
			
			for (int i = 0; i < files.length; i++){
				emotions[i] = f.readLine().trim();
				System.out.println(emotions[i] + "\t| " + files[i].toString());
			}
			classes = new HashSet<String>(Arrays.asList(emotions));
			System.out.println(classes);			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String[] getAttributes() {
		return attributes;
	}
}
