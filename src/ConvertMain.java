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
			Extractor e = new Extractor(config.files[i]);
			for(int j = 0; j < Config.columnsSmooth.length; j++){
				e.smooth(Config.columnsSmooth[j], Extractor.SmoothType.SavitzkyGolay, 5);
			}
			double globalExcitement = e.getGlobalMean("excitement");
			double globalEngagement = e.getGlobalMean("engagementBoredom");
			double globalFrustration = e.getGlobalMean("frustration");

			//iterate through data and generate midpoints
			double chunksize = 10;
			for(double t = 0; t < e.getDuration(); t += chunksize){
				
				double[] d = new double[Config.attributes.length];
				
				for(int j = 0; j < Config.columns.length; j++){
					d[j] = e.getMidPoint(Config.columns[j], t, chunksize);
				}
				
				d[Config.columns.length] = globalExcitement;
				d[Config.columns.length+1] = globalEngagement;
				d[Config.columns.length+2] = globalFrustration;
				
				arff.writeData(d, config.emotions[i]);
			}			
		}		
		arff.close();
	}

}
