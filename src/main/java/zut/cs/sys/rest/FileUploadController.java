package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zut.cs.sys.util.FileNameUtils;
import zut.cs.sys.util.FileUtils;

@RestController
@RequestMapping("/upload")
@Api("文件上传")
public class FileUploadController {
    /**
     * @Description: java类作用描述
     * @Author: wastelands
     * @CreateDate: 2019/5/21$ 15:10$
     */
    private final ResourceLoader resourceLoader;

    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //  @Value("${spring.servlet.multipart.location}")
    private String path = "E:/images/upload";

    /**
     * @return 跳转到文件显示页面
     */
    @GetMapping("/show")
    public String show() {
        return "show";
    }

    /**
     * @param file 上传的文件
     * @return
     */
    @PostMapping("/fileUpload")
    public String upload(@RequestParam("file") MultipartFile file) {
        //1定义要上传文件 的存放路径
        String localPath = "E:/images/upload";
        //2获得文件名字
        String fileName = file.getOriginalFilename();
        localPath = localPath + "/" + FileNameUtils.getFileName(fileName);
        //2上传失败提示
        String warning = "";
        String hostUrl = "";
        if (FileUtils.upload(file, localPath, fileName)) {
            //上传成功
            warning = "上传成功";
            hostUrl = "http://118.25.191.46:8080" + localPath.substring(localPath.indexOf(":") + 1);
            return hostUrl;
        } else {
            warning = "上传失败";
            return "-1";
        }
//        System.out.println(warning);
//        return "上传成功";
    }

    /**
     * 显示图片
     *
     * @param fileName 文件名
     * @return
     */

    @GetMapping("showFile")
    public ResponseEntity show(String fileName) {
        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + path + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
}
