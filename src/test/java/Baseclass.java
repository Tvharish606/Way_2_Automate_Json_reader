import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import io.github.bonigarcia.wdm.WebDriverManager;

public class Baseclass 
{

	WebDriver driver;
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
	public void data_filling(String data) throws InterruptedException
	{
		String users[]=data.split(","); //split the recieved data and add to array
		driver.get("https://www.way2automation.com/way2auto_jquery/registration.php#load_box");
		driver.findElement(By.xpath("(//input[@name='name'])[1]")).sendKeys(users[1]); // utilized the data from the array
		Thread.sleep(05000);
		driver.findElement(By.xpath("(//input[@type='text'])[2]")).sendKeys(users[0]);// utilized the data from the array
		Thread.sleep(5000);
		
	}
	@DataProvider(name = "Data_Filling")
	public String[] readfile() throws IOException, ParseException
	{
		JSONParser jsonparse=new JSONParser();  //convert file to text format
		FileReader reader=new FileReader(".\\Json_File\\form_file_data.json"); //read the file format
	Object obj	=(Object)jsonparse.parse(reader);  //convert file to text format 
	JSONObject user_details=(JSONObject) obj; //convert text format to json object 
	JSONArray user_array=(JSONArray)user_details.get("form_filling_data"); //convert to json array
	String arr[]=new String[user_array.size()];
	for(int i=0;i<user_array.size();i++)
	{
		JSONObject user_data = (JSONObject)user_array.get(i); //picking individual json array
		String first_name = (String)user_data.get("First_name"); //pick data in json array
		String last_name=(String)user_data.get("Last_Name");
		arr[i]=first_name+","+last_name; //store the data in java Array
		
	}
	return arr;
	
	}
	
}
