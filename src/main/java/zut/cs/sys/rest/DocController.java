package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zut.cs.sys.domain.Doc;
import zut.cs.sys.service.DocManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/doc")
@Api(tags = "文档接口")
//public class DocController extends GenericController<Doc,Long, DocManager> {
public class DocController {
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/2/5$ 0:54$

     */
    @Autowired
    DocManager docManager;
    @Autowired
    private GridFsTemplate gridFsTemplate;

//    @Autowired
//    public void setDocManager(DocManager docManager){
//        this.docManager = docManager;
//        this.manager = this.docManager;
//    }

    @ApiOperation(value = "get docs by annotator")
    @GetMapping("getByAnnotator")
    public List<Doc> getDocByAnnotator(@RequestParam String annotator){
        return this.docManager.findByAnnotator(annotator);
    }

    @ApiOperation(value = "insert obj doc")
    @PostMapping("/saveDoc")
    public String insertDoc(@RequestBody Doc doc){
        return docManager.setObj(doc);
    }

    @ApiOperation(value = "upload doc")
    @PostMapping(value ="/uploadDoc")
    public Object upload(@RequestParam("file")MultipartFile[] files) throws IOException {
        for (int i = 0;i<files.length;i++){
            //文件名
            String name = files[i].getOriginalFilename();
            System.out.println("name:"+name);
            //文件类型
            String type = files[i].getContentType();
            System.out.println("type:"+type);
            //文件流
            byte[] bytes = files[i].getBytes();
            /*InputStream in = file.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine())!=null){
                System.out.println(line);
            }
            isr.close();*/
//            System.out.println("bytes:"+new String(bytes,"GBK"));
//            String content = file.getInputStream();
            //新建文档实体
            Doc doc = new Doc();
            doc.setName(name);
            doc.setUpdate_time(new Date());
            doc.setPhrase("未标注");
            //转换成中文编码
            doc.setContent(new String(bytes,"GBK"));
            docManager.setObj(doc);
        }

        return "upload success!";
    }
}
