package com.wsccui.util;

import org.apache.log4j.Logger;
import static core.util.ObjectSupplier.$;
import core.util.BaseUtil;
import ru.yandex.qatools.allure.annotations.Step;
///* This class contain utility methods for framework */
public class Util extends BaseUtil{

	static final Logger LOG = Logger.getLogger(Util.class.getName());


	@Step("Parsing CC expire from [{0}] to format [MM - Month, YYYY]")
	public String[] monthYearParsExpire(final String expire) {

		String[] parsedExpired = new String[2];
		// default month - December
		int monthExpireParsed = 12;
		String month = "December - 12";

		// default year - 2025
		String year = "2025";

		if (expire != null && !expire.isEmpty()) {
			// default Year - 2022
			// int yearExpireParsed = 22;
			String expireClean = expire.replaceAll("\\D", "");
			
			if (expireClean.length() == 3) {
				monthExpireParsed = Integer.parseInt(expireClean.substring(0, 1));
				year = "20" + expireClean.substring(1);
			}

			/*Correct length*/
			else if (expireClean.length() == 4) {
				monthExpireParsed = Integer.parseInt(expireClean.substring(0, 2));
				year = "20" + expireClean.substring(2);
			} 
			else{
				String msg = "Unknown Expired [" + expireClean + "], will use default Expire ["+month+", "+year+"]";
				LOG.error(msg);	
			}

			switch (monthExpireParsed) {
			case 1:
				month = "January - 01";
				break;

			case 2:
				month = "February - 02";
				break;

			case 3:
				month = "March - 03";
				break;

			case 4:
				month = "April - 04";
				break;

			case 5:
				month = "May - 05";
				break;

			case 6:
				month = "June - 06";
				break;

			case 7:
				month = "July - 07";
				break;

			case 8:
				month = "August - 08";
				break;

			case 9:
				month = "September - 09";
				break;

			case 10:
				month = "October - 10";
				break;

			case 11:
				month = "November - 11";
				break;

			case 12:
				month = "December - 12";
				break;

			default:
				String msg = "Unknown Month [" + monthExpireParsed + "], will use default Expire ["+month+", "+year+"]";
				LOG.error(msg);
				//throw new IllegalArgumentException(msg);
			}
		}

		else{
			String msg = "Unknown Expired [" + expire + "], will use default Expire ["+month+", "+year+"]";
			LOG.error(msg);
			//throw new IllegalArgumentException(msg);
		}

		parsedExpired = new String[] { month, year };
		
		return parsedExpired;
	}


	public boolean isTrueOrFalse(final String value){
		boolean status = false;
		
		final String cleanValue = $(BaseUtil.class).replaceStringRegex("([\\s\"'])", value, "");
		
		if(value != null && cleanValue.equalsIgnoreCase("Y")){
			status = true;
			//LOG.info("TID["+Thread.currentThread().getId()+"] "+"");
		}
		else if (value == null || cleanValue.equalsIgnoreCase("N")){
			status = false;
			//LOG.info("TID["+Thread.currentThread().getId()+"] "+"");
		}
		
		return status;
	}
}
