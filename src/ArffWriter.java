import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class ArffWriter {
	private FileWriter file;
	private String header;
	private HashSet<String> classes;
	ArffWriter(Config cfg){
		classes = cfg.getClasses();
		try {
			file = new FileWriter(new File(cfg.getPrefix() + ".arff"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createHeader(cfg);
	}

	/**
	 * @param cfg
	 */
	private void createHeader(Config cfg) {
		//TODO: write comment
		header = "%1. Title: Brain Log\n\n";
		
		//write relation
		header+= "@RELATION " + cfg.getPrefix()+"\n\n";
		//add Attributes:
		String[] attr = cfg.getAttributes();
		for(int i = 0; i < attr.length; i++){
			header+= "@ATTRIBUTE " +attr[i]+" NUMERIC\n";
		}
		//add classes
		String[] c = cfg.getClasses().toArray(new String[cfg.getClasses().size()]);
		header +="@ATTRIBUTE class {";
		for(int i = 0; i < c.length; i++){
			if(i!= 0){
				header+=",";
			}
			header+= c[i];			
		}
		header+="}\n\n";
		
		//ready for data
		header+= "@DATA\n";
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
