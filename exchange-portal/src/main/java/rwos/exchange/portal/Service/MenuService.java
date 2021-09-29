package rwos.exchange.portal.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
        }
        
        return menus;
    }

    public String getFileContent(String path) {
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
