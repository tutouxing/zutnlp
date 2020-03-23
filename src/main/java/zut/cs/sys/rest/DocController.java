package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zut.cs.sys.domain.Doc;
import zut.cs.sys.service.DocManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @PostMapping(value = "/saveDoc")
    public String insertDoc(@RequestBody Doc doc){
        return docManager.saveObj(doc);
    }

    @ApiOperation(value = "删除doc")
    @DeleteMapping(value = "/delDoc")
    public Boolean delDoc(@RequestBody Doc doc){
        return docManager.delDoc(doc);
    }

    @ApiOperation(value = "查找doc")
    @GetMapping(value = "/findDocById")
    public Doc findDocById(@RequestParam String id){
        return docManager.findDocById(id);
    }

    @ApiOperation(value = "查找所有任务")
    @GetMapping(value = "/findTask")
    public List<Doc> findAllTask(){
        return docManager.findAllTask();
    }

    @ApiOperation(value = "查找所有文档")
    @GetMapping(value = "/getAllDocs")
    public List<Doc> getAllDocs(){
        return docManager.findAllDocs();
    }

    @ApiOperation(value = "更新档案")
    @PutMapping(value = "/updateDoc")
    public String updateDoc(@RequestBody Doc doc){
        doc.setPublish("词性标注");
        doc.setAnnotator("admin");
        return docManager.updateDoc(doc);
    }

    @ApiOperation(value = "发布文档即为进行分词和词性标注")
    @PutMapping(value = "/publishTask")
    public Boolean publishTask(@RequestBody Doc doc){
        doc.setPublish("词性标注");
        doc.setAnnotator("admin");
        return docManager.publishTask(doc);
    }

    @ApiOperation(value = "联合条件查询")
    @GetMapping(value = "/findDocsByMulti")
    public List<Doc> findAllDocsByMulti(@RequestParam List<String> words){
        return docManager.findAllDocsByMulti();
    }

    @ApiOperation(value = "分词测试")
    @GetMapping(value = "/segmentWord")
    public Boolean getSegWord(@RequestParam Doc doc){
        return docManager.processDoc(doc);
    }

    @ApiOperation(value = "upload doc")
    @PostMapping(value ="/uploadDoc")
    public Object upload(@RequestParam("file")MultipartFile[] files) throws IOException {
        System.out.println(files.length);
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
            UUID uuid = UUID.randomUUID();
            doc.setDoc_id(uuid.toString());
            doc.setName(name);
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
            Date date=new Date();
            doc.setUpdate_time(sdf.format(date));
            doc.setPhrase("未标注");
            doc.setPublish("中文分词/词性标注");
            doc.setDone("无");
            //转换成中文编码
            doc.setContent(new String(bytes,"GBK"));
            docManager.saveObj(doc);
        }

        return "upload success!";
    }
}
