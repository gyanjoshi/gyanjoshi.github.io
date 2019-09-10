package com.example.projectx.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.form.EditorForm;
import com.example.projectx.mail.EmailService;
import com.example.projectx.mail.Mail;
import com.example.projectx.model.AppUser;
import com.example.projectx.model.Article;
import com.example.projectx.model.Editor;
import com.example.projectx.repository.ArticleRepository;
import com.example.projectx.repository.EditorRepository;

@Service
public class EditorService {
	
	@Autowired
	private EditorRepository editorRepo;
	
	@Value("${upload.path.profile}")
	private String profilePath;
	
	@Value("${upload.path.article}")
    private String path;
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
    private ArticleRepository articleRepo;
	
	public List<Editor> getAllEditors()
    {
    	return editorRepo.findAll();
    }
	
	public Map<Integer,byte[]> getAllProfilePictures()
	{
		Map<Integer,byte[]> profileMap = new HashMap<Integer,byte[]>();
		
		List<Editor> editors = editorRepo.findAll();
		
		if(editors != null)
		{
			for(Editor j: editors)
			{
				byte[] bytes = getProfilePicture(j.getProfilePicture());
				profileMap.put(j.getId(), bytes);
			}
		}
		
		
		return profileMap;
	}
	
	private byte[] getProfilePicture(String profileFileName)
	{
		
		if(profileFileName != null)
		{
			String absoultePath = profilePath + profileFileName;
			
			File f = new File(absoultePath);
			byte[] bytes=null;
			try {
				bytes =  Files.readAllBytes(f.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bytes;
		}
		else return null;
		
	}
	
	public String addEditor(EditorForm editor)
	{
		String message;
		Editor neweditor = new Editor();
		
		
			neweditor.setTitle(editor.getTitle());
			neweditor.setFullName(editor.getFullName());
			neweditor.setAddress1(editor.getAddress1());
			neweditor.setAddress2(editor.getAddress2());
			
			if(editor.getEmail().equals(editor.getConfirmEmail()))
				neweditor.setEmail(editor.getEmail());
			else
				message = "ERROR: E-mail does not match";
			neweditor.setPhone(editor.getPhone());
			neweditor.setCity(editor.getCity());
			neweditor.setState(editor.getState());
			neweditor.setQualification(editor.getQualification());
			neweditor.setProfession(editor.getProfession());			
			
			neweditor.setFullName(editor.getFullName());
			
			
			MultipartFile file = editor.getProfilePicture();
			
			if(file !=null)
			{
				String profileFileName = editor.getFullName()+"_editorProfile."+FilenameUtils.getExtension(file.getOriginalFilename());				
				
				neweditor.setProfilePicture(profileFileName);
				FileStorageService.uploadFile(profilePath,profileFileName, file);
				
			}
			
			editorRepo.save(neweditor);
			message = "editor Registered successfully";		
		
		return message;	
		
		
	}

	public void editEditor(Integer id, EditorForm newUser) {

				Editor oldUser = editorRepo.getOne(id);			
				oldUser.setTitle(newUser.getTitle());
				oldUser.setFullName(newUser.getFullName());
				oldUser.setAddress1(newUser.getAddress1());
				oldUser.setAddress2(newUser.getAddress2());
				oldUser.setPhone(newUser.getPhone());
				oldUser.setCity(newUser.getCity());
				oldUser.setState(newUser.getState());
				oldUser.setQualification(newUser.getQualification());
				oldUser.setProfession(newUser.getProfession());			
				oldUser.setEmail(newUser.getEmail());			
			
				editorRepo.save(oldUser);
			
			
			
		}

	public void updateProfilePicture(Integer id, MultipartFile file)
	{
		Editor editor = editorRepo.getOne(id);
		if(file !=null)
		{
			String existingFile = editor.getProfilePicture();
			
			if(existingFile != null)
			{
				FileStorageService.deleteFile(profilePath, existingFile);
			}
				
			String profileFileName = editor.getFullName()+"_editorProfile."+FilenameUtils.getExtension(file.getOriginalFilename());				
			
			editor.setProfilePicture(profileFileName);
			FileStorageService.uploadFile(profilePath,profileFileName, file);
			
		}
	}

	public void toReviewer(int id, String userName, String message, String reviewers) {
		
		Article a = articleRepo.findById(id).get();
		//AppUser user = userDetailsService.getUserByUsername(userName);
		
		File f = new File(path+a.getFileName());
		
		if(f.exists())
		{
			Mail mail = new Mail();

	        String editors[] = reviewers.split(",");
	        
	        for(String editor: editors)
	        {
	        	// send email to each editor
	        	int i = Integer.valueOf(editor);
	        	
	        	Editor e = editorRepo.getOne(i);
	        	
	        	mail.setTo(e.getEmail());
	            mail.setSubject("Request to Review article: "+a.getTopic());
	            mail.setContent(message);
	            
	            try {
	     			emailService.sendHtmlMessage(mail,f);
	     			a.setStatus("Sent to Reviewer");	    	        
	    	        articleRepo.save(a);
	     			
	     		} catch (MessagingException ex) {
	     			// TODO Auto-generated catch block
	     			ex.printStackTrace();
	     		} catch (Exception ex) {
	    			// TODO Auto-generated catch block
	    			ex.printStackTrace();
	    		}
	        }       
	        
		}
		else
		{
			System.out.println("Article file "+f.getAbsolutePath()+" does not exist!");
		}		
		
	}
}
