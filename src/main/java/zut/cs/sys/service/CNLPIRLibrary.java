package zut.cs.sys.service;




import com.sun.jna.Library;
import com.sun.jna.Native;

public interface CNLPIRLibrary extends Library {
    /**
     * @Description: java类作用描述

     * @Author: wastelands

     * @CreateDate: 2020/3/24$ 0:45$

     */
    //根据当前计算机环境决定使用resources下的NLPIR.dll插件，这里是win10-64
    CNLPIRLibrary Instance = (CNLPIRLibrary) Native.loadLibrary("E:\\java\\workspace\\platform\\src\\main\\resources\\NLPIR", CNLPIRLibrary.class);

    /**
     * 初始化
     * @param sDataPath Data目录所在路径
     * @param encoding 编码，0是GBK，1是UTF8
     * @param sLicenceCode 为空即可
     * @return
     */
    public Boolean NLPIR_Init(String sDataPath, int encoding, String sLicenceCode);

    /**
     * 分词接口，主要调用此接口解析字符串完成分词
     * @param sParagraph 待分词串
     * @param bPOSTagged 是否带词性
     * @return
     */
    public String NLPIR_ParagraphProcess(String sParagraph, int bPOSTagged);

    public int NLPIR_GetParagraphProcessAWordCount(String para);

    public String NLPIR_FinerSegment(String lenWords);

    public int NLPIR_ImportUserDict(String dictFileName, boolean bOverwrite);

    public int NLPIR_ImportKeyBlackList(String sFilename);

    public String NLPIR_GetWordPOS(String sWords);

    public boolean NLPIR_IsWord(String word);

    public String NLPIR_WordFreqStat(String sText);

    public String NLPIR_FileWordFreqStat(String sFilename);

    public String NLPIR_GetEngWordOrign(String sWord);

    public double NLPIR_GetUniProb(String word);

    //对TXT文件内容进行分词
    public double NLPIR_FileProcess(String sSourceFilename,String sResultFilename, int bPOStagged);
    //从字符串中提取关键词
    public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,boolean bWeightOut);
    //从TXT文件中提取关键词
    public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,boolean bWeightOut);
    //添加单条用户词典
    public int NLPIR_AddUserWord(String sWord);
    //删除单条用户词典
    public int NLPIR_DelUsrWord(String sWord);
    //从TXT文件中导入用户词典
    public int NLPIR_ImportUserDict(String sFilename);
    //将用户词典保存至硬盘
    public boolean NLPIR_SaveTheUsrDic();
    //从字符串中获取新词
    public String NLPIR_GetNewWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);
    //从TXT文件中获取新词
    public String NLPIR_GetFileNewWords(String sTextFile,int nMaxKeyLimit, boolean bWeightOut);
    //获取一个字符串的指纹值
    public long NLPIR_FingerPrint(String sLine);
    //设置要使用的POS map
    public int NLPIR_SetPOSmap(int nPOSmap);
    //获取报错日志
    public String NLPIR_GetLastErrorMsg();
    //退出
    public void NLPIR_Exit();

}
