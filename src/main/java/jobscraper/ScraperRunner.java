package jobscraper;


public class ScraperRunner {
	
	private static String query, location, filePath;

	public static void main(String[] args) {		
		ScraperRunner.getArgs(args);
		
		GlassdoorScraper glassdoorScraper = new GlassdoorScraper(query, location);
		glassdoorScraper.scrape();
		CsvWriter glassdoorWriter = new CsvWriter(filePath + "glassdoor.csv", glassdoorScraper.getResults());
		glassdoorWriter.write();
		
		StepstoneScraper stepstoneScraper = new StepstoneScraper(query, location);
		stepstoneScraper.scrape();
		CsvWriter stepstoneWriter = new CsvWriter(filePath + "stepstone.csv", stepstoneScraper.getResults());
		stepstoneWriter.write();
	}
	
	private static void getArgs(String[] args) {
		ScraperRunner.query = args.length >= 1 ? args[0] : "Java";
		ScraperRunner.location = args.length >= 2 ? args[1] : "Jena";
		ScraperRunner.filePath = args.length >= 3 ? args[2] : "/home/vishal/Desktop/";    // TODO: Change to a generic path
	}

}
