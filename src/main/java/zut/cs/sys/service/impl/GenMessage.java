package zut.cs.sys.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zut.cs.sys.domain.Connection;
import zut.cs.sys.domain.Props;
import zut.cs.sys.domain.TableMessage;
import zut.cs.sys.service.GenMessageManage;
import zut.cs.sys.service.PropsManager;
import zut.cs.sys.service.TableMessageManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GenMessage implements GenMessageManage {
    TableMessageManager tableMessageManager;
    PropsManager propsManager;
    public static final String BASE_PACKAGE = "zut.cs.demo";//生成代码所在的基础包名称，可根据自己公司的项目修改（注意：这个配置修改之后需要手工修改src目录项目默认的包路径，使其保持一致，不然会找不到类）
    private String JAVAPATH = "src/main/java";
    private String RESOURSES = "src/main/resources";
    public String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    private String CONVERTPROJEC = ConvertPathSlash(PROJECT_PATH);    //转换后的路径
    //    String TEMPLATEDIRECTORY=CONVERTPROJEC+"/src/main/resources/template";//模板位置所在路径
//    String TEMPLATEDIRECTORY=ConvertPathSlash("../src/main/resources/template");
    String TEMPLATEDIRECTORY = "/root/zutnlp_platform/web/zutnlp-platform-module/src/main/resources/template";//模板位置所在路径

    public void testPROJECT_PATH() {
        System.out.println("HELLO" + TEMPLATEDIRECTORY);
    }//请换成你的本地项目的路径，点击template，copyPath

    public HashMap<String, String> getTableModule(String tablename) {
        HashMap<String, String> packageModule = new HashMap<>();
        TableMessage tableMessage = new TableMessage();
        tableMessage = this.tableMessageManager.findByTableName(tablename);
        if (tableMessage == null) {
            return null;
        }
        if (tableMessage.getTablename() != null && tableMessage.getPackageName() != null && tableMessage.getPackageNameLoad() != null && tableMessage.getProjectName() != null) {
            packageModule.put("TableNam文件存在被重写e", tableMessage.getTablename());
            packageModule.put("TablePackageName", tableMessage.getPackageName());
            packageModule.put("TablePackageLoad", tableMessage.getPackageNameLoad());
            packageModule.put("TableProjectName", tableMessage.getProjectName());
            packageModule.put("TableModuleName", tableMessage.getModuleName());
            return packageModule;
        } else {
            return null;
        }
    }


    public HashMap<String, String> getPath(String tableName, String choosePath) {
        choosePath = ConvertPathSlash(choosePath);
        HashMap<String, String> packageModule = getTableModule(tableName);
        if (packageModule != null) {


            String ENTITY_PACKAGE = choosePath +
                    packageConvertPath(packageModule.get("TableProjectName") + "."//生成Entity的路径
                            + packageModule.get("TableModuleName") + "dao" + "."
                            + JAVAPATH + "."
                            + packageModule.get("TablePackageLoad") + "."
                            + "dao." + packageModule.get("TablePackageName") + ".domain");//生成的Model所在包
            System.out.println("ENTITY_PACKAGE    " + ENTITY_PACKAGE);
            String DAO_PACKAGE = choosePath + packageConvertPath(
                    packageModule.get("TableProjectName") + "."//生成Dao的路径
                            + packageModule.get("TableModuleName") + "dao" + "."
                            + JAVAPATH + "."
                            + packageModule.get("TablePackageLoad") + "."
                            + "dao." + packageModule.get("TablePackageName") + ".dao");//生成的dao所在包
            System.out.println(DAO_PACKAGE);
            String SERVICE_PACKAGE = choosePath + packageConvertPath(
                    packageModule.get("TableProjectName") + "."//生成的Service所在包
                            + packageModule.get("TableModuleName") + "service" + "."
                            + JAVAPATH + "."
                            + packageModule.get("TablePackageLoad") + "."
                            + packageModule.get("TablePackageName"));
            System.out.println(SERVICE_PACKAGE);
            String SERVICE_IMPL_PACKAGE = choosePath + packageConvertPath(
                    packageModule.get("TableProjectName") + "."//生成的ServiceImpl所在包
                            + packageModule.get("TableModuleName") + "service" + "."
                            + JAVAPATH + "."
                            + packageModule.get("TablePackageLoad") + "."
                            + packageModule.get("TablePackageName") + ".impl");//生成的ServiceImpl所在包
            System.out.println(SERVICE_IMPL_PACKAGE);
            String CONTROLLER_PACKAGE = choosePath + packageConvertPath(
                    packageModule.get("TableProjectName") + "."//生成的ServiceImpl所在包
                            + packageModule.get("TableModuleName") + "web" + "."
                            + JAVAPATH + "."
                            + packageModule.get("TablePackageLoad") + "."
                            + "web." + packageModule.get("TablePackageName"));//生成的Controller所在包
            String VUE_PACKAGE = choosePath + packageConvertPath(
                    packageModule.get("TableProjectName") + "."//生成的ServiceImpl所在包
                            + packageModule.get("TableModuleName") + "vue" + "."
                            + RESOURSES + "."
                            + packageModule.get("TablePackageLoad") + "."
                            + "vue." + packageModule.get("TablePackageName"));//生成的Controller所在包
            System.out.println(VUE_PACKAGE);
            HashMap<String, String> pathMap = new HashMap<>();
            pathMap.put("ENTITY_PACKAGE", ENTITY_PACKAGE);
            pathMap.put("DAO_PACKAGE", DAO_PACKAGE);
            pathMap.put("SERVICE_IMPL_PACKAGE", SERVICE_IMPL_PACKAGE);
            pathMap.put("SERVICE_PACKAGE", SERVICE_PACKAGE);
            pathMap.put("CONTROLLER_PACKAGE", CONTROLLER_PACKAGE);
            pathMap.put("VUE_PACKAGE", VUE_PACKAGE);
            return pathMap;
        } else return null;
    }

    @Autowired
    public void setTableMessageManager(TableMessageManager tableMessageManager) {
        this.tableMessageManager = tableMessageManager;
    }

    @Autowired
    public void setPropsManager(PropsManager propsManager) {
        this.propsManager = propsManager;
    }

    public HashMap<String, Object> getPropsList(String tableName) {
        List<Props> propsList = new ArrayList<Props>();
        List<Connection> connectionList = new ArrayList<>();
        TableMessage tableMessage = this.tableMessageManager.findByTableName(tableName);
        connectionList = tableMessage.getConnections();
        propsList = tableMessage.getProps();
        HashMap<String, Object> tableData = new HashMap<>();
        tableData.put("tableName", tableNameConvertFirstLetter(tableName));
        tableData.put("tableLowName", tableNameConvertFirstLowLetter(tableName));
        tableData.put("tableAuthod", this.tableMessageManager.findByTableName(tableName).getAuthod());
        tableData.put("tablePackageName", this.tableMessageManager.findByTableName(tableName).getPackageName());
        tableData.put("TableType", this.tableMessageManager.findByTableName(tableName).getTableType());
        tableData.put("TablePackageNameLoad", this.tableMessageManager.findByTableName(tableName).getPackageNameLoad());
        List<Map<String, Object>> connectionMapList = new ArrayList<>();
        List<Map<String, Object>> propsMapList = new ArrayList<>();
        for (int i = 0; i < connectionList.size(); i++) {
            Map<String, Object> connectionMap = new HashMap<>();
            connectionMap.put("connectionMapName", tableNameConvertFirstLetter(connectionList.get(i).getConnectionName()));
            connectionMap.put("connectionMapName", tableNameConvertFirstLowLetter(connectionList.get(i).getConnectionName()));
            connectionMap.put("ConnectionSonTableName", tableNameConvertFirstLetter(connectionList.get(i).getTableConnectionSonTableName()));
            connectionMap.put("ConnectionLowSonTableName", tableNameConvertFirstLowLetter(connectionList.get(i).getTableConnectionSonTableName()));
            connectionMap.put("ConnectionParentTableName", tableNameConvertFirstLetter(connectionList.get(i).getTableConnectionParentTableName()));
            connectionMap.put("ConnectionLowParentTableName", tableNameConvertFirstLowLetter(connectionList.get(i).getTableConnectionParentTableName()));
            connectionMap.put("ConnectionPropsCaseCadeType", connectionList.get(i).getTableConnectionPropsCaseCadeType());
            connectionMap.put("ConnectionTableConnectionType", connectionList.get(i).getTableConnectionType());
            connectionMap.put("ConnectionTableConnectionPropsOneOrTwo", connectionList.get(i).getTableConnectionPropsOneOrTwo());
            connectionMapList.add(connectionMap);
        }
        for (int i = 0; i < propsList.size(); i++) {
            Map<String, Object> propsMap = new HashMap<>();
            propsMap.put("propsName", tableNameConvertFirstLetter(propsList.get(i).getPrtysName()));
            propsMap.put("propsLowName", tableNameConvertFirstLowLetter(propsList.get(i).getPrtysName()));
            propsMap.put("propsType", propsList.get(i).getPropertyType());
            propsMap.put("propsIsNull", propsList.get(i).getPrtys_Isindex());
            propsMap.put("propsIsUnique", propsList.get(i).getPrtys_IsUnique());
            propsMap.put("propsIsKey", propsList.get(i).getPrtysIsnull());
            propsMap.put("propsLong", propsList.get(i).getPrtysLong());
            propsMapList.add(propsMap);
        }
        tableData.put("propsMapList", propsMapList);
        tableData.put("ConnectionMapList", connectionMapList);
        System.out.println(tableData);
        return tableData;
    }


    public void genCodeAll(String tableName, String choosePath) {
        try {
            genEntity(tableName, choosePath);
            genDao(tableName, choosePath);
            genService(tableName, choosePath);
            genServiceImpl(tableName, choosePath);
            genController(tableName, choosePath);
            genVueAndHtml(tableName, choosePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void genCodeEntity(String tableName, String chooosePath) {
        try {
            genEntity(tableName, chooosePath);
            genDao(tableName, chooosePath);
//            genService(tableName);
//            genServiceImpl(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //获取Entity
    public void genEntity(String tableName, String choosePath) throws IOException {
        //模板位置
        String templateDirectory = TEMPLATEDIRECTORY;
        //模板名称
        String templateFile = "Entity.ftl";
        //生成位置
        HashMap<String, String> pathMap = new HashMap<>();
        pathMap = getPath(tableName, choosePath);
        String ENTITY_PACKAGE = pathMap.get("ENTITY_PACKAGE");//生成的路径
        String targetPath = ENTITY_PACKAGE;
        //创建ENTITY的
        String fileName = tableNameConvertFirstLetter(tableName);
        HashMap<String, Object> tableData = new HashMap<>();
        tableData = getPropsList(tableName);
        new File(targetPath).mkdirs();
        File entity = new File(targetPath + fileName + ".java");
        System.out.println("这是新生成的entity的路径" + entity.getCanonicalPath());
        if (entity.exists()) {
            FileWriter fileWriter = new FileWriter(entity);
            System.out.println(entity.getCanonicalPath());
            System.out.println("文件存在被重写");
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        }
        Writer writer = null;
        try {
            writer = new FileWriter(entity);
            Template template = getConfiguration(TEMPLATEDIRECTORY).getTemplate(templateFile, "UTF-8");
            System.out.println("template找到了");
            template.process(tableData, writer);
            System.out.println("Entity.java 生成成功");

        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
        }

    }

    //获取Dao
    public void genDao(String tableName, String choosePath) throws IOException {
        String templateDirectory = TEMPLATEDIRECTORY;
        String templateFile = "Dao.ftl";
        HashMap<String, String> pathMap = new HashMap<>();
        pathMap = getPath(tableName, choosePath);
        String DAO_PACKAGE = pathMap.get("DAO_PACKAGE");//生成的dao所在包路径
        String targetPath = DAO_PACKAGE;
        String fileName = tableNameConvertFirstLetter(tableName) + "Dao" + ".java";
        HashMap<String, Object> tableData = new HashMap<>();
        tableData = getPropsList(tableName);
        new File(targetPath).mkdirs();
        File entityDao = new File(targetPath + fileName);
        if (entityDao.exists()) {
            FileWriter fileWriter = new FileWriter(entityDao);
            System.out.println(entityDao.getCanonicalPath());
            System.out.println("entityDao文件存在被重写");
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            System.out.println(entityDao.getCanonicalPath());
        }
        Writer writer = null;
        try {
            writer = new FileWriter(entityDao);
            Template template = getConfiguration(templateDirectory).getTemplate(templateFile, "UTF-8");
            template.process(tableData, writer);
            System.out.println("Dao.java 生成成功");
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
        }
    }

    //获取Service
    public void genService(String tableName, String choosePath) throws IOException {
        String templateDirectory = TEMPLATEDIRECTORY;
        String templateFile = "Service.ftl";
        HashMap<String, String> pathMap = new HashMap<>();
        pathMap = getPath(tableName, choosePath);
        String SERVICE_PACKAGE = pathMap.get("SERVICE_PACKAGE");
        String targetPath = SERVICE_PACKAGE;//Service的路径
        String fileName = tableNameConvertFirstLetter(tableName) + "Manager" + ".java";
        HashMap<String, Object> tableData = new HashMap<>();
        tableData = getPropsList(tableName);
        new File(targetPath).mkdirs();
        File entityService = new File(targetPath + fileName);
        if (entityService.exists()) {
            FileWriter fileWriter = new FileWriter(entityService);
            System.out.println(entityService.getCanonicalPath());
            System.out.println("entityService文件存在被重写");
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            System.out.println(entityService.getCanonicalPath());
        }
        Writer writer = null;
        try {
            writer = new FileWriter(entityService);
            Template template = getConfiguration(templateDirectory).getTemplate(templateFile, "UTF-8");
            template.process(tableData, writer);
            System.out.println("Service.java 生成成功");
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
        }
    }

    //获取serviceImpl
    public void genServiceImpl(String tableName, String choosePath) throws IOException {
        String templateDirectory = TEMPLATEDIRECTORY;
        String templateFile = "Impl.ftl";
        HashMap<String, String> pathMap = new HashMap<>();
        pathMap = getPath(tableName, choosePath);
        String SERVICE_IMPL_PACKAGE = pathMap.get("SERVICE_IMPL_PACKAGE");//生成的ServiceImpl所在包路径
        String targetPath = SERVICE_IMPL_PACKAGE;
        String fileName = tableNameConvertFirstLetter(tableName) + "ManagerImpl" + ".java";
        HashMap<String, Object> tableData = new HashMap<>();
        tableData = getPropsList(tableName);
        new File(targetPath).mkdirs();
        File entityServiceImpl = new File(targetPath + fileName);
        if (entityServiceImpl.exists()) {
            FileWriter fileWriter = new FileWriter(entityServiceImpl);
            System.out.println(entityServiceImpl.getCanonicalPath());
            System.out.println("entityServiceImpl文件存在被重写");
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            System.out.println(entityServiceImpl.getCanonicalPath());
        }
        Writer writer = null;
        try {
            writer = new FileWriter(entityServiceImpl);
            Template template = getConfiguration(templateDirectory).getTemplate(templateFile, "UTF-8");
            template.process(tableData, writer);
            System.out.println("ServiceImpl.java 生成成功");
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
        }
    }

    //获取Controller
    public void genController(String tableName, String choosePath) throws IOException {
        String templateDirectory = TEMPLATEDIRECTORY;
        String templateFile = "CodeController.ftl";
        String CONTROLLER_PACKAGE = "module";
        HashMap<String, String> pathMap = new HashMap<>();
        pathMap = getPath(tableName, choosePath);
        CONTROLLER_PACKAGE = pathMap.get("CONTROLLER_PACKAGE");//生成的Controller所在包
        String targetPath = CONTROLLER_PACKAGE;
        String fileName = tableNameConvertFirstLetter(tableName) + "Controller" + ".java";
        new File(targetPath).mkdirs();
        HashMap<String, Object> tableData = new HashMap<>();
        tableData = getPropsList(tableName);
        File entityController = new File(targetPath + fileName);
        if (entityController.exists()) {
            FileWriter fileWriter = new FileWriter(entityController);
            System.out.println(entityController.getCanonicalPath());
            System.out.println("entityController文件存在被重写");
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            System.out.println(entityController.getCanonicalPath());
        }
        Writer writer = null;
        try {
            writer = new FileWriter(entityController);
            Template template = getConfiguration(templateDirectory).getTemplate(templateFile, "UTF-8");
            template.process(tableData, writer);
            System.out.println("Controller.java 生成成功");
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
        }
    }

    //获取vue.js和HTML前端代码
    public void genVueAndHtml(String tableName, String choosePath) throws IOException {
        String templateDirectory = TEMPLATEDIRECTORY;
        String templateHtmlFile = "webhtml.ftl";
        String templateVueFile = "webvue.ftl";
        HashMap<String, String> pathMap = new HashMap<>();
        pathMap = getPath(tableName, choosePath);
        String VUE_PACKAGE = pathMap.get("VUE_PACKAGE");//生成的dao所在包路径
        String targetPath = VUE_PACKAGE;
        String fileName = tableNameConvertFirstLetter(tableName) + "HTML" + ".html";
        String fileName2 = tableNameConvertFirstLetter(tableName) + "VUE" + ".vue";
        HashMap<String, Object> tableData = new HashMap<>();
        tableData = getPropsList(tableName);
        new File(targetPath).mkdirs();
        File entityHTML = new File(targetPath + fileName);
        new File(targetPath).mkdirs();
        File entityVUE = new File(targetPath + fileName2);
        if (entityHTML.exists()) {
            FileWriter fileWriter = new FileWriter(entityHTML);
            System.out.println(entityHTML.getCanonicalPath());
            System.out.println("entityHTML文件存在被重写");
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            System.out.println(entityHTML.getCanonicalPath());
        }
        Writer writer = null;
        try {
            writer = new FileWriter(entityHTML);
            Template template = getConfiguration(templateDirectory).getTemplate(templateHtmlFile, "UTF-8");
            template.process(tableData, writer);
            System.out.println("templateVueFile.Html 生成成功");
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer.close();
        }
        if (entityVUE.exists()) {
            FileWriter fileWriter = new FileWriter(entityVUE);
            System.out.println(entityVUE.getCanonicalPath());
            System.out.println("entityVue文件存在被重写");
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            System.out.println(entityVUE.getCanonicalPath());
        }
        Writer writer1 = null;
        try {
            writer1 = new FileWriter(entityVUE);
            Template template = getConfiguration(templateDirectory).getTemplate(templateVueFile, "UTF-8");
            template.process(tableData, writer1);
            System.out.println("templateVueFile.vue 生成成功");
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writer1.close();
        }
    }

    //freemaker配置
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

    //转义package到路径
    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

    //转义路径中的反斜杠
    private static String ConvertPathSlash(String path) {
        return path.replaceAll("\\\\", "/");
    }

    //首字母大写
    public String tableNameConvertFirstLetter(String tableName) {
        if (tableName == null) {
            return "";
        }
        tableName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
        return tableName;
    }

    public String tableNameConvertFirstLowLetter(String tableName) {
        if (tableName == null) {
            return "";
        }
        tableName = tableName.substring(0, 1).toLowerCase() + tableName.substring(1);
        return tableName;
    }

}
