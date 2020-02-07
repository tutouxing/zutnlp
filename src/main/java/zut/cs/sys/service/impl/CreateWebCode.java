package zut.cs.sys.service.impl;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class CreateWebCode {
   /* private String path="C:/Users/i/Desktop/template";
    private String filehtmlName="webforhtml.html";
    private String filevueName="webforvue.vue";
    private String templatepath="D:\\spring tools\\IDEA\\space\\zutnlp-admin\\zutnlp-platform-module\\src\\main\\resources\\template";
    private String templatenamehtml="webhtml.ftl";
    private String templatenamevue="webvue.ftl";*/

    public void codehtml(String path, String filehtmlName, String templatepath, String templatenamehtml) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        new File(path).mkdirs();
        File nFile = new File(path + "/" + filehtmlName);//新建一个文件

        if (nFile.exists()) {
            System.out.println("文件建立成功");
            throw new RuntimeException("File \'" + filehtmlName + "\' already exists");
        }
        Writer writer = null;
        try {
            writer = new FileWriter(nFile);//吧生成的模板内容写入文件
            Template template = getConfiguration(templatepath).getTemplate(templatenamehtml, "UTF-8");
            template.process(map, writer);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
        }
    }

    private static Configuration getConfiguration(String templateDirectory) {

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
        try {
            configuration.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);

            configuration.setDirectoryForTemplateLoading(new File(templateDirectory));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return configuration;
    }

}
