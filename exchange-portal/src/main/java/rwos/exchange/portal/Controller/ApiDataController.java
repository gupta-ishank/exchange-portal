package rwos.exchange.portal.Controller;

import java.io.File;


import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rwos.exchange.portal.Entity.AdminData;
import rwos.exchange.portal.Entity.ApiData;
import rwos.exchange.portal.Entity.ApiPath;
import rwos.exchange.portal.Entity.FileData;
import rwos.exchange.portal.Entity.LoginData;
import rwos.exchange.portal.Entity.LoginSignupResponse;
import rwos.exchange.portal.Entity.SwaggerApiData;
import rwos.exchange.portal.Repository.AdminDataRepository;
import rwos.exchange.portal.Repository.ApiDataRepository;
import rwos.exchange.portal.Service.AdminDataService;
import rwos.exchange.portal.Service.ApiDataService;
import rwos.exchange.portal.Service.SwaggerApiDataService;

//import rwos.exchange.portal.Repository.ApiDataRepository;

@RestController
@RequestMapping
@CrossOrigin(origins="*")
public class ApiDataController {
	@Autowired
	ApiDataService apiDataService;
	@Autowired
	AdminDataService adminDataService;
	@Autowired
	ApiDataRepository apiDataRepository;
	@Autowired
	AdminDataRepository adminDataRepository;
	@Autowired
	SwaggerApiDataService swaggerApiDataService;
	
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
	public FileData getFileData(@RequestBody ApiData apiPath) {
		
		String path = apiPath.getRoute();
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
		if(loginDataFromDb == null) return new LoginSignupResponse(false);
		boolean flag = apiDataService.validateLogin(loginDataFromDb, loginData);
		return new LoginSignupResponse(flag);
	}
	
	@PostMapping("/signup")
	public LoginSignupResponse AddUser(@RequestBody LoginData userData) {
		LoginData userDataFromDb = apiDataRepository.isUserPresent(userData.getEmail());
		if(userDataFromDb != null) return new LoginSignupResponse(false);
		String status = apiDataService.addUserData(userData);
		return new LoginSignupResponse(true);
	}
	
	@GetMapping("/admin")
	public List<AdminData> getdata(){
		return adminDataRepository.findAll();
	}
	
	@PostMapping("/admin")
	public String addData(@RequestBody AdminData adminData) {
		return adminDataService.addAdminData(adminData);
	}
	
	@PutMapping("/admin")
	public String updateData(@RequestBody AdminData adminData) {
		return adminDataService.updateAdminData(adminData);
	}
	
	@PostMapping("/apiContent")
	public List<SwaggerApiData> getApicontent(@RequestBody ApiPath apiPath) {
		return swaggerApiDataService.getAllApiContent(apiPath.getPath());
	}
}
