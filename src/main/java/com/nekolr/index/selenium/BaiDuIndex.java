package com.nekolr.index.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 百度搜索指数模拟登录以及抓取数据
 *
 * @author nekolr
 */
public class BaiDuIndex {

    /**
     * firefox 驱动属性名
     */
    private static final String FIREFOX_DRIVER_PROPERTY = "webdriver.gecko.driver";

    /**
     * 浏览器 bin 所在目录
     */
    private static final String BROWSER_BINARY_PROPERTY = "webdriver.firefox.bin";

    /**
     * 主页面
     */
    private static final String MAIN_PAGE = "http://index.baidu.com";

    /**
     * 驱动 URL
     */
    private static final String DRIVER_URL = "D:/code/webDriver/geckodriver.exe";

    /**
     * 浏览器可执行文件 URL
     */
    private static final String BROWSER_BINARY_URL = "D:/Program Files/Mozilla Firefox/firefox.exe";

    /**
     * 模拟搜索，逻辑中先登录
     *
     * @throws InterruptedException
     */
    public void mockSearch() throws InterruptedException {

        // 设置环境变量
        System.setProperty(FIREFOX_DRIVER_PROPERTY, DRIVER_URL);
        System.setProperty(BROWSER_BINARY_PROPERTY, BROWSER_BINARY_URL);

        FirefoxDriver driver = new FirefoxDriver();
        driver.get(MAIN_PAGE);

        try {
            // 选择登录按钮，需要主动点击才会加载登录元素
            WebElement loginClickElement = driver.findElementByXPath("/html/body/div[1]/div[1]/div[2]/div[1]/div[4]/span/span");
            // 点击加载登录元素
            loginClickElement.click();
            // 由于登录元素中的 input 的 id 全部是自动生成的，每次都会变化，因此只能从不变的 DOM 一级级往下寻找
            WebElement container = driver.findElementByXPath("//*[@id=\"passport-container\"]");
            // 此处需要暂缓操作，时间看情况调整
            TimeUnit.SECONDS.sleep(5);
            // 获取登录表单
            WebElement form = container.findElement(By.tagName("FORM"));
            // 定位到账号输入框
            WebElement accountInputText = form.findElement(By.name("userName"));
            // 定位到密码输入框
            WebElement passwordInputPassword = form.findElement(By.name("password"));
            // 输入账号
            accountInputText.sendKeys("");
            // 输入密码
            passwordInputPassword.sendKeys("");
            // 提交表单
            form.submit();

            try {
                // 验证码
                form.findElement(By.name("verifyCode"));
                // 处理验证码
                processVerifyCode();

            } catch (WebDriverException var1) {
                System.out.println("页面中不需要填写验证码");
            }

            // 表单提交后处理逻辑
            submitPostProcess(driver);

        } catch (WebDriverException var2) {
            throw new IllegalArgumentException("获取元素出错");
        }
    }

    /**
     * 处理验证码
     */
    private void processVerifyCode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("页面需要输入验证码，填写完毕后，按回车继续：");
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if ("\r\n".equals(line)) {
                return;
            }
        }
    }

    /**
     * 提交表单后处理逻辑
     *
     * @param driver
     */
    private void submitPostProcess(FirefoxDriver driver) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("页面是否需要验证手机号（y/n）：");
        for (; ; ) {
            String line = scanner.nextLine();
            if ("y".equalsIgnoreCase(line)) {
                System.out.println("页面需要验证手机号，填写短信验证码后，按回车继续：");
                continue;
            } else if ("\r\n".equals(line)) {
                break;
            } else {
                break;
            }
        }
        // 执行搜索
        executeSearch(driver);
    }

    /**
     * 执行搜索
     *
     * @param driver
     */
    private void executeSearch(FirefoxDriver driver) {
        // 搜索输入框
        WebElement searchInputText = driver.findElementByXPath("/html/body/div/div[2]/div[2]/div/div[1]/div/div[2]/form/input[3]");
        // 搜索按钮
        WebElement searchInputButton = driver.findElementByXPath("/html/body/div/div[2]/div[2]/div/div[1]/div/div[2]/div/span/span");
        // 输入值
        searchInputText.sendKeys("北京时装周");
        // 模拟点击
        searchInputButton.click();
    }

    public static void main(String[] args) throws InterruptedException {
        BaiDuIndex baiDuIndex = new BaiDuIndex();
        baiDuIndex.mockSearch();
    }
}
