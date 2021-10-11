package rwos.exchange.portal.Service;

import java.io.File;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Entity.YamlParser;


@Service
public class MenuService {

    private FileFilter filter = (file) -> file.isDirectory() || !file.isHidden()
                                    || file.getName().endsWith(".json")
                                    || file.getName().endsWith(".yaml");

    public int getType(File file){
        if(file.isDirectory()) return 1;
        else if(file.getName().endsWith(".json")) return 2;
        else return 3;
    }

    // returns folder structure along with file method with schemas (excluding the empty folders) and 
    public List<Menu> getAllMenu(File folder){
        List<Menu> menus = new ArrayList<>();
        if(folder.isDirectory()){
            for(File file: folder.listFiles(filter)){
                List<Menu> subDirectory = getAllMenu(file);
                if(subDirectory.isEmpty() == false)
                    menus.add(new Menu(file.getName(),file.getAbsolutePath(),getType(file), subDirectory));    
            }
        }else{
            menus.addAll(getAllApis(folder.getAbsolutePath()));
        }
        return menus;
    }

    //return file methods with their schemas in a particular JSON or YAML file
    public List<Menu> getAllApis(String path){ //parameter: file path

        List<Menu> data = new ArrayList<>();

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);
        OpenAPI store = new OpenAPIV3Parser().read(path, null, parseOptions);

        store.getPaths().forEach((api, value) ->{
            value.readOperationsMap().forEach((method, val) ->{
                
                
                try {
                    Menu menu = new Menu(method.name(), api, val.getSummary());
                    YamlParser yamlParser = new YamlParser();
                    if(!Objects.isNull(val.getResponses())){
                        val.getResponses().forEach((resKey, resVal) ->{
                        	menu.setSubDescription(resVal.getDescription());
                            resVal.getContent().forEach((contKey, contVal) ->{
                                yamlParser.setResponsePayloadDetails(filterRedundedData(nullFieldFilter(contVal.getSchema().getProperties())));
                            });
                        });
                    }
                    if(!Objects.isNull(val.getParameters())){
                        val.getParameters().forEach(parVal ->{
                        	menu.setSubDescription(parVal.getDescription());
                            yamlParser.setParameterPayloadDetails(filterRedundedData(nullFieldFilter(parVal.getSchema().getProperties())));
                            
                        });
                    }
                    if(!Objects.isNull(val.getRequestBody())){
                    	menu.setSubDescription(val.getRequestBody().getDescription());
                        val.getRequestBody().getContent().forEach((contentKey, contentVal) ->{                	
                            yamlParser
                            .setRequestPayloadDetails(filterRedundedData(nullFieldFilter(contentVal.getSchema().getProperties())));
                        });
                    }
                    menu.setSchema(yamlParser);
                    data.add(menu);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }   
            });
        });
        return data;
    }
    
    public Object getData(String path) {
    	try {
    		ParseOptions parseOptions = new ParseOptions();
    		parseOptions.setResolve(true);
    		parseOptions.setResolveFully(true);
    		OpenAPI store = new OpenAPIV3Parser().read(path, null, parseOptions);
//    		JSONObject data = new JSONObject();
    		
    		return store.getPaths().get("/store/order").getPost().getRequestBody().getContent().get("*/*").getSchema().getProperties();
//    		return store;
    	}
    	catch(Exception e) {
    		System.out.println(e);
    	}
    	return null;
    }

    //filters the null fields from the schema
    public Object nullFieldFilter(Object schema){
        try {
            ObjectMapper mapper = new ObjectMapper();  
            mapper.setSerializationInclusion(Include.NON_NULL); 
            String filteredSchema = mapper.writeValueAsString(schema);
            return new ObjectMapper().readTree(filteredSchema);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    
    //
    @SuppressWarnings("unchecked")
    public Object filterRedundedData(Object obj) {
//    	JSONObject jsonObj = new JSONObject();
    	try {
    		Map<String, Object> map = new HashMap<>();
    		
    		ObjectMapper mapper = new ObjectMapper();
    		Map<String, Object> objMap = mapper.convertValue(obj, Map.class);
    		if(!Objects.isNull(objMap)) {
    			objMap.forEach((key, value)->{
    				map.put(key, mapper.convertValue(value, Map.class).get("type")) ;
    			});    		
    		}
    		
    		return map;    		
    	}
    	catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    	return null;
    }
}
