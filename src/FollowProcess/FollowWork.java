package FollowProcess;
import PreProcess.Corpus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
/* 
 * 
 * Created on March 16 14:02:14 2015
 * author: Xiangfu Song 
 * email : bintasong@gmail.com
 *
 * */
class KW{
	public int topicIndex;
	public int wordIndex;
	public int wordNumber;
	public KW(int topic,int word,int number){
		topicIndex = topic;
		wordIndex = word;
		wordNumber = number;
	}
}
class MK{
	public int docIndex;
	public int topicIndex;
	public int topicNumber;
	public MK(int doc,int topic,int number){
		docIndex = doc;
		topicIndex = topic;
		topicNumber = number;
	}
}

class Comparator1 implements Comparator<KW>{
	public int compare (KW left,KW right) { 
        return(right.wordNumber - left.wordNumber); 
    }
}
class Comparator2 implements Comparator<MK>{
	public int compare (MK left,MK right) { 
        return(right.topicNumber - left.topicNumber); 
    }
}

public class FollowWork{
	public Corpus corpus;
	public String MKpath;
	public String KWpath;
	public String Phipath;
	
	public FollowWork(Corpus corpus,String MKpath,String KWpath,String Phipath){
		this.corpus = corpus;
		this.MKpath = MKpath;
		this.KWpath = KWpath;
		this.Phipath = Phipath;
	}
	
	public void sortKWandSave(){
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(KWpath)); 
			
			Comparator1 comp1 = new Comparator1();
			int k,w;
			for(k = 0;k < corpus.K;k++){
				ArrayList<KW> ki = new ArrayList<KW>();
				for(w = 0;w < corpus.V;w++){
					ki.add(new KW(k,w,corpus.nkw[k][w]));
				}
				Collections.sort(ki,comp1);
				String line = "topic " + String.valueOf(k)+": ";
				for(w = 0;w < 30;w++){
					KW kw = ki.get(w);
					line += corpus.getWordByIndex(kw.wordIndex)/*+":"+String.valueOf(kw.wordNumber/(double)corpus.nkwSum[k])+*/+"|";
				}
				writer.write(line + "\n");				
			}	
		}catch(IOException e){
			System.out.println("写入 topic-word 文档错误");
			e.printStackTrace();				
		}finally{
			if (writer != null) {
			try {
					writer.close();
				} catch (IOException e) {
					System.out.println("关闭 topic-word文档错误!");
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sortMKandSave(){
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(MKpath)); 
			
			Comparator2 comp2 = new Comparator2();
			int m,k;
			for(m = 0;m < corpus.M;m++){
				ArrayList<MK> mi = new ArrayList<MK>();
				//int nmkSum = 0;
				for(k = 0;k < corpus.K;k++){
					mi.add(new MK(m,k,corpus.nmk[m][k]));
					
					//统计每个文档中的的主题分布概率（未排序）
					if(k < corpus.MA)
						corpus.phi[m][k] = (double)(corpus.nmk[m][k]+corpus.alpha_aux[k])/(corpus.nmkSum[m]+corpus.alpha_aux_sum); 
					else
						corpus.phi[m][k] = (double)(corpus.nmk[m][k]+corpus.alpha_tar[k])/(corpus.nmkSum[m]+corpus.alpha_tar_sum); 
				}
				Collections.sort(mi,comp2);
				String line = corpus.docs.get(m).docname+":  ";
				for(k = 0;k < corpus.K;k++){
					MK mij = mi.get(k);
					line += "topic "+mij.topicIndex+" :"+String.valueOf(mij.topicNumber/(double)corpus.nmkSum[m])+" ";
				}
				/*if(nmkSum != corpus.nmkSum[m]){
					System.out.println("***************ERROR***************");
				}
				*/
				writer.write(line + "\n");				
			}	
		}catch(IOException e){
			System.out.println("写入 topic-word 文档错误");
			e.printStackTrace();				
		}finally{
			if (writer != null) {
			try {
					writer.close();
				} catch (IOException e) {
					System.out.println("关闭 topic-word文档错误!");
					e.printStackTrace();
				}
			}
		}
	}

	public void writeTheta(){
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(Phipath)); 
			
			int m,k;
			for(m = 0;m < corpus.M;m++){
				String line = corpus.docs.get(m).docname + " ";
				for(k = 0;k < corpus.K;k++){
					line += String.valueOf(corpus.phi[m][k]) + " ";	
				}
				writer.write(line + "\n");
			}
			//System.out.println("写入 phi 完成！");
		}catch(IOException e){
			System.out.println("写入 phi 文档错误");
			e.printStackTrace();				
		}finally{
			if (writer != null) {
			try {
					writer.close();
				} catch (IOException e) {
					System.out.println("关闭 phi文档错误!");
					e.printStackTrace();
				}
			}
		}
	}
}