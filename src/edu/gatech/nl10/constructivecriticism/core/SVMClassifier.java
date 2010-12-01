package edu.gatech.nl10.constructivecriticism.core;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.functions.SMO;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class SVMClassifier {
	private Instances traininsts[];
	private Instances testinsts[];

	public SVMClassifier() {
	}
	
//	public static void getArff(String trainfile, String testfile, FeatureExtractor fe, String outname) {
//		ArrayList<Tweet> traintweets = TweetFileParser.parseFile(trainfile);
//		ArrayList<Tweet> testtweets = TweetFileParser.parseFile(testfile);
//		Instances trains = fe.extractFeatures(traintweets);
//		Instances tests = fe.extractFeatures(testtweets);		
//		 ArffSaver saver = new ArffSaver();
//		 saver.setInstances(trains);
//		 ArffSaver saver2 = new ArffSaver();
//		 saver2.setInstances(tests);
//		 try {
//			saver.setFile(new File("output/" + outname + "_train.arff"));
//			saver.writeBatch();
//			saver2.setFile(new File("output/" + outname + "_test.arff"));
//			saver2.writeBatch();
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public SVMClassifier(String[] arfftrainfiles, String[] arfftestfiles) {
		try{
			ArffLoader arff = new ArffLoader();
			traininsts = new Instances[arfftrainfiles.length];
			testinsts = new Instances[arfftestfiles.length];
			for(int i=0;i<arfftrainfiles.length;i++) {
				//BufferedReader reader = new BufferedReader(new FileReader(arfftrainfiles[i]));
				//ArffReader arff = new ArffReader(reader);
				arff.setSource(new File(arfftrainfiles[i]));

				traininsts[i] = arff.getDataSet();
				traininsts[i].setClassIndex(0);
				//reader = new BufferedReader(new FileReader(arfftestfiles[i]));
				//arff = new ArffReader(reader);
			}
			for(int i=0;i<arfftestfiles.length;i++) {
				arff = new ArffLoader();
				arff.setSource(new File(arfftestfiles[i]));
				testinsts[i] = arff.getDataSet();
				testinsts[i].setClassIndex(0);

				//if(i == 0)
				//	testinsts[i].setClassIndex(0);
				//if(i > 0) {
				//	testinsts[i] = Instances.mergeInstances(testinsts[i], testinsts[i-1]);
				//}
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void crossValidation(int numfolds) {

		for(int i=0;i<traininsts.length;i++) {
			Random rand = new Random(8);
			Instances randData = new Instances(traininsts[i]);
			randData.randomize(rand);
			randData.stratify(10);

			for(int n = 0; n < 10; n++) {
				Instances train = randData.trainCV(numfolds, n);
				Instances test = randData.testCV(numfolds, n);

				SMO classifier = new SMO();
				//String[] opts = {"-C 1.0", "-L 0.0010", "-P 1.0E-12", "-N 0", "-V -1", "-W 1", "-K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""};
				try {
					//classifier.setOptions(opts);
					classifier.buildClassifier(train);
					System.out.println("***************************************");
					System.out.println("Cross Validation " + n + ":");
					testAndReport(classifier, test);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}
	}

	public void testValidation() {

		for(int i=0;i<traininsts.length;i++) {
			Instances train = traininsts[i];
			Instances test = testinsts[i];
			SMO classifier = new SMO();
			String[] opts = {"-C 1.0", "-L 0.0010", "-P 1.0E-12", "-N 0", "-V -1", "-W 1", "-K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""};
			try {
				classifier.setOptions(opts);
				classifier.buildClassifier(train);
				System.out.println("***************************************");
				System.out.println("Test Validation:");
				testAndReport(classifier, test);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}

	public void testAndReport(SMO classifier, Instances test) {
		int accuracy = 0;
		int nonneutral = 0;
		for (int i = 0; i < test.numInstances(); i++) {
			try {
				double cld = classifier.classifyInstance(test.instance(i));
				Attribute attr = test.attribute(0);
				String cl = attr.value((int) cld);
				double actd = test.instance(i).value(0);
				String act = attr.value((int) actd);

				if(!act.equals("Neutral")) {
					nonneutral++;
					System.out.println("Class: " + cl + ", Act: " + act);
					if (cl.equals(act))
						accuracy++;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		System.out.println("Number of test instances: " + test.numInstances());
		System.out.println("Nonneutral: " + nonneutral);
		System.out.println("Accuracy:");
		System.out.println(accuracy + " // " + nonneutral);
		System.out.println(accuracy / (double)nonneutral);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*String arfftrainfiles[] = {"output/baseline_train.arff",
		                           "output/linguistic_train.arff",
		                           "output/contextual_train.arff",
		                           "output/semantic_train.arff"};
		String arfftestfiles[] = {"output/baseline_test.arff",
                				  "output/linguistic_test.arff",
                                  "output/contextual_test.arff",
                                  "output/semantic_test.arff"};*/
		
		// Independent Executions
		//SVMClassification.getArff("data/train.40000.2009.05.25", "data/testdata.manual.2009.05.25", new BaselineFeatureExtractor(), "baseline");
		//SVMClassification.getArff("data/train.40000.2009.05.25", "data/testdata.manual.2009.05.25", new ContextualFeatureExtractor(), "contextual");
		
		//Combined
		String arfftrainfiles[] = {"output/extracted_features.arff"};
		String arfftestfiles[] = {};
		SVMClassifier svmc = new SVMClassifier(arfftrainfiles, arfftestfiles);
		svmc.crossValidation(10);
		//svmc.testValidation();
	}
}
