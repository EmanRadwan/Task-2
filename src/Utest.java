import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class Utest {
	
	@Mock 
	Connection conn;
	@Mock
	PreparedStatement psmt;
	@InjectMocks
	DAOImpl newDAO = new DAOImpl();
	
	
	public Product p=new Product(1);
	public DAOImpl dao = new DAOImpl();

	
	
	@Test
	public void testProductCon(){
		
		Assert.assertEquals(1,p.getId());
		}
	
	@Test
	public void testTypeSetter() {
     	p.setType("x");
	   Assert.assertEquals("x", p.getType());    
	}
	
	@Test
	public void testManufacturerSetter() {
     	p.setManufacturer("x");
	   Assert.assertEquals("x", p.getManufacturer());    
	}
	
	@Test
	public void testProductionDateSetter() {
		p.setProductionDate("x");
	   Assert.assertEquals("x", p.getProductionDate());    
	}
	
	@Test
	public void testExpiryDateSetter() {
     	p.setExpiryDate("x");
	   Assert.assertEquals("x", p.getExpiryDate());    
	}

	@Test
	public void HappyTest() throws SQLException, DAOException{
		when(conn.prepareStatement(anyString())).thenReturn(psmt);
		when(psmt.executeUpdate()).thenReturn(1);
		newDAO.insertProduct(p);
		verify(psmt,times(1)).executeUpdate();
		ArgumentCaptor<Integer> intcaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<String> stringcaptor = ArgumentCaptor.forClass(String.class);
		verify(psmt, times(1)).setInt(anyInt(), intcaptor.capture());
		verify(psmt, times(4)).setString(anyInt(), stringcaptor.capture());
		Assert.assertEquals(Integer.valueOf(p.getId()),intcaptor.getAllValues().get(0));
		Assert.assertEquals(p.getType(),stringcaptor.getAllValues().get(0));
		Assert.assertEquals(p.getManufacturer(),stringcaptor.getAllValues().get(1));
		Assert.assertEquals(p.getProductionDate(),stringcaptor.getAllValues().get(2));
		Assert.assertEquals(p.getExpiryDate(),stringcaptor.getAllValues().get(3));
	}

	@Test (expected = DAOException.class)
	public void ExceptionCase() throws SQLException, DAOException{
		when(conn.prepareStatement(anyString())).thenReturn(psmt);
		when(psmt.executeUpdate()).thenThrow(new SQLException());
		newDAO.insertProduct(p);
	}
	
	@Test
	public void IntegrationTest() throws SQLException, DAOException{
	     dao.insertProduct(p);
	     Assert.assertNotNull(dao.getProduct(1));
	     dao.deleteProduct(1);
	     Assert.assertNull( dao.getProduct(1));

	}


	}

