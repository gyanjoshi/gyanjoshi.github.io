package com.example.projectx.service;


import org.apache.commons.io.FilenameUtils;

import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.form.AddDownloadForm;

import com.example.projectx.form.AddNoticeForm;
import com.example.projectx.model.Article;

import com.example.projectx.model.Download;
import com.example.projectx.model.Notice;
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
		String title = download.getDownloadTopic();
		MultipartFile file = download.getDownloadFilePath();
		
		// add in database
		// Download Object
		Download d = new Download();
		d.setDownloadTopic(title);
		d.setUploadedDate(new java.sql.Date(System.currentTimeMillis()));
        
        
        
        Download obj = downloadRepo.save(d);
		
		String downloadFileName = "Download_"+obj.getId()+"."+FilenameUtils.getExtension(file.getOriginalFilename());
		
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
		else if	(type.equalsIgnoreCase("download"))
			return downloadspath;
		else
			return basepath;
	}


	
	public void editDownload(int id, AddDownloadForm download)
	{
		String title = download.getDownloadTopic();
		
		MultipartFile file = download.getDownloadFilePath();
		
		String fileName = download.getDownloadFilePath().getOriginalFilename();
		System.out.println("file name is :"+fileName);
		System.out.println("file name is :"+title);
		
		//download.setdownloadTopic(title);	
		
		String downloadFileName = "Download_"+id+"."+FilenameUtils.getExtension(file.getOriginalFilename());
        
		FileStorageService.uploadFile(downloadspath,downloadFileName,file);
        
        Date date = new java.sql.Date(System.currentTimeMillis());

        downloadRepo.editDownload(id, title, downloadFileName, date);
	}

	
	public void addNotice(AddNoticeForm notice) {
		System.out.println("reached to addnotice of download service:");
		String noticenumber  = notice.getNoticeNumber();
		String noticetitle = notice.getNoticeTitle();
		String noticetext = notice.getNoticeText();
		
//		String noticefilename = notice.getNoticeFileName().getOriginalFilename();
//		MultipartFile file = notice.getNoticeFileName();
		
		
		
		// add in database
		// Download Object
		Notice n = new Notice();
		
		n.setNoticeNumber(noticenumber);
		n.setNoticeText(noticetext);
		n.setNoticeTitle(noticetitle);
		//n.setNoticeFileName(noticefilename);
        n.setUploadedDate(new java.sql.Date(System.currentTimeMillis()));    
        
        
        Notice n2 = noticeRepo.save(n);
        
//        if(file != null)
//		{
//        	String noticeFileName = "Notice_"+n2.getId()+"."+FilenameUtils.getExtension(file.getOriginalFilename());
//    		
//    		FileStorageService.uploadFile(downloadspath,noticeFileName, file);
//    		
//    		n2.setNoticeFileName(noticeFileName);
//    		noticeRepo.save(n2);
//		}
        
        
		
		
		
	}

	public void editnotice(int id, AddNoticeForm notice) {
		String noticenumber  = notice.getNoticeNumber();
		String noticetitle = notice.getNoticeTitle();
		String noticetext = notice.getNoticeText();
		
//		String noticefilename = notice.getNoticeFileName().getOriginalFilename();
//		MultipartFile file = notice.getNoticeFileName();
//		
//		String noticeFileName = "Notice_"+id+"."+FilenameUtils.getExtension(file.getOriginalFilename());
//		
//		FileStorageService.uploadFile(downloadspath,noticeFileName, file);
		
		
		noticeRepo.updatenotice(id,noticenumber,noticetitle);
		
	}

	public void attachFile(int id, MultipartFile file) {
		// TODO Auto-generated method stub
		Notice notice = noticeRepo.getOne(id);
		
		String noticeFileName = "Notice_"+id+"."+FilenameUtils.getExtension(file.getOriginalFilename());
		
		notice.setNoticeFileName(noticeFileName);
		
		FileStorageService.uploadFile(downloadspath,noticeFileName, file);
		
		noticeRepo.save(notice);
		
		
		
	}
}
