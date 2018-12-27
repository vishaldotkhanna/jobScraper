package jobscraper;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
	
	private String title, url, employerName, employerLocation;
	
	SearchResult(String title, String url, String employerName, String employerLocation) {
		this.title = title;
		this.url = url;
		this.employerName = employerName;
		this.employerLocation = employerLocation;
	}
	
	public String toString() {
		return "(title: " + this.title + ", employerName: " + this.employerName + ", employerLocation: " + this.employerLocation + ")";
	}
	
	public List<String> getRecord() {
		List<String> record = new ArrayList<String>();
		record.add(this.title);		
		record.add(this.employerName);
		record.add(this.employerLocation);
		record.add(this.url);
		
		return record;
	}
}
