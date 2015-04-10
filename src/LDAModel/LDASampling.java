package LDAModel;

import PreProcess.Corpus;
import FollowProcess.FollowWork;

import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/*
 *  Created on March 15 14:02:14 2015
 * author: Xiangfu Song 
 * email : bintasong@gmail.com
 * 
 * 
 * */
public class LDASampling{
	public static int topicNumOfAux = 25;
	public static int topicNumOfTar = 25;
	public static int iteration = 200;
	public static int docNumOfAux = 0;
	public static void main(String[] argv){
		System.out.println("add document to memory ...");
		Corpus corpus =new Corpus(topicNumOfAux,topicNumOfTar);
		corpus.addDoc("C:/Users/binta/Desktop/lda_java/alpha-LDA/Data/Auxiliary");
		docNumOfAux = corpus.M;
		corpus.MA = docNumOfAux;
		corpus.addDoc("C:/Users/binta/Desktop/lda_java/alpha-LDA/Data/Target");
		System.out.println("add document to memory done");
		System.out.printf("the number of words: %d\n",corpus.V);
		corpus.init();
		System.out.println("init done");
		System.out.println("iteration...");
		corpus.sampling(iteration);
		System.out.println("iteration done");
		FollowWork fw = new FollowWork(corpus,"C:/Users/binta/Desktop/lda_java/alpha-LDA/Data/LDAResults/document-topic.txt","C:/Users/binta/Desktop/lda_java/alpha-LDA/Data/LDAResults/topic-word.txt","C:/Users/binta/Desktop/lda_java/alpha-LDA/Data/LDAResults/theta.txt");
		fw.sortMKandSave();
		fw.sortKWandSave();
		fw.writeTheta();
		System.out.println("alpha-dlda done");
		//------------- k-means ------------
		//System.out.println("using weka k-means...");
		//SimpleKMeans s = new SimpleKMeans();
	
	}
}
