public class LearnSin {

	public static void main(String[] args) {
		
		double [] sampleinput = new double[10];
		double [] sampleoutput = new double[10];
		double [] hiddeninput = new double[2];
		double [] hiddenoutput = new double[2];
		
		double []hbias = new double[2];
		double []w = new double[2];
		double []v = new double[2];
		double obias = 0;
		
		double outlayerinput = 0;
		double outlayeroutput;
		
		double []fixv = new double[2];
		double fixobias;
		double []fixw = new double[2];
		double []fixhbias = new double[2];
		double rate = 0.2;
		
		double samplein = 0;
		double sampleout = 0;
		for (int i = 0; i < 10; i++){
			sampleinput[i] = i;
			sampleoutput[i] = Math.sin(i);
			
		}
		
	   for (int i = 0 ; i < 2 ; i++){
		   hiddeninput[i] = samplein*w[i]-hbias[i];
		   hiddenoutput[i] = sigmod(hiddeninput[i]);
		   
	   }
	   
	   for (int i = 0 ; i < 2 ; i++){
		   outlayerinput += (hiddenoutput[i]*v[i] - obias);
		   
		   
	   }
	   
	   outlayeroutput = sigmod(outlayerinput);
	   
	   for (int i = 0; i < 2; i++){
		   fixv[i] = -rate*(outlayeroutput-sampleout)*outlayeroutput*(1-outlayeroutput)*hiddenoutput[i];
	   }
	   
	   
		   fixobias = rate*(outlayeroutput-sampleout)*outlayeroutput*(1-outlayeroutput);
	   
	   
		   
		   for (int i = 0; i < 2; i++){
			   fixw[i] = -rate*(outlayeroutput-sampleout)*outlayeroutput*(1-outlayeroutput)*v[i]*hiddenoutput[i]*(1-hiddenoutput[i])*samplein;
			   fixhbias[i] = rate*(outlayeroutput-sampleout)*outlayeroutput*(1-outlayeroutput)*v[i]*hiddenoutput[i]*(1-hiddenoutput[i]);
		   }
		   
		   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	  
	   
			
	}
	
	
	
	private static double sigmod(double x){
		
		double y = 1 + Math.exp(-x);
		
		double result = 1/y;
		return result;
	}
	
	
}
