package zut.cs.sys.service;


import com.sun.jna.Library;
import com.sun.jna.Native;
import zut.cs.sys.util.fileutil.OSinfo;

public interface DeepClassifierLibrary extends Library {

    DeepClassifierLibrary Instance =
            (DeepClassifierLibrary) Native.loadLibrary("E:\\java\\workspace\\platform\\win64\\DeepClassifier.dll", DeepClassifierLibrary.class);

    /**
     * @param sDataPath
     * @param encode
     * @param nFeatureCount �����ʣ�Ĭ����800
     * @param sLicenceCode
     * @return
     */
    public boolean DC_Init(String sDataPath, int encode, int nFeatureCount, String sLicenceCode);

    public boolean DC_Exit();

    public boolean DC_AddTrain(String sClassName, String sText);

    public boolean DC_AddTrainFile(String sClassName, String sFilePath);

    public boolean DC_AddTrain(String sClassName, String sText, Long handle);

    public Long DC_NewInstance(Long nFeatureCount);

    public boolean DC_Train(Long handle);

    public boolean DC_Train();

    public boolean DC_LoadTrainResult();

    public boolean DC_LoadTrainResult(Long handle);

    public String DC_Classify(String sText, Long handle);


    public String DC_Classify(String sText);

    public String DC_ClassifyFile(String sFilename);

    public String DC_GetLastErrorMsg();


    public static final int ENCODING_GBK = 0;
    public static final int ENCODING_UTF8 = 1;
}
