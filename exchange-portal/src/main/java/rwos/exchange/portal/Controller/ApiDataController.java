package rwos.exchange.portal.Controller;

import java.io.File;


import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rwos.exchange.portal.Entity.ApiData;
import rwos.exchange.portal.Entity.ApiPath;
import rwos.exchange.portal.Entity.FileData;
import rwos.exchange.portal.Entity.LoginData;
import rwos.exchange.portal.Entity.LoginSignupResponse;
import rwos.exchange.portal.Repository.ApiDataRepository;
import rwos.exchange.portal.Service.ApiDataService;

//import rwos.exchange.portal.Repository.ApiDataRepository;

@RestController
@RequestMapping
public class ApiDataController {
	@Autowired
	ApiDataService apiDataService;
	@Autowired
	ApiDataRepository apiDataRepository;
	
	@Value("${mypath}")
	private String mypath;
	
	@GetMapping("")
	public List<ApiData> getAllApiData(){
		File file = new File(mypath);
//		System.out.println(file.listFiles());
//		File[] files = file.listFiles();
//		System.out.println(file.getName());
//		for(File f : files) {
//			if(f.isDirectory())
//				System.out.println(f.getName());
//		}
		System.out.println(mypath);
		
		return apiDataService.getAllApiData(file);
	}
	
	@PostMapping("")
	public FileData getFileData(@RequestBody ApiPath apiPath) {
		
		String path = apiPath.getPath();
//		if(apidata.getType() == 2) {
//			return apiDataService.getJsonFileContent(path);			
//		}
//		else {
//			return apiDataService.getYamlFileContent(path);			
//		}
//			if(apidata.getType() == 3) {
//			return apiDataService.getYamlFileContent(path);			
//		}
//		return apiDataService.getJsonFileContent(path);
//		return apiDataService.getYamlFileContent(path);
		
		String str = apiDataService.getFileContent(path);
		return new FileData(str);
	}
	
	@PostMapping("/login")
	public LoginSignupResponse checkLoginDetails(@RequestBody LoginData loginData) {
		LoginData loginDataFromDb = apiDataRepository.getData(loginData.getUsername());
		if(loginDataFromDb == null) return new LoginSignupResponse("Incorrect credentials");
		boolean flag = apiDataService.validateLogin(loginDataFromDb, loginData);
		String str="";
		if(!flag) {
			str = "Incorrect credentials";
		}else {
			str = "correct credentials";
		}
		return new LoginSignupResponse(str);
	}
	
	@PostMapping("/signup")
	public LoginSignupResponse AddUser(@RequestBody LoginData userData) {
		LoginData userDataFromDb = apiDataRepository.isUserPresent(userData.getEmail());
		if(userDataFromDb != null) return new LoginSignupResponse("User already exists");
		String status = apiDataService.addUserData(userData);
		return new LoginSignupResponse(status);
	}
}
