import java.util.HashMap;
import java.util.Map;

import org.hibernate.mapping.Index;
import org.springframework.jdbc.core.JdbcTemplate;

public class SpringJDBCDemo {

	private JdbcTemplate jdbcTemplate;  

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
		this.jdbcTemplate = jdbcTemplate;  
	}  


//	public static void main(String[] args) {
//		Map<String, Object> map =new HashMap<>();
//		//  map.put(key, value)
//		System.out.println("map is :"+map);
//
//
//		Integer[] obj = new Integer[10];
//      obj[0]=5;
//      
//      int[] source = {10,12,3,4,5,6,7,8,9};
//      
//      int len= source.length;
//      System.out.println("source len :"+len);
//      
//      int[] source1 = {1,2,3,5,8};
//      int len1= source1.length;
//      System.out.println("source1 len :"+len1);
//      
//      
//    		  System.arraycopy(source, 0, source1, 0, 2);
//    		  
//    		  System.out.print(source1[0]+" ");
//    		  System.out.print(source1[1]+" ");
//    		  System.out.print(source1[2]+" ");
//    		  System.out.print(source1[3]+" ");
//    		  System.out.print(source1[4]+" ");
//    		  System.out.println();
//    		  
//    	for(int i= 0 ;i<len;i++)	{
//    		System.out.println("****source******"+source[i]);
//    	}
//    	
//        for(int i= 0 ;i<len1;i++)	{
//        	System.out.println("****source1******"+source1[i]);
//    	}
//    		  
//    		  
//      System.out.println("inteer valu : "+obj[0]);
//
//		System.out.println(obj);
//
//		String st[] = new String[3];
//
//		st[0]="a";
//		st[1]="a";st[2]="c";
//
//		for(String strObj : st){
//			System.out.println("value at index : "+strObj);
//		}
//		System.out.println("+++++++++++++++++++++++++++++++++");
//
//
//		for(int index=0;index < st.length ;index++){
//			System.out.println("value at index : "+index+" == "+st[index]);
//			
//
//		}
//	}
}