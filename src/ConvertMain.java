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
		CsvWriter csv = new CsvWriter(config);
		arff.writeHeader();
		csv.writeHeader();
		
		for(int i =0; i < config.files.length; ++i){
			Extractor e = new Extractor(config.files[i]);
			/*
			for(int j = 0; j < Config.columnsScale.length; j++){
				e.scale(Config.columnsScale[j],50);
			}*/
			for(int j = 0; j < Config.columnsActivate.length; j++){
				e.scale(Config.columnsActivate[j],50);
				e.smooth(Config.columnsActivate[j], Extractor.SmoothType.SavitzkyGolay, 50);
				e.activate(Config.columnsActivate[j], 0.1);
			}
			
			for(int j = 0; j < Config.columnsSmooth.length; j++){
				e.smooth(Config.columnsSmooth[j], Extractor.SmoothType.SavitzkyGolay, 5);
			}

			//iterate through data and generate midpoints
			double chunksize = 3;
			for(double t = e.getDuration()/2; t < e.getDuration(); t += chunksize){
				
				double[] d = new double[Config.attributes.length];
				
				for(int j = 0; j < Config.columns.length; j++){
					if(j < 3){
						d[j] = e.getLinearRegression(Config.columns[j], t, chunksize);
					}else{
						d[j] = e.getSum(Config.columns[j], t, chunksize);
					}
				}

				d[Config.columns.length] = e.getMin(Config.columns[0], t, chunksize);
				d[Config.columns.length+1] = e.getMin(Config.columns[1], t, chunksize);
				d[Config.columns.length+2] = e.getMin(Config.columns[2], t, chunksize);

				d[Config.columns.length+3] = e.getMax(Config.columns[0], t, chunksize);
				d[Config.columns.length+4] = e.getMax(Config.columns[1], t, chunksize);
				d[Config.columns.length+5] = e.getMax(Config.columns[2], t, chunksize);
				
				d[Config.columns.length+6] = e.getMean(Config.columns[0], t, chunksize);
				d[Config.columns.length+7] = e.getMean(Config.columns[1], t, chunksize);
				d[Config.columns.length+8] = e.getMean(Config.columns[2], t, chunksize);
				
				arff.writeData(d, config.emotions[i]);
				csv.writeData(d, config.emotions[i]);
			}			
		}		
		arff.close();
		csv.close();
	}

}
