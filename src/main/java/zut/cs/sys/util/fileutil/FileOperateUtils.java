package zut.cs.sys.util.fileutil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileOperateUtils {
	
	/**
	 * @description���õ�Ŀ¼�µ������ļ���·��
	 * @param dir
	 *            :Ҫ�������ļ��е�·��
	 * @return���ļ����������ļ��ľ���·������
	 * @throws Exception
	 * @author tanshuguo
	 */
	// 
	static ArrayList allFilesPath = new ArrayList();

	public static ArrayList getAllFilesPath(File dir) {

		if (!dir.isDirectory()) {
			String filePath = dir.getAbsolutePath();
//			System.out.println(filePath);
			allFilesPath.add(filePath);
		} else {
			File[] fs = dir.listFiles();
			for (int i = 0; i < fs.length; i++) {

				if (fs[i].isDirectory()) {
					try {
						getAllFilesPath(fs[i]);
					} catch (Exception e) {
					}
				} else {
					String filePath = fs[i].getAbsolutePath();
//					System.out.println(filePath);
					allFilesPath.add(filePath);
				}
			}
		}
		System.out.println("Utils.getAllFilesPath-�ļ�����---->" + allFilesPath.size());
		return allFilesPath;
	}

	/**
	 * @description���õ��ļ�����
	 * @param filePath
	 *            :Ҫ��ȡ���ļ�·��
	 * @return �����ļ�����
	 * @author tanshuguo
	 */
	public static String getFileContent(String filePath) {
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = null;
		BufferedReader bufferedReader = null;
		// String fileContent="";
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // �ж��ļ��Ƿ����
				isr = new InputStreamReader(new FileInputStream(file), encoding);// ���ǵ������ʽ
				bufferedReader = new BufferedReader(isr);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					//System.out.println(lineTxt);
					sb.append(lineTxt);
				}

				isr.close();
			} else {
				System.out.println("�Ҳ���ָ�����ļ�");
			}
		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
		} finally {
			try {
				if (isr != null) {
					isr.close();
					isr = null;
				}
				if (bufferedReader != null) {
					bufferedReader.close();
					bufferedReader = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//System.out.println("--->" + sb.toString());
		// System.out.println("---->"+);
		return sb.toString();
	}
	/**
	 * @description���õ��ļ�����
	 * @param filePath
	 *            :Ҫ��ȡ���ļ�·��
	 * @return �����ļ�����
	 * @author tanshuguo
	 */
	public static String getFileContent(String filePath,String encode) {
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = null;
		BufferedReader bufferedReader = null;
		// String fileContent="";
		try {
			String encoding = encode;
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // �ж��ļ��Ƿ����
				isr = new InputStreamReader(new FileInputStream(file), encoding);// ���ǵ������ʽ
				bufferedReader = new BufferedReader(isr);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					//System.out.println(lineTxt);
					sb.append(lineTxt);
				}

				isr.close();
			} else {
				System.out.println("�Ҳ���ָ�����ļ�");
			}
		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
		} finally {
			try {
				if (isr != null) {
					isr.close();
					isr = null;
				}
				if (bufferedReader != null) {
					bufferedReader.close();
					bufferedReader = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//System.out.println("--->" + sb.toString());
		// System.out.println("---->"+);
		return sb.toString();
	}
	/**
	 * ����ļ����ֽ�����
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] getByteFromFile(String filename) throws IOException{  
        
        File f = new File(filename);  
        if(!f.exists()){  
            throw new FileNotFoundException(filename);  
        }  
  
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());  
        BufferedInputStream in = null;  
        try{  
            in = new BufferedInputStream(new FileInputStream(f));  
            int buf_size = 1024;  
            byte[] buffer = new byte[buf_size];  
            int len = 0;  
            while(-1 != (len = in.read(buffer,0,buf_size))){  
                bos.write(buffer,0,len);  
            }  
            return bos.toByteArray();  
        }catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        }finally{  
            try{  
                in.close();  
            }catch (IOException e) {  
                e.printStackTrace();  
            }  
            bos.close();  
        }  
    }  
	/**
	 * @decription:��dataд��targetFilePath��
	 * @param data
	 *            ��Ҫд������ݣ����ñ���Ϊ��utf-8
	 * @param targetFilePath
	 *            ��Ҫд�뵽���ļ�·��
	 * @author tanshuguo
	 */
	public static void writeFile(String data, String targetFilePath) {
		OutputStreamWriter osw = null;
		BufferedWriter output = null;
		FileOutputStream fos=null;
		String encoding = "utf-8";
//		String encoding = "gbk";
//		String encoding = "gb2312";

		try {
			File file = new File(targetFilePath);
			if (file.exists()) {
				System.out.println("Utils.writeFile--�ļ����ڣ�׷������");
                fos=new FileOutputStream(file, true);
				osw = new OutputStreamWriter(fos,
						encoding);// ���ǵ������ʽ
				output = new BufferedWriter(osw);
				output.write(data + "\r\n");
			} else {
				System.out.println("Utils.writeFile--�ļ�������--�Ѵ���");
				File parentOfFile = file.getParentFile();
				if (!parentOfFile.exists()) {
					parentOfFile.mkdirs();
					System.out.println("Utils--writeFile--�洢�ļ���·��-->" + parentOfFile.getPath());

				}
				// file.mkdirs();
				file.createNewFile();// �������򴴽�
				fos=new FileOutputStream(file, true);
				osw = new OutputStreamWriter(fos,
						encoding);// ���ǵ������ʽ
				output = new BufferedWriter(osw);
				output.write(data + "\r\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
		
				if (output != null) {
					output.close();
					output = null;
				}
				if (osw != null) {
					osw.close();
					osw = null;
				}
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @decription:��dataд��targetFilePath��
	 * @param data
	 *            ��Ҫд������ݣ����ñ���Ϊ��utf-8
	 * @param targetFilePath
	 *            ��Ҫд�뵽���ļ�·��
	 * @param encoding:д�ļ�ʱҪ���õı����ʽ
	 */
	public static void writeFile(String data, String targetFilePath,String encoding) {
		OutputStreamWriter osw = null;
		BufferedWriter output = null;
		FileOutputStream fos=null;
		//String encoding = "utf-8";
//		String encoding = "gbk";
//		String encoding = "gb2312";

		try {
			File file = new File(targetFilePath);
			if (file.exists()) {
				System.out.println("Utils.writeFile--�ļ����ڣ�׷������");
                fos=new FileOutputStream(file, true);
				osw = new OutputStreamWriter(fos,
						encoding);// ���ǵ������ʽ
				output = new BufferedWriter(osw);
				output.write(data + "\r\n");
			} else {
				System.out.println("Utils.writeFile--�ļ�������--�Ѵ���");
				File parentOfFile = file.getParentFile();
				if (!parentOfFile.exists()) {
					parentOfFile.mkdirs();
					System.out.println("Utils--writeFile--�洢�ļ���·��-->" + parentOfFile.getPath());

				}
				// file.mkdirs();
				file.createNewFile();// �������򴴽�
				fos=new FileOutputStream(file, true);
				osw = new OutputStreamWriter(fos,
						encoding);// ���ǵ������ʽ
				output = new BufferedWriter(osw);
				output.write(data + "\r\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
		
				if (output != null) {
					output.close();
					output = null;
				}
				if (osw != null) {
					osw.close();
					osw = null;
				}
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @decription:��dataд��targetFilePath��
	 * @param data
	 *            ��Ҫд�������
	 * @param targetFilePath
	 *            ��Ҫд�뵽���ļ�·��
	 * @param encoding:д�ļ�ʱҪ���õı����ʽ
	 */
	public static void writeFileNotAppend(String data, String targetFilePath,String encoding) {
		OutputStreamWriter osw = null;
		BufferedWriter output = null;
		FileOutputStream fos=null;

		try {
			File file = new File(targetFilePath);
			if (file.exists()) {
				System.out.println("--writeFileNotAppend--->�ļ��Ѵ���");
				file.delete();
				System.out.println("--writeFileNotAppend--->�ļ���ɾ��");
			}
			
			System.out.println("--writeFileNotAppend--->�ļ��Ѵ���");
				File parentOfFile = file.getParentFile();
				if (!parentOfFile.exists()) {
					parentOfFile.mkdirs();
					System.out.println("--writeFileNotAppend--->�洢�ļ���·��-->" + parentOfFile.getPath());

				}
				// file.mkdirs();
				file.createNewFile();// �������򴴽�
				fos=new FileOutputStream(file, true);
				osw = new OutputStreamWriter(fos,
						encoding);// ���ǵ������ʽ
				output = new BufferedWriter(osw);
				output.write(data + "\r\n");
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
		
				if (output != null) {
					output.close();
					output = null;
				}
				if (osw != null) {
					osw.close();
					osw = null;
				}
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @param zhStr ��Ҫ�õ�unicode������ַ���
	 * @return  unicode������ַ���
	 */
	public static String getUnicodeFromStr(String zhStr){ 
		StringBuffer unicode = new StringBuffer(); 
		for(int i=0; i<zhStr.length();i++){ 
		char c = zhStr.charAt(i); 
		unicode.append("\\u" + Integer.toHexString(c)); 
		} 
		return unicode.toString(); 
		} 
	/**
	 * 
	 * @param unicode  �ַ���unicode���룺���籱����ӭ���unicode����Ϊ��\u5317\u4eac\u6b22\u8fce\u4f60
	 * @return �����ַ������籱����ӭ��
	 */
	public static String getStrFromUnicode(String unicode){ 
		StringBuffer sb = new StringBuffer(); 
		String[] hex = unicode.split("\\\\u");  // \����"\\u"������ "\\\\u" 
		for(int i=1;i<hex.length;i++){          // ע��Ҫ�� 1 ��ʼ�������Ǵ�0��ʼ����һ���ǿա� 
		int data = Integer.parseInt(hex[i],16);  //  ��16������ת��Ϊ 10���Ƶ����ݡ� 
		sb.append((char)data);  //  ǿ��ת��Ϊchar���;������ǵ������ַ��ˡ� 
		} 
		//System.out.println("���Ǵ� Unicode���� ת��Ϊ �����ַ���: "  +sb.toString()); 
		return sb.toString(); 
		} 
	
	public static void main(String[] args) throws Exception {
         getUnicodeFromStr("�й�");
	}

}