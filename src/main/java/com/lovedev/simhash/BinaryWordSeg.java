/**
 * 
 */
package com.lovedev.simhash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangcheng
 *
 */
public class BinaryWordSeg implements IWordSeg {
	public static final Logger logger = LoggerFactory.getLogger(BinaryWordSeg.class);

	private Configuration configuration;

	public BinaryWordSeg(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public List<String> tokens(String doc) {
		IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(doc), configuration);
		Lexeme lexeme;
		List<String> list = new ArrayList<>();
		try {
			while ((lexeme = ikSegmenter.next()) != null) {
				list.add(lexeme.getLexemeText());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}

}
