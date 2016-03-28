package flca.xlsx.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

class XlsxDateFmtHelper {

	List<DateFormatInfo> dateFormatInfos;
	
	
	public XlsxDateFmtHelper(List<SimpleDateFormat> dateFormats) {
		super();
		this.dateFormatInfos = new ArrayList<XlsxDateFmtHelper.DateFormatInfo>();
		for (SimpleDateFormat datefmt : dateFormats) {
			this.dateFormatInfos.add(new DateFormatInfo(datefmt));
		}
	}

	SimpleDateFormat find(final String value) throws XlsxSetValueException {
		SimpleDateFormat r = findByLongPattern(value);
		if (r != null) {
			return r;
		}
		
		r = findByShortPattern(value);
		if (r != null) {
			return r;
		}

		throw new XlsxSetValueException("Can not find matching dateformat pattern for ", value);
	}
	
	SimpleDateFormat getDefault() {
		return dateFormatInfos.get(0).dateformat;
	}
	
	SimpleDateFormat findByLongPattern(String value) {
		final String longPattern = replaceAll(value,"x");
		for (DateFormatInfo fmtinfo : dateFormatInfos) {
			if (longPattern.equals(fmtinfo.longPattern)) {
				return fmtinfo.dateformat;
			}
		}
		return null;
	}

	SimpleDateFormat findByShortPattern(String value) {
		final String longPattern = replaceAll(value,"");
		for (DateFormatInfo fmtinfo : dateFormatInfos) {
			if (longPattern.equals(fmtinfo.shortPattern)) {
				return fmtinfo.dateformat;
			}
		}
		return null;
	}

	private static String replaceAll(final String value, final String withChar) {
		return value.replaceAll("[A-Za-z0-9]", withChar); 
	}
	
	class DateFormatInfo {
		SimpleDateFormat dateformat;
		String longPattern;
		String shortPattern;
		
		public DateFormatInfo(SimpleDateFormat dateformat) {
			super();
			this.dateformat = dateformat;
			this.longPattern = replaceAll(dateformat.toPattern(), "x");
			this.shortPattern = replaceAll(dateformat.toPattern(), "");
		}
	}
}
