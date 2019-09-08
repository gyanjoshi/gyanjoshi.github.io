package com.example.projectx.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

//import org.aioobe.cloudconvert.CloudConvertService;
//import org.aioobe.cloudconvert.ConvertProcess;
//import org.aioobe.cloudconvert.ProcessStatus;

public class PdfConverter {
	
	private static String apikey="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImUzMTZhYWZmYzI4YTQ0NjRlOTYzMTJmMDk0ZDY4NTZmZTA2ZDQwZDU4YWU3MzNhMTIxZGVlNGZlODMxMzY2OTNlZDc3MTkxNzllZGQ3Njc3In0.eyJhdWQiOiIxIiwianRpIjoiZTMxNmFhZmZjMjhhNDQ2NGU5NjMxMmYwOTRkNjg1NmZlMDZkNDBkNThhZTczM2ExMjFkZWU0ZmU4MzEzNjY5M2VkNzcxOTE3OWVkZDc2NzciLCJpYXQiOjE1Njc4MTkwODMsIm5iZiI6MTU2NzgxOTA4MywiZXhwIjo0NzIzNDkyNjgzLCJzdWIiOiIzODMyNTg2NSIsInNjb3BlcyI6WyJ1c2VyLndyaXRlIiwidXNlci5yZWFkIl19.Q54lP4c2YxgynhEU1aHobGE61cDOT5nPKyySUHC_fNZTtud9qlU6ZEIic38l78eJtRi-PU_gaVGGkoTcaGPyiL9IkG9qwU2lyo3sEfj5Hj6eOyuyoFRXxVR-cdlMqQk51QpiNha4YJ-KW3G7Ytj1OAX8LMxDKUasdZz6fAFGChMvCoBJdxL_qvthgtW00WplQfvaD4AT9Ybc8Vrpi2g284V-oZZPcwqBZg6RMrDWuHL5TOwY4FYPk5jKWP-Iu8EcfuRCL9khcvRa1B6QUW6hXSsTt2F5GtgZy0eLGp4oHFW7ZdC9XAD_MmO-LXgxQQlYw-3Rc5P_c6PLmd2GENVVlyqDanh-dpVkkAPFhjk0rpoSS0t4PTURk9-ZGy3Om0AEUIB390B5fPqkPwQx6qkjq-GcFbk7VExGWh1XaZPeICWLAGow8sCbV6VtyZgm7LZHtTAuWbh052OX2IFrPf874WPhzHgHiiIUU4gG3ys92swZNJGgCricdQs53chVF0RpFaLoljA3mg1jMCgSWVTUOK_XGryf-yJ2AiWty5UCxYauq3yAAbuUahvTm0WjDt4vRsZe_eCbpGq-8nVm4ZXex3bjXHcTCVHzPdQt-lh7eGvbWJJ8x3x7YPAl4GeYtDH95swqVCTNRuFDyJJt5i3nRz_WH0LKKqSgSg8R9HzdHBY";

	public static void main(String[] args) throws URISyntaxException, ParseException, InterruptedException, IOException
	{
		String inFile = "D:\\FileStore\\Article_102.docx";
		String outFile = "D:\\FileStore\\Article_102.pdf";
		toPdf(inFile,outFile);
	}
	public static void toPdf(String inFile, String outFile) throws URISyntaxException, ParseException, InterruptedException, IOException
	{
		// Create service object
//		CloudConvertService service = new CloudConvertService(apikey);
//		
//		
//
//		// Create conversion process
//		ConvertProcess process = service.startProcess("docx", "pdf");
//
//		// Perform conversion
//		process.startConversion(new java.io.File(inFile));
//
//		// Wait for result
//		ProcessStatus status;
//		waitLoop: while (true) {
//		    status = process.getStatus();
//		    
//		    switch (status.step) {
//		    case FINISHED: break waitLoop;
//		    case ERROR: throw new RuntimeException(status.message);
//			default:
//				break;
//		    }
//		    
//		    // Be gentle
//		    Thread.sleep(200);
//		}
//
//		java.io.File output = new java.io.File(outFile);
//		
//		// Download result
//		service.download(status.output.url, output);
//
//		// Clean up
//		process.delete();
	}
	

}
