import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.ArffLoader.ArffReader;

public class SVMClassification {
	private FeatureExtractor featexts[] = {new BaselineFeatureExtractor(),
			new ContextualFeatureExtractor(),
			new LinguisticFeatureExtractor(),
			new SemanticFeatureExtractor()};
	
	private Instances traininsts[];
	private Instances testinsts[];

	public SVMClassification(String trainfile, String testfile) {
		ArrayList<Tweet> traintweets = TweetFileParser.parseFile(trainfile);
		ArrayList<Tweet> testtweets = TweetFileParser.parseFile(testfile);
		traininsts = new Instances[4];
		testinsts = new Instances[4];

		for(int i=0;i<featexts.length;i++) {
			traininsts[i] = featexts[i].extractFeatures(traintweets);
			if(i > 0) {
				traininsts[i] = Instances.mergeInstances(traininsts[i-1], traininsts[i]);
				traininsts[i].setClassIndex(0);
			}
			testinsts[i] = featexts[i].extractFeatures(testtweets);
			if(i > 0) {
				testinsts[i] = Instances.mergeInstances(testinsts[i-1], testinsts[i]);
				testinsts[i].setClassIndex(0);
			}
		}
	}
	
	public static void getArff(String trainfile, String testfile, FeatureExtractor fe, String outname) {
		ArrayList<Tweet> traintweets = TweetFileParser.parseFile(trainfile);
		ArrayList<Tweet> testtweets = TweetFileParser.parseFile(testfile);
		Instances trains = fe.extractFeatures(traintweets);
		Instances tests = fe.extractFeatures(testtweets);		
		 ArffSaver saver = new ArffSaver();
		 saver.setInstances(trains);
		 ArffSaver saver2 = new ArffSaver();
		 saver2.setInstances(tests);
		 try {
			saver.setFile(new File("output/" + outname + "_train.arff"));
			saver.writeBatch();
			saver2.setFile(new File("output/" + outname + "_test.arff"));
			saver2.writeBatch();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SVMClassification(String[] arfftrainfiles, String[] arfftestfiles) {

		try{
			for(int i=1;i<featexts.length;i++) {
				ArffLoader arff = new ArffLoader();
				arff.setSource(new File(arfftrainfiles[i]));
					
				traininsts[i] = arff.getDataSet();
				if(i == 0)
					traininsts[i].setClassIndex(0);
				if(i > 0) {
					traininsts[i] = Instances.mergeInstances(traininsts[i], traininsts[i-1]);
				}

				arff = new ArffLoader();
				arff.setSource(new File(arfftestfiles[i]));
				testinsts[i] = arff.getDataSet();
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void crossValidation(int numfolds) {

		for(int i=0;i<featexts.length;i++) {
			Random rand = new Random(8);
			Instances randData = new Instances(traininsts[i]);
			randData.randomize(rand);
			randData.stratify(10);
			double accsum = 0.0;

			for(int n = 0; n < 10; n++) {
				Instances train = randData.trainCV(numfolds, n);
				Instances test = randData.testCV(numfolds, n);

				SMO classifier = new SMO();
				RBFKernel kernel = new RBFKernel();
				classifier.setKernel(kernel);
				//String[] opts = {"-C 1.0", "-L 0.0010", "-P 1.0E-12", "-N 0", "-V -1", "-W 1", "-K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""};
				try {
					//classifier.setOptions(opts);
					classifier.buildClassifier(train);
					System.out.println("***************************************");
					System.out.println("Cross Validation " + n + ":");
					accsum += testAndReport(classifier, test);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}

			System.out.println("***************************************");
			System.out.println("Cross Validation Average Accuracy: " + (accsum / 10.0));
		}
	}

	public void testValidation() {

		for(int i=0;i<featexts.length;i++) {
			Instances train = traininsts[i];
			Instances test = testinsts[i];
			SMO classifier = new SMO();
			RBFKernel kernel = new RBFKernel();
			classifier.setKernel(kernel);
			//String[] opts = {"-C 1.0", "-L 0.0010", "-P 1.0E-12", "-N 0", "-V -1", "-W 1", "-K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""};
			try {
				//classifier.setOptions(opts);
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

	public double testAndReport(SMO classifier, Instances test) {
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
					//System.out.println("Class: " + cl + ", Act: " + act);
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
		return accuracy / (double) nonneutral;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		///Linking all feature selections
		SVMClassification svmc = new SVMClassification("data/train.40000.2009.05.25", "data/testdata.manual.2009.05.25");
		svmc.crossValidation(10);
		svmc.testValidation();
	}
}
