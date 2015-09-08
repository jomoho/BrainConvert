import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import flanagan.analysis.CurveSmooth;
import flanagan.analysis.Stat;
import flanagan.analysis.Regression;


/**
 * 
 */

/**
 * @author moritz
 * This Class takes a .csv logfile and stores all the values into a hashmap
 * with the column names being the keys and the columns being double arrays.
 * it assumes a "timestamp column for timing"
 * 
 * It provides methods to extract slices based on a timeframe.
 * And also methods to calculate the mean of a specific timeframe
 *
 */
public class Extractor {

	public enum SmoothType {
		MovingWindow,
		SavitzkyGolay
	}

	private HashMap<String, double[]> values;
	
	Extractor(File csvFile){
		values = new HashMap<>();
		parseFile(csvFile);
	}


	/**
	 * @param csvFile
	 */
	private void parseFile(File csvFile) {
		String split = ",";
		values.clear();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			System.out.println("parsing File: " + csvFile.toString());
			
			//read first line with names
			String line = br.readLine();
			String[] columns = line.split(split);
			
			
			//create arrays
			ArrayList<ArrayList<Double>> arrays = new ArrayList<>();
			for(int i= 0; i < columns.length; i++){
				arrays.add(new ArrayList<Double>());
			}
			//read all lines
			while ((line = br.readLine()) != null) {
				String[] s = line.split(split);
				for(int i= 0; i < columns.length; i++){					
					arrays.get(i).add(new Double(s[i]));
				}
			}

			//fill the map
			for(int i = 0; i<columns.length; i++){
				//convert values
				double[] dvals = new double[arrays.get(i).size()];
				for(int j = 0; j < arrays.get(i).size(); j++){
					dvals[j] = arrays.get(i).get(j);
				}

				values.put(columns[i], dvals);
			}
			br.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void smooth(String column, SmoothType type, int filterWidth ){
		
		CurveSmooth cs = new CurveSmooth(values.get(column));
		
		if(type == SmoothType.MovingWindow){
			System.out.println("smoothing ["+ column +"]: MovingWindow");
			values.put(column, cs.movingAverage(filterWidth));
		}
		
		if(type == SmoothType.SavitzkyGolay){
			System.out.println("smoothing ["+ column +"]: SavitzkyGolay");
			values.put(column, cs.savitzkyGolay(filterWidth));
		}
	}
	
	public double getMidPoint(String col, double offTime, double lengthTime){		
		return getMean(col, offTime, lengthTime);
	}
	
	public double getLinearRegression(String col, double offTime, double lengthTime){		
		double[] slice = getSlice(col,offTime,lengthTime); 
		double[] timeX=new double [slice.length];
		double[] resultY=new double [slice.length];
		if(slice.length>1){
			for (int i=0; i<slice.length; i++){
			timeX[i]=i;
		}
        Regression reg = new Regression(timeX,slice);
        reg.linear();
		resultY= reg.getYcalc();
		return resultY[slice.length/2];

		}
		else{
			return getMean(col, offTime, lengthTime);
		}
	}
	

	public double getMean(String col, double offTime, double lengthTime){
		double[] slice = getSlice(col,offTime,lengthTime); 
		Stat mean = new Stat(slice);
		
		double r = mean.geometricMean();
		Double d = new Double(r);
		if(d.isNaN()){
			System.out.println("NaN warning: "+ col);
			r=0;
		}
		return r;
	}
	
	public double getGlobalMean(String col){
		return getMean(col, 0, getDuration());
	}

	public double getDuration() {
		double [] t = values.get("timestamp");		
		return t[t.length-1]-t[0];
	}
	
	public double getStart() {
		double [] t = values.get("timestamp");		
		return t[0];
	}
	
	public double[] getSlice(String col,  double offTime, double lengthTime){

		System.out.println("getSlice: " + col + " off:"+ offTime + " lenT: " + lengthTime);
		//calculate indices based on timestamps
		double [] t = values.get("timestamp");
		double startTime = t[0] + offTime; 
		int start = 0;
		int end = 0;
		for(int i = 0; i < t.length; i++){
			if(t[i] < startTime){
				start = i;
			}
			if(t[i] < (startTime + lengthTime)){
				end = i;
			}
		}
		
		//get the values
		int len = (end-start);
		double[] ret = new double[len];
		double[] src = values.get(col);

		System.out.println("len: "+len +" src.length: "+src.length+" start:"+start+" end:"+end);
		String s= "";
		for(int i = 0; i < len; i++){
			s+= src[i+start] +",";
			ret[i] = src[i+start]; 
		}
		System.out.println(s);
		return ret;
	}

}
