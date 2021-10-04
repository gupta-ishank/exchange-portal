package rwos.exchange.portal.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import rwos.exchange.portal.Entity.Menu;


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


    public List<Menu> getAllApis(String path){

        List<Menu> data = new ArrayList<>();
        ObjectMapper oMapper = new ObjectMapper();
        try {
            Yaml yaml = new Yaml();
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            LinkedHashMap<String, Object> customer = yaml.load(reader);
            Map<String, Object> paths = oMapper.convertValue(customer.get("paths"), Map.class);
            for (Map.Entry<String,Object> pathEntry : paths.entrySet()){
                Map<String, Object> methods = oMapper.convertValue(pathEntry.getValue(), Map.class);
                for (Map.Entry<String,Object> methodEntry : methods.entrySet()){
                    Map<String, Object> methodData = oMapper.convertValue(methodEntry.getValue(), Map.class);
                    data.add(new Menu(methodEntry.getKey(),pathEntry.getKey(), methodData.get("summary").toString())); 
                }
            } 
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return data;
    }

    public Object getFileContent(String path) {
        File file = new File(path);
        String data = "";
        if(!file.isDirectory() && !file.isHidden() && file.exists()){
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(file));
                data = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
