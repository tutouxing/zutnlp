package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zut.cs.sys.domain.AnnotateTask;
import zut.cs.sys.domain.Doc;
import zut.cs.sys.service.DocManager;
import zut.cs.sys.util.DateGenerate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

//    @Autowired
//    public void setDocManager(DocManager docManager){
//        this.docManager = docManager;
//        this.manager = this.docManager;
//    }

    @ApiOperation(value = "insert obj doc")
    @PostMapping(value = "/saveDoc")
    public Boolean saveDoc(@RequestParam("file") MultipartFile file) throws IOException {
        docManager.save(file.getOriginalFilename(),file);
        return true;
//        return id;
    }

    @ApiOperation(value = "通过id查找")
    @GetMapping("/{id}")
    public Doc getDoc(@PathVariable String id) throws IOException {
        Doc doc=docManager.findDocById(id);
        return doc;
    }

    @ApiOperation(value = "删除doc")
    @DeleteMapping(value = "/delDoc/{id}")
    public Boolean delDoc(@PathVariable String id){
        return docManager.delDocById(id);
    }

    @ApiOperation(value = "查找所有文档")
    @GetMapping(value = "/getAllDocs")
    public List<Doc> getAllDocs(){
        return docManager.findAllDocs();
    }

    //    @ApiOperation(value = "更新档案")
//    @PutMapping(value = "/updateDoc")
//    public Boolean updateDoc(@RequestBody Doc doc){
//        return docManager.updateDoc(doc);
//    }

    @ApiOperation(value = "分词和词性标注")
    @PostMapping(value = "/publishTask")
    public Boolean publishTask(@RequestParam(required = false) String annotate_type,@RequestParam(required = false) String doc_id) throws Exception {
        System.out.println(annotate_type+doc_id);
//        if (annotate_type.equals("中文分词")||annotate_type.equals("词性标注")){
            docManager.segmentWord(doc_id,annotate_type);
//        }
        return true;
    }

    @ApiOperation(value = "联合条件查询")
    @GetMapping(value = "/findDocsByMulti")
    public List<Doc> findAllDocsByMulti(@RequestParam(required = false) List<String> words){
        return docManager.findAllDocsByMulti();
    }

    @ApiOperation(value = "词性分析/中文分词等")
    @GetMapping(value = "/getAllTasks")
    public ArrayList<AnnotateTask> getAllTasks(){

        return docManager.getAllTasks();
    }

    @ApiOperation(value = "撤销发布")
    @PostMapping(value = "/cancelPublishTask")
    public Boolean cancelPublishTask(@RequestParam(required = false) String doc_id,@RequestParam(required = false) String annotation_type){
        return docManager.recallPublish(doc_id,annotation_type);
    }

    @ApiOperation(value = "upload doc")
    @PostMapping(value ="/uploadDoc")
    public Object upload(@RequestParam("file")MultipartFile file) throws IOException {
//        System.out.println(files.length);
//        for (MultipartFile file:files){
        //文件名
//            String name = file.getOriginalFilename();
//            docManager.save(name,file);
//        }
        if (!file.isEmpty()) {
            try {
                String name = file.getOriginalFilename();
                docManager.save(name, file);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else return false;

    }
}
