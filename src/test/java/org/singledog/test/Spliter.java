package org.singledog.test;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.StringReader;
import java.util.Arrays;

/**
 * Created by Adam.Wu on 2016/11/2.
 */
public class Spliter {

    /**
     * 分词器分词
     *
     * @param text
     * @throws Exception
     */
    private static String token(String text, boolean isMaxWordLength)
            throws Exception {
        try {
            java.util.List<String> list = new java.util.ArrayList<String>();

            Settings setting = Settings.builder()
                    .put("path.home","")
                    .put("path.conf", "E:\\java\\workspace_wp\\edooon-server\\elasticsearch-analysis-ik\\target\\")
                    .build();
            Environment environment = new Environment(setting);
            Configuration configuration = new Configuration(environment, setting);
            configuration.setUseSmart(isMaxWordLength);
            IKAnalyzer analyzer = new IKAnalyzer(configuration);
            IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(text), configuration);
            Lexeme lexeme;
            while ((lexeme = ikSegmenter.next()) != null) {
                list.add(lexeme.getLexemeText());
            }
            return Arrays.toString(list.toArray());
        } catch (Exception e) {
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        String text = "希腊晋级16强不要奖金要基地 称是为人民而战 人民大会堂";
         text = "为了让播客推荐算法正常运作，需要得到大量播客的标题和描述。幸运的是，有一个iTunes API，但不幸的是，我需要的信息是不允许查询的。\n" +
                 "我需要解决的第一个问题是指定需要查询哪些播客。iTunes API不允许获取iTunes集合中的部分或所有播客的列表[脚注1]。必须通过（例如，按标题）搜索特定的播客或使用其iTunes ID查找特定的播客。我从github上找到了一个大约135,000个播客的数据集，从中得到数千个播客的标题，解决了这个问题。\n" +
                 "\n" +
                 "我需要解决的第二个问题是获得每个播客对应的标题和描述。但很遗憾，这些信息不能通过API获得，但通过播客的ID是可以从iTunes网站上获取到的。这里需要两个步骤来完成。首先，用数据集中播客的标题通过iTunes API搜索相关的播客。然后使用另外一个API通过播客ID查找到与该播客关联的iTunes网页，并使用BeautifulSoup库从其中提取标题和描述信息。\n" +
                 "\n" +
                 "因为API查询和网页抓取都比较耗费时间，所以我尽量减少了我的数据集。在查询API之前，我使用guess_ language包删除那些不是英语类的播客。在网络抓取之前，我删除了那些在过去45天内没有发布剧集的播客（因为我只需要活跃的播客）。不过，我在尝试下载数据时遇到了一些问题。首先，iTunes API有一个速率限制，但不会告诉你它具体是多少。因此，查询会在开始一段时间之后就会出现超时现象，于是我的脚本就被挂起了。我使用下面的函数解决了这个问题，这个函数允许我测试当前API查询的URL是否正常，如果异常就等待和重试。60秒内进行四次重试直到成功。";

        System.out.println(token(text, false));
        System.out.println(token(text, true));

    }

}
