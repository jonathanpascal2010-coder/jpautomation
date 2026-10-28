package id.co.juaracoding.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By heading = By.tagName("h1");
    private final By navbarUserInfo = By.cssSelector("[data-testid='navbar-user-info']");
    private final By sidebar = By.cssSelector("[data-testid='app-sidebar']");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getHeadingText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(heading)).getText();
    }

    public String getLoggedInUserName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(navbarUserInfo)).getText();
    }

    public boolean isSidebarVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(sidebar)).isDisplayed();
    }
}
