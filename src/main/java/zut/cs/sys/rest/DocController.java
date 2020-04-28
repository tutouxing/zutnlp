package zut.cs.sys.rest;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
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
    public Boolean saveDoc(@RequestParam("file") MultipartFile file,@RequestParam(required = false)String user) throws IOException {
        docManager.save(file.getOriginalFilename(),file,user);
        return true;
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

    @ApiOperation(value = "分词、词性标注、命名实体抽取")
    @PostMapping(value = "/publishTask")
    public Boolean publishTask(@RequestParam(required = false) String annotate_type,
                               @RequestParam(required = false) String doc_id,
                               @RequestParam(required = false) String username) throws Exception {
        return docManager.segmentWord(doc_id,annotate_type,username);
    }

    @ApiOperation(value = "重新进行词性标注")
    @PostMapping(value = "/reAnnotate")
    public String[] reAnnotate(@RequestParam(required = false) String str,@RequestParam(required = false) String annotation_type){
        return docManager.reAnnotation(str,annotation_type);
    }

    @ApiOperation(value = "人工修改标注后提交更新结果")
    @PostMapping(value = "/saveReAnnotateByUser")
    public Boolean saveReAnnotateByUser(@RequestBody ArrayList<String> words,
                                        @RequestParam(required = false)String annotator,
                                        @RequestParam(required = false)String doc_id,
                                        @RequestParam(required = false)String task_id){

        return docManager.saveReAnnotateByUser(annotator,words,doc_id,task_id);
    }

    @ApiOperation(value = "合并标注")
    @PostMapping(value = "/mergeAnnotation")
    public Boolean mergeAnnotation(@RequestBody ArrayList<String> words,
                                   @RequestParam(required = false)String doc_id,
                                   @RequestParam(required = false)String task1_id,
                                   @RequestParam(required = false)String task2_id,
                                   @RequestParam(required = false)String annotator){
        //task1_id为要更新的任务id，task2_id为要删除的task

        return docManager.mergeAnnotation(words,doc_id,task1_id,task2_id,annotator);
    }

    @ApiOperation(value = "联合条件查询")
    @GetMapping(value = "/findDocsByMulti")
    public List<Doc> findAllDocsByMulti(@RequestParam(required = false) List<String> words){
        return docManager.findAllDocsByMulti();
    }

    @ApiOperation(value = "获取词性分析/中文分词等所有标注任务")
    @GetMapping(value = "/getAllTasks")
    public ArrayList<AnnotateTask> getAllTasks(){
        return docManager.getAllTasks();
    }

    @ApiOperation(value = "初审通过")
    @PostMapping(value = "/passInitialReview")
    public Boolean passInitialReview(@RequestParam(required = false)String doc_id,@RequestParam(required = false)String task_id){
        return docManager.passInitialReview(doc_id,task_id);
    }

    @ApiOperation(value = "终审通过")
    @PostMapping(value = "/passFinalReview")
    public Boolean passFinalReview(@RequestParam(required = false)String doc_id,
                                   @RequestParam(required = false)String task_id){
        return docManager.passFinalReview(doc_id,task_id);
    }

    @ApiOperation(value = "撤销发布")
    @PostMapping(value = "/cancelPublishTask")
    public Boolean cancelPublishTask(@RequestParam(required = false) String doc_id,@RequestParam(required = false) String annotation_type){
        return docManager.recallPublish(doc_id,annotation_type);
    }

    @ApiOperation(value = "upload doc")
    @PostMapping(value ="/uploadDoc")
    public Object upload(@RequestParam("file")MultipartFile file,@RequestParam(required = false) String user) throws IOException {
        System.out.println("??username="+user);
        if (!file.isEmpty()) {
            try {
                String name = file.getOriginalFilename();
                return docManager.save(name, file,user);
//                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else return false;
    }

    @ApiOperation(value = "文本分类")
    @GetMapping(value = "/textClassify")
    public String textClassify(@RequestParam(required = false) String doc_id) throws IOException, TencentCloudSDKException {
        return docManager.textClassify(doc_id);
    }

    @ApiOperation(value = "保存文本分类结果")
    @PostMapping(value = "/saveClassifyResult")
    public Boolean saveClassifyResult(@RequestParam(required = false) String doc_id,@RequestParam(required = false)String classifyResult) {
        return docManager.saveClassifyResult(doc_id,classifyResult);
    }

    @ApiOperation(value = "撤销文本分类结果")
    @PostMapping(value = "/recallClassifyResult")
    public Boolean recallClassifyResult(@RequestParam(required = false) String doc_id) {
        return docManager.recallClassifyResult(doc_id);
    }

    @ApiOperation(value = "机器翻译")
    @GetMapping(value = "/getTextTranslate")
    public String getDocExtractor(@RequestParam(required = false) String text,@RequestParam(required = false) String targetLang) {
        return docManager.machineTranslate(text,targetLang);
    }
}
