package com.wallethub.qa.testcases;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.wallethub.qa.util.TestUtil;

public class SubmitReviewTest {

	public static WebDriver driver;

	public static void main(String[] args) {

		initializeApp("https://wallethub.com/");
		loginApp("udaytest695@gmail.com", "Tester12#");
		submitReview("http://wallethub.com/profile/test_insurance_company/", "Health Insurance",
				"Test review for Test Insurance Company. "
						+ "This company does not exist in reality, and hence any review provided "
						+ "here as part of the training or assessment shall not be taken "
						+ "into consideration for referring this company in any means.");

	}

	public static void initializeApp(String url) {

		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICITLY_WAIT, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);

		driver.get(url);
	}

	public static void loginApp(String userName, String password) {

		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath("//a[@class='login']")));
		action.click().perform();

		driver.findElement(By.xpath("//input[@name='em']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@name='pw']")).sendKeys(password);

		TestUtil.clickOn(driver, driver.findElement(By.cssSelector("[data-hm-tap='doLogin\\(\\$event\\)\\;']")),
				TestUtil.TIME_OUT);

	}

	public static void submitReview(String url2, String policyOption, String reviewMessage) {

		String xpath_star = "(//*[@id=\"reviews-section\"]/div[1]/div[3]/review-star/div/*[name()=\"svg\"])[";
		String xpath_star5 = "(//*[@id=\"reviews-section\"]/div[1]/div[3]/review-star/div/*[name()=\"svg\"])[5]";
		String xpath_dropdown = "//*[@id=\"reviews-section\"]/modal-dialog/div/div/write-review/div/ng-dropdown/div/span";
		String xpath_dropdownlist = "//*[@id=\"reviews-section\"]/modal-dialog/div/div/write-review/div/ng-dropdown/div/ul/li";
		String xpath_reviewTextField = "//*[@id=\"reviews-section\"]/modal-dialog/div/div/write-review/div/div[1]/textarea";
		String xpath_submitBtn = "//*[@id=\"reviews-section\"]/modal-dialog/div/div/write-review/sub-navigation/div/div[2]";

		driver.navigate().to(url2);

		Actions action = new Actions(driver);
		for (int i = 1; i < 5; i++) {
			action.moveToElement(driver.findElement(By.xpath(xpath_star + i + "]"))).perform();
		}
		action.moveToElement(driver.findElement(By.xpath(xpath_star5))).click().perform();

		action.moveToElement(driver.findElement(By.xpath(xpath_dropdown))).click().perform();

		List<WebElement> options = driver.findElements(By.xpath(xpath_dropdownlist));
		for (int i = 0; i < options.size(); i++) {
			String optionName = options.get(i).getText();
			if (optionName.equals(policyOption)) {
				options.get(i).click();
				break;
			}
		}

		driver.findElement(By.xpath(xpath_reviewTextField)).sendKeys(reviewMessage);
		driver.findElement(By.xpath(xpath_submitBtn)).click();

		String xpath_confirmPost = "//*[@id=\"scroller\"]/main/div/div/div[1]/h4";
		action.moveToElement(driver.findElement(By.xpath(xpath_confirmPost))).perform();
		String confirmMessage = driver.findElement(By.xpath(xpath_confirmPost)).getText();
		System.out.println(confirmMessage);
		Assert.assertTrue(confirmMessage, true);

		String css_reviewText = ".review-first p";
		String xpath_activityTab = "//*[@id=\"wh-body-inner\"]/div[1]/div[1]/div[2]/ul/li[2]/a";
		String xpath_reviewsTab = "//*[@id=\"wh-body-inner\"]/div[1]/div[1]/div[2]/ul/li[3]/a";

		driver.navigate().to("https://wallethub.com/profile/udaytest695/reviews/");

		// The posted review is not reflecting here, so switching tabs.
		driver.findElement(By.xpath(xpath_activityTab)).click();
		driver.findElement(By.xpath(xpath_reviewsTab)).click();

		String reviewMessageText = driver.findElement(By.cssSelector(css_reviewText)).getText();
		System.out.println(reviewMessageText);
		Assert.assertTrue(reviewMessage, true);

	}

}