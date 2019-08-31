package com.example.projectx.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;


public class PdfMerger {

	public static void main(String[] args) throws InvalidPasswordException, IOException {
		// TODO Auto-generated method stub

		File coverPage = new File("D:\\FileStore\\Journal\\coverimage\\test.pdf");
		
		PDDocument c = PDDocument.load(coverPage);
		
		File editorialPage = new File("D:\\FileStore\\Journal\\coverimage\\bhanja ko america visa for dv loterry confirmarion page.pdf");
		
		PDDocument e = PDDocument.load(editorialPage);
		
		PDFMergerUtility PDFmerger = new PDFMergerUtility(); 
		
		PDFmerger.setDestinationFileName("D:\\FileStore\\Journal\\Result.pdf");
		
	
		PDFmerger.addSource(coverPage);
		PDFmerger.addSource(editorialPage);	
		
		PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
		
		c.close();
		e.close();
	}
	
	public static File mergePdfs(List<File> files, String destFile) throws InvalidPasswordException, IOException
	{
		
		PDFMergerUtility PDFmerger = new PDFMergerUtility(); 
		
		PDFmerger.setDestinationFileName(destFile);
		
		List<PDDocument> docs = new ArrayList<PDDocument>();
		
		
		for(File f: files)
		{
			PDDocument document = PDDocument.load(f);
			
			PDFmerger.addSource(f);
			
			docs.add(document);
		}		

		
		PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
		
		for(PDDocument doc: docs)
		{
			doc.close();
		}
		
		File f = new File(destFile);
		
		return f;
	}
	

}
