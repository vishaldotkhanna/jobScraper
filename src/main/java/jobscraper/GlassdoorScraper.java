package jobscraper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;


public class GlassdoorScraper extends Scraper {
	
	private static final String url = "https://www.glassdoor.de/index.htm";
	private static Map<String, String> xpaths;
	private static int numPages = 3;
		
	static {
		xpaths = new HashMap<String, String>();
		xpaths.put("query", "//input[@name=\"sc.keyword\"]");
		xpaths.put("location", "//input[@id=\"LocationSearch\"]");
		xpaths.put("searchButton", "//button[@id=\"HeroSearchButton\"]");
		xpaths.put("results", "//div[contains(@class, \"jobTitle\")]");
		xpaths.put("jobLink", ".//a[contains(@class, \"jobLink\")]");
		xpaths.put("nextPage", "//div[contains(@class, \"pagingControls\")]//li[contains(@class, \"next\")]");
		xpaths.put("crossButton", "//div[contains(@class, \"xBtn\")]");
		xpaths.put("employerDetails", "//div[contains(@class, \"empLoc\")]");
		xpaths.put("employerLocation", ".//span[contains(@class, \"loc\")]");
	}
	
	GlassdoorScraper(String query, String location) {
		super(query, location);
	}

	@Override
	protected Map<String, String> getXpaths() {
		return GlassdoorScraper.xpaths;
	}
	
	@Override
	protected int getNumPages() {
		return GlassdoorScraper.numPages;
	}
			
	@Override
	protected void parsePage() {		
		List<WebElement> pageResults = this.getListByXpath("results");
		List<WebElement> employerDetails = this.getListByXpath("employerDetails");
		int initialLen = this.results.size();
		
		// The last result is always empty
		for(int i = 0; i < pageResults.size() - 1; i++) {
			WebElement result = pageResults.get(i);
			WebElement employerDetail = employerDetails.get(i);
			String jobXpath = GlassdoorScraper.xpaths.get("jobLink");
			String employerLocationXpath = GlassdoorScraper.xpaths.get("employerLocation");
			
			try {
				WebElement jobLink = result.findElement(By.xpath(jobXpath));	
				WebElement employerLocation = employerDetail.findElement(By.xpath(employerLocationXpath));
				SearchResult searchResult = new SearchResult(jobLink.getText(), 
															 jobLink.getAttribute("href"), 
															 GlassdoorScraper.formatStr(employerDetail.getText()), 
															 employerLocation.getText());
				this.results.add(searchResult);	
				System.out.println("Parsed searchResult: " + searchResult.toString());
			} catch(NoSuchElementException e) {}			
		}
		
		System.out.println("Parsed " + (this.results.size() - initialLen) + " results");
	}
	
	protected static String formatStr(String s) {		
		char separator = (int) 8211;
		int separatorIdx = s.indexOf(separator);
		int end = separatorIdx == -1 ? Math.max(0, s.length() - 1) : separatorIdx;
		try {
			return s.substring(0, end).trim();		
			
		} catch(StringIndexOutOfBoundsException e) {
			return "";
		}
	}

	@Override
	protected String getUrl() {
		return GlassdoorScraper.url;
	}
		
}
