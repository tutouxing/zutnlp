package test;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import zut.cs.sys.service.DeepClassifierLibrary;
import zut.cs.sys.util.fileutil.FileOperateUtils;


public class DeepClassifierTest {
	
	
	/**
	 * ѵ������
	 * @throws IOException 
	 */
	@Test
	public void didTrain() throws IOException {
		boolean flag = DeepClassifierLibrary.Instance.DC_Init("", 1, 800, "");
		if (flag) {
			System.out.println("deepClassifier初始化失败");
		} else {
			System.out.println("deepClassifier初始化失败" + DeepClassifierLibrary.Instance.DC_GetLastErrorMsg());
			System.exit(1);
		}

		ArrayList list = FileOperateUtils.getAllFilesPath(new File("E:\\java\\workspace\\platform\\src\\main\\resources\\训练分类用文本"));
		for (int i = 0; i < list.size(); i++) {
			File f = new File(list.get(i).toString());
			String className = f.getParent();
			className = className
					.substring(className.lastIndexOf("\\") + 1);
			String contentText = FileUtils.readFileToString(f, "utf-8");
			DeepClassifierLibrary.Instance.DC_AddTrain(
					className, contentText);
		}
		DeepClassifierLibrary.Instance.DC_Train();
		DeepClassifierLibrary.Instance.DC_Exit();
	}
	
	/**
	 * �������
	 * @throws IOException
	 */
	@Test
	public void didClassify() throws IOException {
		if (DeepClassifierLibrary.Instance.DC_Init("", 1, 800, "")) {
			System.out.println("deepClassifier训练失败");
		} else {
			System.out.println("deepClassifier初始化失败" + DeepClassifierLibrary.Instance.DC_GetLastErrorMsg());
			System.exit(1);
		}
		DeepClassifierLibrary.Instance.DC_LoadTrainResult();

		String content = FileOperateUtils.getFileContent("test.txt", "utf-8");
		
		//4���������--���������
		System.out.println("错误信息" + DeepClassifierLibrary.Instance.DC_Classify(content));
		
		//5���������--�˳�
		DeepClassifierLibrary.Instance.DC_Exit();
	}

}
