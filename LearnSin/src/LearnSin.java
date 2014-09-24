public class LearnSin {
	
	public static void main(String[] args) {
		double[] sampleinput = new double[10];
		double[] sampleoutput = new double[10];
		for (int i = 0; i < 10; i++) {
			sampleinput[i] = i;
			sampleoutput[i] = sigmod(Math.sin(i));
            //System.out.println(sampleoutput[i]);
		}
		
		int learntimes = 200000;
		double samplein = 0;
		double sampleout = 0;
		int hiddenlayers = 10;
		double[] hiddeninput = new double[hiddenlayers];
		double[] hiddenoutput = new double[hiddenlayers];

		double[] hbias = new double[hiddenlayers];
		for (int i = 0; i < hiddenlayers; i++)
			hbias[i] = Math.random();
		
		double[] w = new double[hiddenlayers];
		for (int i = 0; i < hiddenlayers; i++)
			w[i] = Math.random();
		
		double[] v = new double[hiddenlayers];
		for (int i = 0; i < hiddenlayers; i++)
			v[i] = Math.random();
		
		double obias = 0.5;

		double outlayerinput;
		double outlayeroutput;

		double[] fixv = new double[hiddenlayers];
		double fixobias;
		double[] fixw = new double[hiddenlayers];
		double[] fixhbias = new double[hiddenlayers];
		double rate = 0.2;

		int count = 0;
		
		while (count < learntimes){
			samplein = sampleinput[count%10];
			sampleout = sampleoutput[count%10];
			
			for (int i = 0; i < hiddenlayers; i++) {
				hiddeninput[i] = samplein * w[i] - hbias[i];
				hiddenoutput[i] = sigmod(hiddeninput[i]);

			}

			
			double temp = 0;
			for (int i = 0; i < hiddenlayers; i++)
				temp +=  hiddenoutput[i] * v[i];
			
			outlayerinput = temp - obias;
			outlayeroutput = sigmod(outlayerinput);
	          
			for (int i = 0; i < hiddenlayers; i++) {
				fixv[i] = -rate * (outlayeroutput - sampleout) * outlayeroutput
						* (1 - outlayeroutput) * hiddenoutput[i];
				v[i] += fixv[i];
			}

			fixobias = rate * (outlayeroutput - sampleout) * outlayeroutput
					* (1 - outlayeroutput);
            obias += fixobias;
			
            for (int i = 0; i < hiddenlayers; i++) {
				fixw[i] = -rate * (outlayeroutput - sampleout) * outlayeroutput
						* (1 - outlayeroutput) * v[i] * hiddenoutput[i]
						* (1 - hiddenoutput[i]) * samplein;
				w[i] += fixw[i];
				fixhbias[i] = rate * (outlayeroutput - sampleout) * outlayeroutput
						* (1 - outlayeroutput) * v[i] * hiddenoutput[i]
						* (1 - hiddenoutput[i]);
				hbias[i] += fixhbias[i];
			}
			
            System.out.println(count+": "+w[0]+" "+v[0]+" "+hbias[0]+" "+obias);
			count++;
		}
		
		samplein = (Math.PI)/6;
		for (int i = 0; i < hiddenlayers; i++) {
			hiddeninput[i] = samplein * w[i] - hbias[i];
			
			hiddenoutput[i] = sigmod(hiddeninput[i]);

		}

		//for (int i = 0; i < 2; i++) {
			//outlayerinput += (hiddenoutput[i] * v[i]);

		//}
		
		//outlayerinput -= obias;
		double temp1 = 0;
		for (int i = 0; i < hiddenlayers; i++)
			temp1 +=  hiddenoutput[i] * v[i];
		
		outlayerinput = temp1 - obias;
		outlayeroutput = sigmod(outlayerinput);
		System.out.println(outlayerinput);
		System.out.println(outlayeroutput);
		System.out.println(sigmod(0.5));
		
		//System.out.println(sigmod(0));
		
		

	}

	private static double sigmod(double x) {

		double y = 1 + Math.exp(-x);

		double result = 1 / y;
		return result;
	}
	
	//private static void 

}
