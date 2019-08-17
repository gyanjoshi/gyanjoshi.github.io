package com.example.projectx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.form.AddDownloadForm;
import com.example.projectx.model.Download;
import com.example.projectx.repository.DownloadRepository;

@Service
public class DownloadService {
	
	@Value("${upload.path.download}")
    private String downloadspath;
	
	@Value("${upload.path.article}")
    private String articlespath;
	
	@Value("${upload.path.journal}")
    private String journalspath;
	
	@Value("${upload.path.coverimage}")
    private String coverpagepath;
	
	@Value("${upload.path}")
    private String basepath;
	
		
	@Autowired
	private DownloadRepository downloadRepo;
	
	
	public void addDownload(AddDownloadForm download)
	{
		String title = download.getTitle();
		MultipartFile file = download.getFile();
		
		FileStorageService.uploadFile(downloadspath, file);
		
		// add in database
		
		// Download Object
		Download d = new Download();
		d.setDownloadTopic(title);
		d.setDownloadFilePath(file.getOriginalFilename());
		
        d.setUploadedDate(new java.sql.Date(System.currentTimeMillis()));
        
        
        
        downloadRepo.save(d);
	}
	
	public String getDownloadPath(String type)
	{
		if(type.equalsIgnoreCase("article"))
			return articlespath;
		else if (type.equalsIgnoreCase("journal"))
			return journalspath;
		else if	(type.equalsIgnoreCase("cover"))
			return coverpagepath;
		else
			return basepath;
	}

}
