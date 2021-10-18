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

import org.springframework.stereotype.Service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Entity.PappuPassHogya;
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

    public List<Object> isNull(List<Object> data){
        if(Objects.isNull(data)) return new ArrayList<>();
        else return data;
    }
    public String isNull(String data){
        if(Objects.isNull(data)) return "";
        else return data;
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
    @SuppressWarnings("unchecked")
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
                    menu.setSubDescription(val.getDescription());
                    YamlParser yamlParser = new YamlParser();
                    if(!Objects.isNull(val.getResponses())){
                        id = 100;
                        val.getResponses().forEach((resKey, resVal) ->{
                            Map<String, Object> res = new HashMap<>();
                            PappuPassHogya response = new PappuPassHogya();
                            if(resKey.equalsIgnoreCase("200")){
                                if(resVal.getContent() != null){
                                    resVal.getContent().forEach((contKey, contVal) ->{
                                        res.put(resKey, filterRedundedData(contVal.getSchema()
                                        .getProperties(), isNull(contVal
                                        .getSchema().getType())));
                                        response.setSuccess(res);
                                        response.setSuccessDetails(formatTableData2(contVal.getSchema().getProperties(), -1, 
                                        isNull(contVal.getSchema().getRequired()),
                                        isNull(contVal.getSchema().getType())));
                                    });                                
                                } 
                            }else{
                                if(resVal.getContent() != null){
                                    resVal.getContent().forEach((contKey, contVal) ->{
                                        res.put(resKey, filterRedundedData(contVal.getSchema()
                                        .getProperties(), isNull(contVal
                                        .getSchema().getType())));
                                        response.setFailure(res);
                                        response.setFailureDetails(formatTableData2(contVal.getSchema().getProperties(), -1, 
                                        isNull(contVal.getSchema().getRequired()),
                                        isNull(contVal.getSchema().getType())));
                                    });                                  
                                }
                            }                    
                            yamlParser.setResponsePayload(response);
                        });

                    }
                    if(!Objects.isNull(val.getParameters())){
                        id = 100;
                        List<Object> parameterDetails = new ArrayList<>();
                        Map<String, String> parameter = new HashMap<>();
                        val.getParameters().forEach(parVal ->{
                            Map<String, Object> parameterMap = new HashMap<>();
                            parameter.put(parVal.getName(), parVal.getSchema().getType());
                            parameterMap.put("Level", ++id);
                            parameterMap.put("description", parVal.getDescription() == null ? "-" : parVal.getDescription());
                            parameterMap.put("parentId", -1);
                            parameterMap.put("Mendate", parVal.getRequired());
                            parameterMap.put("parameter", parVal.getName());
                            parameterDetails.add(parameterMap); 
                        });
                        yamlParser.setParameterPayloadDetails(parameterDetails);
                        yamlParser.setParameterPayload(parameter);
                    }
                    if(!Objects.isNull(val.getRequestBody())){
                        id = 100;
                        MediaType contentVal = val.getRequestBody()
                        .getContent().get("application/json");
                        yamlParser
                            .setRequestPayloadExample(contentVal.getSchema().getExample());
                        yamlParser
                            .setRequestPayload(filterRedundedData(contentVal
                            .getSchema(), isNull(contentVal
                            .getSchema().getType())));
                        yamlParser
                            .setRequestPayloadDetails(formatTableData2(contentVal
                            .getSchema().getProperties(), -1, isNull(contentVal.getSchema().getRequired()),
                            isNull(contentVal.getSchema().getType())));
                    }
                    if(!Objects.isNull(val.getSecurity())){
                        id = 100;
//                        MediaType contentVal = val.getSecurity()
//                        .getContent().get("application/json");
                        yamlParser
                            .setSecurity(val.getSecurity());
                    }
                    menu.setSchema(yamlParser);
                    data.add(menu);
                } catch (Exception e) {
                    System.out.println("excemtion in" + method.name());
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
                            // if(!Objects.isNull(mapper.convertValue(value, Map.class).get("items")))
                                map.put(key, filterRedundedData(mapper.convertValue(value, Map.class).get("items"), "array"));
                        } else{
                            map.put(key, mapper.convertValue(value, Map.class).get("type"));
                        }
                    });    		
                }
                return map;   
            }else if(type.equals("array")){
                List<Object> list = new ArrayList<>();
                if(!Objects.isNull(objMap)) {
                    if(objMap.get("type").equals("object") && !Objects.isNull(objMap.get("properties"))){
                        list.add(filterRedundedData(objMap.get("properties"), "object"));
                    }else if(objMap.get("type").equals("array")){
                        list.add(filterRedundedData(objMap.get("items"), "array"));
                    }else{
                        list.add(objMap.get("type"));
                    }		
                }
                return list;
            }
    		
    		 		
    	}
    	catch(Exception e) {
    		
    	}
    	return null;
    }


    @SuppressWarnings("unchecked")
    public List<Object> formatTableData2(Object obj, int pId, List<Object> requiredFileds, String type) {

        List<Object> tableData = new ArrayList<>();
    	try {
    		ObjectMapper mapper = new ObjectMapper();
    		Map<String, Object> objMap = mapper.convertValue(obj, Map.class);
            if(type.equals("object")){
                if(!Objects.isNull(objMap)) {
                    objMap.forEach((key, value)->{

                        Map<String, Object> table = new HashMap<>();
                        table.put("Level", ++id);
                        table.put("parentId", pId);
                        table.put("parameter", key);
                        table.put("Mendate", requiredFileds.contains(key));
                        table.put("Description", Objects.isNull(mapper.convertValue(value, Map.class)
                        .get("description")) ? "-" : mapper.convertValue(value, Map.class)
                        .get("description"));

                        if(mapper.convertValue(value, Map.class).get("type").equals("object")){
                            tableData.addAll(formatTableData2(mapper.convertValue(value, Map.class).get("properties"), id, requiredFileds, "object")); 
                        }else if(mapper.convertValue(value, Map.class).get("type").equals("array")){
                            tableData.addAll(formatTableData2(mapper.convertValue(value, Map.class).get("items"), id, requiredFileds, "array")); 
                        } else{
                            table.put("Type", mapper.convertValue(value, Map.class).get("type"));
                        }
                        tableData.add(table);
                    });    		
                }
                  
            }else if(type.equals("array")){
                if(!Objects.isNull(objMap)) {  
                    if(objMap.get("type").equals("object") && !Objects.isNull(objMap.get("properties"))){
                        tableData.addAll( formatTableData2(objMap.get("properties"),id, requiredFileds, "object"));
                    }else if(objMap.get("type").equals("array")){
                        tableData.addAll( formatTableData2(objMap.get("items"),id, requiredFileds, "array"));
                    }		
                }
            }
    		
    		 		
    	}
    	catch(Exception e) {
    		
    	}
    	return tableData; 
    }


    public Object testing(String path) {

        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        parseOptions.setResolveFully(true);
        OpenAPI store = new OpenAPIV3Parser().read(path, null, parseOptions);

        // return nullFieldFilter( store.getPaths().get("/mplace/selleritems").getPost().getResponses().get("200").getContent().get("*/*").getSchema());
        // return nullFieldFilter( store.getPaths().get("/mplace/selleritems").getPost().getRequestBody().getContent().get("application/json").getSchema()) ;
         return formatTableData2( nullFieldFilter( store.getPaths().get("/mplace/selleritems").getPost().getSecurity()),id, new ArrayList<>(), "array") ;
    }

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
}