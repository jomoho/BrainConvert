import com.sun.xml.internal.ws.util.StringUtils;

/**
 * @author moritz
 *
 */
public class ConvertMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(args[0]+" ");
		Config config = new Config(args[0]);

		ArffWriter arff = new ArffWriter(config);
		
		arff.writeHeader();
		for(int i =0; i < config.files.length; ++i){
			
			//BrainLog file = new BrainLog(config.files[i]);
			//file.smooth("excitement");
			//arff.writeSet(file, config.emotions[i]);
			
		}
		
		arff.close();

	}

}
