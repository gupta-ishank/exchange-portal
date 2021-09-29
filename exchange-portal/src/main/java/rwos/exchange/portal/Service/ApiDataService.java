package rwos.exchange.portal.Service;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import rwos.exchange.portal.Entity.ApiData;
import rwos.exchange.portal.Entity.ApiPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import javax.activation.MimetypesFileTypeMap;

@Service
public class ApiDataService {
	public List<ApiData> getAllApiData(File file) {
	
		System.out.println( file.getPath() );
		List<ApiData> dataList = new ArrayList<>();
		for(File f : file.listFiles()) {
			if(!f.isHidden()) {
				ApiData data = new ApiData();
				data.setName(f.getName());
//				data.addRoute(file.getPath());
//				data.addRoute(f.getAbsolutePath());
				data.setRoute(f.getAbsolutePath());
				System.out.println(f.getAbsolutePath());
				data.setType(1);
				helper(f, data);
				dataList.add(data);
			}
		}
		return dataList;
	}
	
	private static String getFileExtension(String fullName) {
	    String fileName = new File(fullName).getName();
	    int dotIndex = fileName.lastIndexOf('.');
	    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}
	
	private void helper(File file, ApiData pData) {
		
		for(File f : file.listFiles()) {
			if(!f.isHidden()) {
				ApiData data = new ApiData();
				data.setName(f.getName());
				data.setRoute(f.getAbsolutePath());
				System.out.println(f.getAbsolutePath());
				if(f.isDirectory()) {
					data.setType(1);
					helper(f, data);
				}
				else {
					System.out.println( getFileExtension(f.getName()) );
					if(getFileExtension(f.getName()).equals("json")) {
						data.setType(2);
					}else if(getFileExtension(f.getName()).equals("yaml") || getFileExtension(f.getName()).equals("yml")){
						data.setType(3);
					}
				}
				pData.addChildren(data);
			}
//			else {
//				if(f.isFile()) {
//					pData.addFiles(f.getName());
//					System.out.println(f.getName());
//				}
//			}
		}
//		return data;
	}
	
	
	public Object getJsonFileContent(String path) {
		JSONParser parser = new JSONParser();
		Object data = new Object();
		System.out.println(path);
		
		try {
			Object obj = parser.parse(new FileReader(path));
			data = obj;
		}catch(FileNotFoundException fe) {
			fe.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public Object getYamlFileContent(String path) {
		Object data = new Object();
		System.out.println();
		try {
			InputStream inputStream = new FileInputStream(new File("D:\\\\New folder\\\\folder2\\\\TextDocument.yml"));
			Yaml yaml = new Yaml();
			Object obj = yaml.load(inputStream);
			data = obj;
			System.out.println(data);
		}catch(FileNotFoundException fe) {
			fe.printStackTrace();
		}
		return data;
	}
	
	public String getFileContent(String path) {
		File file = new File(path);
		String data = "";
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			data = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
}
