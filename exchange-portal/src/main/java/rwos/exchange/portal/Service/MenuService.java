package rwos.exchange.portal.Service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import rwos.exchange.portal.Entity.Menu;
import rwos.exchange.portal.Entity.MyFile;

@Service
public class MenuService {

    FileFilter fileFilter = new FileFilter(){

        @Override
        public boolean accept(File pathname) {
            if(pathname.getName().endsWith(".json") || 
            pathname.getName().endsWith(".yml")) return true;
            return false;
        }
        
    };


    public List<Menu> getAllMenu(File folder){
        List<Menu> menus = new ArrayList<>();
        for(File file: folder.listFiles()){
            if(file.isDirectory() && !file.isHidden()){
                Menu node = new Menu();
                node.setPath(file.getAbsolutePath());
                node.setType(1);
                for(File files: file.listFiles(fileFilter)){
                    if(!files.isDirectory() && !files.isHidden()){
                        if(files.getName().endsWith(".json")) node.addFiles( new MyFile(files.getName(), 2) );
                        else node.addFiles( new MyFile(files.getName(), 3) );
                    }
                }
                node.setName(file.getName());
                node.setChilds(getAllMenu(file));
                menus.add(node);
            }
        }
        return menus;
    }
}
