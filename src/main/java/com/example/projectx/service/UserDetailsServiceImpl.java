package com.example.projectx.service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.projectx.dao.AppUserDao;
import com.example.projectx.dto.UserDto;
import com.example.projectx.form.UserForm;
import com.example.projectx.mail.EmailService;
import com.example.projectx.mail.Mail;
import com.example.projectx.model.AppRole;
import com.example.projectx.model.AppUser;

import com.example.projectx.model.Qualification;
import com.example.projectx.model.Title;
import com.example.projectx.repository.UserRepository;
import com.example.projectx.utils.EncryptedPasswordUtils;
import com.example.projectx.utils.PasswordGenerator;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
	@Value("${upload.path.profile}")
	private String profilePath;
	
	@Autowired
    private AppUserDao userDao;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private EmailService emailService;
    
 
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		com.example.projectx.model.AppUser user = userDao.getActiveUser(userName);
		
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		
		System.out.println("Role="+user.getRole());
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
		UserDetails userDetails = (UserDetails)new User(user.getUserName(),
				user.getPassword(), Arrays.asList(authority));
		return userDetails;		
		
	}
    
    public List<AppUser> getAllEditors()
    {
    	return userDao.getAllEditors();
    }
    
    public AppUser getUserByUsername(String userName)
    {
    	return userDao.findUserAccount(userName);
    }

	public List<AppUser> getAllAuthors() {
		
		return userDao.getAllAuthors();
	}
	
	public List<AppRole> getAllRoles()
	{
		return userDao.getAllRoles();
	}
	public List<Title> getAllTitles()
	{
		return userDao.getAllTitles();
	}
	public List<Qualification> getAllQualifications()
	{
		return userDao.getAllQualifications();
	}
	
	public String addUser(UserForm user)
	{
		String message;
		AppUser newUser = new AppUser();
		AppUser u = userDao.findUserAccount(user.getUserName());
		
		if(u==null)
		{
			newUser.setTitle(user.getTitle());
			newUser.setFullName(user.getFullName());
			newUser.setAddress1(user.getAddress1());
			newUser.setAddress2(user.getAddress2());
			
			if(user.getEmail().equals(user.getConfirmEmail()))
				newUser.setEmail(user.getEmail());
			else
				
				return "ERROR: E-mail does not match";
			newUser.setPhone(user.getPhone());
			newUser.setCity(user.getCity());
			newUser.setState(user.getState());
			newUser.setQualification(user.getQualification());
			newUser.setProfession(user.getProfession());			
			
			newUser.setUserName(user.getUserName());
			if(user.getPassword().contentEquals(user.getConfirmpassword()))
				newUser.setPassword(EncryptedPasswordUtils.encrytePassword(user.getPassword()));
			else
				return "ERROR: Password does not match";
			newUser.setRole(user.getRole());
			
			newUser.setEnabled((short) 1);
			
			MultipartFile file = user.getProfilePicture();
			
			if(file !=null)
			{
				String profileFileName = user.getUserName()+"_Profile."+FilenameUtils.getExtension(file.getOriginalFilename());				
				
				newUser.setProfilePicture(profileFileName);
				FileStorageService.uploadFile(profilePath,profileFileName, file);
				
			}
			
			userRepo.save(newUser);
			
			message = "User Registered successfully";
			
			// send email to user
			
			Mail mail = new Mail();

            
            mail.setTo(user.getEmail());
            mail.setSubject("You are now registered !!");
            mail.setContent("Dear "+user.getFullName()+",<br>"
            		+ "Congratulations!!<br>"
            		+ "You are registered in our Journal Publication system as a "+user.getRole().replaceAll("ROLE_", "")+"<br>"
            		+ "UserName :"+user.getUserName()+"<br>"
            		+ "Password :"+user.getPassword()+"<br>"
            		+ " Regards,<br>"
            		+ "Administrator<br>"
            		);
            
            try {
     			emailService.sendHtmlMessage(mail);
     		} catch (MessagingException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		else
		{
			message = "ERROR: UserName "+user.getUserName()+" is already used.";
		}
		
		
		return message;	
		
		
	}
    
	public Map<String,byte[]> getAllProfilePictures()
	{
		Map<String,byte[]> profileMap = new HashMap<String,byte[]>();
		
		List<AppUser> users = userDao.getAllUsers();
		
		if(users != null)
		{
			for(AppUser j: users)
			{
				byte[] bytes = getProfilePicture(j.getUserName());
				profileMap.put(j.getUserName(), bytes);
			}
		}
		
		
		return profileMap;
	}
	
	private byte[] getProfilePicture(String username)
	{
		String fileName=getUserByUsername(username).getProfilePicture();
		
		if(fileName != null)
		{
			String absoultePath = profilePath + fileName;
			
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
	
	public String getProfileLocation()
	{
		return profilePath;
		
	}
	
	public void updateProfilePicture(String uname, MultipartFile file)
	{
		AppUser user = userRepo.getOne(uname);
		
		//String fileName = "Journal_Cover_"+journal.getId()+FilenameUtils.getExtension(file.getOriginalFilename());
		if(file !=null)
		{
			String profileFileName = user.getUserName()+"_Profile."+FilenameUtils.getExtension(file.getOriginalFilename());				
			
			user.setProfilePicture(profileFileName);
			FileStorageService.uploadFile(profilePath,profileFileName, file);
			
		}
	}

	public void editUser(String uname, AppUser newUser, String role) 
	{
		
		AppUser oldUser = userRepo.getOne(uname);
		
		if(role.equalsIgnoreCase("ROLE_AUTHOR"))
		{
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
		}
		else
		{
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
			oldUser.setRole(newUser.getRole());// for admin right users only
		}
		
		
		
		userRepo.save(oldUser);
		
		
		
	}


	public void changePassword(String userName, String password) {
		
		AppUser user = userRepo.getOne(userName);
		
		String encryptedPassword = EncryptedPasswordUtils.encrytePassword(password);
		user.setPassword(encryptedPassword);
		
		userRepo.save(user);
		
	}

	public UserDto getUserByEmail(String email) {
		
		List<UserDto> users = userRepo.findByEmail(email);
		
		if(users == null || users.size() == 0 )
		{
			return null;
		}
		else
		{
			return users.get(0);
		}		
		
	}


	//fetch editor's profile pictures
	public Map<String,byte[]> getEditorsProfilePictures()
	{
		Map<String,byte[]> profileMap = new HashMap<String,byte[]>();
		
		List<AppUser> users = userDao.getAllEditors();
		
		if(users != null)
		{
			for(AppUser j: users)
			{
				byte[] bytes = getProfilePicture(j.getUserName());
				profileMap.put(j.getUserName(), bytes);
			}
		}
		
		
		return profileMap;
	}
	public void resetPassword(UserDto user) {
		
		
		String randomPassword = PasswordGenerator.getAlphaNumericString(8);
		
		changePassword(user.getUserName(), randomPassword);
		
		
		Mail m = new Mail();
		
		m.setSubject("Password Reset");
		m.setTo(user.getEmail());
		m.setContent("Your login credentials have been sent as per your request. UserName: "+user.getUserName()+"; Password: "+randomPassword+". Please logon using this password and change this password after you log on.");
		try {
			emailService.sendSimpleMessage(m);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void registerAuthor(String email, String username, String password, String fullName) {
		// TODO Auto-generated method stub
		AppUser user = new AppUser();
		
		String encryptedPassword = EncryptedPasswordUtils.encrytePassword(password);
		
		user.setEmail(email);
		user.setUserName(username);
		user.setPassword(encryptedPassword);
		user.setFullName(fullName);
		
		user.setRole("ROLE_AUTHOR");
		user.setEnabled((short) 1);
		
		userRepo.save(user);
		
		// send email to user
		
					Mail mail = new Mail();

		            
		            mail.setTo(user.getEmail());
		            mail.setSubject("You are now registered !!");
		            mail.setContent("Dear "+user.getFullName()+",<br>"
		            		+ "Congratulations!!<br>"
		            		+ "You are registered in our Journal Publication system as an author. <br>"
		            		+ "UserName :"+user.getUserName()+"<br>"
		            		+ "Password :"+user.getPassword()+"<br>"
		            		+ " Regards,<br>"
		            		+ "Administrator<br>"
		            		);
		            
		            try {
		     			emailService.sendHtmlMessage(mail);
		     		} catch (MessagingException e) {
		     			// TODO Auto-generated catch block
		     			e.printStackTrace();
		     		} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
	}
	
//	public void updateProfilePicture(String userid, MultipartFile file)
//	{
//		String fileName = file.getOriginalFilename();
//		
//		FileStorageService.uploadFile(coverpage, file);
//		
//		journalRepo.updateCoverPage(userid,fileName);
//	}
}
