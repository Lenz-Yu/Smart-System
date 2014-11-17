/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
 */
package segmentation;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***********在训练集上训练*************/
public class Train {
	private static List<String> unigram;
	private static List<String> bigram;
	private static List<String> labels;
	private static List<String> sentence;
	private static List<String> correct;
	private static List<String> correcttemp;
	private static List<String> preput;
	private static List<int[]> results;

	private static HashMap templates;
	private static int usize;
	private static int bsize;
	private static int lsize;

	public static void main(String[] args) throws IOException {

		int trainingtimes = 10;
		InitTraining test = new InitTraining();

		test.Initialize();
		System.out.println("initial successfully");
		unigram = test.getUnigram();
		bigram = test.getBigram();
		labels = test.getLabels();

		templates = test.getTemplates();
		usize = unigram.size();
		bsize = bigram.size();
		lsize = labels.size();
		int firstbias;
		int secondbias;
		String[] bias;

		for (int lt = 0; lt < trainingtimes; lt++) {
			int correctnumber = 0;
			int totalnumber = 0;
			FileReader fr = new FileReader("train.utf8");
			BufferedReader br = new BufferedReader(fr);
			sentence = new ArrayList<String>();
			correcttemp = new ArrayList<String>();
			correct = new ArrayList<String>();
			preput = new ArrayList<String>();
			results = new ArrayList<int[]>();
			sentence.add("");
			sentence.add("");
			correcttemp.add("S");
			correct.add("S");
			String line = "";
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				if (!line.equals("")) {
					sentence.add("" + line.charAt(0));
					correct.add("" + line.charAt(2));
					continue;

				}
				sentence.add("");
				sentence.add("");
				int tempresult[] = new int[lsize];

				int ssize = sentence.size();
				String preput1 = "";
				String preput2 = "";
				String preput3 = "";
				String preput4 = "";
				int result1[] = null;
				int result2[] = null;
				int result3[] = null;
				int result4[] = null;

				for (int i = 2; i < ssize - 2; i++) {

					for (int q = 0; q < lsize; q++)
						tempresult[q] = 0;

					for (int j = 0; j < usize; j++) {

						String utemp = unigram.get(j);
						if (utemp.length() <= 2) {
							firstbias = Integer.parseInt(utemp);
							String biasword = sentence.get(i + firstbias);
							preput1 = utemp + biasword;

							// System.out.println(preput1);
							result1 = (int[]) templates.get(preput1);

							if (result1 != null) {
								for (int k = 0; k < lsize; k++) {

									tempresult[k] += result1[k];

								}
								preput.add(preput1);
								results.add(result1);
							}

						}

						else if (utemp.length() > 2) {
							bias = utemp.split(" ");
							firstbias = Integer.parseInt(bias[0]);
							secondbias = Integer.parseInt(bias[1]);
							preput2 = bias[0] + sentence.get(i + firstbias)
									+ bias[1] + sentence.get(i + secondbias);
							result2 = (int[]) templates.get(preput2);

							if (result2 != null) {
								for (int k = 0; k < lsize; k++) {

									tempresult[k] += result2[k];

								}
								preput.add(preput2);
								results.add(result2);
							}
						}

					}

					for (int j = 0; j < bsize; j++) {

						String btemp = bigram.get(j);
						if (btemp.length() <= 2) {
							firstbias = Integer.parseInt(btemp);

							String biasword = sentence.get(i + firstbias);

//							preput3 = correcttemp.get(i - 2) + btemp + biasword;
							preput3 = correct.get(i - 2) + btemp + biasword;
							result3 = (int[]) templates.get(preput3);

							if (result3 != null) {
								for (int k = 0; k < lsize; k++) {
									tempresult[k] += result3[k];

								}
								preput.add(preput3);
								results.add(result3);
							}
						}

						else if (btemp.length() > 2) {
							bias = btemp.split(" ");
							firstbias = Integer.parseInt(bias[0]);
							secondbias = Integer.parseInt(bias[1]);

//							preput4 = correcttemp.get(i - 2) + bias[0]
//									+ sentence.get(i + firstbias) + bias[1]
//									+ sentence.get(i + secondbias);

							preput4 = correct.get(i - 2) + bias[0]
									+ sentence.get(i + firstbias) + bias[1]
									+ sentence.get(i + secondbias);
							result4 = (int[]) templates.get(preput4);

							if (result4 != null) {
								for (int k = 0; k < lsize; k++) {
									tempresult[k] += result4[k];

								}
								preput.add(preput4);
								results.add(result4);
							}

						}

					}

					int max = sort(tempresult);
					correcttemp.add(labels.get(max));
					if (labels.get(max).equals(correct.get(i - 1))) {
						correctnumber++;

					}

					if (!labels.get(max).equals(correct.get(i - 1))) {

						int right = findindex(correct.get(i - 1));

						int psize = preput.size();

						for (int q = 0; q < psize; q++) {
							
//							if (preput.get(q).charAt(0)=='0'){
//							int[] temp = results.get(q);
//							temp[right]+=2;
//							temp[max]-=2;
//
//							templates.put(preput.get(q), temp);
//							continue;
//							}
//							
							
						
//							if (preput.get(q).length()==3&&preput.get(q).charAt(0)=='-'&&preput.get(q).charAt(1)=='1'){
//								int[] temp = results.get(q);
//								temp[right]+=2;
//								temp[max]-=2;
//
//								templates.put(preput.get(q), temp);
//								continue;
//								}
							
//							if (preput.get(q).length()==2&&preput.get(q).charAt(0)=='0'){
//								int[] temp = results.get(q);
//								temp[right]+=2;
//								temp[max]-=2;
//
//								templates.put(preput.get(q), temp);
//								continue;
//								}
							
							if (preput.get(q).length()==4&&preput.get(q).charAt(0)=='0'&&preput.get(q).charAt(2)=='1'){
								int[] temp = results.get(q);
								temp[right]+=5;
								temp[max]-=5;

								templates.put(preput.get(q), temp);
								continue;
								}
                            
							int[] temp = results.get(q);
							temp[right]++;
							temp[max]--;

							templates.put(preput.get(q), temp);
						}

					}
					preput.clear();
					results.clear();
					totalnumber++;
				}
				sentence.clear();
				correcttemp.clear();
				correct.clear();
				sentence.add("");
				sentence.add("");
				correcttemp.add("S");
				correct.add("S");

			}

			System.out.println(correctnumber + " " + totalnumber+" "+(double)correctnumber/totalnumber);
		}
		
		FileOutputStream outStream = new FileOutputStream("hash.txt");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				outStream);

		objectOutputStream.writeObject(templates);
		
		outStream.close();

	}

	

	private static int sort(int[] test) {
		int length = test.length;
		int index = 0;

		for (int i = 1; i < length; i++) {
			if (test[i] > test[index])
				index = i;
		}

		return index;

	}

	private static int findindex(String test) {

		for (int i = 0; i < lsize; i++) {
			if (labels.get(i).equals(test))
				return i;

		}

		return 0;
	}

}
