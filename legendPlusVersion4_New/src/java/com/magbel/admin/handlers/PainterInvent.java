
package com.magbel.admin.handlers;
/**
 *
 * Painter version 1.2.0
 * Called by chart.jsp
 *
 */
import java.io.*;
import java.awt.*;
import java.util.*;
import java.awt.image.*;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.*;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import magma.net.dao.MagmaDBConnection;

import org.jfree.chart.*;
import org.jfree.chart.plot.*; 
import org.jfree.chart.urls.*;
import org.jfree.chart.entity.*;
import javax.servlet.http.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.Timer;

public class PainterInvent {

	private String title;
	private String variables;
	private int type;
	private Map data;
    static MagmaDBConnection mgDbCon = null;
	public static final int PIE = 0;
	public static final int BAR = 1;

	class Rotator extends Timer implements ActionListener{

	  public void actionPerformed(ActionEvent actionevent)
	  {
		  plot.setStartAngle(angle);
		  angle = angle + 1;
		  if(angle == 360)
			  angle = 0;
	  }

	  private PiePlot plot;
	  private int angle;

	  Rotator(PiePlot pieplot)
	  {
		  super(100, null);
		  angle = 270;
		  plot = pieplot;
		  addActionListener(this);
	  }
  }

  public PainterInvent(String title,String variables,Map data) {

  	this.title = title;
  	this.variables = variables;
  	this.data = data;

  } 
 
  private DefaultPieDataset getPieDataset() {
    // categories...
//	  System.out.println("<<<<<<<variables>>>> "+this.variables);
//	  System.out.println("<<<<<<<data>>>> "+data);
//	  System.out.println("<<<<<<<Title>>>> "+this.title);


    String[] sections = this.variables.split(",");
    // data...
    double[] values = new double[sections.length];
    for (int i = 0; i < values.length; i++) {
//    	System.out.println("<<<<<<<sections>>>> "+sections[i]);
        values[i] = ((Double)data.get(sections[i])).doubleValue();
 //       System.out.println("<<<<<<<Values>>>> "+values[i]);
    }

    // create the dataset...
    DefaultPieDataset dataset = new DefaultPieDataset();
    for (int i = 0; i < values.length; i++) {
        dataset.setValue(sections[i], values[i]);
    }
    return dataset;
  }

  public CategoryDataset getBarDataset(){

	  String s="Year";
	  DefaultCategoryDataset dataset  = new DefaultCategoryDataset();
	  String[] sections = this.variables.split(",");
	  double[] values = new double[sections.length];
	  for (int i = 0; i < values.length; i++) {
			  values[i] = ((Double)data.get(sections[i])).doubleValue();
	   }

	  for (int i = 0; i < values.length; i++) {
			values[i] = ((Double)data.get(sections[i])).doubleValue();
			dataset.addValue(values[i], s, sections[i]);
	   }

	  return dataset;
   }

  public String paint(HttpServletRequest request, HttpServletResponse response,int type) {

    boolean USE_LEGEND = true;
    boolean USE_TOOLTIP = true;
    boolean USE_URL = false;
    // create the chart...
    JFreeChart chart = null;
    Rotator rotator = null;

    switch(type){

		case PainterInvent.PIE:

		DefaultPieDataset dataset = getPieDataset();
/*		System.out.println("<<<<<<<Title>>>> "+this.title);
		System.out.println("<<<<<<<dataset>>>> "+dataset);
		System.out.println("<<<<<<<USE_LEGEND>>>> "+USE_LEGEND);
		System.out.println("<<<<<<<USE_TOOLTIP>>>> "+USE_TOOLTIP);
		System.out.println("<<<<<<<USE_URL>>>> "+USE_URL);
		*/
    	chart = ChartFactory.createPieChart3D(this.title,dataset,USE_LEGEND,USE_TOOLTIP,USE_URL);
    	// set the background color for the chart...
    	chart.setBackgroundPaint(Color.cyan);
    	PiePlot plot = (PiePlot) chart.getPlot();
		plot.setNoDataMessage("No data available");
		plot.setCircular(true);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
    	// set drilldown capability...
		plot.setURLGenerator(new StandardPieURLGenerator("hdCharts.jsp","section"));
		rotator = new Rotator(plot);
        rotator.start();
        break;

    	case PainterInvent.BAR: 

    	CategoryDataset dataset0 = getBarDataset();
    	chart = ChartFactory.createBarChart(this.title, null, "No of Incidents", dataset0, PlotOrientation.VERTICAL, true, false, false);
		CategoryPlot categoryplot = (CategoryPlot)chart.getPlot();
		NumberAxis numberaxis = (NumberAxis)categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		BarRenderer barrenderer = (BarRenderer)categoryplot.getRenderer();
		barrenderer.setDrawBarOutline(false);
		barrenderer.setMaximumBarWidth(0.10000000000000001D);
		barrenderer.setLegendItemLabelGenerator(new StandardCategorySeriesLabelGenerator("{0} series"));
		//categoryplot.setURLGenerator(new StandardCategoryURLGenerator("chart.jsp","section","mmmm"));
		break;

        default:
        //;
	}
    // OPTIONAL CUSTOMISATION COMPLETED.

    ChartRenderingInfo info = null;
    HttpSession session = request.getSession();
    try {

      //Create RenderingInfo object
      response.setContentType("text/html");
      info = new ChartRenderingInfo(new StandardEntityCollection());
      BufferedImage chartImage = chart.createBufferedImage(640, 400, info);

      // putting chart as BufferedImage in session,
      // thus making it available for the image reading action Action.
      session.setAttribute("chartImage"+Integer.toString(type), chartImage);

      PrintWriter writer = new PrintWriter(response.getWriter());
      ChartUtilities.writeImageMap(writer, "imageMap", info,false);
      writer.flush();

    }
    catch (Exception e) {
       // handel your exception here
       e.printStackTrace();
    }

    String pathInfo = "http://";
    pathInfo += request.getServerName();
    int port = request.getServerPort();
    pathInfo += ":"+String.valueOf(port);
    pathInfo += request.getContextPath();
    String chartViewer = pathInfo + "/chart.do?t="+Integer.toString(type);
    return chartViewer;
  }

  public static String warehouse(){

	  StringBuffer sb = new StringBuffer();
	  sb.append("select b.name from ST_INVENTORY_TOTALS a,ST_WAREHOUSE b ");
	  sb.append("where a.WAREHOUSE_CODE = b.WAREHOUSE_CODE group by b.name");
//	  sb.append("select b.user_name from hd_complaint a,am_gb_user b ");
//	  sb.append("where a.technician = b.user_id group by b.user_name");
//	  System.out.println("===== warehouse StringBuffer sb ===="+sb);
	  
	  return doText(sb.toString());
  } 

  public static String status(){

  	  StringBuffer sb = new StringBuffer();
  	  sb.append("select b.STATUS_NAME from ST_WAREHOUSE a,ST_GB_STATUS b  ");
  	  sb.append("where a.STATUS = b.STATUS_CODE group by b.STATUS_NAME");
//  	  sb.append("select b.priority_description from hd_complaint a,hd_priority b  ");
//  	  sb.append("where a.priority = b.priority_code group by b.priority_description");  	  
//System.out.println("===== StringBuffer sb ===="+sb);
  	  return doText(sb.toString());
  }

  public static Map findDataBywarehouse(){
//	  System.out.println("Inside findDataBywarehouse");
  	  StringBuffer sb = new StringBuffer();
  	  sb.append("select b.name,sum(a.balance) as total_balance ");
  	  sb.append(" from ST_INVENTORY_TOTALS a,ST_WAREHOUSE b ");
  	  sb.append(" where a.warehouse_code = b.warehouse_code group by b.name");
 // 	  System.out.println("===sb.toString()== "+sb.toString());
  	  return load(sb.toString());
  }

  public static Map findDataByItem(){

	  StringBuffer sb = new StringBuffer();
	  sb.append("select b.name,sum(a.balance) as total_balance ");
	  sb.append(" from ST_INVENTORY_TOTALS a,ST_WAREHOUSE b ");
      sb.append(" where a.warehouse_code = b.warehouse_code group by b.name ");

	  return load(sb.toString());
  }

private static String ListToText(java.util.ArrayList a){

	StringBuffer sb = new StringBuffer();
	for(int x = 0; x < a.size(); x++){
		sb.append((String)a.get(x));
		if(x < (a.size() -1)){
			sb.append(",");
		}
	}

	return sb.toString();
}
   private static String doText(String sql){

    	java.sql.Connection con = null;
    	java.sql.PreparedStatement ps = null;
    	java.sql.ResultSet rs = null;

    	StringBuffer sb = new StringBuffer();
    	ArrayList a = new ArrayList();

    	try{

    		 con =  new MagmaDBConnection().getConnection("FixedAssetPlus");
             ps = con.prepareStatement(sql);
             rs = ps.executeQuery();

            while(rs.next()){
              a.add(rs.getString(1));
  		  }

    	}catch(Exception e){
            e.printStackTrace();
    	}finally{close(con,ps,rs);}

    	return PainterInvent.ListToText(a);
   }

  private static Map load(String sql){

  	java.sql.Connection con = null;
  	java.sql.PreparedStatement ps = null;
  	java.sql.ResultSet rs = null;
  	java.util.Map m = new HashMap();

  	try{
  	   con = new MagmaDBConnection().getConnection("FixedAssetPlus");
         // con = getConnection();
          ps = con.prepareStatement(sql);
          rs = ps.executeQuery();

          while(rs.next()){
			  m.put(rs.getString(1),new Double(rs.getInt(2)));
		  }

  	}catch(Exception e){
          e.printStackTrace();
  	}finally{close(con,ps,rs);}

  	return m;

   }
/*
   private static java.sql.Connection getConnection()throws Exception{

	  String url = "jdbc:sqlserver://127.0.0.1:1433;database=FixedAssetPlus";
	  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	  java.sql.Connection con = java.sql.DriverManager.getConnection(url,"sa","magbel");

	  return con;

   }
*/
   private static void close(java.sql.Connection con,java.sql.PreparedStatement ps,java.sql.ResultSet rs){

   	  if(rs != null) try{rs.close();}catch(Exception e){}
   	  if(ps != null) try{ps.close();}catch(Exception e){}
  	  if(con != null) try{con.close();}catch(Exception e){}

   }
}