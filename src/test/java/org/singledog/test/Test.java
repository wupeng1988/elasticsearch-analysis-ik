package org.singledog.test;

import com.lovedev.simhash.BinaryWordSeg;
import com.lovedev.simhash.Simhash;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.wltea.analyzer.cfg.Configuration;

/**
 * Created by Adam.Wu on 2016/11/3.
 */
public class Test {

    private static final Settings setting = Settings.builder()
            .put("path.home","")
            .put("path.conf", "E:\\java\\workspace_wp\\edooon-server\\elasticsearch-analysis-ik\\target\\")
            .build();
    private static final Environment environment = new Environment(setting);

    private static Configuration configuration;

    static {
        configuration = new Configuration(environment, setting);
        configuration.setUseSmart(true);
    }

    public static void main(String[] args) {
        String text = "事实上，传我们考虑采用为每一个web文档通过hash的方式生成一个指纹（fingerprint）。传统的加密式hash，比如md5，其设计的目的是为了让整个分布尽可能地均匀，输入内容哪怕只有轻微变化，hash就会发生很大地变化。我们理想当中的哈希函数，需要对几乎相同的输入内容，产生相同或者相近的hashcode，换句话说，hashcode的相似程度要能直接反映输入内容的相似程度。很明显，前面所说的md5等传统hash无法满足我们的需求。 统比较两个文本相似性的方法，大多是将文本分词之后，转化为特征向量距离的度量，比如常见的欧氏距离、海明距离或者余弦角度等等。两两比较固然能很好地适应，但这种方法的一个最大的缺点就是，无法将其扩展到海量数据。例如，试想像Google那种收录了数以几十亿互联网信息的大型搜索引擎，每天都会通过爬虫的方式为自己的索引库新增的数百万网页，如果待收录每一条数据都去和网页库里面的每条记录算一下余弦角度，其计算量是相当恐怖的。 ";
        String text2 = "事实上，我们考虑采用为每一个web文档通过hash的方式生成一个指纹（fingerprint）。传统的加密式hash，比如md5，其设计的目的是为了让整个分布尽可能地均匀，输入内容哪怕只有轻微变化，hash就会发生很大地变化。我们理想当中的哈希函数，需要对几乎相同的输入内容，产生相同或者相近的hashcode，换句话说，hashcode的相似程度要能直接反映输入内容的相似程度。很明显，前面所说的md5等传统hash无法满足我们的需求传统比较两个文本相似性的方法，大多是将文本分词之后，转化为特征向量距离的度量，比如常见的欧氏距离阿双方还开始就刚好看见看见好看、海明距离或者余弦角度等等。两两比较固然能很好地适应，但这种方法的一个最大的缺点就是，无法将其扩展到海量数据。例如，试想像Google那种收录了数以几十亿互联网信息的大型搜索引擎，每天都会通过爬虫的方式为自己的索引库新增的数百万网页，如果待收录每一条数据都去和网页库里面的每条记录算一下余弦角度，其计算量是相当恐怖的。 ";
        String text3 = "事实上，我们考虑采用为每一个web文档通过hash的方式生成一个指纹（fingerprint）。传统的加密式hash，比如md5，其设计的目的是为了让整个分布尽可能地均匀，输入内容哪怕只有轻微变化，hash就会发生很大地变化。我们理想当中的哈希函数，需要对几乎相同的输入内容，产生相同或者相近的hashcode，换句话说，hashcode的相似程度要能直接反映输入内容的相似程度。很明显，前面所说的md5等传统hash无法满足我们的需求传统比较两个文本相似性的方法，大多是将文本分词之后，转化为特征向量距离的度量，比如常见的欧氏距离、海明距离或者余弦角度等等。两两比较固然能很好地适应，但这种方法的一个最大的缺点就是，无法将其扩展到海量数据。例如，试想像Google那种收录了数以几十亿互联网信息的大型搜索引擎，每天都会通过爬虫的方式为自己的索引库新增的数百万网页，如果待收录每一条数据都去和网页库里面的每条记录算一下余弦角度，其计算量是相当恐怖的。 asdj十多个方式的分公司对方给第三个";
        Simhash simhash = new Simhash(new BinaryWordSeg(configuration));

        long hash1 = simhash.simhash64(text);
        long hash2 = simhash.simhash64(text2);
        long hash3 = simhash.simhash64(text3);
        System.out.println(Long.toBinaryString(hash1));
        System.out.println(Long.toBinaryString(hash2));
        System.out.println(Long.toBinaryString(hash3));

        System.out.println(simhash.hammingDistance(hash1, hash2));
        System.out.println(simhash.hammingDistance(hash1, hash3));
        System.out.println(simhash.hammingDistance(hash3, hash2));


        hash1 = simhash.simhash32(text);
        hash2 = simhash.simhash32(text2);
        hash3 = simhash.simhash32(text3);
        System.out.println(hash1);
        System.out.println(hash2);
        System.out.println(hash3);

        System.out.println(simhash.hammingDistance(hash1, hash2));
        System.out.println(simhash.hammingDistance(hash1, hash3));
        System.out.println(simhash.hammingDistance(hash3, hash2));

    }

}
