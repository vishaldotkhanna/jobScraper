package jobscraper;


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
	
	public String[] getRecord() {
		return new String[] {this.title, this.employerName, this.employerLocation, this.url};
	}
}
