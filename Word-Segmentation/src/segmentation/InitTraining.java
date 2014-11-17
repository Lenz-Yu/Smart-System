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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InitTraining {
	/* 多加-1 0 */
	List<String> sentence = new ArrayList<String>();
	List<String> unigram = new ArrayList<String>();
	List<String> bigram = new ArrayList<String>();
	List<String> labels = new ArrayList<String>();
	
	List<String> correcttemp = new ArrayList<String>();
	HashMap templates = new HashMap();

	public void Initialize() throws IOException {
		sentence.add("");
		sentence.add("");
		correcttemp.add("S");
		unigram = readunigram();
		bigram = readbigram();
		labels = readlabels();
		int usize = unigram.size();
		int bsize = bigram.size();
		int lsize = labels.size();
		int firstbias;
		int secondbias;
		String[] bias;
		FileReader fr = new FileReader("train.utf8");
		BufferedReader br = new BufferedReader(fr);

	
		String line = "";
		while ((line = br.readLine()) != null) {
			// System.out.println(line);
			if (!line.equals("")) {
				sentence.add("" + line.charAt(0));
				
				correcttemp.add(""+line.charAt(2));
				// System.out.println(line);
				continue;

			}
			sentence.add("");
			sentence.add("");
			int ssize = sentence.size();
			for (int i = 2; i < ssize - 2; i++) {
				for (int j = 0; j < usize; j++) {

					String utemp = unigram.get(j);
					if (utemp.length() <= 2) {
						firstbias = Integer.parseInt(utemp);
						String biasword = sentence.get(i + firstbias);
//						for (int k = 0; k < lsize; k++) {
////							String preput = utemp + " " + biasword + " "
////									+ labels.get(k);
//							templates.put(preput, 0);
//							System.out.println(preput);
//						}
						int label[] = new int[lsize];
						for (int k = 0; k < lsize; k++)
							label[k] = 0;
						String preput = utemp + biasword;
						templates.put(preput, label);
					}

					else if (utemp.length() > 2) {
						bias = utemp.split(" ");
						firstbias = Integer.parseInt(bias[0]);
						secondbias = Integer.parseInt(bias[1]);

//						for (int k = 0; k < lsize; k++) {
//							String preput = bias[0] + " "
//									+ sentence.get(i + firstbias) + " "
//									+ bias[1] + " "
//									+ sentence.get(i + secondbias) + " "
//									+ labels.get(k);
//							templates.put(preput, 0);
//							// System.out.println(preput);
//						}
						int label[] = new int[lsize];
						for (int k = 0; k < lsize; k++)
							label[k] = 0;
						String preput = bias[0] + sentence.get(i + firstbias)+bias[1]+sentence.get(i + secondbias);
						templates.put(preput, label);
					}

				}

				for (int j = 0; j < bsize; j++) {

					String btemp = bigram.get(j);
					if (btemp.length() <= 2) {
						firstbias = Integer.parseInt(btemp);

						String biasword = sentence.get(i + firstbias);
						String lastlabel = correcttemp.get(i-2);
						
							for (int p = 0; p < lsize; p++) {
								String preput = lastlabel+ btemp+biasword;
								int label[] = new int[lsize];
								for (int k = 0; k < lsize; k++)
									label[k] = 0;
								templates.put(preput,label);
								//System.out.println(preput);
							
						}
					}

					else if (btemp.length() > 2) {
						bias = btemp.split(" ");
						firstbias = Integer.parseInt(bias[0]);
						secondbias = Integer.parseInt(bias[1]);
						String lastlabel = correcttemp.get(i-2);
						// System.out.println(sentence.get(i+firstbias)+" "+firstbias+" "+sentence.get(i+firstbias)+" "+secondbias);

						
							for (int p = 0; p < lsize; p++) {
								String preput = lastlabel+bias[0]+sentence.get(i+firstbias)+bias[1]
										         + sentence.get(i + secondbias);
								int label[] = new int[lsize];
								for (int k = 0; k < lsize; k++)
									label[k] = 0;	
								templates.put(preput, label);
								//System.out.println(preput);
							}
						
					}

				}
			}

			sentence.clear();
			correcttemp.clear();
			sentence.add("");
			sentence.add("");
			correcttemp.add("S");

		}
		System.out.println(templates.size());
	}

	

	public List<String> getSentence() {
		return sentence;
	}

	public void setSentence(List<String> sentence) {
		this.sentence = sentence;
	}

	public List<String> getUnigram() {
		return unigram;
	}

	public void setUnigram(List<String> unigram) {
		this.unigram = unigram;
	}

	public List<String> getBigram() {
		return bigram;
	}

	public void setBigram(List<String> bigram) {
		this.bigram = bigram;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public HashMap getTemplates() {
		return templates;
	}

	public void setTemplates(HashMap templates) {
		this.templates = templates;
	}

	public static List<String> readunigram() throws IOException {
		FileReader fr = new FileReader("template.utf8");
		BufferedReader br = new BufferedReader(fr);
		List<String> unigram = new ArrayList<String>();

		String line = "";
		while ((line = br.readLine()) != null) {
			if (line.length() >= 1) {
				if (line.charAt(0) == 'U') {
					if (line.indexOf(',') != -1)
						if (line.lastIndexOf(',') == line.indexOf(',')) {
							unigram.add((line.substring(line.indexOf('[') + 1,
									line.indexOf(','))));

						} else {
							String firstindex = (line.substring(
									line.indexOf('[') + 1, line.indexOf(',')));
							String secondindex = (line.substring(
									line.lastIndexOf('[') + 1,
									line.lastIndexOf(',')));
							unigram.add(firstindex + " " + secondindex);

						}

				}
			}
		}
		return unigram;

	}

	public static List<String> readbigram() throws IOException {
		FileReader fr = new FileReader("template.utf8");
		BufferedReader br = new BufferedReader(fr);

		List<String> bigram = new ArrayList<String>();
		String line = "";
		while ((line = br.readLine()) != null) {
			if (line.length() > 1) {
				if (line.charAt(0) == 'B') {
					if (line.indexOf(',') != -1)
						if (line.lastIndexOf(',') == line.indexOf(',')) {
							bigram.add((line.substring(line.indexOf('[') + 1,
									line.indexOf(','))));

						} else {
							String firstindex = (line.substring(
									line.indexOf('[') + 1, line.indexOf(',')));
							String secondindex = (line.substring(
									line.lastIndexOf('[') + 1,
									line.lastIndexOf(',')));
							bigram.add(firstindex + " " + secondindex);

						}

				}
			}
		}
		return bigram;

	}

	private static List<String> readlabels() throws IOException {
		FileReader fr = new FileReader("labels.utf8");
		BufferedReader br = new BufferedReader(fr);

		List<String> labels = new ArrayList<String>();
		String line = "";
		while ((line = br.readLine()) != null) {
			labels.add(line);
		}
		return labels;

	}

}
