package zut.cs.sys.rest;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zut.cs.sys.base.rest.GenericController;
import zut.cs.sys.domain.Picture;
import zut.cs.sys.service.ContentManager;
import zut.cs.sys.service.PictureManager;
import zut.cs.sys.util.FileNameUtils;
import zut.cs.sys.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/picture")
@Api("图片接口")
public class PictureController extends GenericController<Picture, Long, PictureManager> {
    /**
     * @Description: java类作用描述
     * @Author: wastelands
     * @CreateDate: 2019/5/14$ 15:26$
     */
    PictureManager pictureManager;
    private ResourceLoader resourceLoader;

    @Autowired
    ContentManager contentManager;

    @Autowired
    public void setPictureManager(PictureManager pictureManager) {
        this.pictureManager = pictureManager;
        this.manager = this.pictureManager;
//        this.resourceLoader = resourceLoader;
    }

    @Autowired
    public PictureController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //    @Value("${spring.servlet.multipart.location}")
    private String path;

    @PostMapping("/fileUpload")
    public List<Picture> upload(@RequestParam("file") MultipartFile file) {
        //1定义要上传文件 的存放路径
        String localPath = "E:/images/upload";
        //2获得文件名字
        String fileName = file.getOriginalFilename();
        //2上传失败提示
        String warning = "";
        String hostUrl = null;
        String realPath = localPath + "/" + FileNameUtils.getFileName(fileName);
        System.out.println("realpath:" + realPath);
        if (FileUtils.upload(file, realPath, fileName)) {
            //上传成功
            warning = "上传成功";
            //图片信息保存到数据库
            Picture picture = new Picture();
            //部署服务器地址
            hostUrl = "http://118.25.191.46:8080" + realPath.substring(realPath.indexOf(":") + 1);
            //本地运行
//            String hostUrl = "http://localhost:8080"+realPath.substring(realPath.indexOf(":")+1);
//            System.out.println(path.substring(path.indexOf(":")+1));
            picture.setUrl(hostUrl);
//            picture.setContent(contentManager.findById(content_id));
            //后续进一步处理content中img地址
            pictureManager.save(picture);
            List<Picture> newPicture = new ArrayList<>();
            newPicture.add(pictureManager.findByUrl(hostUrl));
            return newPicture;
        }else{
            warning="上传失败";
            return null;
        }
//        System.out.println(warning);
//        return hostUrl;
    }

//    @ApiOperation(value = "得到与内容相关的所有图片")
//    @PostMapping("content_id/")
//    public Set<Picture> getPicturesByContent(@RequestBody Long content_id){
//        Content content = contentManager.findById(content_id);
//        return pictureManager.findByContent(content);
//    }


}