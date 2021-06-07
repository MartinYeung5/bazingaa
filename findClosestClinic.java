import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class findClosestClinic {
	
	public static double distince(double x1, double y1, double x2, double y2) {
		double x = Math.pow(x2 - x1, 2);
		double y = Math.pow(y2 - y1, 2);
		return Math.sqrt(x+y);
	}
	
	public static double[] nearestPoint(double[] coordinate, double[][] points ) {
		final int X = 0;
		final int Y = 1;
		double[] closestPoint = points[0];
		double closestDist = distince(coordinate[X],coordinate[Y],closestPoint[X],closestPoint[Y]);
		
		for(int i = 0; i < points.length; i++) {
			double dist = distince(coordinate[X],coordinate[Y],points[i][X],points[i][Y]);
			if (dist < closestDist) {
				closestDist = dist;
				closestPoint = points[i];
			}
		}
		
		return closestPoint;
	
	}
	
	public static boolean isNumeric(String string) {
	    double intValue;
			
	    if(string == null || string.equals("")) {
	        return false;
	    }
	    
	    try {
	        intValue = Double.parseDouble(string);
	        return true;
	    } catch (NumberFormatException e) {
	    }
	    return false;
	}
 
    public static void main(String[] args) {
         
        FileInputStream input = null;       
        String fileName = "C:/Users/yeung/chas-clinics-geojsonEXCEL.xlsx"; //dataset from SG gov
        double[] userLocation = {32339.5,39107.77178}; //capture user location from their device
        final int X = 0;
		final int Y = 1;
		int locationIndex = 1;
		
        try {
            input = new FileInputStream(fileName);
             
            @SuppressWarnings("resource")
            XSSFWorkbook book = new XSSFWorkbook(input);
             
            XSSFSheet sheet = book.getSheetAt(0);
            XSSFRow row = sheet.getRow(0);
            XSSFCell cell = null;
            
            double[][] points;
            points = new double[1167][2];
            
            String[] clinicName;
            clinicName = new String[1167];
            
            for(int i = 1; i< 1167; i++) {
            	row = sheet.getRow(i);
                cell = row.getCell(5);
                
                String cellValue = cell.getStringCellValue();
                //System.out.println(cellValue);
                int X_COORDINATE_INT = cellValue.indexOf("X_COORDINATE", 1);
                //System.out.println(X_COORDINATE_INT);
                String X_COORDINATE = cellValue.substring(X_COORDINATE_INT+22, X_COORDINATE_INT+33);
                //System.out.println("substring = " + X_COORDINATE);
                
                int Y_COORDINATE_INT = cellValue.indexOf("Y_COORDINATE", 1);
                //System.out.println(Y_COORDINATE_INT);
                String Y_COORDINATE = cellValue.substring(Y_COORDINATE_INT+22, Y_COORDINATE_INT+33);
                //System.out.println("substring = " + Y_COORDINATE);
                
                int HCI_NAME_INT = cellValue.indexOf("HCI_NAME", 1);
                int first_td = cellValue.indexOf("<td>", HCI_NAME_INT);
                int second_td = cellValue.indexOf("</td>", HCI_NAME_INT);
                
                clinicName[i-1] = cellValue.substring(first_td+4, second_td);
                //System.out.println("HCI_NAME = " + clinicName[i-1]);
                
                if (isNumeric(X_COORDINATE)){
	                points[i-1][0]= Double.parseDouble(X_COORDINATE);
	                points[i-1][1]= Double.parseDouble(Y_COORDINATE);
                } else {
                	points[i-1][0]= 0;
	                points[i-1][1]= 0;
                
                }
                

                
                if (cell.getCellType() == CellType.STRING) {
                    //System.out.println(cell.getStringCellValue());
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    //System.out.println(cell.getNumericCellValue());
                }
            }
       
        double[]closest = nearestPoint(userLocation, points);
    		System.out.println("{"+ closest[X]+","+ closest[Y]+"}");
    		double d = distince(userLocation[X],userLocation[Y],closest[X],closest[Y]);
    		System.out.println("Distince: " + d);
    		
    		for (int i = 0 ; i < points.length; i++)
    	    {
            if (points[i][0] == closest[X]) {
            locationIndex = i;
    			}
    	    }
    		
    		System.out.println ("Clinic Name = "+clinicName[locationIndex]);
    		
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
 
}
