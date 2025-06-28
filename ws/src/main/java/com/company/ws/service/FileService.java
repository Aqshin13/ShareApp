package com.company.ws.service;

import com.company.ws.dto.FileLocation;
import com.company.ws.error.CommonException;
import com.company.ws.error.UnknownFileTypeException;
import org.apache.tika.Tika;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {


    private static final Tika tika=new Tika();

    @Bean
    CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                createFolder(Paths.get(FileLocation.PROFILE.getValue()));
                createFolder(Paths.get(FileLocation.SHARES.getValue()));

            }
        };
    }

    public void createFolder(Path path) {
        File file = path.toFile();
        boolean isFolderExist = file.exists() && file.isDirectory();
        if (!isFolderExist) {
            file.mkdir();
        }
    }

    public  String writeFile(byte[] bytes,FileLocation fileLocation) {
        try {
            String fileName = UUID.randomUUID().toString();
            File file = new File(fileLocation.getValue() + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            return fileName;
        } catch (Exception e) {
            throw new CommonException("File is not uploaded",500);
        }
    }


    public  void deleteFile(String fileName,FileLocation location){
        File file=new File(location.getValue()+fileName);
        if (fileName==null || fileName.isEmpty() || !file.exists()){
         return;
        }
        file.delete();
    }


    public  void deleteAllFiles(List<String> filenames){
        for (String filename:filenames){
            deleteFile(filename,FileLocation.SHARES);
        }
    }


    public  void detect(byte[] bytes){
       String type= tika.detect(bytes);
       if(!(type.contains("image/png") || type.contains("image/jpg") || type.contains("image/jpeg"))){
       throw new UnknownFileTypeException("File must be png and jpg format");
       }
    }

}
