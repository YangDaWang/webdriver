package com.yxy.webdriver.chrome.damai;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

/**
 * @Author XinYu Yang
 * @date 2020/12/18  10:36 上午
 */
public class ChromeWebDriver {

    public static final String DRIVER_PATH = "";
    public static final String USER_NAME = "";
    public static final String PASSWORD = "";

    public static void main(String[] args) throws InterruptedException {

        /*
        设置系统参数webdriver.chrome.driver， 指定chromedriver路径, 下载地址：https://chromedriver.chromium.org/downloads ;
        注：chrome driver版本要和chrome版本对应
         */
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);

        ChromeDriver driver = buildDriver();

        try {
            //启动大麦的登录界面
            driver.get("https://passport.damai.cn/login");

            //找到对应的登录frame
            WebDriver loginFram = setUserNameAndPassWord(driver);

            //处理滑动框
            slidingBox(driver);

            //点击登录页面
            WebElement loginButton = loginFram
                    .findElement(By.tagName("button"));
            loginButton.click();

            //登录到主页面
            driver.get("https://www.damai.cn/");

            //拿到搜索框
            WebElement search = driver.findElement(By.className("input-search"));
            search.sendKeys("动物视界");

            //点击搜索
            WebElement searchButton = loginFram.findElement(By.className("btn-search"));
            searchButton.click();
            Thread.sleep(20000000);
        } finally {
            driver.quit();
        }
    }

    private static void slidingBox(ChromeDriver driver) {
        Actions move = new Actions(driver);
        for (int i = 0; i < 2; i++) {
            move.moveByOffset(100, 0);
        }
        move.moveByOffset(58, 0);
        System.out.println("start move");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置用户名和密码
     */
    private static WebDriver setUserNameAndPassWord(ChromeDriver driver) {
        WebDriver loginFram = driver.switchTo().frame("alibaba-login-box");
        WebElement userName = loginFram.findElement(By.id("fm-login-id"));
        userName.sendKeys(USER_NAME);
        WebElement password = loginFram.findElement(By.id("fm-login-password"));
        password.sendKeys(PASSWORD);
        return loginFram;
    }

    private static ChromeDriver buildDriver() {
        ChromeOptions chromeOptions = buildOptions();
        ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
        setParam(chromeDriver);
        return chromeDriver;
    }

    /**
     * 隐藏window.navigator.webdriver
     */
    private static void setParam(ChromeDriver chromeDriver) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source",
                "Object.defineProperty(navigator, 'webdriver', { get: () => undefined })");
        chromeDriver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);
    }

    /**
     * 设置免检测（开发者模式）
     */
    private static ChromeOptions buildOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches",
                Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        return options;
    }

}
