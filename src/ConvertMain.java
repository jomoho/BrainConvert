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
		/*
		ARFF arff = new ARFF(args[2]);
		
		arff.writeHader();
		for(int i =0; i < config.sets.length(); ++i){
			
			BrainLog file = new BrainLog(config.sets[i].filename);
			file.smooth("excitement");
			file.smooth("engagementBoredom");
			file.smooth("frustration");
			file.smooth("smile");
			file.smooth("laugh");
			file.smooth("clench");
			arff.writeSet(file, config.sets[i].emotion);
			
		}
		
		arff.close();
		*/

	}

}
