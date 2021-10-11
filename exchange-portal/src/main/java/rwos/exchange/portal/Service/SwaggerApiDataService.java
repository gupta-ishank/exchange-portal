package rwos.exchange.portal.Service;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.swagger.parser.util.ParseOptions;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import rwos.exchange.portal.Entity.SwaggerApiData;

@Service
public class SwaggerApiDataService {
//	public List<SwaggerApiData> getAllApiContent(String path){
//		List<SwaggerApiData> data = new ArrayList<>();
//        ObjectMapper oMapper = new ObjectMapper();
//        try {
//            Yaml yaml = new Yaml();
//            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
//            HashMap<String, Object> obj = yaml.load(reader);
//            Map<String, Object> paths = oMapper.convertValue(obj.get("paths"), Map.class);
//            Map<String, Object> pathsc = oMapper.convertValue(obj.get("components"), Map.class);
//            System.out.println(pathsc.get("schemas"));
//            for (Map.Entry<String,Object> pathEntry : paths.entrySet()){
//                Map<String, Object> methods = oMapper.convertValue(pathEntry.getValue(), Map.class);
//                for (Map.Entry<String,Object> methodEntry : methods.entrySet()){
//                    Map<String, Object> methodData = oMapper.convertValue(methodEntry.getValue(), Map.class);
//                    data.add(new SwaggerApiData(pathEntry.getKey(), methodEntry.getKey(), methodData.get("responses"), methodData.get("parameters")));
//                    Object arr = methodData.get("parameters");
////                    System.out.println(arr);
//                    Map<String, Object> arro = oMapper.convertValue(arr, Map.class);
////                    System.out.println(arro);
////                    Map<String, Object> arro = oMapper.convertValue(arr, Map.class);
////                    System.out.println(Object.class.isArray());
//                    
////                    Map<String, Object> methodsDatas = oMapper.convertValue(methodData.get("parameters"), Map.class);
////                    for(Map.Entry<String,Object> methodDataEntry : methodsDatas.entrySet()) {
////                    	System.out.println(methodDataEntry.getKey());
////                    }
//                    
////                    ObjectMapper mapper = new ObjectMapper();
////                    Object javaObject;
////                    System.out.println((JSON.parse(arr)));
////                    Object value = mapper.readValue(arr , javaObject);
////                    System.out.println(javaObject);
////                    JsonGenerator jsonTarget = null;
////					oMapper.writeValue(jsonTarget, arr);
////                    System.out.println(jsonTarget);
//                    
////                    JSONObject arro = (JSONObject)arr;
////                    System.out.println(arro);
////                    System.out.println(methodData.get("parameters").getClass("schema"));
//                }
//            } 
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        
//        System.out.println(data.get(2).getParamaters());
//        Object map = oMapper.convertValue(data.get(2).getParamaters(), Object.class);
//        System.out.println(map);
//        return data;
//	}
	
	
	
	
	
	public List<SwaggerApiData> getAllApiContent(String path){
		ParseOptions parseOptions = new ParseOptions();
		parseOptions.setResolve(true); 
		final OpenAPI openAPI = new OpenAPIV3Parser().read(path);
		System.out.println(openAPI);
		return null;
	}
//	
//	private static String convertYamlToJson(String yaml) {
//        try {
//            ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
//            Object obj = yamlReader.readValue(yaml, Object.class);
//            ObjectMapper jsonWriter = new ObjectMapper();
//            return jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
//        } catch (JsonProcessingException ex) {
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
}
