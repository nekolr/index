package com.nekolr.index.selenium;

import com.nekolr.index.util.JsonUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 百度搜索指数模拟登录以及抓取数据
 *
 * @author nekolr
 */
@Slf4j
public class BaiDuIndex {

    /**
     * firefox 驱动属性名
     */
    private static final String FIREFOX_DRIVER_PROPERTY = "webdriver.gecko.driver";

    /**
     * chrome 驱动属性名
     */
    private static final String CHROME_DRIVER_PROPERTY = "webdriver.chrome.driver";

    /**
     * 浏览器 bin 所在目录
     */
    private static final String FIREFOX_BINARY_PROPERTY = "webdriver.firefox.bin";

    /**
     * chrome bin 所在目录
     */
    private static final String CHROME_BINARY_PROPERTY = "webdriver.chrome.bin";

    /**
     * Firefox 驱动 URL
     */
    private static final String FIREFOX_DRIVER_URL = "D:/code/webDriver/geckodriver.exe";

    /**
     * chrome 驱动 URL
     */
    private static final String CHROME_DRIVER_URL = "D:/code/webDriver/chromedriver.exe";

    /**
     * firefox 可执行文件 URL
     */
    private static final String FIREFOX_BINARY_URL = "D:/Program Files/Mozilla Firefox/firefox.exe";

    /**
     * chrome 可执行文件 URL
     */
    private static final String CHROME_BINARY_URL = "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe";

    /**
     * 获取所有指数接口前缀
     */
    private static final String GET_ALL_INDEX_PREFIX = "http://index.baidu.com/Interface/Search/getAllIndex/";

    /**
     * 主页面
     */
    private static final String MAIN_PAGE = "http://index.baidu.com";

    /**
     * 驱动
     */
    private static WebDriver driver;

    /**
     * 任务线程池
     */
    @Getter
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    static {

        // 设置驱动环境变量
        System.setProperty(CHROME_DRIVER_PROPERTY, CHROME_DRIVER_URL);
        // 设置浏览器可执行文件环境变量
        System.setProperty(CHROME_BINARY_PROPERTY, CHROME_BINARY_URL);

        driver = new ChromeDriver();
    }

    /**
     * 模拟搜索，逻辑中先登录
     *
     * @throws InterruptedException
     */
    public static void mockSearch() {
        driver.get(MAIN_PAGE);
        // 页面加载超时时间全局设置
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        try {
            // 选择登录按钮，需要主动点击才会加载登录元素
            WebElement loginClickElement = ((ChromeDriver) driver).findElementByXPath("/html/body/div[1]/div[1]/div[2]/div[1]/div[4]/span/span");
            // 点击加载登录元素
            loginClickElement.click();
            // 由于登录元素中的 input 的 id 全部是自动生成的，每次都会变化，因此只能从不变的 DOM 一级级往下寻找
            WebElement container = ((ChromeDriver) driver).findElementByXPath("//*[@id=\"passport-container\"]");
            // 获取登录表单，此处可以超时获取
            WebElement form = new WebDriverWait(driver, 5).until(f -> container.findElement(By.tagName("FORM")));
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
                // 获取验证码的父节点的 display
                String display = form.findElement(By.name("verifyCode")).findElement(By.xpath("..")).getCssValue("display");
                if (!"none".equals(display)) {
                    // 处理验证码
                    processVerifyCode();
                }
            } catch (WebDriverException var1) {
                log.info("页面中不需要填写验证码");
            }

            // 表单提交后处理逻辑
            submitPostProcess();

        } catch (WebDriverException var2) {
            throw new IllegalArgumentException("获取元素出错");
        }
    }

    /**
     * 处理验证码
     */
    private static void processVerifyCode() {
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
     */
    private static void submitPostProcess() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("页面是否需要验证手机号或邮箱（y/n）：");
        for (; ; ) {
            String line = scanner.nextLine();
            if ("y".equalsIgnoreCase(line)) {
                System.out.println("页面需要验证手机号或邮箱，填写短信验证码后，按回车继续：");
                continue;
            } else if ("\r\n".equals(line)) {
                break;
            } else {
                break;
            }
        }
        // 执行搜索
        executeSearch();
    }

    /**
     * 执行搜索
     */
    private static void executeSearch() {
        // 搜索输入框
        WebElement searchInputText = ((ChromeDriver) driver).findElementByXPath("/html/body/div/div[2]/div[2]/div/div[1]/div/div[2]/form/input[3]");
        // 搜索按钮
        WebElement searchInputButton = ((ChromeDriver) driver).findElementByXPath("/html/body/div/div[2]/div[2]/div/div[1]/div/div[2]/div/span/span");
        // 输入值
        searchInputText.sendKeys("北京时装周");
        // 模拟点击
        searchInputButton.click();
    }

    /**
     * 访问获取所有指数的接口
     */
    public static Map<String, Object> getAllIndex() throws InterruptedException {
        // 获取接口需要携带的参数，等待页面加载
        String res = new WebDriverWait(driver, 10L).until(f -> (String) ((ChromeDriver) driver).executeScript("return PPval.ppt"));
        String res2 = new WebDriverWait(driver, 5L).until(f -> (String) ((ChromeDriver) driver).executeScript("return PPval.res2"));
        String url = GET_ALL_INDEX_PREFIX + "?res=" + res + "&res2=" + res2;
        // 主页
        String main_page = driver.getWindowHandle();
        // 新标签页
        ((ChromeDriver) driver).executeScript("window.open('" + url + "')");
        // 等待页面加载，根据需要调整
        TimeUnit.SECONDS.sleep(5L);
        // 获取所有窗口句柄
        Set<String> handles = driver.getWindowHandles();
        // 切换到新标签页
        for (String handle : handles) {
            System.out.println(handle);
            if (!main_page.equals(handle)) {
                // 定位到接口结果页面
                driver.switchTo().window(handle);
            }
        }
        // 接口数据在 body 中
        WebElement body = new WebDriverWait(driver, 5L).until(f -> ((ChromeDriver) driver).findElementByTagName("body"));
        String jsonText = body.getText();
        return JsonUtils.readObject(jsonText);
    }

    /**
     * 执行获取所有指数的接口
     *
     * @return
     */
    public static Map<String, Object> executeGetAllIndex() {
        FutureTask<Map<String, Object>> task = new FutureTask<>(() -> getAllIndex());
        BaiDuIndex.getExecutor().submit(task);
        try {
            return task.get();
        } catch (InterruptedException e) {
            log.error("执行获取所有指数接口线程被中断", e);
        } catch (ExecutionException e) {
            log.error("执行发生错误", e);
        }
        return null;
    }

    public static void main(String[] args) {
        // 登录、搜索
        BaiDuIndex.mockSearch();

        Map<String, Object> allIndex = BaiDuIndex.executeGetAllIndex();
    }
}
