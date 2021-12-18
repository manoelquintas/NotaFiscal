package br.com.exaple.NotaFiscal;

import br.com.exaple.NotaFiscal.utils.AnyCaptchaText;
import br.com.exaple.NotaFiscal.utils.ConvertImageToBase64;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class Inicio extends ApiGoogleSheets {


    public void start() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://nfse.recife.pe.gov.br/senhaweb/login.aspx");

        driver.findElement(By.id("aviso-importante-fechar")).click();

        driver.findElement(By.cssSelector("[name$=CpfCnpj]")).sendKeys("28.578.895/0001-65");

        driver.findElement(By.cssSelector("[name$=tbSenha]")).sendKeys("28578895");

        String src = driver.findElement(By.cssSelector("[src*=CaptchaImage]")).getAttribute("src");

        Set<Cookie> cookies = driver.manage().getCookies();
        Map<String, String> mapCookies = new HashMap<String, String>();
        for (Cookie cookie : cookies) {
            mapCookies.put(cookie.getName(), cookie.getValue());
        }

        System.out.println(src);
        String captcha="";
        try {
            //Connection connection = Jsoup.connect(src);//.cookies(mapCookies);
            Connection.Response response =  Jsoup.connect(src)
                    .method(Connection.Method.GET)
                    .cookies(mapCookies)
                    .referrer("https://nfse.recife.pe.gov.br/senhaweb/login.aspx")
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .validateTLSCertificates(false)
                    .timeout(3 * 1000)
                    .execute();

            byte[] bufferedInputStream = response.bodyAsBytes();
            File imagem = new File("captcha.jpg");
            FileUtils.writeByteArrayToFile(imagem, bufferedInputStream);

            String base64 = ConvertImageToBase64.convert("captcha.jpg");

            AnyCaptchaText anyCaptchaText = new AnyCaptchaText();
            captcha = anyCaptchaText.imagemToText(base64);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver.findElement(By.cssSelector("[name$=ccCodigo]")).sendKeys(captcha);

        driver.findElement(By.cssSelector("[value=ENTRAR]")).click();

        driver.get("https://nfse.recife.pe.gov.br/contribuinte/nota.aspx");

        driver.findElement(By.cssSelector("[id=ctl00_cphCabMenu_tbCPFCNPJTomador]")).sendKeys("129.197.814-30");

        driver.findElement(By.cssSelector("[id=ctl00_cphCabMenu_btAvancar]")).click();

        driver.findElement(By.cssSelector("[id=ctl00_cphCabMenu_tbCEP]")).sendKeys("52120300");

        driver.findElement(By.cssSelector("[id=ctl00_cphCabMenu_btCEP]")).click();

        driver.findElement(By.cssSelector("[id=ctl00_cphCabMenu_btCEP]")).click();

        driver.findElement(By.cssSelector("[id=ctl00_cphCabMenu_ctrlServicos_tbAliquota]")).sendKeys("2.00");
    }
}
