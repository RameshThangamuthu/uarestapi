package com.usageaccounting.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.usageaccounting.dao.AppAndMSUsageDAO;
import com.usageaccounting.entity.AppAndMSMetricUsage;
import com.usageaccounting.entity.AppDataMonitoring;
import com.usageaccounting.entity.AppDataMonitoringInput;
import com.usageaccounting.entity.App_usage_data;
import com.usageaccounting.entity.Microservice_usage;
import com.usageaccounting.entity.Application_Usage_RollUp5;
import com.usageaccounting.entity.Application_Usage_RollUp60;
import com.usageaccounting.entity.Application_Usage_RollUp1440;
import com.usageaccounting.entity.Application_Usage_RollUp1;
import com.usageaccounting.service.impl.AppAndMSUsageServiceImpl;
import com.usageaccounting.entity.Application_usage;
import com.usageaccounting.entity.Cloudlet_usage;
import com.usageaccounting.entity.Content;
import com.usageaccounting.entity.Data_points;
import com.usageaccounting.util.MyCassandraTemplate;



/**
 * DAOImpl class for Application to perform CRUD operation.
 * @author Aricent
 * @version 1.0
 */
@Repository
public class AppAndMSUsageDAOImpl implements AppAndMSUsageDAO {

    @Autowired
    private MyCassandraTemplate myCassandraTemplate;
    
    private List<Application_usage> l_application;
    private List<Microservice_usage> l_microService;
    
    private Content message;
	//public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    static Logger log = Logger.getLogger(AppAndMSUsageDAOImpl.class.getName());
	Map<String, Integer> l_dataPointProceed = new HashMap<String, Integer>();
	
    @ExceptionHandler({NullPointerException.class})
	void handleBadRequestsNull (HttpServletResponse response) throws IOException {
		System.out.println("Inside handler handleBadRequestsNull()");
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
    
    
    @Override
    public void createAppAndMSUsage(AppAndMSMetricUsage appAndMSMetricUsage) {

    	System.out.println("Inside createAppAndMSUsage " ); 
    	l_application = appAndMSMetricUsage.getApplications();
    	myCassandraTemplate.createList(l_application);
    	
    	l_microService = appAndMSMetricUsage.getMicroservices();
    	myCassandraTemplate.createList(l_microService);
 
        return;
    }
   
    @Override
    public void createCloudletUsage(List<Cloudlet_usage> l_aggcloudletUsage) {

    	System.out.println("Inside createCloudletUsage " ); 
    	myCassandraTemplate.createList(l_aggcloudletUsage);
        return;
    }
    
    @Override
	public AppDataMonitoring getLastOnedayAppDate(AppDataMonitoringInput appDataMonitoringInput,
												  int l_StartDay,
												  int l_Starthour,
												  int l_Startminute)
	{
    	log.info("AppAndMSUsageDAOImpl::AppDataMonitoring");
    	AppDataMonitoring l_appDataMonitoring  = new AppDataMonitoring();
    	Select select;
		int l_EndDay = l_StartDay -1 ;
		int l_Endhour = l_Starthour;
		int l_countHour = 0;
		int l_countDay = l_EndDay;
		List<Application_Usage_RollUp60> l_AppUsageHourly = null;
		List<App_usage_data> l_aggAppUsagedata_list = null;

		try
		{
    	Map<String, Integer> mapAppname = new HashMap<String, Integer>();
    	
    	if ( !(appDataMonitoringInput.getAppName1().isEmpty()) ) {
    		mapAppname.put(appDataMonitoringInput.getAppName1(), 1);
    	}
 
    	if (!(appDataMonitoringInput.getAppName2().isEmpty()) ) {
    		mapAppname.put(appDataMonitoringInput.getAppName2(), 1);
    	}
    	if (!(appDataMonitoringInput.getAppName3().isEmpty()) ) {
    		mapAppname.put(appDataMonitoringInput.getAppName3(), 1);
    	}
		
		
    	for (String name: mapAppname.keySet()){
            log.info("Calling app names <"+name+">");
    	}
    	
    	//Create and start populate the response structure
    	l_appDataMonitoring.setUserID(appDataMonitoringInput.getUserID());
    	l_aggAppUsagedata_list = new ArrayList<App_usage_data>();
    	
    	for(String key:mapAppname.keySet()){
	    	select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type","day", "day_hour","metric_value").from("APPLICATION_USAGE_ROLLUP60");
	    	select.where(QueryBuilder.eq("userId",appDataMonitoringInput.getUserID())).and(QueryBuilder.eq("appName",key))
	    	      .and(QueryBuilder.eq("day",l_StartDay));	
	    	select.allowFiltering();
	    	
			log.info("Calling getLastOnedayAppDate :: getDataFromRollUp() for day of year"+l_StartDay);
			l_AppUsageHourly = myCassandraTemplate.getDataFromRollUp(select,Application_Usage_RollUp60.class);
			log.info("Calling getLastOnedayAppDate :: getLastOnedayAppDate() Start l_StartDay" +l_StartDay +"###"+l_AppUsageHourly.size());
			
			if (!((l_Starthour == 0) && (l_Startminute == 0 )))
			{
		    	select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type", "day", "day_hour","metric_value").from("APPLICATION_USAGE_ROLLUP60");
		    	select.where(QueryBuilder.eq("userId",appDataMonitoringInput.getUserID())).and(QueryBuilder.eq("appName",key))
	    	          .and(QueryBuilder.eq("day",l_EndDay));
		    	select.allowFiltering();
				log.info("Calling getLastOnedayAppDate :: getDataFromRollUp() for day of year"+l_EndDay);
		    	List<Application_Usage_RollUp60> l_AppUsageHourly_2 = myCassandraTemplate.getDataFromRollUp(select, Application_Usage_RollUp60.class);
		    	log.info("Calling getLastOnedayAppDate :: getLastOnedayAppDate() Start l_StartDay" +l_EndDay +"###"+l_AppUsageHourly_2.size());
		    	l_AppUsageHourly.addAll(l_AppUsageHourly_2);
			}
    //	}
    	
    	log.info("Calling getLastOnedayAppDate :: getLastOnedayAppDate() total size " +l_AppUsageHourly.size());
    	Map<String, Integer> mapHourlyrange = new HashMap<String, Integer>();

    	
    	if (l_Startminute == 0 ){
    		l_countHour=l_Endhour +1;
    	}
    	else
    	{
    		l_countHour=l_Endhour +2;
    	}
    	

    	while(true)
    	{
    		if ((l_countHour) == 24)
    		{
    			l_countHour=0;
    			l_countDay=l_StartDay;
    		}
    		mapHourlyrange.put(l_countDay+"_"+l_countHour, 1);
    		if (l_countHour == l_Starthour){
    			break;
    		}
     		l_countHour++;
    	}
    	log.info("Calling getLastOnedayAppDate :: getLastOnedayAppDate() Start");
    	for (String name: mapHourlyrange.keySet()){
            log.info("<"+name+">");
    	}
    	log.info("Calling getLastOnedayAppDate :: getLastOnedayAppDate() End");
    	log.info("Calling getLastOnedayAppDate :: value of hourly data () Start");
    	/*  	for (int i = 0; i < l_AppUsageHourly.size(); i++) {
    		log.info("<"+l_AppUsageHourly.get(i).toString()+">");
    		
    	}
    	log.info("Calling getLastOnedayAppDate :: value of hourly data () End");*/
    	
    	//Populate the response structure
    	//l_appDataMonitoring.setUserID(appDataMonitoringInput.getUserID());
    	//l_aggAppUsagedata_list = new ArrayList<App_usage_data>();

    	
    	
//    	for(String key:mapAppname.keySet()){
        	int l_lastHourDataPresent=0;
        	List<Data_points> l_data_points_list = new ArrayList<Data_points>();
        	List<Application_Usage_RollUp5> l_AppUsage5Min_RollUpdata = new ArrayList<Application_Usage_RollUp5>();
        	
        	log.info("All details <l_StartDay ="+l_StartDay+"#l_Starthour="+l_Starthour+"#l_Startminute="+l_Startminute );
        	log.info("All details <l_EndDay ="+l_EndDay+"#l_Endhour="+l_Endhour+"#l_Startminute="+l_Startminute );
        	
        	//Find and populate the start monitoring data from rollup5 table
        	if (l_Startminute != 0)
        	{
        		String l_day_hour_min = l_EndDay+"_"+l_Endhour+"_"+l_Startminute;
        		String l_day_hour = l_EndDay+"_"+l_EndDay;
        		
        		if (!(mapHourlyrange.containsKey(l_day_hour)))
        		{
        			List<Application_Usage_RollUp5> l_AppUsage5Min_RollUpdata_1 = findAndCalculate5MinDataPoint(appDataMonitoringInput.getUserID(),key,l_EndDay,l_Endhour,l_Startminute,60,1);
	    			log.info("Inside  AppAndMSUsageDAOImpl::l_data_points_list length 00000000 <"+l_AppUsage5Min_RollUpdata_1.size()+">");
	    			if (l_AppUsage5Min_RollUpdata_1.size() > 0)
	    			{
	    				String l_data_point= l_EndDay+"_"+l_Endhour;
	    				
	    				Data_points l_data_points_1=populateApplicationWiseMinsDataPoints(l_AppUsage5Min_RollUpdata_1,appDataMonitoringInput.getMetricType(),l_data_point);
	    				l_data_points_list.add(l_data_points_1);
	    			}	
        			
        		}	
        	}
        	
    		App_usage_data l_applicationUsageData = new App_usage_data();
			String l_end_Day_Hour = l_StartDay+"_"+l_Starthour;
			
			//Populate the Data points for app.
			l_data_points_list=populateApplicationWiseHourDataPoints(mapHourlyrange,
											  l_AppUsageHourly,
											  key,
											  appDataMonitoringInput.getUserID(),
											  appDataMonitoringInput.getMetricType(),
											  l_end_Day_Hour);
			
	    	//Find and populate the End monitoring data from rollup5 table
	    	if (l_Startminute != 0)
	    	{
	    		String l_day_hour_min = l_EndDay+"_"+l_Endhour+"_"+l_Startminute;
	    		String l_day_PreviousHour = l_EndDay+"_"+ (l_Endhour-1);
	    		String l_day_Hour = l_EndDay+"_"+ l_Endhour;
	    		String l_day_CurrentHour = l_StartDay+"_"+ l_Starthour;
	    		
	    		log.info("Inside  AppAndMSUsageDAOImpl::l_data_points_list length verify whether last but one data processed or not ");
	    		if (!(l_dataPointProceed.containsKey(l_day_CurrentHour)))
	    		{
	    			l_AppUsage5Min_RollUpdata= findAndCalculate5MinDataPoint(appDataMonitoringInput.getUserID(),key,l_StartDay,(l_Starthour-1),0,60,0);
	    			log.info("Inside  AppAndMSUsageDAOImpl::l_data_points_list length 1111111111 <"+l_AppUsage5Min_RollUpdata.size()+">");
	    			if (l_AppUsage5Min_RollUpdata.size() > 0)
	    			{
	    				String l_data_point= l_StartDay+"_"+(l_Starthour-1);
	    				Data_points l_data_points_1=populateApplicationWiseMinsDataPoints(l_AppUsage5Min_RollUpdata,appDataMonitoringInput.getMetricType(),l_data_point);
	    				l_data_points_list.add(l_data_points_1);
	    			}	
	    		}
	    		else
	    		{
	    			log.info("Inside  AppAndMSUsageDAOImpl::l_data_points_list no need to  verify whether last but one data processed or not "+l_day_PreviousHour);
	    		}
	    		
	    		List<Application_Usage_RollUp5> l_AppUsage5Min_RollUpdata_2= findAndCalculate5MinDataPoint(appDataMonitoringInput.getUserID(),key,l_StartDay,l_Starthour,0,l_Startminute,2);
    			log.info("Inside  AppAndMSUsageDAOImpl::l_data_points_list length 22222 <"+l_AppUsage5Min_RollUpdata_2.size()+">");
    			if (l_AppUsage5Min_RollUpdata_2.size() > 0)
    			{
    				String l_data_point= l_StartDay+"_"+l_Starthour;
	    			Data_points l_data_points_2=populateApplicationWiseMinsDataPoints(l_AppUsage5Min_RollUpdata_2,appDataMonitoringInput.getMetricType(),l_data_point);
	    			l_data_points_list.add(l_data_points_2);
    			}
	    	}
	    	
	    	log.info("Inside  AppAndMSUsageDAOImpl::l_data_points_list length <"+l_data_points_list.size()+">");
			l_applicationUsageData.setAppName(key);
			l_applicationUsageData.setData_points(l_data_points_list);	
			l_aggAppUsagedata_list.add(l_applicationUsageData);
			
			
			//l_AppUsage5Min_RollUpdata.clear();
			//l_data_points_list.clear();
			
			log.info("Inside  AppAndMSUsageDAOImpl::getLastOnedayAppDate all data set ");	
			l_dataPointProceed.clear();
		}
    	l_appDataMonitoring.setApp_usage_data(l_aggAppUsagedata_list);

		log.info("Inside  AppAndMSUsageDAOImpl::getLastOnedayAppDate return ");
		return l_appDataMonitoring;
		
	  } catch (Throwable e) {
		  log.warn("Inside  CreditServiceImpl::AppAndMSUsageServiceImpl some exception happened----< " + e.getMessage() +">");
		  e.printStackTrace();
		  return l_appDataMonitoring;
	  }
	}
    
    private List<Application_Usage_RollUp5> findAndCalculate5MinDataPoint(String l_userId, String l_appName, int l_day, int  l_hour, int l_startMin, int l_endMin, int l_MinLevelCalculationRequired) {
		log.info("Inside  AppAndMSUsageDAOImpl::findAndCalculate5MinDataPoint.<"+l_day+"_"+l_hour+"_"+l_startMin+"_"+l_endMin+l_userId+"#"+l_appName+"#"+l_MinLevelCalculationRequired+">");
		Select select;
		List<Application_Usage_RollUp5> l_AppUsage5Min_RollUpdata = new ArrayList<Application_Usage_RollUp5>();
		List<Application_Usage_RollUp1> l_AppUsage1Min_RollUpdata = new ArrayList<Application_Usage_RollUp1>();
		int l_5MinProcessed=0;
		int l_min = l_startMin;
		try {
			
			if (l_MinLevelCalculationRequired == 0)
			{	
			  l_min= 3;
			}  
			  
			while(true)
			{
				String l_day_hour_min;
				if (l_min == 59)
				{
					l_day_hour_min=l_day+"_"+(l_hour+1)+"_"+0;
				}
				else
				{
					l_day_hour_min=l_day+"_"+l_hour+"_"+l_min;
				}
				//log.info("Inside  AppAndMSUsageDAOImpl::going to call .<"+l_day_hour_min+"#"+l_min+"#"+l_userId+"#"+l_appName+">");
				
		    	select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type","hour", "day_hour_min","metric_value").from("Application_Usage_RollUp5");
		    	select.where(QueryBuilder.eq("day_hour_min",l_day_hour_min))
		    	       .and(QueryBuilder.eq("userId",l_userId)).and(QueryBuilder.eq("appName",l_appName));
		    	select.allowFiltering();
		    	
				log.info("Calling getLastOnedayAppDate :: getDataFromRollUp() for day of year"+l_day_hour_min);
				List<Application_Usage_RollUp5> l_AppUsage5Min_1 = myCassandraTemplate.getDataFromRollUp(select,Application_Usage_RollUp5.class);
				
				if ((l_AppUsage5Min_1.isEmpty()) && (l_5MinProcessed == 0)) 
				{
					if (l_MinLevelCalculationRequired == 1)
					{	
						log.info("Inside calling min level calcultion as .<"+l_day_hour_min+">");
						//String l_day_hour_min=l_day+"_"+l_hour+"_"+l_min;
						List<Application_Usage_RollUp1> l_AppUsage1Min_RollUpdata_2=findAndCalculateMinDataPoint(l_userId,l_appName,l_day_hour_min);
						if (l_AppUsage1Min_RollUpdata_2.size() != 0)
						{
							l_AppUsage1Min_RollUpdata.addAll(l_AppUsage1Min_RollUpdata_2);
						}
					}
					l_min++;
				}
				else
				{
					l_5MinProcessed = 1;
					l_min += 5;
					l_AppUsage5Min_RollUpdata.addAll(l_AppUsage5Min_1);
				}
				
				if (l_min >= l_endMin)
				{
					break;
				}
			}
			log.info("Inside  AppAndMSUsageDAOImpl::findAndCalculate5MinDataPoint retrive data start "+ l_AppUsage5Min_RollUpdata.size());
			List<Application_Usage_RollUp5> l_AppUsage5Min_2 = new ArrayList<Application_Usage_RollUp5>();
			
			if ((l_MinLevelCalculationRequired == 2) )
			{
				int l_count=0;
				if (l_AppUsage5Min_RollUpdata.size() == 0)
				{
					l_count = l_startMin;
				}
				else
				{
					l_count = l_min - 4;
				}
				
				for (int j= l_count; j <l_endMin ;j++)
				{
					String l_day_hour_min=l_day+"_"+l_hour+"_"+j;
					List<Application_Usage_RollUp1> l_AppUsage1Min_RollUpdata_2=findAndCalculateMinDataPoint(l_userId,l_appName,l_day_hour_min);
					if (l_AppUsage1Min_RollUpdata_2.size() != 0)
					{
						l_AppUsage1Min_RollUpdata.addAll(l_AppUsage1Min_RollUpdata_2);
					}
				}
			}
			
			log.info("Inside  AppAndMSUsageDAOImpl::findAndCalculate5MinDataPoint retrive data start l_AppUsage1Min_RollUpdata "+ l_AppUsage1Min_RollUpdata.size());
			for (int j=0;j<l_AppUsage1Min_RollUpdata.size();j++)
			{
				l_AppUsage5Min_2.get(j).setAppName(l_AppUsage1Min_RollUpdata.get(j).getAppName());
				l_AppUsage5Min_2.get(j).setUserId(l_AppUsage1Min_RollUpdata.get(j).getUserId());
				l_AppUsage5Min_2.get(j).setRegion(l_AppUsage1Min_RollUpdata.get(j).getRegion());
				l_AppUsage5Min_2.get(j).setTelcoId(l_AppUsage1Min_RollUpdata.get(j).getTelcoId());
				l_AppUsage5Min_2.get(j).setCloudletId(l_AppUsage1Min_RollUpdata.get(j).getCloudletId());
				l_AppUsage5Min_2.get(j).setMetric_Type(l_AppUsage1Min_RollUpdata.get(j).getMetric_Type());
				l_AppUsage5Min_2.get(j).setMetric_Value(l_AppUsage1Min_RollUpdata.get(j).getMetric_Value());
				l_AppUsage5Min_2.get(j).setDay_Hour_Min(l_AppUsage1Min_RollUpdata.get(j).getDay_Hour_Min());
			}
			if (l_AppUsage1Min_RollUpdata.size()> 0)
			{
				l_AppUsage5Min_RollUpdata.addAll(l_AppUsage5Min_2);
			}	
			
			log.info("Inside  AppAndMSUsageDAOImpl::findAndCalculate5MinDataPoint retrive data start post merging of 1 min data"+ l_AppUsage5Min_RollUpdata.size());
			  } catch (Throwable e) {
			 log.warn("Inside  CreditServiceImpl::AppAndMSUsageServiceImpl some exception happened----< " + e.getMessage() +">");
			e.printStackTrace();
		  }
		  return l_AppUsage5Min_RollUpdata;
	}
    

 
    
    private List<Application_Usage_RollUp1> findAndCalculateMinDataPoint(String l_userId, String l_appName,String l_day_hour_min) {
		log.info("Inside  AppAndMSUsageDAOImpl::findAndCalculateMinDataPoint.<"+l_day_hour_min+">");
		Select select;
		//List<Application_Usage_RollUp1> l_AppUsage1Min_RollUpdata = new ArrayList<Application_Usage_RollUp1>();
		//String l_day_hour_min=l_day+"_"+l_hour+"_"+l_min;
		List<Application_Usage_RollUp1> l_AppUsage1Min_RollUpdata = null;
		
		try {
	    	select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type","day_hour_min","metric_value").from("Application_Usage_RollUp1");
	    	select.where(QueryBuilder.eq("day_hour_min",l_day_hour_min))
	    	       .and(QueryBuilder.eq("userId",l_userId)).and(QueryBuilder.eq("appName",l_appName));
	    	select.allowFiltering();

	    	l_AppUsage1Min_RollUpdata = myCassandraTemplate.getDataFromRollUp(select,Application_Usage_RollUp1.class);
				
			log.info("Inside  AppAndMSUsageDAOImpl::findAndCalculateMinDataPoint retrive data size "+ l_AppUsage1Min_RollUpdata.size());	
			  } catch (Throwable e) {
			 log.warn("Inside  AppAndMSUsageDAOImpl::findAndCalculateMinDataPoint some exception happened----< " + e.getMessage() +">");
			e.printStackTrace();
		  }
		  return l_AppUsage1Min_RollUpdata;
	}

    private List<Data_points>  populateApplicationWiseHourDataPoints(Map<String, Integer> l_mapHourlyrange,
    		 									   List<Application_Usage_RollUp60> l_AppUsageHourly,
										  		   String l_AppName,
										  		   String l_UserID,
										  		   String l_metric_type,
										  		   String l_end_Day_Hour)
    {
    	log.info("Inside  AppAndMSUsageDAOImpl::populateApplicationWiseDataPoints.");
		
    	int l_lastHourDataPresent=0;
    	List<Data_points> l_data_points_list = new ArrayList<Data_points>();
    	Data_points l_data_points=null;


    	try
    	{
    		String l_key ="";
    		for(String key:l_mapHourlyrange.keySet()){
    			
				l_lastHourDataPresent=0;
				
    			for (int i = 0; i < l_AppUsageHourly.size(); i++) 
    			{
    				if (key.equalsIgnoreCase(l_AppUsageHourly.get(i).getDay_hour()))
    				{
    					
    					l_lastHourDataPresent=1;
    					if (!(l_dataPointProceed.containsKey(key)))
    					{
    		    			l_data_points = new Data_points();
    		    			String[] parts = key.split("_");
    		    			String l_DayHourkey = parts[0]+"_"+(Integer.parseInt(parts[1]) - 1);
    		    			log.info("Inside  AppAndMSUsageDAOImpl::populateApplicationWiseDataPoints. matching key>"
    		    			+ key +"<And a new data point is created and key =<"+l_DayHourkey+">");
    		    			l_data_points.setPlotDataPoint(l_DayHourkey);
    		    			l_dataPointProceed.put(key, 1);
    					}
						if ((l_AppUsageHourly.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME"))
							&& ((l_metric_type.equalsIgnoreCase("ALL")) || (l_metric_type.equalsIgnoreCase("APP_LATENCY"))))
						{
							l_data_points.setAppLatency(l_AppUsageHourly.get(i).getMetric_Value());
						}
	
						if ((l_AppUsageHourly.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE"))
							&& ((l_metric_type.equalsIgnoreCase("ALL")) || (l_metric_type.equalsIgnoreCase("CPU_USAGE"))))
						{
							l_data_points.setCpuUsage(l_AppUsageHourly.get(i).getMetric_Value());
						}
						
						if ((l_AppUsageHourly.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE"))
							&& ((l_metric_type.equalsIgnoreCase("ALL")) || (l_metric_type.equalsIgnoreCase("MEMORY_USAGE"))))
						{
							l_data_points.setMemoryUsage(l_AppUsageHourly.get(i).getMetric_Value());
						}
    						
						if ((l_AppUsageHourly.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH"))
							&& ((l_metric_type.equalsIgnoreCase("ALL")) || (l_metric_type.equalsIgnoreCase("SESSION_LENGTH"))))
						{
							l_data_points.setsessionLength(l_AppUsageHourly.get(i).getMetric_Value());
						}
    						
						/*log.info("Inside  AppAndMSUsageDAOImpl::populateApplicationWiseDataPoints.<"+l_AppUsageHourly.get(i).getDay_hour()+">_<"+l_end_Day_Hour+""
    								+ ">_<"+l_AppUsageHourly.get(i).getMetric_Value()+">");
    						if (l_AppUsageHourly.get(i).getDay_hour().equalsIgnoreCase(l_end_Day_Hour))
    						{
    							l_lastHourDataPresent=1;
    						}*/
    				}
    			}
    			if(l_lastHourDataPresent ==1)
    			{
    				l_data_points_list.add(l_data_points);
    			}
    		}
		  } catch (Throwable e) {
			  log.warn("Inside  CreditServiceImpl::populateApplicationWiseDataPoints some exception happened----< " + e.getMessage() +">");
		e.printStackTrace();
	  }
    	return l_data_points_list;
    }
    
    
    private Data_points populateApplicationWiseMinsDataPoints(List<Application_Usage_RollUp5> l_AppUsage5Min_RollUpdata,
    														  String l_metric_type,String l_Plotdatapoint)
    {
    	log.info("Inside  AppAndMSUsageDAOImpl::populateApplicationWiseMinsDataPoints.l_AppUsage5Min_RollUpdata.size()=="+l_AppUsage5Min_RollUpdata.size());
		
    	int l_lastHourDataPresent=0;
    	Data_points l_data_point= new Data_points();
    	double total_cpu_usage=0,total_MemoryUsed=0,total_App_latency=0,total_App_session_length=0;
    	int count_1=0,count_2=0,count_3=0,count_4=0; 
    	
    	try
    	{

    		l_data_point.setPlotDataPoint(l_Plotdatapoint);		

			for (int i = 0; i < l_AppUsage5Min_RollUpdata.size(); i++) {
					if ((l_AppUsage5Min_RollUpdata.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME"))
						&& ((l_metric_type.equalsIgnoreCase("ALL")) || (l_metric_type.equalsIgnoreCase("APP_LATENCY"))))
					{
						//log.warn("AppAndMSUsageDAOImpl::populateApplicationWiseMinsDataPoints< " + l_AppUsage5Min_RollUpdata.get(i).getMetric_Value() +"><"+ l_AppUsage5Min_RollUpdata.get(i).getMetric_Type()+">");
						total_App_latency += Double.parseDouble(l_AppUsage5Min_RollUpdata.get(i).getMetric_Value());
						count_1 += 1;
					}

					if ((l_AppUsage5Min_RollUpdata.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE"))
						&& ((l_metric_type.equalsIgnoreCase("ALL")) || (l_metric_type.equalsIgnoreCase("CPU_USAGE"))))
					{
						//log.warn("AppAndMSUsageDAOImpl::populateApplicationWiseMinsDataPoints< " + l_AppUsage5Min_RollUpdata.get(i).getMetric_Value() +"><"+ l_AppUsage5Min_RollUpdata.get(i).getMetric_Type()+">");
						total_cpu_usage += Double.parseDouble(l_AppUsage5Min_RollUpdata.get(i).getMetric_Value());
						count_2 += 1;
					}
					
					if ((l_AppUsage5Min_RollUpdata.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE"))
						&& ((l_metric_type.equalsIgnoreCase("ALL")) || (l_metric_type.equalsIgnoreCase("MEMORY_USAGE"))))
					{
						//log.warn("AppAndMSUsageDAOImpl::populateApplicationWiseMinsDataPoints< " + l_AppUsage5Min_RollUpdata.get(i).getMetric_Value() +"><"+ l_AppUsage5Min_RollUpdata.get(i).getMetric_Type()+">");
						total_MemoryUsed += Double.parseDouble(l_AppUsage5Min_RollUpdata.get(i).getMetric_Value());
						count_3 += 1;
					}
					
					if ((l_AppUsage5Min_RollUpdata.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH"))
						&& ((l_metric_type.equalsIgnoreCase("ALL")) || (l_metric_type.equalsIgnoreCase("SESSION_LENGTH"))))
					{
						//log.warn("AppAndMSUsageDAOImpl::populateApplicationWiseMinsDataPoints< " + l_AppUsage5Min_RollUpdata.get(i).getMetric_Value() +"><"+ l_AppUsage5Min_RollUpdata.get(i).getMetric_Type()+">");
						total_App_session_length += Double.parseDouble(l_AppUsage5Min_RollUpdata.get(i).getMetric_Value());
						count_4 += 1;
					}

			}
			
		    double avg_app_latency = total_App_latency / count_1;
			double avg_cpu_usage = total_cpu_usage / count_2;
			double avg_MemoryUsed = total_MemoryUsed / count_3;
			double avg_App_see_length = total_App_session_length / count_4;
		
			log.warn("AppAndMSUsageDAOImpl::populateApplicationWiseMinsDataPoints<total_App_latency= "+avg_app_latency
					+"total_cpu_usage="+avg_cpu_usage+"total_MemoryUsed="+avg_MemoryUsed+"total_App_session_length"
					+avg_App_see_length);
			
			l_data_point.setAppLatency(Double.toString(avg_app_latency)); 
			l_data_point.setMemoryUsage(Double.toString(avg_MemoryUsed));
			l_data_point.setCpuUsage(Double.toString(avg_cpu_usage));
			l_data_point.setsessionLength(Double.toString(avg_App_see_length));
				
		  } catch (Throwable e) {
			  log.warn("Inside  CreditServiceImpl::populateApplicationWiseDataPoints some exception happened----< " + e.getMessage() +">");
		e.printStackTrace();
	  }
    	 return l_data_point;
    }
    
    
    @Override
  	public AppDataMonitoring getLastOneHourAppData(AppDataMonitoringInput appDataMonitoringInput,
  												  int l_StartDay,
  												  int l_Starthour,
  												  int l_Startminute)
  	{
      	log.info("AppAndMSUsageDAOImpl::getLastOneHourAppData");
    	AppDataMonitoring l_appDataMonitoring  = new AppDataMonitoring();
    	Select select;
		int l_EndDay = l_StartDay ;
		int l_Endhour = l_Starthour -1 ;
		int l_countHour = 0;
		int l_countDay = l_EndDay;
		
		if (l_Starthour == 0)
		{
			l_EndDay = l_StartDay - 1;
			l_Endhour = 23 ;
		}
		
		List<Application_Usage_RollUp5> l_AppUsageHourly = null;
		List<App_usage_data> l_aggAppUsagedata_list = null;

		try
		{
    	Map<String, Integer> mapAppname = new HashMap<String, Integer>();
    	
    	if ( !(appDataMonitoringInput.getAppName1().isEmpty()) ) {
    		mapAppname.put(appDataMonitoringInput.getAppName1(), 1);
    	}
 
    	if (!(appDataMonitoringInput.getAppName2().isEmpty()) ) {
    		mapAppname.put(appDataMonitoringInput.getAppName2(), 1);
    	}
    	if (!(appDataMonitoringInput.getAppName3().isEmpty()) ) {
    		mapAppname.put(appDataMonitoringInput.getAppName3(), 1);
    	}
		
    	for (String name: mapAppname.keySet()){
            log.info("Calling app names <"+name+">");
    	}
    	
    	//Create and start populate the response structure
    	l_appDataMonitoring.setUserID(appDataMonitoringInput.getUserID());
    	l_aggAppUsagedata_list = new ArrayList<App_usage_data>();
    	
    	for(String key:mapAppname.keySet())
    	{
        	List<Data_points> l_data_points_list = new ArrayList<Data_points>();
        	App_usage_data l_applicationUsageData = new App_usage_data();
        	
        	log.info("All details <l_StartDay ="+l_StartDay+"#l_Starthour="+l_Starthour+"#l_Startminute="+l_Startminute );
        	log.info("All details <l_EndDay ="+l_EndDay+"#l_Endhour="+l_Endhour+"#l_Startminute="+l_Startminute );
        	
        	
        	l_data_points_list = findAndCalculate5And1MinDataPoint(appDataMonitoringInput.getUserID(),key,
																					l_StartDay,l_Starthour,l_EndDay,l_Endhour,l_Startminute,60,
																					appDataMonitoringInput.getMetricType());
		
	
	    	
			l_applicationUsageData.setAppName(key);
			l_applicationUsageData.setData_points(l_data_points_list);	
			l_aggAppUsagedata_list.add(l_applicationUsageData);
			//log.info("Inside  AppAndMSUsageDAOImpl::getLastOnedayAppDate all data set ");	
			l_dataPointProceed.clear();
		}
    	l_appDataMonitoring.setApp_usage_data(l_aggAppUsagedata_list);

		log.info("Inside  AppAndMSUsageDAOImpl::getLastOnedayAppDate return ");
		return l_appDataMonitoring;
		
	  } catch (Throwable e) {
		  log.warn("Inside  CreditServiceImpl::AppAndMSUsageServiceImpl some exception happened----< " + e.getMessage() +">");
		  e.printStackTrace();
		  return l_appDataMonitoring;
	  }
    
    	
  	}
    
	//List<Application_Usage_RollUp5> l_AppUsage5Min_RollUpdata_1 = findAndCalculate5And1MinDataPoint(appDataMonitoringInput.getUserID(),key,
	//		l_StartDay,l_Starthour,l_EndDay,l_Endhour,l_Startminute,60);
	
    private List<Data_points> findAndCalculate5And1MinDataPoint(String l_userId, String l_appName, int l_StartDay, int  l_Starthour,
    																		  int l_EndDay, int l_Endhour, int l_startMin,int l_HourMin,
    																		  String l_metric_type) {
    	
  		//log.info("Inside  AppAndMSUsageDAOImpl::findAndCalculate5And1MinDataPoint.<"+l_StartDay+"#_#"+l_Starthour+"#_#"+l_EndDay+"#_#"+l_Endhour
  		//																			+l_startMin+"#_#"+l_appName+"#_#"+l_HourMin+">");
  		Select select;
  		//List<Application_Usage_RollUp5> l_AppUsage5Min_RollUpdata = new ArrayList<Application_Usage_RollUp5>();
  		
  		
  		List<Data_points> l_data_points_list = new ArrayList<Data_points>();
  		List<Application_Usage_RollUp1> l_AppUsage1Min_RollUpdata = new ArrayList<Application_Usage_RollUp1>();
  		
  		int l_5MinProcessed=0;
  		int l_min = l_startMin;
  		int l_count = l_startMin;
  		int l_startMinFor1Min = -99;
  		try {
  			while(true)
  			{
  				String l_day_hour_min;
  			  	  			
  				if (l_min > 59)
  				{
  					l_min = l_min - 59;
  					if (l_StartDay == l_EndDay)
  					{
  						l_day_hour_min = l_EndDay+"_"+l_Starthour+"_"+l_min;
  					}
  					else
  					{
  						l_day_hour_min = l_StartDay+"_"+l_Starthour+"_"+l_min;
  					}
  					l_Endhour = l_Starthour;
  					l_EndDay  = l_StartDay;
  				}
  				else
  				{
  					l_day_hour_min = l_EndDay+"_"+l_Endhour+"_"+l_min;
  				}
  				
  				//log.info("Calling getLastOnedayAppDate :: findAndCalculate5And1MinDataPoint() for day of year<"+l_day_hour_min+">");
  				
  		    	select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type","hour", "day_hour_min","metric_value").from("Application_Usage_RollUp5");
  		    	select.where(QueryBuilder.eq("day_hour_min",l_day_hour_min))
  		    	       .and(QueryBuilder.eq("userId",l_userId)).and(QueryBuilder.eq("appName",l_appName));
  		    	select.allowFiltering();
  		    	
  				
  				List<Application_Usage_RollUp5> l_AppUsage5Min_1 = myCassandraTemplate.getDataFromRollUp(select,Application_Usage_RollUp5.class);
  				
  				if ((l_AppUsage5Min_1.isEmpty()) && (l_5MinProcessed == 0)) 
  				{
					//log.info("Inside calling min level calcultion as .<"+l_day_hour_min+">");
					List<Application_Usage_RollUp1> l_AppUsage1Min_RollUpdata_2=findAndCalculateMinDataPoint(l_userId,l_appName,l_day_hour_min);
					if (l_AppUsage1Min_RollUpdata_2.size() != 0)
					{
						if (l_startMinFor1Min ==-99)
						{
							l_startMinFor1Min=l_min;
						}
						l_AppUsage1Min_RollUpdata.addAll(l_AppUsage1Min_RollUpdata_2);
						//log.info("total size of  .<"+l_AppUsage1Min_RollUpdata.size()+">");
					}
  					l_min++;
  					l_count++;
  				}
  				else
  				{
  					if (l_5MinProcessed == 0)
  					{
  						List<Application_Usage_RollUp5> l_AppUsage5Min_2 = new ArrayList<Application_Usage_RollUp5>();
  						//log.info("size of  .<"+l_AppUsage1Min_RollUpdata.size()+">");
  			  			for (int j=0;j<l_AppUsage1Min_RollUpdata.size();j++)
  			  			{
  			  			    Application_Usage_RollUp5 l_AppUsage5MinObj = new Application_Usage_RollUp5();
  			  			    
  			  			    l_AppUsage5MinObj.setAppName(l_AppUsage1Min_RollUpdata.get(j).getAppName());
  			  			    l_AppUsage5MinObj.setUserId(l_AppUsage1Min_RollUpdata.get(j).getUserId());
  			  			    l_AppUsage5MinObj.setRegion(l_AppUsage1Min_RollUpdata.get(j).getRegion());
  			  			    l_AppUsage5MinObj.setTelcoId(l_AppUsage1Min_RollUpdata.get(j).getTelcoId());
  			  			    l_AppUsage5MinObj.setCloudletId(l_AppUsage1Min_RollUpdata.get(j).getCloudletId());
  			  			    l_AppUsage5MinObj.setMetric_Type(l_AppUsage1Min_RollUpdata.get(j).getMetric_Type());
  			  			    l_AppUsage5MinObj.setMetric_Value(l_AppUsage1Min_RollUpdata.get(j).getMetric_Value());
  			  			    l_AppUsage5MinObj.setDay_Hour_Min(l_EndDay+"_"+l_Endhour+"_"+l_startMin);
  			  			    l_AppUsage5Min_2.add(l_AppUsage5MinObj);
  			  			}
  			  		    log.info("Inside  l_AppUsage5Min_2 size "+ l_AppUsage5Min_2.size());
  			  		    if (l_AppUsage5Min_2.size() > 0)
  			  		    {   
  		  					String l_day_hour_min_1=l_EndDay+"_"+l_Endhour+"_"+l_startMinFor1Min;
  			  		    	Data_points  l_data_point = populateApplicationWiseMinsDataPoints(l_AppUsage5Min_2,l_metric_type,l_day_hour_min_1);
  			  		    	l_data_points_list.add(l_data_point);
  			  		    }
  					}
  					else
  					{
  						if (l_AppUsage5Min_1.size() != 0)
  						{
  							Data_points  l_data_point = populateApplicationWiseMinsDataPoints(l_AppUsage5Min_1,l_metric_type,l_day_hour_min);
  							l_data_points_list.add(l_data_point);
  						}
  					}
  					
  					
  					l_5MinProcessed = 1;
  					l_min += 5;
  					l_count +=5;
  					
					
  					//l_AppUsage5Min_RollUpdata.addAll(l_AppUsage5Min_1);
  				}
  				
  				if (l_count > (l_startMin + l_HourMin))
  				{
  					break;
  				}
  			}
  			//log.info("Inside  AppAndMSUsageDAOImpl::findAndCalculate5MinDataPoint retrive data start "+ l_AppUsage5Min_RollUpdata.size());
  	/*		List<Application_Usage_RollUp5> l_AppUsage5Min_2 = new ArrayList<Application_Usage_RollUp5>();
  			
  			if ((l_MinLevelCalculationRequired == 2) )
  			{
  				int l_count=0;
  				if (l_AppUsage5Min_RollUpdata.size() == 0)
  				{
  					l_count = l_startMin;
  				}
  				else
  				{
  					l_count = l_min - 4;
  				}
  				
  				for (int j= l_count; j <l_endMin ;j++)
  				{
  					List<Application_Usage_RollUp1> l_AppUsage1Min_RollUpdata_2=findAndCalculateMinDataPoint(l_userId,l_appName,l_day,l_hour,j);
  					if (l_AppUsage1Min_RollUpdata_2.size() != 0)
  					{
  						l_AppUsage1Min_RollUpdata.addAll(l_AppUsage1Min_RollUpdata_2);
  					}
  				}
  			}
  			
  			log.info("Inside  AppAndMSUsageDAOImpl::findAndCalculate5MinDataPoint retrive data start l_AppUsage1Min_RollUpdata "+ l_AppUsage1Min_RollUpdata.size());
  			for (int j=0;j<l_AppUsage1Min_RollUpdata.size();j++)
  			{
  				l_AppUsage5Min_2.get(j).setAppName(l_AppUsage1Min_RollUpdata.get(j).getAppName());
  				l_AppUsage5Min_2.get(j).setUserId(l_AppUsage1Min_RollUpdata.get(j).getUserId());
  				l_AppUsage5Min_2.get(j).setRegion(l_AppUsage1Min_RollUpdata.get(j).getRegion());
  				l_AppUsage5Min_2.get(j).setTelcoId(l_AppUsage1Min_RollUpdata.get(j).getTelcoId());
  				l_AppUsage5Min_2.get(j).setCloudletId(l_AppUsage1Min_RollUpdata.get(j).getCloudletId());
  				l_AppUsage5Min_2.get(j).setMetric_Type(l_AppUsage1Min_RollUpdata.get(j).getMetric_Type());
  				l_AppUsage5Min_2.get(j).setMetric_Value(l_AppUsage1Min_RollUpdata.get(j).getMetric_Value());
  				l_AppUsage5Min_2.get(j).setDay_Hour_Min(l_AppUsage1Min_RollUpdata.get(j).getDay_Hour_Min());
  			}
  			if (l_AppUsage1Min_RollUpdata.size()> 0)
  			{
  				l_AppUsage5Min_RollUpdata.addAll(l_AppUsage5Min_2);
  			}	
  			*/
  			log.info("Inside  AppAndMSUsageDAOImpl::findAndCalculate5And1MinDataPoint size of l_data_points_list"+ l_data_points_list.size());
  			  } catch (Throwable e) {
  			 log.warn("Inside  CreditServiceImpl::findAndCalculate5And1MinDataPoint some exception happened----< " + e.getMessage() +">");
  			e.printStackTrace();
  		  }
  		  return l_data_points_list;
  	}
    

    @Override
    public AppDataMonitoring getPreviousDaysAppData(AppDataMonitoringInput appDataMonitoringInput, int endDay, int endHour, int endMin, int year, int interval) {
       AppDataMonitoring l_appDataMonitoring  = new AppDataMonitoring();
       Map<String, Integer> appNameMap = new HashMap<String, Integer>();
    
       Select select;
            
       int startMin = 0;
       int startHour = 0;
       int startDay = 0;
    
       boolean isLeapYear = false;
                              
           if ((year - 1) % 100 == 0) {
                  if ((year - 1) % 4 == 0) {
                        isLeapYear = true;
                  }
           }
           else if ((year - 1) % 4 == 0) {
                  isLeapYear = true;
           }
    
           startDay = endDay - (interval - 2);
           
           if (startDay <= 0 && isLeapYear) {
                  endDay = endDay + 366;
           }
           else if (startDay <= 0 && ! isLeapYear) {
                  endDay = endDay + 355;
           }
           
           if (endHour == 0 && endMin == 0) {
                  startDay = endDay - interval;
           }
           else {
                  startDay = endDay - (interval - 2);
           }
                   
           try {
                  if (!(appDataMonitoringInput.getAppName1().isEmpty())) {
                        appNameMap.put(appDataMonitoringInput.getAppName1(), 1);
                  }
                  if (!(appDataMonitoringInput.getAppName2().isEmpty())) {
                        appNameMap.put(appDataMonitoringInput.getAppName2(), 1);
                  }
                  if (!(appDataMonitoringInput.getAppName2().isEmpty())) {
                        appNameMap.put(appDataMonitoringInput.getAppName3(), 1);
                  }

                  for (String name: appNameMap.keySet()){
                        log.info("Calling app names <" + name + ">");
                  }
          
                  l_appDataMonitoring.setUserID(appDataMonitoringInput.getUserID());
                  List<App_usage_data> l_aggAppUsagedata_list = new ArrayList<App_usage_data>();

                  for(String key:appNameMap.keySet()) {
    
                        List<Data_points> l_dataPoints = new ArrayList<Data_points>();
                        List<Application_Usage_RollUp5> l_endRollUpData = new LinkedList<Application_Usage_RollUp5>();
                        List<Application_Usage_RollUp5> l_startRollUpData = new LinkedList<Application_Usage_RollUp5>();
                        
                        App_usage_data applicationUsageData = new App_usage_data();
                        
                        List<Application_Usage_RollUp1440> l_appUsage1440 = new LinkedList<Application_Usage_RollUp1440>();
                        
                        boolean lastDayPresent = false;
                        boolean lastHourPresent = false;
                              
                        for (int dayCount = startDay; dayCount <= endDay; dayCount ++) {
                 
                               int tempVar = dayCount;
                                  
                               if (dayCount > 366 && isLeapYear) {
                                      dayCount = dayCount - 366;
                               }
                               else if (dayCount > 365 && ! isLeapYear) {
                                      dayCount = dayCount - 365;
                               }
                               
                               log.info("Checking application_usage_rollup1440 for data");

                               select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type", "metric_value", "day").from("application_usage_rollup1440");
                               select.where(QueryBuilder.eq("day", dayCount)).and(QueryBuilder.eq("appname", key)).and(QueryBuilder.eq("userid", appDataMonitoringInput.getUserID()));
                               select.allowFiltering();

                               List<Application_Usage_RollUp1440> appUsage1440 = myCassandraTemplate.getDataFromRollUp(select, Application_Usage_RollUp1440.class);
    
                               if (tempVar == endDay && appUsage1440.isEmpty()) {
                                      lastDayPresent = false;
                               }
                               else {
                                      lastDayPresent = true;
                               }

                               if (! appUsage1440.isEmpty()) {
                                      log.info("Data found in application_usage_rollup1440 for day " + (dayCount - 1));
                                      Data_points dataPoints = populateApplicationWiseDayDataPoints(appDataMonitoringInput.getMetricType(), appUsage1440, (dayCount - 1));
                                      l_dataPoints.add(dataPoints);
                               }
                               else {
                                      log.info("No data in application_usage_rollup1440 for day " + (dayCount - 1));
                               }
                        }
                             
                if (! lastDayPresent) {
                       
                 int dayVar = endDay;

                 if (isLeapYear && endDay > 366) {
                        dayVar = endDay - 366;
                }
                else if (! isLeapYear && endDay > 365) {
                        dayVar = endDay - 365;
                }
                      
                 log.info("Checking application_usage_rollup60 for data for day " + (dayVar - 1));
                
                 select = QueryBuilder.select("metric_value", "userId", "appName", "region", "telcoId", "cloudletid", "metric_type", "day", "day_hour").from("Application_Usage_RollUp60");
                select.where(QueryBuilder.eq("day", (dayVar - 1))).and(QueryBuilder.eq("appname", key)).and(QueryBuilder.eq("userid", appDataMonitoringInput.getUserID()));
                select.allowFiltering();

                List<Application_Usage_RollUp60> l_appUsage60 = myCassandraTemplate.getDataFromRollUp(select, Application_Usage_RollUp60.class);
                           
                 Application_Usage_RollUp1440 appUsage1440 = new Application_Usage_RollUp1440();
                                                                                               
                 for (Application_Usage_RollUp60 appUsage60 : l_appUsage60) {
                        if (appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME") || appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE")
                                      || appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE") || appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH")) {
                                     
                               appUsage1440.setAppName(appUsage60.getAppName());

                               appUsage1440.setCloudletId(appUsage60.getCloudletId());
                               appUsage1440.setRegion(appUsage60.getRegion());
                               appUsage1440.setTelcoId(appUsage60.getTelcoId());
                               appUsage1440.setUserId(appUsage60.getUserId());
                               appUsage1440.setMetric_Type(appUsage60.getMetric_Type());
                               appUsage1440.setDay(appUsage60.getDay() + 1);
                               appUsage1440.setMetric_Value(appUsage60.getMetric_Value());

                               l_appUsage1440.add(appUsage1440);
                        }
                }
                              
                 if (! l_appUsage1440.isEmpty()) {
                        log.info("Data found in application_usage_rollup60 for day " + (dayVar - 1));
                        Data_points dataPoints = populateApplicationWiseDayDataPoints(appDataMonitoringInput.getMetricType(), l_appUsage1440, (dayVar - 1));
                        l_dataPoints.add(dataPoints);
                }
                else {
                        log.info("No data in application_usage_rollup60 for day " + (dayVar - 1));
                }
                }
                        
                if (endHour != 0) {
                
                 log.info("Checking hourwise data for day " + endDay);
                
                 //End hours of the period
                for (startHour = 0; startHour <= endHour; startHour ++) {
                        String dayHour = endDay + "_" + startHour;
                              
                        if (isLeapYear && endDay > 366) {
                               dayHour = (endDay - 366) + "_" + startHour;
                        }
                        else if (! isLeapYear && endDay > 365) {
                               dayHour = (endDay - 365) + "_" + startHour;
                        }
    
                        select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type", "metric_value", "day", "day_hour").from("application_usage_rollup60");
                        select.where(QueryBuilder.eq("day_hour", dayHour)).and(QueryBuilder.eq("appname", key)).and(QueryBuilder.eq("userid", appDataMonitoringInput.getUserID()));
                        select.allowFiltering();
    
                        List<Application_Usage_RollUp60> l_appUsage60 = myCassandraTemplate.getDataFromRollUp(select, Application_Usage_RollUp60.class);
    
                        if (startHour == endHour && l_appUsage60.isEmpty()) {
                               lastHourPresent = false;
                        }
                        else {
                               lastHourPresent = true;
                        }
                                               
                        for (Application_Usage_RollUp60 appUsage60 : l_appUsage60) {
            
                               if (appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME") || appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE")
                                             || appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE") || appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH")) {
    
                                      Application_Usage_RollUp5 ruData = new Application_Usage_RollUp5();
                                                                
                                      ruData.setAppName(appUsage60.getAppName());
                                      ruData.setCloudletId(appUsage60.getCloudletId());
                                      ruData.setRegion(appUsage60.getRegion());
                                      ruData.setTelcoId(appUsage60.getTelcoId());
                                      ruData.setUserId(appUsage60.getUserId());
                                      ruData.setMetric_Type(appUsage60.getMetric_Type());
                                      ruData.setDay_Hour_Min(endDay + "_" + endHour);
                                      ruData.setMetric_Value(appUsage60.getMetric_Value());
                                            
                                      l_endRollUpData.add(ruData);
                               }
                        }
                }
                     
                 log.info("Checking hourwise data for day " + (endDay - interval));
                //Start hours of the period
                for (startHour = (endHour + 2); startHour < 24 ; startHour++) {
                              
                        String dayHour = "";
                        int dayVar = endDay - interval;
                        int hourVar = startHour;
                              
                        if (isLeapYear && dayVar > 366) {
                               dayVar = endDay - 366;
                        }
                        else if (! isLeapYear && dayVar > 365) {
                               dayVar = endDay - 365;
                        }
                              
                        dayHour = dayVar + "_" + hourVar;
    
                        select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type", "metric_value", "day", "day_hour").from("application_usage_rollup60");
                        select.where(QueryBuilder.eq("day_hour", dayHour)).and(QueryBuilder.eq("appname", key)).and(QueryBuilder.eq("userid", appDataMonitoringInput.getUserID()));
                        select.allowFiltering();
                                   
                        List<Application_Usage_RollUp60> l_appUsage60 = myCassandraTemplate.getDataFromRollUp(select, Application_Usage_RollUp60.class);
                                   
                        for (Application_Usage_RollUp60 appUsage60 : l_appUsage60) {
                               if (appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME") || appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE")
                                             || appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE") || appUsage60.getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH")) {
                                                       
                                      Application_Usage_RollUp5 ruData = new Application_Usage_RollUp5();
                                                
                                      ruData.setAppName(appUsage60.getAppName());
                                      ruData.setCloudletId(appUsage60.getCloudletId());
                                      ruData.setRegion(appUsage60.getRegion());
                                      ruData.setTelcoId(appUsage60.getTelcoId());
                                      ruData.setUserId(appUsage60.getUserId());
                                      ruData.setMetric_Type(appUsage60.getMetric_Type());
                                      ruData.setDay_Hour_Min((endDay - interval) + "_" + endHour + "_" + endMin);
                                      ruData.setMetric_Value(appUsage60.getMetric_Value());
                                                
                                      l_startRollUpData.add(ruData);
                               }
                        }
                }
                 
                 
                 log.info("Checking data for hour " + (endHour - 1) + " in application_usage_rollup5 table");
                if (! lastHourPresent) {
                        startMin = 0;
                        int rec = 0;
                        while (startMin < 60) {
                               int dayVar = endDay;
                               if (isLeapYear && dayVar > 366) {
                                      dayVar = endDay - 366;
                               }
                               else if (! isLeapYear && dayVar > 365) {
                                      dayVar = endDay - 365;
                               }
                               
                               String dayHourMin = dayVar + "_" + (endHour - 1) + "_" + startMin;
    
                               select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type", "metric_value", "hour", "day_hour_min").from("application_usage_rollup5");
                               select.where(QueryBuilder.eq("day_hour_min", dayHourMin)).and(QueryBuilder.eq("appname", key)).and(QueryBuilder.eq("userid", appDataMonitoringInput.getUserID()));
                               select.allowFiltering();
                                           
                               List<Application_Usage_RollUp5> l_appRollUp5 = myCassandraTemplate.getDataFromRollUp(select, Application_Usage_RollUp5.class);
                                          
                               for (Application_Usage_RollUp5 appRollUp5 : l_appRollUp5) {
    
                                      if (appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME") || appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE")
                                                   || appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE") || appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH")) {
                                                                                                   
                                             Application_Usage_RollUp5 ruData = new Application_Usage_RollUp5();
    
                                             ruData.setAppName(appRollUp5.getAppName());
                                             ruData.setCloudletId(appRollUp5.getCloudletId());
                                             ruData.setRegion(appRollUp5.getRegion());
                                             ruData.setTelcoId(appRollUp5.getTelcoId());
                                             ruData.setUserId(appRollUp5.getUserId());
                                             ruData.setMetric_Type(appRollUp5.getMetric_Type());
                                             ruData.setDay_Hour_Min(dayVar + "_" + endHour + "_" + endMin);
                                             ruData.setMetric_Value(appRollUp5.getMetric_Value());
                                                                                               
                                             l_endRollUpData.add(ruData);
    
                                             rec = 1;
                                      }
                               }
                               if (l_appRollUp5.isEmpty() && rec == 0) {
                                      startMin = startMin + 1;
                               }
                               else if (rec == 1) {
                                      startMin = startMin + 5;
                               }
                        }
                }
                }
                
                
                log.info("Checking for minutewise data for hour " + endHour + " in application_usage_rollup5 table for day " + endDay);
                //End mins
                if (endMin != 0) {
                startMin = 0;
                int lastMinProcessed = 0;
                int rec = 0;
                            
                 while (startMin <= endMin) {
                        int dayVar = endDay;
                        
                        if (isLeapYear && dayVar > 366) {
                               dayVar = endDay - 366;
                        }
                        else if (! isLeapYear && dayVar > 365) {
                               dayVar = endDay - 365;
                        }
                        
                        String dayHourMin = dayVar + "_" + endHour + "_" + startMin;
    
                        select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type", "metric_value", "hour", "day_hour_min").from("application_usage_rollup5");
                        select.where(QueryBuilder.eq("day_hour_min", dayHourMin)).and(QueryBuilder.eq("appname", key)).and(QueryBuilder.eq("userid", appDataMonitoringInput.getUserID()));
                        select.allowFiltering();
                                   
                        List<Application_Usage_RollUp5> l_appRollUp5 = myCassandraTemplate.getDataFromRollUp(select, Application_Usage_RollUp5.class);
                                   
                        for (Application_Usage_RollUp5 appRollUp5 : l_appRollUp5) {
                               if (appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME") || appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE")
                                             || appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE") || appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH")) {
                                                 
                                      Application_Usage_RollUp5 ruData = new Application_Usage_RollUp5();
                                                 
                                      ruData.setAppName(appRollUp5.getAppName());
                                      ruData.setCloudletId(appRollUp5.getCloudletId());
                                      ruData.setRegion(appRollUp5.getRegion());
                                      ruData.setTelcoId(appRollUp5.getTelcoId());
                                      ruData.setUserId(appRollUp5.getUserId());
                                      ruData.setMetric_Type(appRollUp5.getMetric_Type());
                                      ruData.setDay_Hour_Min(dayVar + "_" + endHour + "_" + endMin);
                                      ruData.setMetric_Value(appRollUp5.getMetric_Value());
    
                                      l_endRollUpData.add(ruData);
                                                 
                                      rec = 1;
                               }
                        }
                                   
                        if (l_appRollUp5.isEmpty() && rec == 0) {
                               startMin = startMin + 1;
                        }
                        else if (rec == 1) {
                               lastMinProcessed = startMin;
                               startMin = startMin + 5;
                        }
                }
                    
                 log.info("Checking for minutewise data for hour " + endHour + " in application_usage_rollup5 table for day " + (endDay - interval));
                //Start mins
                int startProcessed = 0;
                startMin = endMin;
                rec = 0;
                            
                 while (startMin <= 60) {
                        
                        int dayVar = endDay;
                        
                        if (isLeapYear && dayVar > 366) {
                               dayVar = endDay - 366;
                        }
                        else if (! isLeapYear && dayVar > 365) {
                               dayVar = endDay - 365;
                        }
                        
                        String dayHourMin = dayVar + "_" + endHour + "_" + startMin;
    
                        select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type", "metric_value", "hour", "day_hour_min").from("application_usage_rollup5");
                        select.where(QueryBuilder.eq("day_hour_min", dayHourMin)).and(QueryBuilder.eq("appname", key)).and(QueryBuilder.eq("userid", appDataMonitoringInput.getUserID()));
                        select.allowFiltering();
    
                        List<Application_Usage_RollUp5> l_appRollUp5 = myCassandraTemplate.getDataFromRollUp(select, Application_Usage_RollUp5.class);
    
                        for (Application_Usage_RollUp5 appRollUp5 : l_appRollUp5) {
                               if (appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME") || appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE")
                                             || appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE") || appRollUp5.getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH")) {
                                       
                                      Application_Usage_RollUp5 ruData = new Application_Usage_RollUp5();
                                          
                                      if ((startMin - endMin) < 5 && (startMin - endMin) > 0) {
                                             startProcessed = startMin;
                                      }
                                      else {
                                             ruData.setAppName(appRollUp5.getAppName());
                                             ruData.setCloudletId(appRollUp5.getCloudletId());
                                             ruData.setRegion(appRollUp5.getRegion());
                                             ruData.setTelcoId(appRollUp5.getTelcoId());
                                             ruData.setUserId(appRollUp5.getUserId());
                                             ruData.setMetric_Type(appRollUp5.getMetric_Type());
                                             ruData.setDay_Hour_Min(endDay + "_" + endHour + "_" + endMin);
                                             ruData.setMetric_Value(appRollUp5.getMetric_Value());
    
                                             l_startRollUpData.add(ruData);
    
                                             rec = 1;
                                      }
                               }      
                        }
                                   
                        if (l_appRollUp5.isEmpty() && rec == 0) {
                               startMin = startMin + 1;
                        }
                        else if (rec == 1) {
                               startMin = startMin + 5;
                        }
                }
                    
                 log.info("Checking for minutewise data for hour " + endHour + " in application_usage_rollup1 table for day " + endDay);
                //last 4 mins if not considered
                if ((endMin - lastMinProcessed) > 0) {
                        while (lastMinProcessed <= endMin) {
                               int dayVar = endDay;
                               
                               if (isLeapYear && dayVar > 366) {
                                      dayVar = endDay - 366;
                               }
                               else if (! isLeapYear && dayVar > 365) {
                                      dayVar = endDay - 365;
                               }
                               
                               String dayHourMin = dayVar + "_" + endHour + "_" + lastMinProcessed;
    
                               select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type", "metric_value", "min", "day_hour_min").from("application_usage_rollup1");
                               select.where(QueryBuilder.eq("day_hour_min", dayHourMin)).and(QueryBuilder.eq("appname", key)).and(QueryBuilder.eq("userid", appDataMonitoringInput.getUserID()));
                               select.allowFiltering();
    
                               List<Application_Usage_RollUp1> l_appRollUp1 = myCassandraTemplate.getDataFromRollUp(select, Application_Usage_RollUp1.class);
    
                               for (Application_Usage_RollUp1 appRollUp1 : l_appRollUp1) {
                                      if (appRollUp1.getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME") || appRollUp1.getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE")
                                                   || appRollUp1.getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE") || appRollUp1.getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH")) {
       
                                             Application_Usage_RollUp5 ruData = new Application_Usage_RollUp5();
    
                                             ruData.setAppName(appRollUp1.getAppName());
                                             ruData.setCloudletId(appRollUp1.getCloudletId());
                                             ruData.setRegion(appRollUp1.getRegion());
                                             ruData.setTelcoId(appRollUp1.getTelcoId());
                                             ruData.setUserId(appRollUp1.getUserId());
                                             ruData.setMetric_Type(appRollUp1.getMetric_Type());
                                             ruData.setDay_Hour_Min(endDay + "_" + endHour + "_" + endMin);
                                             ruData.setMetric_Value(appRollUp1.getMetric_Value());
    
                                             l_endRollUpData.add(ruData);
                                      }
                               }
                               lastMinProcessed = lastMinProcessed + 1;
                        }
                }
                    
                 log.info("Checking for minutewise data for hour " + endHour + " in application_usage_rollup1 table for day " + (endDay - interval));
                //First 4 mins if not processed
                if (startProcessed != 0) {
                        startMin = endMin;
                                   
                        while (startMin <= startProcessed) {
                               int dayVar = endDay;
                               
                               if (isLeapYear && dayVar > 366) {
                                      dayVar = endDay - 366;
                               }
                               else if (! isLeapYear && dayVar > 365) {
                                      dayVar = endDay - 365;
                               }
                               String dayHourMin = dayVar + "_" + endHour + "_" + startMin;
    
                               select = QueryBuilder.select("userid", "appname", "region", "telcoid", "cloudletid", "metric_type", "metric_value", "min", "day_hour_min").from("application_usage_rollup1");
                               select.where(QueryBuilder.eq("day_hour_min", dayHourMin)).and(QueryBuilder.eq("appname", key)).and(QueryBuilder.eq("userid", appDataMonitoringInput.getUserID()));
                               select.allowFiltering();
    
                               List<Application_Usage_RollUp1> l_appRollUp1 = myCassandraTemplate.getDataFromRollUp(select, Application_Usage_RollUp1.class);
    
                               for (Application_Usage_RollUp1 appRollUp1 : l_appRollUp1) {
                                      if (appRollUp1.getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME") || appRollUp1.getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE")
                                                   || appRollUp1.getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE") || appRollUp1.getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH")) {
                                                        
                                             Application_Usage_RollUp5 ruData = new Application_Usage_RollUp5();
    
                                             ruData.setAppName(appRollUp1.getAppName());
                                             ruData.setCloudletId(appRollUp1.getCloudletId());
                                             ruData.setRegion(appRollUp1.getRegion());
                                             ruData.setTelcoId(appRollUp1.getTelcoId());
                                             ruData.setUserId(appRollUp1.getUserId());
                                             ruData.setMetric_Type(appRollUp1.getMetric_Type());
                                             ruData.setDay_Hour_Min(endDay + "_" + endHour + "_" + endMin);
                                             ruData.setMetric_Value(appRollUp1.getMetric_Value());
    
                                             l_startRollUpData.add(ruData);
                                      }
                               }
                               startMin = startMin + 1;
                        }
                }
                }
                          
                if (! l_endRollUpData.isEmpty()) {
                Data_points dataPoints = populateApplicationMinsDataPointsDay(l_endRollUpData, appDataMonitoringInput.getMetricType(), endDay);
                l_dataPoints.add(dataPoints);
                }
    
                if (! l_startRollUpData.isEmpty()) {
                Data_points dataPoints = populateApplicationMinsDataPointsDay(l_endRollUpData, appDataMonitoringInput.getMetricType(), (endDay - interval));
                l_dataPoints.add(dataPoints);
                }
                          
                applicationUsageData.setAppName(key);
                applicationUsageData.setData_points(l_dataPoints);
                l_aggAppUsagedata_list.add(applicationUsageData);
                    
                  }
                  l_appDataMonitoring.setApp_usage_data(l_aggAppUsagedata_list);
           }
           catch (Exception exception) {
                  log.error("An exception occurred in App Data Monitoring. " + exception.getMessage());
           }
                   
           return l_appDataMonitoring;
                   
    }
         
         
    private Data_points populateApplicationWiseDayDataPoints(String metric_Type, List<Application_Usage_RollUp1440> l_appUsage1440, int day) {
            
       log.info("Inside  AppAndMSUsageDAOImpl::populateApplicationWiseDayDataPoints.");
       log.info("Plotting data points for day " + day);
       
       Data_points dataPoints = new Data_points();
       double total_cpu_usage = 0, total_MemoryUsed = 0, total_App_latency = 0, total_App_session_length = 0;
       int appLatCount = 0, cpuUsageCount = 0, memUsageCount = 0, appSessLth = 0;
    
       try {
              dataPoints.setPlotDataPoint(Integer.toString(day));
                         
              for (Application_Usage_RollUp1440 appUsage1440 : l_appUsage1440) {
                  
                     if ((appUsage1440.getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME"))
                                  && ((metric_Type.equalsIgnoreCase("ALL")) || (metric_Type.equalsIgnoreCase("APP_LATENCY")))) {
                           total_App_latency += Double.parseDouble(appUsage1440.getMetric_Value());
                           appLatCount += 1;
                     }
    
                     if ((appUsage1440.getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE"))
                                  && ((metric_Type.equalsIgnoreCase("ALL")) || (metric_Type.equalsIgnoreCase("CPU_USAGE")))) {
                           total_cpu_usage += Double.parseDouble(appUsage1440.getMetric_Value());
                           cpuUsageCount += 1;
                     }
    
                     if ((appUsage1440.getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE"))
                                  && ((metric_Type.equalsIgnoreCase("ALL")) || (metric_Type.equalsIgnoreCase("MEMORY_USAGE")))) {
                           total_MemoryUsed += Double.parseDouble(appUsage1440.getMetric_Value());
                           memUsageCount += 1;
                     }
    
                     if ((appUsage1440.getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH"))
                                  && ((metric_Type.equalsIgnoreCase("ALL")) || (metric_Type.equalsIgnoreCase("SESSION_LENGTH")))) {
                           total_App_session_length += Double.parseDouble(appUsage1440.getMetric_Value());
                           appSessLth += 1;
                     }
              }
                                 
              double avg_cpu_usage = total_cpu_usage / cpuUsageCount;
              double avg_MemoryUsed = total_MemoryUsed / memUsageCount;
              double avg_App_sess_length = total_App_session_length / appSessLth;
              double avg_app_latency = total_App_latency / appLatCount;
    
              dataPoints.setAppLatency(Double.toString(avg_app_latency)); 
              dataPoints.setMemoryUsage(Double.toString(avg_MemoryUsed));
              dataPoints.setCpuUsage(Double.toString(avg_cpu_usage));
              dataPoints.setsessionLength(Double.toString(avg_App_sess_length));
       }
       catch (Exception exception) {
              log.error("An exception occurred in calculating data points for day : " + exception.getMessage());
       }
            
       return dataPoints;
    }
         
         
    private Data_points populateApplicationMinsDataPointsDay(List<Application_Usage_RollUp5> l_AppUsage5Min_RollUpdata, String metric_type,int day) {
       log.info("Inside  AppAndMSUsageDAOImpl::populateApplicationMinsDataPointsDay.");
       log.info("Plotting data points for day " + day);
    
       Data_points dataPoint= new Data_points();
       double total_cpu_usage=0,total_MemoryUsed=0,total_App_latency=0,total_App_session_length=0;
       int appLatCount = 0, cpuUsageCount = 0, memUsageCount = 0, appSessLth = 0;
    
       try {
              dataPoint.setPlotDataPoint(Integer.toString(day));         
                          
              for (int i = 0; i < l_AppUsage5Min_RollUpdata.size(); i++) {
                                       
                     if ((l_AppUsage5Min_RollUpdata.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_APP_LATENCY_TIME"))
                                  && ((metric_type.equalsIgnoreCase("ALL")) || (metric_type.equalsIgnoreCase("APP_LATENCY")))) {
                           total_App_latency += Double.parseDouble(l_AppUsage5Min_RollUpdata.get(i).getMetric_Value());
                           appLatCount += 1;
                     }
    
                     if ((l_AppUsage5Min_RollUpdata.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_CPU_USAGE"))
                                  && ((metric_type.equalsIgnoreCase("ALL")) || (metric_type.equalsIgnoreCase("CPU_USAGE")))) {
                           total_cpu_usage += Double.parseDouble(l_AppUsage5Min_RollUpdata.get(i).getMetric_Value());
                           cpuUsageCount += 1;
                     }
    
                     if ((l_AppUsage5Min_RollUpdata.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_MEMORY_USAGE"))
                                  && ((metric_type.equalsIgnoreCase("ALL")) || (metric_type.equalsIgnoreCase("MEMORY_USAGE")))) {
                           total_MemoryUsed += Double.parseDouble(l_AppUsage5Min_RollUpdata.get(i).getMetric_Value());
                           memUsageCount += 1;
                     }
    
                     if ((l_AppUsage5Min_RollUpdata.get(i).getMetric_Type().equalsIgnoreCase("TOTAL_APP_SESSION_LENGTH"))
                                  && ((metric_type.equalsIgnoreCase("ALL")) || (metric_type.equalsIgnoreCase("SESSION_LENGTH")))) {
                           total_App_session_length += Double.parseDouble(l_AppUsage5Min_RollUpdata.get(i).getMetric_Value());
                           appSessLth += 1;
                     }
              }
                          
              double avg_cpu_usage = total_cpu_usage / cpuUsageCount;
              double avg_MemoryUsed = total_MemoryUsed / memUsageCount;
              double avg_App_sess_length = total_App_session_length / appSessLth;
              double avg_app_latency = total_App_latency / appLatCount;
    
              dataPoint.setAppLatency(Double.toString(avg_app_latency)); 
              dataPoint.setMemoryUsage(Double.toString(avg_MemoryUsed));
              dataPoint.setCpuUsage(Double.toString(avg_cpu_usage));
              dataPoint.setsessionLength(Double.toString(avg_App_sess_length));
       }
       catch (Throwable e) {
              log.error("An exception occurred while setting data points minute wise : " + e.getMessage());
       }
                   
       return dataPoint;
    }


}
 