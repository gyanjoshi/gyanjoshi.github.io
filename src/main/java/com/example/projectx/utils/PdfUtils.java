package com.example.projectx.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtils {

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
//	
//	private static void generatePDFFromImage(String filename, String extension) {
//	    Document document = new Document();
//	    String input = filename + "." + extension;
//	    String output = "src/output/" + extension + ".pdf";
//	    FileOutputStream fos = new FileOutputStream(output);
//	 
//	    PdfWriter writer = PdfWriter.getInstance(document, fos);
//	    writer.open();
//	    document.open();
//	    document.add(Image.getInstance((new URL(input))));
//	    document.close();
//	    writer.close();
//	}
//	public static void generatePDFFromTxt(String inFilename, String inPath, String outFilename, String outPath) throws IOException, DocumentException {
//		Document pdfDoc = new Document(PageSize.A4);
//		PdfWriter.getInstance(pdfDoc, new FileOutputStream(outPath+outFilename))
//				.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
//		pdfDoc.open();
//		
//		Font myfont = new Font();
//		myfont.setStyle(Font.NORMAL);
//		myfont.setSize(11);
//		pdfDoc.add(new Paragraph("\n"));
//		
//		BufferedReader br = new BufferedReader(new FileReader(inFilename));
//		String strLine;
//		while ((strLine = br.readLine()) != null) {
//			Paragraph para = new Paragraph(strLine + "\n", myfont);
//			para.setAlignment(Element.ALIGN_JUSTIFIED);
//			pdfDoc.add(para);
//		}
//		
//		pdfDoc.close();
//		br.close();
//	}

}
