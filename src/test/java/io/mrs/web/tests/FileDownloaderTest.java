package io.mrs.web.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.mrs.web.WebBaseTest;
import io.mrs.web.utils.downloader.FileDownloader;
import io.mrs.web.utils.downloader.HashType;
import io.mrs.web.utils.downloader.RequestType;

import java.io.File;
import java.net.URI;

import static io.mrs.web.utils.downloader.CheckFileHash.generateHashForFileOfType;
import static org.assertj.core.api.Assertions.assertThat;

public class FileDownloaderTest extends WebBaseTest {

    @Test
    public void downloadAFile() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(getDriver());
        getDriver().get("http://web.masteringselenium.com/downloadTest.html");
        WebElement fileThatShouldExist = getDriver().findElement(By.id("fileToDownload"));
        URI fileAsURI = new URI(fileThatShouldExist.getAttribute("href"));

        downloadHandler.setURI(fileAsURI);
        downloadHandler.setHTTPRequestMethod(RequestType.GET);

        File downloadedFile = downloadHandler.downloadFile();

        assertThat(downloadedFile.exists()).isEqualTo(true);
        assertThat(downloadHandler.getLinkHTTPStatus()).isEqualTo(200);
    }

    @Test
    public void downloadAFileWhilstMimickingSeleniumCookiesAndCheckTheSHA1Hash() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(getDriver());
        getDriver().get("http://web.masteringselenium.com/downloadTest.html");
        WebElement fileThatShouldExist = getDriver().findElement(By.id("fileToDownload"));
        URI fileAsURI = new URI(fileThatShouldExist.getAttribute("href"));

        downloadHandler.setURI(fileAsURI);
        downloadHandler.setHTTPRequestMethod(RequestType.GET);
        File downloadedFile = downloadHandler.downloadFile();

        assertThat(downloadedFile.exists()).isEqualTo(true);
        assertThat(downloadHandler.getLinkHTTPStatus()).isEqualTo(200);
        assertThat(generateHashForFileOfType(downloadedFile, HashType.SHA1))
                .isEqualTo("8882e3d972be82e14a98c522745746a03b97997a");
    }

    @Test
    public void downloadAFileWhilstMimickingSeleniumCookiesAndCheckTheMD5Hash() throws Exception {
        FileDownloader downloadHandler = new FileDownloader(getDriver());
        getDriver().get("http://web.masteringselenium.com/downloadTest.html");
        WebElement fileThatShouldExist = getDriver().findElement(By.id("fileToDownload"));
        URI fileAsURI = new URI(fileThatShouldExist.getAttribute("href"));

        downloadHandler.setURI(fileAsURI);
        downloadHandler.setHTTPRequestMethod(RequestType.GET);
        File downloadedFile = downloadHandler.downloadFile();

        assertThat(downloadedFile.exists()).isEqualTo(true);
        assertThat(downloadHandler.getLinkHTTPStatus()).isEqualTo(200);
        assertThat(generateHashForFileOfType(downloadedFile, HashType.MD5))
                .isEqualTo("d1f296f523b74462b31b912a5675a814");
    }
}