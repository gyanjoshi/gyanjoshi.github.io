package com.example.projectx.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.projectx.model.Download;

import com.example.projectx.repository.Top2DownloadsRepository;

public class topfourdownloads {
	@Autowired
	private static Top2DownloadsRepository ttdrepo;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<Download> topTenDownloads = ttdrepo.findFirst10ById();
		
		for (Download x :  topTenDownloads) {
			System.out.println("toptendownloads" + x.toString());
		}
		

	}

}
