

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.WordUtils;

public class DataTable {
	private HashMap<String, Object> table = new HashMap<String, Object>();
	private boolean bRegex = false;
	private List<Boolean> bRegex_ = new ArrayList<Boolean>();
	private List<Boolean> bSearchable_ = new ArrayList<Boolean>();
	private List<Boolean> bSortable_ = new ArrayList<Boolean>();
	private List<String> columns = new ArrayList<String>();;
	private String entityName;
	private int iColumns = 0;
	private int iDisplayLength = 0;
	private int iDisplayStart = 0;
	private List<Integer> iSortCol_ = new ArrayList<Integer>();
	private int iSortingCols = 0;
	private HttpServletRequest request;
	private String sEcho = "1";
	private String sSearch = "";
	private List<String> sSearch_ = new ArrayList<String>();
	private List<String> sSortDir_ = new ArrayList<String>();
	private String entity = "";
	private boolean shouldSearch = false;

	public DataTable(HttpServletRequest request, String entityName,
			List<String> columns) {
		this.request = request;
		this.iDisplayStart = this.parseIntegerFromRequest("iDisplayStart");
		this.iDisplayLength = this.parseIntegerFromRequest("iDisplayLength");
		this.iColumns = this.parseIntegerFromRequest("iColumns");
		this.sSearch = this.parseStringFromRequest("sSearch");
		this.bRegex = this.parseBoolFromRequest("bRegex");
		this.bSearchable_ = this.parseBooleanListFromRequest("bSearchable_");
		this.sSearch_ = this.parseStringListFromRequest("sSearch_");
		this.bRegex_ = this.parseBooleanListFromRequest("bRegex_");
		this.bSortable_ = this.parseBooleanListFromRequest("bSortable_");
		this.iSortingCols = this.parseIntegerFromRequest("iSortingCols");
		this.iSortCol_ = this.parseIntegerListFromRequest("iSortCol_",
				this.iSortingCols);
		this.sSortDir_ = this.parseStringListFromRequest("sSortDir_");
		this.sEcho = this.parseStringFromRequest("sEcho");
		this.entityName = entityName;
		this.columns = columns;
		this.entity = WordUtils.uncapitalize(this.entityName);

	}

	public List<Boolean> parseBooleanListFromRequest(String string) {
		List<Boolean> bools = new ArrayList<Boolean>();
		for (int i = 0; i < this.iColumns; i++) {
			bools.add(parseBoolFromRequest(string + i));

		}
		return bools;
	}

	public boolean parseBoolFromRequest(String attribute) {
		String value = (String) this.request.getParameter(attribute);
		try {
			return Boolean.parseBoolean(value);
		} catch (Exception e) {
			return false;
		}

	}

	public int parseIntegerFromRequest(String attribute) {
		String value = (String) this.request.getParameter(attribute);
		int number = 0;
		try {
			number = Integer.parseInt(value);
		} catch (Exception e) {
			number = 0;
		}
		return number;
	}

	public List<Integer> parseIntegerListFromRequest(String string) {
		List<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < this.iColumns; i++) {
			ints.add(parseIntegerFromRequest(string + i));

		}
		return ints;
	}

	public List<Integer> parseIntegerListFromRequest(String string, int count) {
		List<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {
			ints.add(parseIntegerFromRequest(string + i));

		}
		return ints;
	}

	public String parseStringFromRequest(String attribute) {
		return (String) this.request.getParameter(attribute);
	}

	public List<String> parseStringListFromRequest(String string) {
		List<String> strings = new ArrayList<String>();
		for (int i = 0; i < this.iColumns; i++) {
			strings.add(parseStringFromRequest(string + i));

		}
		return strings;
	}

	public String order() {
		String order = "";
		if (this.iSortCol_.size() > 0)
			order = " ORDER BY ";

		for (Iterator iterator = this.iSortCol_.iterator(); iterator.hasNext();) {
			Integer columnNumber = (Integer) iterator.next();

			if (this.bSortable_.get(columnNumber)) {

				order += columns.get(columnNumber) + " "
						+ this.sSortDir_.get(0);
			}
		}
		return order + " ";
	}

	public String where() {
		String where = "";
		if (this.sSearch=="") return "";

		for (int i = 0; i < columns.size(); i++) {
			if (this.bSearchable_.get(i)) {
				if (where == "") {
					where = "WHERE (";
				} else {
					where += "";
				}
				if (i == columns.size() - 1) {
					where += entity + "." + columns.get(i) + " LIKE :search)";
				} else {
					where += entity + "." + columns.get(i)
							+ " LIKE :search OR ";
				}

			}

		}

		return where + " ";
	}

	public String select() {
		String select = "SELECT ";

		for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
			String col = (String) iterator.next();

			if (col == columns.get(columns.size() - 1)) {
				select += entity + "." + col + " ";
			} else {
				select += entity + "." + col + ", ";
			}

		}

		return select;
	}

	public String buildHibernateQuery() {
		String query = select() + from() + where() + order();
		System.out.print(query);
		return query;
	}

	public String from() {
		return " FROM " + entityName + " " + entity + " ";
	}

	public String getsSearch() {
		return sSearch;
	}

	public int getiDisplayLength() {
		return iDisplayLength;
	}

	public int getiDisplayStart() {
		return iDisplayStart;
	}

	public boolean isShouldSearch() {
		this.shouldSearch = where()!="";
		return shouldSearch;
	}

	 

}
