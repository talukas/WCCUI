package com.wsccui.xunit;

import org.testng.annotations.Test;
import com.wsccui.template.BaseXunitTest;
import com.wsccui.util.Util;
import core.util.BaseUtil;
import core.util.Override;
import ru.yandex.qatools.allure.annotations.Parameter;
import static org.hamcrest.MatcherAssert.*;
import static com.wsccui.util.Util.attachText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.core.Is.is;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.testng.annotations.DataProvider;


public class XunitBaseUtilsTest extends BaseXunitTest {
	@Parameter("Test case name")
	private String testCaseName;


	@DataProvider(name = "monthYearParsExpire", parallel = true)
	public Object[][] dp() {
		return new Object[][] {
			
			{ "TC2_January", "0117","January - 01", "2017" },
			{ "TC3_February", "0217","February - 02", "2017" },
			{ "TC4_March", "0317","March - 03", "2017" },
			{ "TC5_April", "0417","April - 04", "2017" },
			{ "TC6_May", "0517","May - 05", "2017" },
			{ "TC7_June", "0617","June - 06", "2017" },
			{ "TC8_July", "0717","July - 07", "2017" },
			{ "TC9_August", "0817","August - 08", "2017" },
			{ "TC10_September", "0917","September - 09", "2017" },
			{ "TC11_October", "1017","October - 10", "2017" },
			{ "TC12_November", "1117","November - 11", "2017" },
			{ "TC13_December", "1217","December - 12", "2017" },
			
			{ "TC14_January", "122","January - 01", "2022" },
			{ "TC15_February", "222","February - 02", "2022" },
			{ "TC16_March", "322","March - 03", "2022" },
			{ "TC17_April", "422","April - 04", "2022" },
			{ "TC17_May", "522","May - 05", "2022" },
			{ "TC18_June", "622","June - 06", "2022" },
			{ "TC19_July", "722","July - 07", "2022" },
			{ "TC20_August", "822","August - 08", "2022" },
			{ "TC21_September", "922","September - 09", "2022" },
			{ "TC22_October", "1022","October - 10", "2022" },
			{ "TC23_November", "1122","November - 11", "2022" },
			{ "TC24_December", "1222","December - 12", "2022" },
			
			{ "TC1_Null_Expire", null, "December - 12", "2025" }, 
			{ "TC25_Not_Correct_Month", "1322", "December - 12", "2022" },
			{ "TC26_Not_Correct_Year", "13224", "December - 12", "2025" },
			{ "TC27_Not_Correct_Expire", "12", "December - 12", "2025" },
			{ "TC28_Empty_Expire", new String(), "December - 12", "2025" },
			{ "TC29_Not_Correct_Expire", "1322453434543", "December - 12", "2025" },
			{ "TC30_Not_Correct_Expire", "abcerw", "December - 12", "2025" },
			{ "TC31_Not_Correct_Expire", "abcw", "December - 12", "2025" }
		};}
	
	@Override()
	@Test(dataProvider = "monthYearParsExpire")
	public void monthYearParsExpireTest01(final String... expire) {
		
		testCaseName = expire[0];
		Util baseUtil = new Util();
		String[] parsedExpire = baseUtil.monthYearParsExpire(expire[1]);
		String monthActual = parsedExpire[0];
		String yearActual = parsedExpire[1];
		
		String monthExpected = expire[2];
		String yearExpected = expire[3];
		
		attachText("monthActual", monthActual);
		attachText("yearActual", yearActual);
		
	assertThat(monthActual, describedAs("Expected not null month.", allOf(notNullValue(), is(monthExpected))));
	assertThat(yearActual, describedAs("Expected not null year.", allOf(notNullValue(), is(yearExpected))));
	
	}
	
	
	@DataProvider(name = "dpGetRespondeCode", parallel = true)
	public Object[][] dpGetRespondeCode() {

		return new Object[][] {		
			{ "TC1_StatusCode200", "http://ya.ru", "200" }, 
			{ "TC2_StatusCode404", "http://ya.ru/bla", "404" },
			{ "TC3_StatusCode0", "http://my.ru", "0" },
			
			/*Incorrect URI or Null or Empty*/
			{ "TC4_StatusCode0", null, "0" },
			{ "TC5_StatusCode0", " ", "0" },
			{ "TC6_StatusCode0", "http:my.ru", "0" },
			
			/*SSL*/
			{ "TC7_StatusCode200", "https://ya.ru", "200" }, 
			{ "TC8_StatusCode404", "https://ya.ru/bla", "404" },
			
			/*SSL Certificates*/
			{ "TC9_SSL_Expired", "https://expired.badssl.com/", "200" }, 
			{ "TC10_SSL_WrongHost", "https://wrong.host.badssl.com/", "200" },
			
			{ "TC11_SSL_Expired", "https://expired.badssl.com/", "200" }, 
			{ "TC12_SSL_WrongHost", "https://wrong.host.badssl.com/", "200" },
			{ "TC13_SSL_Expired", "https://expired.badssl.com/", "200" }, 
			{ "TC14_SSL_SelfSigned", "https://self-signed.badssl.com/", "200" },
			{ "TC15_SSL_UnTrustedRoot", "https://untrusted-root.badssl.com/", "200" },
			{ "TC16_SSL_Revoked", "https://revoked.badssl.com/", "200" },
			{ "TC17_SSL_PinningTest", "https://pinning-test.badssl.com/", "200" }

		};}
	
	@Override()
	@Test(dataProvider = "dpGetRespondeCode")
	//, expectedExceptions = IllegalArgumentException.class
	public void getRespondeCodeTest02(final String... param) throws KeyManagementException, NoSuchAlgorithmException {
		
		testCaseName = param[0];
		BaseUtil baseUtil = new BaseUtil();
		
		int respondeCodeActual = baseUtil.getRespondeCode(param[1]);
		int respondeCodeExpected = Integer.parseInt(param[2]);
		
		attachText("STATUS CODE", "URI ["+param[1]+"] -> Status code ["+respondeCodeActual+"]");
		
		assertThat(respondeCodeActual, describedAs("Expected HTTP Status code ["+respondeCodeExpected+"] - OK", is(respondeCodeExpected)));
	}
	
	
	//3 -> American Express -> 371449635398431,
	//5 -> VISA -> 4788250000028291,
	//7 -> MasterCard -> 5454545454545454,
	//8 -> 5856373196899715,
	//9 -> DISCOVERY -> 6011000995500000."
	@DataProvider(name = "amountWithCents", parallel = true)
		public Object[][] dpAmountWithCents() {
			return new Object[][] {
				
				//{ "Test case name", "Total Amount","ExpectedResul - boolean"},
				{ "TC00_AMEX", "11", "3", "true"},
				{ "TC01_AMEX", "11.0", "3", "true"},
				{ "TC02_AMEX", "11.01", "3", "false"},
				{ "TC03_AMEX", "11.02", "3", "false"},
				{ "TC04_AMEX", "11.03", "3", "false"},
				{ "TC05_AMEX", "11.04", "3", "false"},
				{ "TC06_AMEX", "11.05", "3", "false"},
				{ "TC07_AMEX", "11.06", "3", "false"},
				{ "TC08_AMEX", "11.07", "3", "false"},
				{ "TC09_AMEX", "11.08", "3", "false"},
				{ "TC10_AMEX", "11.09", "3", "false"},
				
				{ "TC11_AMEX", "11.1", "3", "false"},
				{ "TC11_A_AMEX", "11.10", "3", "false"},
				{ "TC12_AMEX", "11.11", "3", "false"},
				{ "TC13_AMEX", "11.12", "3", "false"},
				{ "TC14_AMEX", "11.13", "3", "false"},
				{ "TC15_AMEX", "11.14", "3", "false"},
				{ "TC16_AMEX", "11.15", "3", "false"},
				{ "TC17_AMEX", "11.16", "3", "false"},
				{ "TC18_AMEX", "11.17", "3", "false"},
				{ "TC19_AMEX", "11.18", "3", "false"},
				{ "TC20_AMEX", "11.19", "3", "false"},
				
				{ "TC21_AMEX", "11.2", "3", "false"},
				{ "TC21_A_AMEX", "11.21", "3", "false"},
				{ "TC22_AMEX", "11.22", "3", "false"},
				{ "TC23_AMEX", "11.23", "3", "false"},
				{ "TC24_AMEX", "11.24", "3", "false"},
				{ "TC25_AMEX", "11.25", "3", "false"},
				{ "TC26_AMEX", "11.26", "3", "true"},
				{ "TC27_AMEX", "11.27", "3", "true"},
				{ "TC28_AMEX", "11.28", "3", "true"},
				{ "TC29_AMEX", "11.29", "3", "true"},
				{ "TC30_AMEX", "11.3", "3", "true"},
				
				{ "TC30_A_AMEX", "11.30", "3", "true"},
				{ "TC31_AMEX", "11.31", "3", "true"},
				{ "TC32_AMEX", "11.32", "3", "true"},
				{ "TC33_AMEX", "11.33", "3", "true"},
				{ "TC34_AMEX", "11.34", "3", "true"},
				{ "TC35_AMEX", "11.35", "3", "true"},
				{ "TC36_AMEX", "11.36", "3", "true"},
				{ "TC37_AMEX", "11.37", "3", "true"},
				{ "TC38_AMEX", "11.38", "3", "true"},
				{ "TC39_AMEX", "11.39", "3", "true"},
				{ "TC40_AMEX", "11.40", "3", "true"},
				
				{ "TC40_A_AMEX", "11.4", "3", "true"},
				{ "TC41_AMEX", "11.41", "3", "true"},
				{ "TC42_AMEX", "11.42", "3", "true"},
				{ "TC43_AMEX", "11.43", "3", "true"},
				{ "TC44_AMEX", "11.44", "3", "true"},
				{ "TC45_AMEX", "11.45", "3", "true"},
				{ "TC46_AMEX", "11.46", "3", "true"},
				{ "TC47_AMEX", "11.47", "3", "true"},
				{ "TC48_AMEX", "11.48", "3", "true"},
				{ "TC49_AMEX", "11.49", "3", "true"},
				{ "TC50_AMEX", "11.50", "3", "true"},
				
				{ "TC50_A_AMEX", "11.5", "3", "true"},
				{ "TC51_AMEX", "11.51", "3", "true"},
				{ "TC52_AMEX", "11.52", "3", "true"},
				{ "TC53_AMEX", "11.53", "3", "true"},
				{ "TC54_AMEX", "11.54", "3", "true"},
				{ "TC55_AMEX", "11.55", "3", "true"},
				{ "TC56_AMEX", "11.56", "3", "true"},
				{ "TC57_AMEX", "11.57", "3", "true"},
				{ "TC58_AMEX", "11.58", "3", "true"},
				{ "TC59_AMEX", "11.59", "3", "false"},
				{ "TC60_AMEX", "11.60", "3", "true"},
				
				{ "TC60_A_AMEX", "11.6", "3", "true"},
				{ "TC61_AMEX", "11.61", "3", "true"},
				{ "TC62_AMEX", "11.62", "3", "true"},
				{ "TC63_AMEX", "11.63", "3", "true"},
				{ "TC64_AMEX", "11.64", "3", "true"},
				{ "TC65_AMEX", "11.65", "3", "true"},
				{ "TC66_AMEX", "11.66", "3", "true"},
				{ "TC67_AMEX", "11.67", "3", "true"},
				{ "TC68_AMEX", "11.68", "3", "true"},
				{ "TC69_AMEX", "11.69", "3", "true"},
				{ "TC70_AMEX", "11.70", "3", "true"},
				
				{ "TC70_A_AMEX", "11.7", "3", "true"},
				{ "TC71_AMEX", "11.71", "3", "true"},
				{ "TC72_AMEX", "11.72", "3", "true"},
				{ "TC73_AMEX", "11.73", "3", "true"},
				{ "TC74_AMEX", "11.74", "3", "true"},
				{ "TC75_AMEX", "11.75", "3", "true"},
				{ "TC76_AMEX", "11.76", "3", "true"},
				{ "TC77_AMEX", "11.77", "3", "true"},
				{ "TC78_AMEX", "11.78", "3", "true"},
				{ "TC79_AMEX", "11.79", "3", "true"},
				{ "TC80_AMEX", "11.80", "3", "true"},
				
				{ "TC80_A_AMEX", "11.8", "3", "true"},
				{ "TC81_AMEX", "11.81", "3", "true"},
				{ "TC82_AMEX", "11.82", "3", "true"},
				{ "TC83_AMEX", "11.83", "3", "true"},
				{ "TC84_AMEX", "11.84", "3", "true"},
				{ "TC85_AMEX", "11.85", "3", "true"},
				{ "TC86_AMEX", "11.86", "3", "true"},
				{ "TC87_AMEX", "11.87", "3", "true"},
				{ "TC88_AMEX", "11.88", "3", "true"},
				{ "TC89_AMEX", "11.89", "3", "true"},
				{ "TC90_AMEX", "11.90", "3", "true"},
				
				{ "TC90_A_AMEX", "11.9", "3", "true"},
				{ "TC91_AMEX", "11.91", "3", "true"},
				{ "TC92_AMEX", "11.92", "3", "false"},
				{ "TC93_AMEX", "11.93", "3", "false"},
				{ "TC94_AMEX", "11.94", "3", "false"},
				{ "TC95_AMEX", "11.95", "3", "true"},
				{ "TC96_AMEX", "11.96", "3", "true"},
				{ "TC97_AMEX", "11.97", "3", "false"},
				{ "TC98_AMEX", "11.98", "3", "false"},
				{ "TC99_AMEX", "11.99", "3", "false"},
				
				{ "TC100_VISA", "11", "5", "true"},
				{ "TC101_VISA", "11.0", "5", "true"},
				{ "TC102_VISA", "11.01", "5", "false"},
				{ "TC103_VISA", "11.02", "5", "false"},
				{ "TC104_VISA", "11.03", "5", "false"},
				{ "TC105_VISA", "11.04", "5", "false"},
				{ "TC106_VISA", "11.05", "5", "false"},
				{ "TC107_VISA", "11.06", "5", "false"},
				{ "TC108_VISA", "11.07", "5", "false"},
				{ "TC109_VISA", "11.08", "5", "false"},
				{ "TC110_VISA", "11.09", "5", "false"},
				
				{ "TC111_VISA", "11.1", "5", "false"},
				{ "TC111_A_VISA", "11.10", "5", "false"},
				{ "TC112_VISA", "11.11", "5", "false"},
				{ "TC113_VISA", "11.12", "5", "false"},
				{ "TC114_VISA", "11.13", "5", "false"},
				{ "TC115_VISA", "11.14", "5", "false"},
				{ "TC116_VISA", "11.15", "5", "false"},
				{ "TC117_VISA", "11.16", "5", "false"},
				{ "TC118_VISA", "11.17", "5", "false"},
				{ "TC119_VISA", "11.18", "5", "false"},
				{ "TC120_VISA", "11.19", "5", "false"},
				
				{ "TC121_VISA", "11.2", "5", "false"},
				{ "TC121_A_VISA", "11.21", "5", "false"},
				{ "TC122_VISA", "11.22", "5", "false"},
				{ "TC123_VISA", "11.23", "5", "false"},
				{ "TC124_VISA", "11.24", "5", "false"},
				{ "TC125_VISA", "11.25", "5", "false"},
				{ "TC126_VISA", "11.26", "5", "true"},
				{ "TC127_VISA", "11.27", "5", "true"},
				{ "TC128_VISA", "11.28", "5", "true"},
				{ "TC129_VISA", "11.29", "5", "true"},
				{ "TC130_VISA", "11.3", "5", "true"},
				
				{ "TC130_A_VISA", "11.30", "5", "true"},
				{ "TC131_VISA", "11.31", "5", "true"},
				{ "TC132_VISA", "11.32", "5", "true"},
				{ "TC133_VISA", "11.33", "5", "true"},
				{ "TC134_VISA", "11.34", "5", "true"},
				{ "TC135_VISA", "11.35", "5", "true"},
				{ "TC136_VISA", "11.36", "5", "true"},
				{ "TC137_VISA", "11.37", "5", "true"},
				{ "TC138_VISA", "11.38", "5", "true"},
				{ "TC139_VISA", "11.39", "5", "true"},
				{ "TC140_VISA", "11.40", "5", "true"},
				
				{ "TC140_A_VISA", "11.4", "5", "true"},
				{ "TC141_VISA", "11.41", "5", "true"},
				{ "TC142_VISA", "11.42", "5", "true"},
				{ "TC143_VISA", "11.43", "5", "true"},
				{ "TC144_VISA", "11.44", "5", "true"},
				{ "TC145_VISA", "11.45", "5", "true"},
				{ "TC146_VISA", "11.46", "5", "true"},
				{ "TC147_VISA", "11.47", "5", "true"},
				{ "TC148_VISA", "11.48", "5", "true"},
				{ "TC149_VISA", "11.49", "5", "true"},
				{ "TC150_VISA", "11.50", "5", "true"},
				
				{ "TC150_A_VISA", "11.5", "5", "true"},
				{ "TC151_VISA", "11.51", "5", "true"},
				{ "TC152_VISA", "11.52", "5", "true"},
				{ "TC153_VISA", "11.53", "5", "true"},
				{ "TC154_VISA", "11.54", "5", "true"},
				{ "TC155_VISA", "11.55", "5", "true"},
				{ "TC156_VISA", "11.56", "5", "true"},
				{ "TC157_VISA", "11.57", "5", "true"},
				{ "TC158_VISA", "11.58", "5", "true"},
				{ "TC159_VISA", "11.59", "5", "false"},
				{ "TC160_VISA", "11.60", "5", "true"},
				
				{ "TC160_A_VISA", "11.6", "5", "true"},
				{ "TC161_VISA", "11.61", "5", "true"},
				{ "TC162_VISA", "11.62", "5", "true"},
				{ "TC163_VISA", "11.63", "5", "true"},
				{ "TC164_VISA", "11.64", "5", "true"},
				{ "TC165_VISA", "11.65", "5", "true"},
				{ "TC166_VISA", "11.66", "5", "true"},
				{ "TC167_VISA", "11.67", "5", "true"},
				{ "TC168_VISA", "11.68", "5", "true"},
				{ "TC169_VISA", "11.69", "5", "true"},
				{ "TC170_VISA", "11.70", "5", "true"},
				
				{ "TC170_A_VISA", "11.7", "5", "true"},
				{ "TC171_VISA", "11.71", "5", "true"},
				{ "TC172_VISA", "11.72", "5", "true"},
				{ "TC173_VISA", "11.73", "5", "true"},
				{ "TC174_VISA", "11.74", "5", "true"},
				{ "TC175_VISA", "11.75", "5", "true"},
				{ "TC176_VISA", "11.76", "5", "true"},
				{ "TC177_VISA", "11.77", "5", "true"},
				{ "TC178_VISA", "11.78", "5", "true"},
				{ "TC179_VISA", "11.79", "5", "true"},
				{ "TC180_VISA", "11.80", "5", "true"},
				
				{ "TC180_A_VISA", "11.8", "5", "true"},
				{ "TC181_VISA", "11.81", "5", "true"},
				{ "TC182_VISA", "11.82", "5", "true"},
				{ "TC183_VISA", "11.83", "5", "true"},
				{ "TC184_VISA", "11.84", "5", "true"},
				{ "TC185_VISA", "11.85", "5", "true"},
				{ "TC186_VISA", "11.86", "5", "true"},
				{ "TC187_VISA", "11.87", "5", "true"},
				{ "TC188_VISA", "11.88", "5", "true"},
				{ "TC189_VISA", "11.89", "5", "true"},
				{ "TC190_VISA", "11.90", "5", "true"},
				
				{ "TC190_A_VISA", "11.9", "5", "true"},
				{ "TC191_VISA", "11.91", "5", "true"},
				{ "TC192_VISA", "11.92", "5", "false"},
				{ "TC193_VISA", "11.93", "5", "false"},
				{ "TC194_VISA", "11.94", "5", "false"},
				{ "TC195_VISA", "11.95", "5", "true"},
				{ "TC196_VISA", "11.96", "5", "true"},
				{ "TC197_VISA", "11.97", "5", "false"},
				{ "TC198_VISA", "11.98", "5", "false"},
				{ "TC199_VISA", "11.99", "5", "false"},
				
				{ "TC200_VISA", "1.69", "5", "false"},
				{ "TC201_VISA", "19.58", "5", "false"},
				
				{ "TC202_VISA", "1,000,000", "5", "false"},
				{ "TC203_VISA", "1,000,000.00", "5", "false"},
				{ "TC204_VISA", "999,999.96", "5", "true"},
				
				{ "TC205_VISA", "98.26", "5", "false"},
				{ "TC206_VISA", "98.27", "5", "false"},
				{ "TC207_VISA", "98.28", "5", "false"},
				{ "TC208_VISA", "98.29", "5", "false"},
				{ "TC209_VISA", "98.3", "5", "false"},
				
				{ "TC210_A_VISA", "98.30", "5", "false"},
				{ "TC211_VISA", "98.31", "5", "false"},
				{ "TC212_VISA", "98.32", "5", "false"},
				{ "TC213_VISA", "98.33", "5", "false"},
				{ "TC214_VISA", "98.34", "5", "false"},
				{ "TC215_VISA", "98.35", "5", "false"},
				{ "TC216_VISA", "98.36", "5", "false"},
				{ "TC217_VISA", "98.37", "5", "false"},
				{ "TC218_VISA", "98.38", "5", "false"},
				{ "TC219_VISA", "98.39", "5", "false"},
				{ "TC220_VISA", "98.40", "5", "false"},
				
				{ "TC221_A_VISA", "98.4", "5", "false"},
				{ "TC222_VISA", "98.41", "5", "false"},
				{ "TC223_VISA", "98.42", "5", "false"},
				{ "TC224_VISA", "98.43", "5", "false"},
				{ "TC225_VISA", "98.44", "5", "false"},
				{ "TC226_VISA", "98.45", "5", "false"},
				{ "TC227_VISA", "98.46", "5", "false"},
				{ "TC228_VISA", "98.47", "5", "false"},
				{ "TC229_VISA", "98.48", "5", "false"},
				{ "TC230_VISA", "98.49", "5", "false"},

				{ "TC231_VISA", "98.50", "5", "false"},
				
				{ "TC232_A_VISA", "98.5", "5", "false"},
				{ "TC233_VISA", "98.51", "5", "false"},
				{ "TC234_VISA", "98.52", "5", "false"},
				{ "TC235_VISA", "98.53", "5", "false"},
				{ "TC236_VISA", "98.54", "5", "false"},
				{ "TC237_VISA", "98.55", "5", "false"},
				{ "TC238_VISA", "98.56", "5", "false"},
				{ "TC239_VISA", "98.57", "5", "false"},
				{ "TC240_VISA", "98.58", "5", "false"},
				{ "TC241_VISA", "98.59", "5", "false"},
				{ "TC242_VISA", "98.60", "5", "false"},
				
				{ "TC243_A_VISA", "98.6", "5", "false"},
				{ "TC244_VISA", "98.61", "5", "false"},
				{ "TC245_VISA", "98.62", "5", "false"},
				{ "TC246_VISA", "98.63", "5", "false"},
				{ "TC247_VISA", "98.64", "5", "false"},
				{ "TC248_VISA", "98.65", "5", "false"},
				{ "TC249_VISA", "98.66", "5", "false"},
				{ "TC250_VISA", "98.67", "5", "false"},
				{ "TC251_VISA", "98.68", "5", "false"},
				{ "TC252_VISA", "98.69", "5", "false"},
				{ "TC253_VISA", "98.70", "5", "false"},
				
				{ "TC254_A_VISA", "98.7", "5", "false"},
				{ "TC255_VISA", "98.71", "5", "false"},
				{ "TC256_VISA", "98.72", "5", "false"},
				{ "TC257_VISA", "98.73", "5", "false"},
				{ "TC258_VISA", "98.74", "5", "false"},
				{ "TC259_VISA", "98.75", "5", "false"},
				{ "TC260_VISA", "98.76", "5", "false"},
				{ "TC261_VISA", "98.77", "5", "false"},
				{ "TC262_VISA", "98.78", "5", "false"},
				{ "TC263_VISA", "98.79", "5", "false"},
				{ "TC264_VISA", "98.80", "5", "false"},
				
				{ "TC265_A_VISA", "98.8", "5", "false"},
				{ "TC266_VISA", "98.81", "5", "false"},
				{ "TC267_VISA", "98.82", "5", "false"},
				{ "TC268_VISA", "98.83", "5", "false"},
				{ "TC269_VISA", "98.84", "5", "false"},
				{ "TC270_VISA", "98.85", "5", "false"},
				{ "TC271_VISA", "98.86", "5", "false"},
				{ "TC272_VISA", "98.87", "5", "false"},
				{ "TC273_VISA", "98.88", "5", "false"},
				{ "TC274_VISA", "98.89", "5", "false"},
				{ "TC275_VISA", "98.90", "5", "false"},
				
				{ "TC276_A_VISA", "98.9", "5", "false"},
				{ "TC277_VISA", "98.91", "5", "false"},
				{ "TC278_VISA", "1.69", "3", "true"}
				
			};}
		
		@Override()
		@Test(dataProvider = "amountWithCents")
		public void isTotalAmountCorrectTest03(final String... totalAmount) {

			BaseUtil baseUtil = new BaseUtil();
			boolean actualResult = baseUtil.isTotalAmountCorrect(totalAmount[2], totalAmount[1]);
	        boolean expectedResult = Boolean.parseBoolean(totalAmount[3]);

			assertThat(actualResult, describedAs("Expected '"+expectedResult+"' 'Total amount' ["+totalAmount[1]+"]", is(expectedResult)));	
		}
}
