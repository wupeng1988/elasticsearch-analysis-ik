/**
 * 
 */
package com.lovedev.simhash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author zhangcheng
 * 
 */
public class Simhash {
	private static final Logger logger = LoggerFactory.getLogger(Simhash.class);
	private IWordSeg wordSeg;
	private boolean useWeight;

	public Simhash(IWordSeg wordSeg) {
		this(wordSeg, true);
	}
	public Simhash(IWordSeg wordSeg, boolean weight) {
		this.wordSeg = wordSeg;
		this.useWeight = weight;
	}

	public int hammingDistance(int hash1, int hash2) {
		int i = hash1 ^ hash2;
		i = i - ((i >>> 1) & 0x55555555);
		i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
		i = (i + (i >>> 4)) & 0x0f0f0f0f;
		i = i + (i >>> 8);
		i = i + (i >>> 16);
		return i & 0x3f;
	}

	public int weight(String text, String keyWord) {
		if (useWeight) {
			int i = 1;
			int j = -1;
			while ((j = text.indexOf(keyWord)) >= 0) {
				i++;
				text = text.substring(j + keyWord.length());
			}
			return i;
		} else
			return 1;
	}

	public int hammingDistance(long hash1, long hash2) {
		long i = hash1 ^ hash2;
		i = i - ((i >>> 1) & 0x5555555555555555L);
		i = (i & 0x3333333333333333L) + ((i >>> 2) & 0x3333333333333333L);
		i = (i + (i >>> 4)) & 0x0f0f0f0f0f0f0f0fL;
		i = i + (i >>> 8);
		i = i + (i >>> 16);
		i = i + (i >>> 32);
		return (int) i & 0x7f;
	}

	public long simhash64(String doc) {
		int bitLen = 64;
		int[] bits = new int[bitLen];
		List<String> tokens = wordSeg.tokens(doc);
		if (logger.isDebugEnabled())
			logger.debug(tokens.toString());
		for (String t : tokens) {
			long v = MurmurHash.hash64(t);
			for (int i = bitLen; i >= 1; --i) {
				if (((v >> (bitLen - i)) & 1) == 1)
					bits[i - 1] += weight(doc, t);
				else
					bits[i - 1] -= weight(doc, t);
			}
		}
		long hash = 0x0000000000000000;
		long one = 0x0000000000000001;
		for (int i = bitLen; i >= 1; --i) {
			if (bits[i - 1] > 1) {
				hash |= one;
			}
			one = one << 1;
		}
		return hash;
	}

	public long simhash32(String doc) {
		int bitLen = 32;
		int[] bits = new int[bitLen];
		List<String> tokens = wordSeg.tokens(doc);
		for (String t : tokens) {
			int v = MurmurHash.hash32(t);
			for (int i = bitLen; i >= 1; --i) {
				if (((v >> (bitLen - i)) & 1) == 1)
					bits[i - 1] += weight(doc, t);
				else
					bits[i - 1] -= weight(doc, t);
			}
		}
		int hash = 0x00000000;
		int one = 0x00000001;
		for (int i = bitLen; i >= 1; --i) {
			if (bits[i - 1] > 1) {
				hash |= one;
			}
			one = one << 1;
		}
		return hash;
	}
}
