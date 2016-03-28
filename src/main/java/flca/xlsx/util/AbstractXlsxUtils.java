package flca.xlsx.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class AbstractXlsxUtils {

	private final static Pattern NRS_LIST_PATTERN = Pattern.compile("(\\d*)(\\.\\.)(\\d*)");

	private static final String DELIM = ",";

	Set<Integer> getNrs(String value) {
		Set<Integer> r = new HashSet<Integer>();

		Matcher matcher = NRS_LIST_PATTERN.matcher(value);
		if (matcher.find()) {
			return getMultipleNrs(matcher);
		} else {
			String[] nrs = value.replace('.', ',').split(DELIM);
			for (int i = 0; i < nrs.length; i++) {
				String nr = nrs[i].trim();
				if (!nr.isEmpty()) {
					r.add(getNr(nr));
				}
			}
			return r;
		}
	}

	private Set<Integer> getMultipleNrs(final Matcher m) {
		Set<Integer> r = new HashSet<Integer>();
		for (int i = getNr(m.group(1)); i <= getNr(m.group(3)); i++) {
			r.add(i);
		}
		return r;
	}

	int getNr(String value) {
		return Integer.valueOf(value);
	}

	boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

}
