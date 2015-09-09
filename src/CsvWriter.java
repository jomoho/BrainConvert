import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class CsvWriter {
	private FileWriter file;
	private String header;
	private HashSet<String> classes;
	
	CsvWriter(Config cfg){
		classes = cfg.getClasses();
		try {
			file = new FileWriter(new File(cfg.getPrefix() + ".csv"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createHeader(cfg);
	}
	private void createHeader(Config cfg) {
		//TODO: write comment
		header = "";
		
		//add Attributes:
		String[] attr = cfg.getAttributes();
		for(int i = 0; i < attr.length; i++){
			
			header+= attr[i]+",";
		}
		header += "class";
		//ready for data
		header+= "\n";
	}
	
	void writeHeader(){
		try {
			file.write(header);
			file.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void writeData(double[] data, String sclass){
		String s = "";
		if(classes.contains(sclass) && data.length == Config.attributes.length){
			for(int i = 0; i< data.length; i++){				
				s+= data[i] + ",";
			}
			s += sclass;
			try {
				file.write(s +"\n");
				file.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	void close(){
		try {
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}	

