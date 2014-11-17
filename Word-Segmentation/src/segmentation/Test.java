package segmentation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/********在测试集上测试*********/
public class Test {
	private static List<String> unigram;
	private static List<String> bigram;
	private static List<String> labels;
	private static List<String> sentence;
	private static List<String> correct;
	private static List<String> correcttemp;
	private static List<String> preput;
	private static List<int[]> results;

	private static int usize;
	private static int bsize;
	private static int lsize;
	private static HashMap hash;

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		ObjectInputStream obin = new ObjectInputStream(new FileInputStream(
				"hash.txt"));
		hash = (HashMap) obin.readObject();
		InitTraining test = new InitTraining();

		test.Initialize();
		System.out.println("initial successfully");
		unigram = test.getUnigram();
		bigram = test.getBigram();
		labels = test.getLabels();
		usize = unigram.size();
		bsize = bigram.size();
		lsize = labels.size();
		int firstbias;
		int secondbias;
		String[] bias;
		int correctnumber = 0;
		int totalnumber = 0;
		FileReader fr = new FileReader("test.utf8");
		BufferedReader br = new BufferedReader(fr);
		sentence = new ArrayList<String>();
		correcttemp = new ArrayList<String>();
		correct = new ArrayList<String>();

		sentence.add("");
		sentence.add("");
		correcttemp.add("S");
		String line = "";
		while ((line = br.readLine()) != null) {
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
						result1 = (int[]) hash.get(preput1);

						if (result1 != null) {
							for (int k = 0; k < lsize; k++) {

								tempresult[k] += result1[k];

							}

						}

					}

					else if (utemp.length() > 2) {
						bias = utemp.split(" ");
						firstbias = Integer.parseInt(bias[0]);
						secondbias = Integer.parseInt(bias[1]);
						preput2 = bias[0] + sentence.get(i + firstbias)
								+ bias[1] + sentence.get(i + secondbias);
						result2 = (int[]) hash.get(preput2);

						if (result2 != null) {
							for (int k = 0; k < lsize; k++) {

								tempresult[k] += result2[k];

							}

						}
					}

				}

				for (int j = 0; j < bsize; j++) {

					String btemp = bigram.get(j);
					if (btemp.length() <= 2) {
						firstbias = Integer.parseInt(btemp);

						String biasword = sentence.get(i + firstbias);

						preput3 = correcttemp.get(i - 2) + btemp + biasword;
						result3 = (int[]) hash.get(preput3);

						if (result3 != null) {
							for (int k = 0; k < lsize; k++) {
								tempresult[k] += result3[k];

							}

						}
					}

					else if (btemp.length() > 2) {
						bias = btemp.split(" ");
						firstbias = Integer.parseInt(bias[0]);
						secondbias = Integer.parseInt(bias[1]);

						preput4 = correcttemp.get(i - 2) + bias[0]
								+ sentence.get(i + firstbias) + bias[1]
								+ sentence.get(i + secondbias);

						result4 = (int[]) hash.get(preput4);

						if (result4 != null) {
							for (int k = 0; k < lsize; k++) {
								tempresult[k] += result4[k];

							}

						}

					}

				}

				int max = sort(tempresult);
				correcttemp.add(labels.get(max));
				if (labels.get(max).equals(correct.get(i - 2))) {
					correctnumber++;

				}

				
				
				totalnumber++;
			}
			sentence.clear();
			correcttemp.clear();
			correct.clear();
			sentence.add("");
			sentence.add("");
			correcttemp.add("S");

		}

		System.out.println(correctnumber + " " + totalnumber+" "+(double)correctnumber/totalnumber);
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
}
