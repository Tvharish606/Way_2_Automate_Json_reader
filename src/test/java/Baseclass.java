import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Baseclass 
{

	public static WebDriver driver;

	@BeforeClass
	public void Appliction_Start()
	{
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(20,TimeUnit.SECONDS);
	}
	@AfterClass
	public void tear_down() throws InterruptedException
	{
		Thread.sleep(2000);
		driver.quit();
	}
	@Test(dataProvider = "Data_Filling")
	public void data_filling(String data) throws InterruptedException, AWTException
		{
		String users[]=data.split(","); //split the recieved data and add to array
		driver.get("https://www.way2automation.com/way2auto_jquery/registration.php#load_box");
		WebElement first_name = driver.findElement(By.xpath("(//input[@name='name'])[1]"));
		first_name.sendKeys(users[0]); // utilized the data from the array
		driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(users[1]);// utilized the data from the array
		driver.findElement(By.xpath("(//input[@type='radio'])[1]")).click();
		driver.findElement(By.xpath("(//input[@name='phone'])[1]")).sendKeys(users[2]);
		driver.findElement(By.xpath("(//input[@name='username'])[1]")).sendKeys(users[3]);
		driver.findElement(By.xpath("(//input[@name='email'])[1]")).sendKeys(users[4]);
		Thread.sleep(3000);
		WebElement fileuploader = driver.findElement(By.xpath("//input[@type='file']"));
		Actions act=new Actions(driver);
		act.moveToElement(fileuploader).click().perform();
		Robot robot=new Robot();
		StringSelection file_path=new StringSelection(users[5]);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(file_path,null);
		Thread.sleep(3000);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(5000);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		driver.findElement(By.xpath("//textarea[@name]")).sendKeys(users[6]);
		driver.findElement(By.xpath("(//input[@type='password'])[1]")).sendKeys(users[7]);
		driver.findElement(By.xpath("//input[@name='c_password']")).sendKeys(users[8]);
		driver.findElement(By.xpath("(//input[@type='submit'])[1]")).click();
		Thread.sleep(2000);
		if(driver.getPageSource().contains("This field is required"))
		{
			Assert.assertTrue(true);
			System.out.println("validated");
			
		}
		else
		{
			Assert.assertTrue(false);
		}
		}
	@DataProvider(name = "Data_Filling")
	public String[] readfile() throws IOException, ParseException
	{
		 //convert file to text format
		FileReader reader=new FileReader(".\\Json_File\\form_file_data.json"); //read the file format
		JSONParser jsonparse=new JSONParser(); 
		Object obj	=(Object)jsonparse.parse(reader);  //convert file to json_object 
		JSONObject user_details=(JSONObject) obj; //convert text format to json object 
		JSONArray user_array=(JSONArray)user_details.get("form_filling_data"); //convert to json array
		String arr[]=new String[user_array.size()];
		for(int i=0;i<user_array.size();i++)
		{
			JSONObject user_data = (JSONObject)user_array.get(i); //picking individual json array
			String first_name = (String)user_data.get("First_name"); //pick data in json array
			String last_name=(String)user_data.get("Last_Name");
			String phone_number=(String)user_data.get("phone_no");
			String Username=(String)user_data.get("username");
			String email=(String)user_data.get("Email");
			String path=(String)user_data.get("user_profile_upload");
			String about=(String)user_data.get("About_yourself");
			String pass=(String)user_data.get("Password");
			String confirm_pass=(String)user_data.get("confirm_password");
			arr[i]=first_name+","+last_name+","+phone_number+","+Username+","+email+","+path+","+about+","+pass+","+confirm_pass; //store the data in java Array
		}
		return arr;
	}
}
