package zut.cs.sys.util;

import org.springframework.web.multipart.MultipartFile;
import zut.cs.sys.domain.Picture;
import zut.cs.sys.service.PictureManager;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    /**
     * @Description: java类作用描述
     * @Author: wastelands
     * @CreateDate: 2019/5/21$ 15:07$
     */

    PictureManager pictureManager;

    public static boolean upload(MultipartFile file, String path, String fileName) {

        // 使用UUID生成新的文件名防止重名
//        String realPath = path + "/" + FileNameUtils.getFileName(fileName);
//        System.out.println("realpath:"+path);
        File dest = new File(path);

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean saveUploadFile(String path) {

        Picture picture = new Picture();
        String hostUrl = "http://118.25.191.46:8080" + path.substring(path.indexOf(":") + 1);
        System.out.println(path.substring(path.indexOf(":") + 1));
        picture.setUrl(hostUrl);
//        pictureManager.save(picture);
        return true;
    }
}
