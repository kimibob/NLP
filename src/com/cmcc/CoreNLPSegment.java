package com.cmcc;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.crf.CRFClassifier;

public class CoreNLPSegment {
	private static CoreNLPSegment instance;
	private CRFClassifier classifier;

	private CoreNLPSegment() {
		Properties props = new Properties();
		props.setProperty("sighanCorporaDict", "data");
		props.setProperty("serDictionary", "data/cus_dict.txt");
		props.setProperty("inputEncoding", "UTF-8");
		props.setProperty("sighanPostProcessing", "true");
		classifier = new CRFClassifier(props);
		classifier.loadClassifierNoExceptions("data/ctb.gz", props);
		classifier.flags.setProperties(props);
	}

	public static CoreNLPSegment getInstance() {
		if (instance == null) {
			instance = new CoreNLPSegment();
		}

		return instance;
	}

	public String[] doSegment(String data) {
		List a = classifier.segmentString(data);
		System.out.println(a);
		return (String[]) a.toArray();
	}

	public static void main(String[] args) {

		String sentence = "周星驰和我在仓山万达广场里一起打乒乓球。";
		String ret[] = CoreNLPSegment.getInstance().doSegment(sentence);
		for (String str : ret) {
			System.out.println(str);
		}

	}
}
