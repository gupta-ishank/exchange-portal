package rwos.exchange.portal.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;

import rwos.exchange.portal.Entity.SwaggerApiData;

@Service
public class SwaggerApiDataService {
	public List<SwaggerApiData> getAllApiContent(String path){
		List<SwaggerApiData> data = new ArrayList<>();
        ObjectMapper oMapper = new ObjectMapper();
        try {
            Yaml yaml = new Yaml();
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            LinkedHashMap<String, Object> obj = yaml.load(reader);
            Map<String, Object> paths = oMapper.convertValue(obj.get("paths"), Map.class);
            for (Map.Entry<String,Object> pathEntry : paths.entrySet()){
                Map<String, Object> methods = oMapper.convertValue(pathEntry.getValue(), Map.class);
                for (Map.Entry<String,Object> methodEntry : methods.entrySet()){
                    Map<String, Object> methodData = oMapper.convertValue(methodEntry.getValue(), Map.class);
                    data.add(new SwaggerApiData(pathEntry.getKey(), methodEntry.getKey(), methodData.get("responses"), methodData.get("parameters"))); 
                }
            } 
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return data;
	}
}
