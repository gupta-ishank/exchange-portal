package rwos.exchange.portal.Service;

import java.io.File;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Entity.YamlParser;


@Service
public class MenuService {

    private int id = 100;

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
                        id = 100;
                        val.getResponses().forEach((resKey, resVal) ->{
                        	menu.setSubDescription(resVal.getDescription());
                            resVal.getContent().forEach((contKey, contVal) ->{
                                yamlParser
                                .setResponsePayload(filterRedundedData(contVal.getSchema().getProperties(), contVal.getSchema().getType().toString()));
                                yamlParser
                                .setResponsePayloadDetails(formatTableData(contVal
                                .getSchema().getProperties(), -1));
                            });
                        });
                    }
                    if(!Objects.isNull(val.getParameters())){
                        id = 100;
                        val.getParameters().forEach(parVal ->{
                        	menu.setSubDescription(parVal.getDescription());
                            yamlParser.setParameterPayload(filterRedundedData(parVal
                            .getSchema().getProperties(), parVal.getSchema().getType().toString()));
                            yamlParser
                                .setParameterPayloadDetails(formatTableData(parVal
                                .getSchema().getProperties(), -1));
                            
                        });
                    }
                    if(!Objects.isNull(val.getRequestBody())){
                        id = 100;
                    	menu.setSubDescription(val.getRequestBody().getDescription());
                        val.getRequestBody().getContent().forEach((contentKey, contentVal) ->{                	
                            yamlParser
                            .setRequestPayload(filterRedundedData(contentVal
                            .getSchema().getProperties(), contentVal.getSchema().getType().toString()));
                            yamlParser
                                .setRequestPayloadDetails(formatTableData(contentVal
                                .getSchema().getProperties(), -1));
                        });
                    }
                    menu.setSchema(yamlParser);
                    data.add(menu);
                } catch (Exception e) {
                    System.out.println(method.name() + " " + e.getMessage());
                }   
            });
        });
        return data;
    }
    
    //
    @SuppressWarnings("unchecked")
    public Object filterRedundedData(Object obj, String type) {

    	try {
    		Map<String, Object> map = new HashMap<>();
    		ObjectMapper mapper = new ObjectMapper();
    		Map<String, Object> objMap = mapper.convertValue(obj, Map.class);
            if(type.equals("object")){
                if(!Objects.isNull(objMap)) {
                    objMap.forEach((key, value)->{
                        if(mapper.convertValue(value, Map.class).get("type").equals("object")){
                            map.put(key, filterRedundedData(mapper.convertValue(value, Map.class).get("properties"), "object")) ;
                        }else if(mapper.convertValue(value, Map.class).get("type").equals("array")){
                            map.put(key, filterRedundedData(mapper.convertValue(value, Map.class).get("items"), "array"));
                        }else{
                            map.put(key, mapper.convertValue(value, Map.class).get("type"));
                        }
                    });    		
                }
                return map;   
            }else{
                List<Object> list = new ArrayList<>();
                if(!Objects.isNull(objMap)) {
                    if(objMap.get("type").toString().equals("object")){
                        list.add(filterRedundedData(objMap.get("properties"), "object"));
                    }else{
                        list.add(objMap.get("type"));
                    }		
                }
                return list;
            }
    		
    		 		
    	}
    	catch(Exception e) {
    		System.out.println("Inside redundant "+e.getMessage());
    	}
    	return null;
    }
    @SuppressWarnings("unchecked")
    public List<Object> formatTableData(Object obj, int pId) {

        List<Object> tableData = new ArrayList<>();
    	try {
    		ObjectMapper mapper = new ObjectMapper();
    		Map<String, Object> objMap = mapper.convertValue(obj, Map.class);
            if(!Objects.isNull(objMap)) {
                objMap.forEach((key, value)->{
                    Map<String, Object> table = new HashMap<>();
                    table.put("Level", ++id);
                    table.put("parentId", pId);
                    table.put("parameter", key);
                    if(mapper.convertValue(value, Map.class).get("type").equals("object")){
                        table.put("Type", "Object");
                        tableData.addAll(formatTableData(mapper.convertValue(value, Map.class).get("properties"), id));
                        
                    }else{
                        table.put("Type", mapper.convertValue(value, Map.class).get("type"));
                    }
                    tableData.add(table);
                    
                });    		
            }
	 		
    	}
    	catch(Exception e) {
    		System.out.println("inside "+e.getMessage());
    	}
    	return tableData;
    }
}