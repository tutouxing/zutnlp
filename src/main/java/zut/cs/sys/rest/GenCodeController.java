package zut.cs.sys.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zut.cs.sys.domain.TableMessage;
import zut.cs.sys.service.GenMessageManage;
import zut.cs.sys.service.TableMessageManager;
import zut.cs.sys.service.impl.ZipCompress;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/GenCode")
public class GenCodeController {
    @Autowired
    GenMessageManage genMessageManage;
    @Autowired
    TableMessageManager tableMessageManager;

    @ResponseBody
    @PostMapping(value = "/GetEntiredProject", consumes = "application/json")
    public Boolean getEntiredProject(@RequestParam("ProjectName") String ProjectName, @RequestParam("choosepath") String choosepath) {
        List<String> AccordTableNames = new ArrayList<>();
        List<TableMessage> AllTableMessage = this.tableMessageManager.findAll();
        if (AllTableMessage == null) {
            System.out.println("在第一处出错");
            return false;
        }
        for (int i = 0; i < AllTableMessage.size(); i++) {
            String transientProjectName = AllTableMessage.get(i).getProjectName();
            if (transientProjectName == null) {
                System.out.println("在第二处出错");
                return false;
            } else if (transientProjectName.equals(ProjectName)) {
                AccordTableNames.add(AllTableMessage.get(i).getTablename());
            }
        }
        if (AccordTableNames.size() != 0) {
            for (int i = 0; i < AccordTableNames.size(); i++) {
                this.genMessageManage.genCodeAll(AccordTableNames.get(i), choosepath);
            }
        } else {
            System.out.println("在第三处出错");
            return false;
        }
        System.out.println("方法执行成功");
        return true;
    }

    //sourceFilePath是源文件夹，zipFilePath是生成的路径，fileName是打包的压缩包的名字,这里的问题第一是很乱，明天整理一下思路，第二是模板和项目的对照
    // 可能会出问题，第三，postman测试会返回乱码，第四，下载的文件时，必须手动输入,第五，对于在服务器生成的文件，在下载之后需要自动删除
    //第六，放置路径问题需要解决
    @ResponseBody
    @GetMapping("/GetEntiredProjectZip")//要求是在服务器创建出压缩文件，然后返回数据。进行下载后，进行删除。
    public Boolean download(@RequestParam("ProjectName") String projectName, HttpServletResponse response) {
//        String choosePath="F:/WorkSpace";
//        String sourceFilePath = "F:\\WorkSpace\\"+projectName;
//        String zipFilePath = "F:\\WorkSpace";
        String choosePath = "/root/zutnlp_platform/generate/WorkSpace";
        String sourceFilePath = "/root/zutnlp_platform/generate/WorkSpace/" + projectName;
        String zipFilePath = "/root/zutnlp_platform/generate/WorkSpace";
        String fileName = projectName + ".zip";
        boolean flagAuto = getEntiredProject(projectName, choosePath);
        if (flagAuto == false) {
            return false;
        } else {
            //ZipCompress zipCompress=new
            ZipCompress zipCompress = new ZipCompress(zipFilePath + "\\" + fileName, sourceFilePath);
            try {
                if (zipCompress.zip() == true) {
                    BufferedInputStream buffInputStream = null;
                    OutputStream outputStream = null;
                    try {
                        System.out.println("读取的压缩包路径" + zipFilePath + "\\" + fileName);
                        buffInputStream = new BufferedInputStream(new FileInputStream(new File(zipFilePath + "\\" + fileName)));
                        outputStream = response.getOutputStream();
                        byte[] buff = new byte[1024 * 1024]; //如果是稍微大的文件，这里配置的大一些
                        int len = 0;
                        while ((len = buffInputStream.read(buff)) > 0) {
                            //把文件流写入到response的输出流中，供请求端请求
                            response.setContentType("text/html; charset=UTF-8"); // 设置编码字符
                            response.setContentType("application/x-msdownload"); // 设置内容类型为下载类型
                            response.setHeader("Content-disposition", "attachment;filename=" + fileName);// 设置下载的文件名称
                            System.out.println("设置下载的文件名称");
                            outputStream.write(buff, 0, len);
                            outputStream.flush();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (buffInputStream != null) {
                                buffInputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//                    File file=new File(zipFilePath+"\\"+fileName);
//                    file.delete();
//                    zipCompress.deleteDirectory(sourceFilePath);
//                    if (new File(sourceFilePath).exists()){
//                        return false;
//                    }
//                    if (file.exists()){
//                        System.out.println("这里还存在");
//                        return false;
//                    }
                    return true;
                } else {
                    System.out.println("文件打包失败!");
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

}
