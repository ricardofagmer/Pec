package com.pec.biosistemico.pec.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.XMLGregorianCalendar;

public class TreatDate {

	static final Pattern PATTERN_DATE = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$");

	public static long calculaDias(Date dataInicio) {
		Calendar dataInicial = Calendar.getInstance();
		dataInicial.setTime(clearDateTime(new Date()));
		Calendar dataFinal = Calendar.getInstance();
		dataFinal.setTime(clearDateTime(dataInicio));
		long diferencaMillis = dataFinal.getTimeInMillis() - dataInicial.getTimeInMillis();
		int tempoDiaMillis = 1000 * 60 * 60 * 24;
		return diferencaMillis / tempoDiaMillis;
	}

	public static final Boolean isDatePattern(String value) {
		Matcher mat = PATTERN_DATE.matcher(value.trim());
		if (mat.matches()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Formata data confome o padrao especificado
	 */
	public static final String format(String pattern, Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sd = new SimpleDateFormat(pattern);
		return sd.format(date);
	}

	/**
	 * Converte uma String para um java.util.Date
	 *
	 * @param pattern
	 *            - padrao esperado
	 * @param date
	 * @return
	 */
	public static final Date format(String pattern, String date) {
		if (TreatString.isBlank(date)) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setLenient(false);
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static final String formatDefaultDate(Date date) {
		return format("dd/MM/yyyy", date);
	}

	public static final String formatTime(Date date) {
		return format("HH:mm", date);
	}

	public static final String formatDateTime(Date date) {
		return format("dd/MM/yyyy HH:mm:ss", date);
	}

	public static final String formatDateIfen(Date date) {
		return format("yyyy-MM-dd", date);
	}

	public static final String formatDateJson(Date date) {
		return format("yyyy-MM-dd'T'HH:mm:ss.SSSZ", date);
	}

	/**
	 * Inicializa data a deixando apenas com informacoes sobre o ddMMyyyy
	 */
	public static final Date clearDateTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calTemp = Calendar.getInstance();
		calTemp.setTime(date);

		Calendar cal = Calendar.getInstance();
		cal.clear();
		// seta apenas campos da data
		cal.set(Calendar.DATE, calTemp.get(Calendar.DATE));
		cal.set(Calendar.YEAR, calTemp.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, calTemp.get(Calendar.MONTH));
		return cal.getTime();
	}

	public static final Date clearLimitDateTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calTemp = Calendar.getInstance();
		calTemp.setTime(date);

		Calendar cal = Calendar.getInstance();
		cal.clear();
		// seta apenas campos da data
		cal.set(Calendar.DATE, calTemp.get(Calendar.DATE));
		cal.set(Calendar.YEAR, calTemp.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, calTemp.get(Calendar.MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static final Date withStartMonth(Date date, Integer month) {
		date = clearDateTime(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static final Date withEndMonth(Date date, Integer month) {
		date = clearLimitDateTime(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	/**
	 * Converte uma String para um Date (String deve estar no padrao dd/MM/yyyy)
	 */
	public static final Date parseDate(String date) {
		return format("dd/MM/yyyy", date);
	}

	public static final Date parseDateTime(String date) {
		return format("dd/MM/yyyy HH:mm:ss", date);
	}

	public static final Date addDays(Date date, Integer dias) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return cal.getTime();
	}

	public static final Date subtractDays(Date date, Integer dias) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1 * dias);
		return cal.getTime();
	}

	public static TreeMap<Integer, String> getMeses() {
		Map<String, Integer> map = Calendar.getInstance().getDisplayNames(Calendar.MONTH, Calendar.LONG,
				Locale.getDefault());

		TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>(new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});

		for (String key : map.keySet()) {
			treeMap.put(map.get(key), key);
		}

		return treeMap;
	}

	// Calcula a Idade baseado em java.util.Date
	public static int calculaIdade(Date dataNasc) {
		return calculaIdade(dataNasc, new Date());
	}

	public static int calculaIdade(Date dataNasc, Date dataComparacao) {
		Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.setTime(dataNasc);

		Calendar today = Calendar.getInstance();
		today.setTime(dataComparacao);

		// Obt�m a idade baseado no ano
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

		dateOfBirth.add(Calendar.YEAR, age);

		// se a data de hoje � antes da data de Nascimento, ent�o diminui 1(um)
		if (today.before(dateOfBirth)) {
			age--;
		}
		return age;
	}

	public static String dateUTC(Date date) {
		String text = TreatDate.format("yyyy-MM-dd'T'HH:mm:ssZ", date);
		// forca inserir o : no timezone, nao consegui por isso no formater
		return new StringBuilder(text).insert(text.length() - 2, ":").toString();
	}

	public static final Date parseDateUTC(String date) {
		return format("yyyy-MM-dd'T'HH:mm:ssZ", date);
	}

	public static Date parse(XMLGregorianCalendar gregorian) {
		if (gregorian != null) {
			return gregorian.toGregorianCalendar().getTime();
		}
		return null;
	}

	public static String anoSigla(Date date) {
		return format("yy", new Date());
	}

}