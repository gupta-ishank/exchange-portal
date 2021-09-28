package rwos.exchange.portal.Service;

import org.springframework.stereotype.Service;

import rwos.exchange.portal.Entity.ApiData;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

@Service
public class ApiDataService {
//	final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
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
				else
					

				System.out.println( getFileExtension(f.getName()) );
				if(getFileExtension(f.getName()).equals("json")) {
					data.setType(2);
				}else if(getFileExtension(f.getName()).equals("yaml")){
					data.setType(3);
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
	
	
}
