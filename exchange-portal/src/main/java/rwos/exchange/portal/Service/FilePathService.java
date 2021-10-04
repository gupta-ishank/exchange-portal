package rwos.exchange.portal.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwos.exchange.portal.Entity.FilePath;
import rwos.exchange.portal.Repository.FilePathRepository;

@Service
public class FilePathService {
    @Autowired
    private FilePathRepository filePathRepository;

    public FilePath addFilePath(String filePath){
        return filePathRepository.save(new FilePath(filePath));
    }

    public FilePath getFilePath(String filePath){
        return filePathRepository.findByPath(filePath);
    }
}
