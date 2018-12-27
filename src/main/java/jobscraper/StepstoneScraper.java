package jobscraper;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class StepstoneScraper extends Scraper {
			
	StepstoneScraper(String query, String location) {
		super(query, location);
	}

	private static final String url = "https://www.stepstone.de/";
	private static Map<String, String> xpaths;
	private static final int numPages = 2;
	private static int sleepTime = 2;
	
	static {
		xpaths = new HashMap<String, String>();
		xpaths.put("query", "//input[@name = \"ke\"]");
		xpaths.put("location", "//input[@name = \"ws\"]");
		xpaths.put("searchButton", "//button[@type = \"submit\"]");
		xpaths.put("crossButton", "(//span[@title = \"Schließen\"])[3]");
		xpaths.put("result", "//article[contains(@class, \"job-element\")]");
		xpaths.put("jobTitle", ".//h2[contains(@class, \"job-element__body__title\")]");
		xpaths.put("jobLink", ".//a[contains(@class, \"job-element__url\")]");
		xpaths.put("employerName", ".//div[contains(@class, \"job-element__body__company\")]");
		xpaths.put("employerLocation", ".//li[contains(@class, \"job-element__body__location\")]");	
		xpaths.put("nextPage", "(//a[contains(@class, \"at-next\")])[1]");
	}

	@Override
	public Map<String, String> getXpaths() {
		return StepstoneScraper.xpaths;
	}

	@Override
	protected String getUrl() {
		return StepstoneScraper.url;
	}
	
	protected void parsePage() {
		List<WebElement> pageResults = this.getListByXpath("result");
		String jobTitleXpath = StepstoneScraper.xpaths.get("jobTitle");
		String jobLinkXpath = StepstoneScraper.xpaths.get("jobLink");
		String employerNameXpath = StepstoneScraper.xpaths.get("employerName");
		String employerLocationXpath = StepstoneScraper.xpaths.get("employerLocation");
		
		for(WebElement result : pageResults) {			
			try {
				WebElement jobTitle = result.findElement(By.xpath(jobTitleXpath));
				WebElement jobLink = result.findElement(By.xpath(jobLinkXpath));
				WebElement employerName = result.findElement(By.xpath(employerNameXpath));
				WebElement employerLocation = result.findElement(By.xpath(employerLocationXpath));
				
				SearchResult searchResult = new SearchResult(jobTitle.getText(), 
															 jobLink.getAttribute("href"), 
															 employerName.getText(),
															 employerLocation.getText());
				this.results.add(searchResult);
				System.out.println("Parsed searchResult: " + searchResult.toString());
			} catch(NoSuchElementException e) {}
		}
	}

	@Override
	protected int getNumPages() {
		return StepstoneScraper.numPages;
	}
	
	@Override
	protected int getSleepTime() {
		return StepstoneScraper.sleepTime;
	}

}
