

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataTableTest {
	@Mock
	HttpServletRequest request;

	@Before
	public void setUp() throws Exception {
		when(request.getAttribute("bRegex")).thenReturn("true");
		when(request.getAttribute("iDisplayStart")).thenReturn("1");
		when(request.getAttribute("iDisplayLength")).thenReturn("10");
		when(request.getAttribute("iColumns")).thenReturn("3");
		when(request.getAttribute("sSearch")).thenReturn("search");
		when(request.getAttribute("bSearchable_0")).thenReturn("true");
		when(request.getAttribute("bSearchable_1")).thenReturn("true");
		when(request.getAttribute("bSearchable_2")).thenReturn("true");

		when(request.getAttribute("bSortable_0")).thenReturn("true");
		when(request.getAttribute("bSortable_1")).thenReturn("true");
		when(request.getAttribute("bSortable_2")).thenReturn("true");

		when(request.getAttribute("sSortDir_0")).thenReturn("asc");
		when(request.getAttribute("sSortDir_1")).thenReturn("asc");
		when(request.getAttribute("sSortDir_2")).thenReturn("desc");

		when(request.getAttribute("sSearch_0")).thenReturn("a");
		when(request.getAttribute("sSearch_1")).thenReturn("b");
		when(request.getAttribute("sSearch_2")).thenReturn("c");
		when(request.getAttribute("iSortingCols")).thenReturn("1");
		when(request.getAttribute("iSortCol_0")).thenReturn("2");

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseStringFromRequest() {
		DataTable table = new DataTable(request, "user", null);
		assertEquals(table.parseStringFromRequest("sSearch"), "search");
	}

	@Test
	public void testParseBoolFromRequest() {

		DataTable table = new DataTable(request, "user", null);
		assertEquals(table.parseBoolFromRequest("bRegex"), true);

	}

	@Test
	public void testParseIntegerFromRequest() {
		DataTable table = new DataTable(request, "user", null);
		assertEquals(table.parseIntegerFromRequest("iDisplayStart"), 1);
	}

	@Test
	public void testParseBooleanListFromRequest() {
		DataTable table = new DataTable(request, "user", null);
		List<Boolean> bools = new ArrayList<Boolean>();
		bools.add(true);
		bools.add(true);
		bools.add(true);
		assertEquals(table.parseBooleanListFromRequest("bSearchable_"), bools);
	}

	@Test
	public void testParseStringListFromRequest() {
		DataTable table = new DataTable(request, "user", null);
		List<String> strings = new ArrayList<String>();
		strings.add("a");
		strings.add("b");
		strings.add("c");
		assertEquals(table.parseStringListFromRequest("sSearch_"), strings);
	}

	@Test
	public void testParseIntegerLostFromRequest() {
		DataTable table = new DataTable(request, "user", null);
		List<Integer> ints = new ArrayList<Integer>();
		ints.add(2);

		assertEquals(table.parseIntegerListFromRequest("iSortCol_", 1), ints);
	}

	@Test
	public void testOrder() {
		List<String> columns = Arrays.asList("rocks", "my", "world");
		DataTable table = new DataTable(request, "User", columns);
		assertEquals(" ORDER BY world desc ", table.order());

	}

	@Test
	public void testWhere() {
		List<String> columns = Arrays.asList("rocks", "my", "world");
		DataTable table = new DataTable(request, "User", columns);
		assertEquals(
				"WHERE (user.rocks LIKE :search OR user.my LIKE :search OR user.world LIKE :search) ",
				table.where());
	}

	@Test
	public void testSelect() {

		List<String> columns = Arrays.asList("rocks", "my", "world");
		DataTable table = new DataTable(request, "User", columns);
		assertEquals(
				"SELECT user.rocks, user.my, user.world ",
				table.select());
	}
	@Test
	public void testFrom(){
		List<String> columns = Arrays.asList("rocks", "my", "world");
		DataTable table = new DataTable(request, "User", columns);
		assertEquals(
				" FROM User user ",
				table.from());
	}
	@Test
	public void testQuery(){
		List<String> columns = Arrays.asList("rocks", "my", "world");
		DataTable table = new DataTable(request, "User", columns);
		assertEquals(
				" sd",
				table.buildHibernateQuery());
	}

}
