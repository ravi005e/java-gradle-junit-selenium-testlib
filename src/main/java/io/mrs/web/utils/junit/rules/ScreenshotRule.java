package io.mrs.web.utils.junit.rules;

import static io.mrs.web.WebBaseTest.getDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.time.Instant;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


public class ScreenshotRule implements TestWatcher {

  private static final Pattern PATTERN_ILLEGAL_SCREENSHOT_CHARS = Pattern.compile("[^\\w#]+");
  private static final int MAX_FILE_NAME_LENGTH = 240;
  private static String fileSeperator = System.getProperty("file.separator");
  protected WebDriver driver;
  
  @Override
  public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
    String testMethodName = extensionContext.getTestMethod().toString().trim();
    String[] tokens = testMethodName.split("\\(")[0].split("\\.");
    String token = tokens[tokens.length-1];
    
    System.out.println("***** Error " + token + " test has failed *****");

    String testClassName = extensionContext.getTestClass().toString().trim();
    String[] bits = testClassName.split("\\.");
    String lastOne = bits[bits.length-1].replaceAll("]", "");
    
    Instant instant = Instant.now();
    String screenShotName = truncateLongName(sanitizeTestName(token +"_"+ instant.toString())) + ".png";
    try {
      if (getDriver() != null) {
        String imagePath = ".." + fileSeperator + "screenshots"
            + fileSeperator + "results" + fileSeperator + lastOne
            + fileSeperator
            + captureScreenshot(getDriver(), screenShotName, lastOne);
        System.out.println("Screenshot can be found : " + imagePath);
      }
    } catch (MalformedURLException e) {
      System.out.println("An exception occured while building image path " + e.getCause());
    }
  }
  
  private String captureScreenshot(WebDriver driver, String screenShotName, String method) {
      try {
         File localScreenshots = new File("screenshots" + fileSeperator + "results");
         if (!localScreenshots.exists()) {
           System.out.println("File created " + localScreenshots);
            localScreenshots.mkdirs();
         }
         
         File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
         File targetFile = new File("screenshots" + fileSeperator + "results" + fileSeperator + method, screenShotName);
         FileUtils.copyFile(screenshotFile, targetFile);
//       logger.info("Screenshot for class={} method={} saved in: {}", className, method, screenshot.getAbsolutePath());
         return screenShotName;
      } catch (Exception e1) {
//         logger.error("Unable to take screenshot", e1);
        return null;
      }
 }

  protected static int calculateMaxNameLength(String filename) {
    int maxNameLength = MAX_FILE_NAME_LENGTH - filename.length();
    maxNameLength -= fileSeperator.length();
    maxNameLength -= getScreenshotDirParent().length();
    if (maxNameLength < 0)
      maxNameLength = 0;
    return maxNameLength;
  }

  protected static String truncateLongName(String name) {
    int maxNameLength = calculateMaxNameLength(name);
    if (name.length() > maxNameLength) return name.substring(0, maxNameLength);
    else return name;
  }

  protected static String sanitizeTestName(String rawTestName) {
    return PATTERN_ILLEGAL_SCREENSHOT_CHARS.matcher(rawTestName).replaceAll("_");
  }

  protected static String getScreenshotDirParent() {
    return System.getProperty("functional-test.screenshots.dir", "target/screenshots");
  }
   
}

