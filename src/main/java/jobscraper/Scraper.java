package jobscraper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public abstract class Scraper {
	
	private String query = null;
	private String location = null;
	private WebDriver driver;
	protected List<SearchResult> results;
		
	public List<SearchResult> getResults() {
		return results;
	}

	public String getQuery() {
		return query;
	}

	public String getLocation() {
		return location;
	}

	public WebDriver getDriver() {
		return driver;
	}
	
	Scraper(String query, String location) {
		this.query = query;
		this.location = location;
		this.driver = new ChromeDriver();
		results = new ArrayList<SearchResult>();
	}
		
	public void scrape() {
		this.getDriver().get(this.getUrl());
		this.doSearch();
		this.parse();
		this.getDriver().close();		
	}
	
	protected abstract Map<String, String> getXpaths();
	protected abstract String getUrl();
	protected abstract int getNumPages();
	protected abstract void parsePage();
	
	protected void nextPage() {	
		WebElement nextLink = this.getByXpath("nextPage");
		this.scrollIntoView(nextLink);
		nextLink.click();
	}
	
	protected void parse() {
		for(int i = 0; i < this.getNumPages(); i++) {
			try {
				this.sleep(this.getSleepTime());
				this.closeModal();
				this.parsePage();
				this.nextPage();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	protected WebElement getByXpath(String key) {
		String xpath = this.getXpaths().get(key);
		
		if(xpath != null) {
			return this.driver.findElement(By.xpath(xpath));
		}
		
		throw new IllegalArgumentException("Invalid key: " + key);
	}
	
	protected List<WebElement> getListByXpath(String key) {
		String xpath = this.getXpaths().get(key);
		
		if(xpath != null) {
			return this.driver.findElements(By.xpath(xpath));
		}
		
		throw new IllegalArgumentException("Invalid key: " + key);

	}
	
	protected void type(WebElement field, String text) {
		field.clear();
		field.sendKeys(text);
	}
	
	protected void doSearch() {
		if(this.query != null) {
			WebElement searchField = this.getByXpath("query");
			this.type(searchField, this.query);
		}
		
		if(this.location != null) {
			WebElement locationField = this.getByXpath("location");
			this.type(locationField, this.location);			
		}
		
		this.getByXpath("searchButton").click();
	}
	
	protected void closeModal() {
		try {
			WebElement crossButton = this.getByXpath("crossButton");
			this.scrollIntoView(crossButton);
			crossButton.click();
		} catch(NoSuchElementException | ElementNotVisibleException e) {}
	}
	
	protected void scrollIntoView(WebElement element) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) this.getDriver();
		jsExecutor.executeScript("arguments[0].scrollIntoView(true)", element);	
	}
	
	protected void sleep(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {}
	}
	
	protected int getSleepTime() {
		return 0;
	}
	
}
