package jobscraper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CsvWriter {
	
	private String filePath;
	private List<SearchResult> results;
	private static final String[] header = {"Title", "Employer Name", "Employer Location", "URL"};
	
	CsvWriter(String filePath, List<SearchResult> results) {
		this.filePath = filePath;
		this.results = results;
	}
	
	public void write() {		
		try(FileWriter writer = new FileWriter(this.filePath);
			CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
				) {
			printer.printRecord((Object[]) CsvWriter.header);
			
			for(SearchResult result : this.results) { 
				printer.printRecord(result.getRecord());
			}
			
			System.out.println("Wrote " + this.results.size() + " records to " + this.filePath);
		} catch(IOException e) {
			e.printStackTrace();
		} 
	}

}
