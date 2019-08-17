package com.example.projectx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.form.AddDownloadForm;
import com.example.projectx.form.AddNoticeForm;
import com.example.projectx.model.Article;
import com.example.projectx.model.Download;
import com.example.projectx.repository.DownloadRepository;
import com.example.projectx.repository.NoticeRepository;

@Service
public class DownloadService {
	
	@Value("${upload.path.download}")
    private String path;
	
	@Autowired
	private FileStorageService fileService;
	
	@Autowired
	private DownloadRepository downloadRepo;
	
	@Autowired
	private NoticeRepository noticeRepo;
	
	
	public void addDownload(AddDownloadForm download)
	{
		String title = download.getTitle();
		MultipartFile file = download.getFile();
		
		fileService.uploadFile(path, file);
		
		// add in database
		
		// Download Object
		Download d = new Download();
		d.setDownloadTopic(title);
		d.setDownloadFilePath(file.getOriginalFilename());
		
        d.setUploadedDate(new java.sql.Date(System.currentTimeMillis()));
        
        
        
        downloadRepo.save(d);
	}


	public void addNotice(AddNoticeForm notice) {
		// TODO Auto-generated method stub
		
	}

}
