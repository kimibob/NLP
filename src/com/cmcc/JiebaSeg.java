package com.cmcc;

import java.io.File;
import java.util.List;

import com.cmcc.analysis.jieba.JiebaSegmenter;
import com.cmcc.analysis.jieba.SegToken;
import com.cmcc.analysis.jieba.WordDictionary;
import com.cmcc.analysis.jieba.JiebaSegmenter.SegMode;

/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver.		Date		Author			Description<br>
 * ------------------------------------------------------<br>
 * 1.0		2018年7月23日 下午3:06:01		zhouqiang		created.<br>
 */

public class JiebaSeg {

	public static void main(String[] args) {
		
		WordDictionary dictAdd = WordDictionary.getInstance();
//	    dictAdd.loadUserDict("wydict/591.dict");
//	    dictAdd.loadUserDict("wydict/cover_type.dict");
	    dictAdd.loadUserDict("wydict/vendor_name.dict");
	    //System.out.println(dictAdd.containsWord("大唐"));
		JiebaSegmenter segmenter = new JiebaSegmenter();
	    String[] sentences =
	        new String[] {"查询上周一至上周五福州和厦门鼓楼区集美区平和县中兴大唐诺西华为的平均无线接通率"
	                      };
	    for (String sentence : sentences) {
	        List<SegToken> list = segmenter.process(sentence, SegMode.SEARCH);
	        for (SegToken token : list) {
	        	System.out.println(token.word);
			}
	    }
	    //System.out.println(dictAdd.containsWord("海润尊品"));
		
	}
}
