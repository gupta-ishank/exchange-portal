package rwos.exchange.portal.Service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

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
                            resVal.getContent().forEach((contKey, contVal) ->{
                                yamlParser.setResponsePayloadDetails(nullFieldFilter(contVal.getSchema()));
                            });
                        });
                    }
                    if(!Objects.isNull(val.getParameters())){
                        val.getParameters().forEach(parVal ->{
                            yamlParser.setResponsePayloadDetails(nullFieldFilter(parVal.getSchema()));
                            
                        });
                    }
                    if(!Objects.isNull(val.getRequestBody())){
                        val.getRequestBody().getContent().forEach((contentKey, contentVal) ->{
                            yamlParser
                            .setRequestPayloadDetails(nullFieldFilter(contentVal.getSchema()));
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
}
