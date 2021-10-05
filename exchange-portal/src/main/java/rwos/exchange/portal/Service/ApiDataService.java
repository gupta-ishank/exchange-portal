package rwos.exchange.portal.Service;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import rwos.exchange.portal.Entity.ApiData;
import rwos.exchange.portal.Entity.ApiPath;
import rwos.exchange.portal.Entity.LoginData;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import javax.activation.MimetypesFileTypeMap;

@Service
public class ApiDataService {
	@Autowired
	ApiDataRepository apiDataRepository;

	//Filtering all the unnecessary folders/files
	public List<ApiData> getFilteredFolderStructure(File file, HashSet<String> accessibleFilePaths){
		List<ApiData> folderStructure = new ArrayList<>(); // to hold final folder structure

		for(File eachFile: file.listFiles()) {
			ApiData attach = new ApiData(); // hold structure of child;
			attach.setType(1);
			attach.setName(eachFile.getName());
			attach.setRoute(eachFile.getAbsolutePath());
			Long countFiles = addSubFiles(eachFile, attach, accessibleFilePaths); // setting count of valid files in subDirectory
			if(countFiles > 0) // If it is present then add else not
				folderStructure.add(attach); // to create sub directory
		}
		return folderStructure;
	}
	Long addSubFiles(File file, ApiData parentDirectory, HashSet<String> accessibleFilePaths) {
		Long countFiles = 0L;
		Long currentDirCount = 0L;
		for(File eachFile : file.listFiles()) {

			if(!eachFile.isHidden() && accessibleFilePaths.contains(eachFile.getAbsolutePath())) {
				ApiData attach = new ApiData();
				attach.setName(eachFile.getName());
				attach.setRoute(eachFile.getAbsolutePath());
				System.out.println(eachFile.getAbsolutePath());
				boolean validFile = false; // to make sure only .json, .yaml, .yml is considered
				if(eachFile.isDirectory()) {
					attach.setType(1);
					currentDirCount = addSubFiles(eachFile, attach, accessibleFilePaths); // add count of all the valid files in the subDirectory
					if(currentDirCount > 0) // check if file extension is valid
						parentDirectory.addChildren(attach);
				}
				else {
					
					int type = valueByType(eachFile.getName());
					if(type > 0) {
						attach.setType(type);
						countFiles++; // increment if the current directory is valid file.
						validFile = true;
					}
					
					if(countFiles > 0 && validFile == true) // check if file extension is valid
						parentDirectory.addChildren(attach);
				}
			}
		}
		return countFiles; // return count of countfiles;
	}
	private static int valueByType(String fileName) {
		if(getFileExtension(fileName).equals("json")) {
			return 2;
		}else if(getFileExtension(fileName).equals("yaml") || getFileExtension(fileName).equals("yml")){
			return 3;
		}
		else {
			return 0;
		}
	}

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


}