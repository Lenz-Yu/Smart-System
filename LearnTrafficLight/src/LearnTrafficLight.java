import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LearnTrafficLight {

	public static void main(String[] args) throws IOException {

		int learntimes = 50;
		int count = 0;
		int inputnumber = 7;
		int outputnumber = 10;
		int hiddennumber = 30;
		double[] samplein = new double[7];
		double[] sampleout = new double[10];
		double w[][] = new double[inputnumber][hiddennumber];
		double v[][] = new double[hiddennumber][outputnumber];
		double fixw[][] = new double[inputnumber][hiddennumber];
		double fixv[][] = new double[hiddennumber][outputnumber];
		
		double[] hbias = new double[hiddennumber];
		double[] obias = new double[outputnumber];
		
		double[] fixh = new double[hiddennumber];
		double[] fixo = new double[outputnumber];

		double[] hiddeninput = new double[hiddennumber];
		double[] hiddenoutput = new double[hiddennumber];

		double[] outlayerinput = new double[outputnumber];
		double[] outlayeroutput = new double[outputnumber];

		double rate = 0.2;
		/**** initial ****/

		for (int i = 0; i < 10; i++)
			sampleout[i] = 0;

		for (int i = 0; i < inputnumber; i++)
			for (int j = 0; j < hiddennumber; j++)
				w[i][j] = Math.random();

		for (int i = 0; i < hiddennumber; i++)
			for (int j = 0; j < outputnumber; j++)
				v[i][j] = Math.random();

		for (int i = 0; i < hiddennumber; i++)
			hbias[i] = Math.random();

		for (int i = 0; i < outputnumber; i++)
			obias[i] = Math.random();
		FileReader fr = new FileReader("learn.txt");

		BufferedReader br = new BufferedReader(fr);
		br.mark(200);
		while (count < learntimes) {
			String line = "";
			String[] arrs = null;
			if (count % 10 == 0)
				br.reset();
			line = br.readLine();
			arrs = line.split(" ");
			sampleout[Integer.parseInt(arrs[7])] = 1;
			// System.out.println(arrs[0] + ":" + arrs[1] + ":" + arrs[2]);

			for (int i = 0; i < hiddennumber; i++) {
				double temp = 0;
				for (int j = 0; j < inputnumber; j++) {

					temp += samplein[j] * w[j][i];

				}
				hiddeninput[i] = temp - hbias[i];
				hiddenoutput[i] = sigmod(hiddeninput[i]);
			}

			
			for (int i = 0; i < outputnumber; i++) {
				double temp = 0;
				for (int j = 0; j < hiddennumber; j++) {

					temp += hiddenoutput[j] * v[j][i];

				}
				outlayerinput[i] = temp - obias[i];
				outlayeroutput[i] = sigmod(outlayerinput[i]);
			}

			
			
			count++;
			
			

		}
		
		

		br.close();
		fr.close();

	}

	private static double sigmod(double x) {

		double y = 1 + Math.exp(-x);

		double result = 1 / y;
		return result;
	}

	// private static void

}
