package rwos.exchange.portal.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import rwos.exchange.portal.Entity.Menu;
import io.swagger.v3.oas.models.OpenAPI;


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

    public List<Menu> getAllMenu(File folder){
        List<Menu> menus = new ArrayList<>();
        if(folder.isDirectory()){
            for(File file: folder.listFiles(filter)){
                menus.add(new Menu(file.getName(),file.getAbsolutePath(),getType(file), getAllMenu(file)));    
            }
        }else{
            menus.addAll(getAllApis(folder.getAbsolutePath()));
        }
        return menus;
    }


    @SuppressWarnings("unchecked")
    public List<Menu> getAllApis(String path){ // gets all content in that particular JSON or YAML file

        List<Menu> data = new ArrayList<>();
        ObjectMapper oMapper = new ObjectMapper();
        Menu menu = new Menu();
        try {
            
            Yaml yaml = new Yaml();
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            LinkedHashMap<String, Object> customer = yaml.load(reader);
            reader.close();

            ParseOptions parseOptions = new ParseOptions();
            parseOptions.setResolve(true);
            parseOptions.setResolveFully(true);
            OpenAPI store = new OpenAPIV3Parser().read(path, null, parseOptions);

            int it = 0;
            Map<String, Object> paths = oMapper.convertValue(customer.get("paths"), Map.class);
            
            for (Map.Entry<String,Object> pathEntry : paths.entrySet()){
        
                Map<String, Object> methods = oMapper.convertValue(pathEntry.getValue(), Map.class);

                for (Map.Entry<String,Object> methodEntry : methods.entrySet()){ // methods

                    menu.setName(methodEntry.getKey());
                    menu.setPath(pathEntry.getKey());
                    String desc = "";
                    Object data2 = null;
                    if(methodEntry.getKey().equals("get")){
                        desc = store.getPaths().get(pathEntry.getKey()).getGet().getSummary();
                        data2 = nullFieldFilter(store.getPaths().get(pathEntry.getKey())
                        .getGet().getParameters()
                        .get(0).getSchema());

                    }
                    if(methodEntry.getKey().equals("post")){
                        desc = store.getPaths().get(pathEntry.getKey()).getPost().getSummary();
                        data2 = nullFieldFilter(store.getPaths().get(pathEntry.getKey())
                        .getPost().getRequestBody()
                        .getContent().get("application/json").getSchema());
                    }
                    if(methodEntry.getKey().equals("put")){
                        desc = store.getPaths().get(pathEntry.getKey()).getPut().getSummary();
                        data2 = nullFieldFilter(store.getPaths().get(pathEntry.getKey())
                        .getPut().getRequestBody()
                        .getContent().get("application/json").getSchema());
                    }
                    if(methodEntry.getKey().equals("delete")){
                        desc = store.getPaths().get(pathEntry.getKey()).getDelete().getSummary();
                        data2 = nullFieldFilter(store.getPaths().get(pathEntry.getKey())
                        .getDelete().getParameters().get(0).getSchema());
                    }

                    menu.setDescription(desc);
                    menu.setResponseData(data2);
                    // System.out.println(menu.getResponseData());
                    
                }
                
                data.add(menu);
            }            
            return data;
            
        } catch (Exception e) {
            System.err.println(menu.getPath()+ " " + menu.getName());
        } 
        return data;
    }

    public Object getFileContent(String path) { // Returns file contents as json object
        /* File file = new File(path);
        String data = "";
        if(!file.isDirectory() && !file.isHidden() && file.exists()){
            try {
                Yaml yaml = new Yaml();
                BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
                LinkedHashMap<String, Object> customer = yaml.load(reader);
                reader.close();
                return customer;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } */

        try {
            ParseOptions parseOptions = new ParseOptions();
            parseOptions.setResolve(true);
            parseOptions.setResolveFully(true);
            OpenAPI store = new OpenAPIV3Parser().read(path, null, parseOptions);
            // return store.getPaths().get("/pet/findByStatus").getGet().getRequestBody().getContent().get("application/json").getSchema();
            return store.getPaths().get("/pet/findByStatus").getGet().getParameters();
        } catch (Exception e) {
            
        }
        
        return null;
    }

    //Delete and Get - getParameters;
    //Put - getRequestBody && getParameters;
    //post : when {params} passed add getParameters too

    public Object nullFieldFilter(Object humaraCode){

        try {
            ObjectMapper mapper = new ObjectMapper();  
            mapper.setSerializationInclusion(Include.NON_NULL); 
            String json = mapper.writeValueAsString(humaraCode);
            return json;
        }
        catch(Exception e) {
            System.out.println("menuservice.java - line 113");
        }

        return null;
    }

        

}
