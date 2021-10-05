package rwos.exchange.portal.Service;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.fasterxml.jackson.databind.ObjectMapper;

import rwos.exchange.portal.Entity.AdminData;
import rwos.exchange.portal.Entity.ApiData;
import rwos.exchange.portal.Entity.ApiPath;
import rwos.exchange.portal.Entity.LoginData;
import rwos.exchange.portal.Repository.AdminDataRepository;
import rwos.exchange.portal.Repository.ApiDataRepository;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import javax.activation.MimetypesFileTypeMap;

@Service
public class ApiDataService {
	@Autowired
	ApiDataRepository apiDataRepository;
	
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
//					ApiData d = new ApiData("POST", "/get", 4);
//					data.addChildren(d);
					List<ApiData> apiList = getAllApis(f.getAbsolutePath());
					data.setChilds(apiList);
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
	
	public boolean validateLogin(LoginData loginDataFromDb, LoginData loginData) {
		if(!loginDataFromDb.getUsername().equals(loginData.getUsername())) {
			return false;
		}else if(!loginDataFromDb.getPassword().equals(loginData.getPassword())){
			return false;
		}
		return true;
	}
	
	public String addUserData(LoginData userData) {
		apiDataRepository.save(userData);
		return "Successfully added user";
	}
	
	
	public List<ApiData> getAllApis(String path){

        List<ApiData> data = new ArrayList<>();
        ObjectMapper oMapper = new ObjectMapper();
        try {
            Yaml yaml = new Yaml();
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            LinkedHashMap<String, Object> obj = yaml.load(reader);
            Map<String, Object> paths = oMapper.convertValue(obj.get("paths"), Map.class);
            for (Map.Entry<String,Object> pathEntry : paths.entrySet()){
                Map<String, Object> methods = oMapper.convertValue(pathEntry.getValue(), Map.class);
                for (Map.Entry<String,Object> methodEntry : methods.entrySet()){
                    Map<String, Object> methodData = oMapper.convertValue(methodEntry.getValue(), Map.class);
                    data.add(new ApiData(methodEntry.getKey(),pathEntry.getKey(), methodData.get("summary").toString())); 
                }
            } 
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return data;
    }
	
	
}
