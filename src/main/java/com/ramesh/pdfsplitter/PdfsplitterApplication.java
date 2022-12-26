package com.ramesh.pdfsplitter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PdfsplitterApplication implements CommandLineRunner {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(PdfsplitterApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("hello");
		
		String sourceDir = args[0];
		String targetDir = args[1];
		
		Set<String> fileList = listFilesUsingFilesList(sourceDir);
		
		for(String file: fileList) {
			
			String fileFullPath = sourceDir+"\\"+file;
			String[] splitted = file.split("-");
			String bridgeId = splitted[0];
			
			splitMyFiles(fileFullPath, bridgeId, targetDir);
			
		}
			
		

	}
	
	public void splitMyFiles(String filePath, String bridgeId, String targetDir) throws IOException
	{
		File file = new File(filePath);
		PDDocument pdoc = PDDocument.load(file);
		
		List<MySplitter> splitters = new ArrayList<MySplitter>();
		splitters.add(new MySplitter("Bridge Inspection QC Form","_InspectReport_Routine","first"));
		splitters.add(new MySplitter("Bridge Inspection QC Form","_QC-Form-1","second"));
		splitters.add(new MySplitter("Repair Recommendations Form","_A5.6_RepairRec_1","third"));
		splitters.add(new MySplitter("Photograph Form - Defect","_Condition_Photo_1","four"));
		splitters.add(new MySplitter("Photograph Form-Inventory","_Standard_Photo_1","five"));
		splitters.add(new MySplitter("SCOUR STREAM & GROUND PROFILE","_A5.7_Scour_Profile_1","six"));
		
		

		for (int pageNumber = 1; pageNumber <= pdoc.getNumberOfPages(); pageNumber++) {

			PDFTextStripper s = new PDFTextStripper();
			s.setStartPage(pageNumber);
			s.setEndPage(pageNumber);
			String contents = s.getText(pdoc);

			for(int i=1; i<splitters.size();i++)
			{
				if (contents.contains(splitters.get(i).getSplitSearchText())) {
					if (splitters.get(i).getStartPage() == null) {
						splitters.get(i).setStartPage(pageNumber);
					} 
					
					splitters.get(i).setEndPage(pageNumber);
					
				}
			}
			
		}
		
		Integer qcLastPage = splitters.get(1).getStartPage()-1;
		splitters.get(0).setStartPage(1);
		splitters.get(0).setEndPage(qcLastPage);

		for(MySplitter mySplit : splitters)
		{
			if(mySplit.getStartPage() != null && mySplit.getEndPage() != null)
			{
				String fileName = mySplit.getFileNameAppender();
				Splitter splitter = new Splitter();
				splitter.setStartPage(mySplit.getStartPage());
				splitter.setEndPage(mySplit.getEndPage());
				splitter.setSplitAtPage(100);
				
				System.out.println("Start Page "+mySplit.getStartPage() +" End page" + mySplit.getEndPage());
				
				List<PDDocument> split = splitter.split(pdoc);
	
				PDDocument pdfDocPartial = split.get(0);
				File f = new File(targetDir+"\\"+mySplit.getFolder()+"\\"+bridgeId+"-"+fileName+".pdf");
				pdfDocPartial.save(f);
			}else {
				System.out.println("ERROR searching with " + mySplit.getSplitSearchText()+" on " + file.getName());
			}
			
		}
	}
	
	public Set<String> listFilesUsingFilesList(String dir) throws IOException {
	    try (Stream<Path> stream = Files.list(Paths.get(dir))) {
	        return stream
	          .filter(file -> !Files.isDirectory(file))
	          .map(Path::getFileName)
	          .map(Path::toString)
	          .collect(Collectors.toSet());
	    }
	}
}
