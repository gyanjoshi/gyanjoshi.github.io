package com.example.projectx.service;

import org.apache.commons.io.FilenameUtils;
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
	
	@Autowired
	private NoticeRepository noticeRepo;
	
	
	public void addDownload(AddDownloadForm download)
	{
		String title = download.getTitle();
		MultipartFile file = download.getFile();
		
		// add in database
		
		// Download Object
		Download d = new Download();
		d.setDownloadTopic(title);
		d.setUploadedDate(new java.sql.Date(System.currentTimeMillis()));
        
        
        
        Download obj = downloadRepo.save(d);
		
		String downloadFileName = "Download_"+obj.getId()+FilenameUtils.getExtension(file.getOriginalFilename());
		
		FileStorageService.uploadFile(downloadspath,downloadFileName, file );
		d.setDownloadFilePath(downloadFileName);
		
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


	public void addNotice(AddNoticeForm notice) {
		// TODO Auto-generated method stub
		
	}

}
