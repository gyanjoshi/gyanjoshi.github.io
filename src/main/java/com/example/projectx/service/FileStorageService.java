package com.example.projectx.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.exception.StorageException;


@Service
public class FileStorageService {
	
	

    public static void uploadFile(String path, String fileName, MultipartFile file) 
    {
    	
    	//String fileName=null;
    	String directoryPath = null;

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }
        
        String separator = File.separator;
        
        if(path.endsWith(separator))
        	directoryPath = path.substring(0, path.length()-1);
        else
        	directoryPath = path;
        
        File directory = new File(directoryPath);
        
        boolean status = directory.mkdirs();
        
        
        if(status)
        	System.out.println("Directory created: "+directoryPath);
        else
        	System.out.println("Directory already exists: "+directoryPath);

        try {	           
	            InputStream is = file.getInputStream();

            System.out.println("File :"+directoryPath + separator+fileName);
            Files.copy(is, Paths.get(directoryPath + separator+fileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {

            String msg = String.format("Failed to store file", file.getName());

            throw new StorageException(msg, e);
        }       
        

    }
    public  static File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
	    File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
	    multipart.transferTo(convFile);
	    return convFile;
	}
	public static void deleteFile(String path, String file) {
		
		File _file = new File(path+file);
		
		if(_file.exists())
			_file.delete();
		
		
	}

}
