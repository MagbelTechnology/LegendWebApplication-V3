/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package exceReport;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import fleet.net.bus.SystemAdminBus;
import fleet.net.vao.Vehicle;
import java.util.*;


public class TestingRep extends HttpServlet {

SystemAdminBus sysBus ;

    public void init(ServletConfig config) throws ServletException {


        sysBus = new SystemAdminBus();

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OutputStream out = null;
String filter = "";
   String  value1 = request.getParameter("");
   String  value2 = request.getParameter("");
   String  value3 = request.getParameter("");


  /* if((value1 != null) && (!value1.equals("")))){
		filter = filter + " WHERE DEPARTMENT = '"+value1+"' ";
		}


   */

ArrayList list =  sysBus.findReportByQuery(filter);

 try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=sampleName.xls");
            WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet s = w.createSheet("Demo", 0);

for(int x = 0; x < list.size();x++){

		 Vehicle vehicle = (Vehicle)list.get(x);


//s.addCell(new Label(0, x,list.get(x)));



s.addCell(new Label(0, x, vehicle.getVehicleNo()));
s.addCell(new Label(1, x, vehicle.getChasisNo()));
s.addCell(new Label(2, x, vehicle.getId()));
s.addCell(new Label(3, x, vehicle.getDate()));
s.addCell(new Label(4, x, vehicle.getMake()));
s.addCell(new Label(5, x, vehicle.getLocation()));
s.addCell( new Number(6, x, vehicle.getCost()));
//s.addCell(new Label(6, x, vehicle.getCost()));
s.addCell(new Label(7, x, vehicle.getDepartment()));
s.addCell(new Label(8, x, vehicle.getregistration()));




		  /*String id = vehicle.getId();
		  String vehicleNo  = vehicle.getVehicleNo();
		  String litre = vehicle.getLitre();
		  String date1 =  vehicle.getDate();
		  double cost = vehicle.getCost();
		  String villingStation = vehicle.getVillingStation();
		  String city = vehicle.getCity();
		  String assignedTo = vehicle.geAssignedTo();
		  String meterReading = vehicle.getMeterReading();
		  String account = vehicle.getAccount();
		  String ratePerLiter = vehicle.getRatePerLiter();
		String deportName = vehicle.getDeportName();
		String chasisNo = vehicle.getChasisNo();
		String make = vehicle.getMake();
		String model = vehicle.getModel();
		String type = vehicle.getType();
		 String dept = vehicle.getDepartment();
		 String ownership = vehicle.getOwnership();
		 String interiorColor = vehicle.getInteriorColor();
		String extColor = vehicle.getExtColor();
		String unitNo = vehicle.getUnitNo();
		String department = vehicle.getDepartment();
		String mot = vehicle.getMot();
		String insurance = vehicle.getInsurance();
		String union = vehicle.getUnion();
		String registration = vehicle.getregistration();
		String engineNo = vehicle.getEngineNo();
		String engineSize = vehicle.getEngineSize();
		String numbOfCylinder = vehicle.getNumbOfCylinder();
		String fuelType = vehicle.getFuelType();
		String frontWheelSize = vehicle.getFrontWheelSize();
		String rearWheelSize = vehicle.getRearWheelSize();
		String frontTireSize = vehicle.getFrontTireSize();
		String rearTireSize = vehicle.getRearTireSize();
		String presure = vehicle.getPresure();
		String purchaseDate = vehicle.getPurchaseDate();
		String purchaseFrom = vehicle.getStatePurchaseFrom();
		String purchaseAddress = vehicle.getPurchaseAddress();
		String waranty = vehicle.getWaranty();
		String purchaseCountry = vehicle.getPurchaseCountry();
		String location = vehicle.getLocation();

		*/

}


     /*   String[] bas = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

        //String[] bas = new String[100];
                      try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=sampleName.xls");
            WritableWorkbook w = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet s = w.createSheet("Demo", 0);

            for (int x = 0; x < bas.length; x++) {

                for(int y = 0;y< 5; y++){

                //bas[x] =(bas.length)+"A";
                // s.addCell(new Label(0, 0, "Hefllo World"));

                  for(int x = 0; x < list.size();x++){
                     }
                s.addCell(new Label(x, y, bas[x]));
                 //s.addCell(new Label(x, y, (String)list.get(x)));

                }
            }

           */
            w.write();
            w.close();
        } catch (Exception e) {
            throw new ServletException("Exception in Excel Sample Servlet", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

