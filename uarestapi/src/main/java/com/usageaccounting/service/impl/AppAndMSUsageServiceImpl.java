package com.usageaccounting.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.mapred.JobClient;
//import org.apache.hadoop.mapred.TextInputFormat;
//import org.apache.hadoop.mapred.TextOutputFormat;
//import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usageaccounting.dao.AppAndMSUsageDAO;
import com.usageaccounting.entity.AppAndMSMetricUsage;
import com.usageaccounting.entity.AppAndMSUsage;
import com.usageaccounting.entity.AppDataMonitoring;
import com.usageaccounting.entity.AppDataMonitoringInput;
import com.usageaccounting.entity.Application_usage;
import com.usageaccounting.entity.CloudletVMUsage;
import com.usageaccounting.entity.Cloudlet_usage;
import com.usageaccounting.entity.Microservice_usage;
import com.usageaccounting.entity.Ms_data;
import com.usageaccounting.entity.Vm_data;
import com.usageaccounting.service.AppAndMSUsageService;

/**
 * Service Impl class for Application to perform CRUD operation.
 * @author Aricent
 * @version 1.0
 */


@Service
public class AppAndMSUsageServiceImpl implements AppAndMSUsageService {

	   @Autowired  
	    private AppAndMSUsageDAO appAndMSUsageDAO;
	    static Logger log = Logger.getLogger(AppAndMSUsageServiceImpl.class.getName());
	    
	    
	    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	    /**
	     * Default Constructor
	     */
	    public AppAndMSUsageServiceImpl() {
	        super();    
	    }

	    public static Date dateStringToDate(final String inDateStr) throws ParseException {
	    	return new SimpleDateFormat(DATE_TIME_FORMAT).parse(inDateStr);
	    }
	    
	    @Override   
	    public void createAppAndMSUsage(AppAndMSUsage appAndMSUsage) throws  IOException  {	
	    	FileWriter MapperInputFile = null;
	    	Exception exception;
	     //   List<Application> l_applications = appAndMSUsage.getApplications() ;
	        
	    	List<Ms_data> l_AppAndMicroService = appAndMSUsage.getMs_data();
	    	
	    	String appPrimaryKey = "";
	    	String appMicroTimeStamp = "";
	    	String microServicePrimaryKey = "";
		    String message = "";
		    String userId = "";
		    int l_totalApiHits=0,l_totalLatency=0;
		    
	        log.info("inside AppAndMSUsageServiceImpl::createAppAndMSUsage() "); 	
		    try {
		    	if (appAndMSUsage.getMs_data().isEmpty()) {
		    		log.error("Application/Microservice information not found.");
		    		throw new NullPointerException("Application/Microservice information not found. Incomplete payload information.");
		    	}  	
		    	//Check for App name.
		    	if (appAndMSUsage.getMs_data().isEmpty())
		    	{
	   				log.error("appName cannot be null.");
    				throw new NullPointerException("appName cannot be null.");
		    	}
		    	
		    	//Populate the micro service developer name
		    	
		    	for (int j = 0; j < l_AppAndMicroService.size(); j++) {
		    		if (l_AppAndMicroService.get(j).getSubscribed().equalsIgnoreCase("Y") )
		    		{
			    		String l_Ms_name = l_AppAndMicroService.get(j).getMs_name();
		    			
		    			if(l_Ms_name.isEmpty() || (l_Ms_name.equals(""))) {
		    				log.error("microService Name cannot be null.");
		    				throw new NullPointerException("microService Name cannot be null.");
		    			}
		    			
		    			userId = "";
		    			log.info("Searching developer id of microservice : " + l_Ms_name);
		    			userId = findUserId(l_Ms_name, "MS");	
		    			l_AppAndMicroService.get(j).setUserId(userId); 			
		    			l_Ms_name = "";
		    		}	
	    		}
				File file = new File("/map_file_input/AppUsageMapInput.txt");
				MapperInputFile = new FileWriter(file);
				
				//Forming  the chargable matrics
				String apiHitDetail="",apiDuration="",apiEvent_in="",apiEvent_out="";
				int l_count=0;
				
				for (int j = 0; j < l_AppAndMicroService.size(); j++) {
					//System.out.println("inside AppAndMSUsageServiceImpl::createAppAndMSUsage() <"+l_AppAndMicroService.get(j).toString()+">"); 
					if (l_AppAndMicroService.get(j).getSubscribed().equalsIgnoreCase("Y") )
					{
						int l_apiHits=0,l_apiEvent_in=0,l_apiEvent_out=0,l_apiDuration=0,l_latency=0;
						for (int k = 0; k < l_AppAndMicroService.get(j).getEventUsage().size(); k++)
						{	
							//System.out.println("inside AppAndMSUsageServiceImpl::createAppAndMSUsage() <<<"+l_AppAndMicroService.get(j).getEventUsage().get(k).getEvent_type()+">>>>");
							if (l_AppAndMicroService.get(j).getEventUsage().get(k).getEvent_type().equalsIgnoreCase("http"))
							{
							   l_apiHits= l_apiHits+l_AppAndMicroService.get(j).getEventUsage().get(k).getApi_hits();
							   l_latency = l_latency + l_AppAndMicroService.get(j).getEventUsage().get(k).getLatency();
							}
							
							if (l_AppAndMicroService.get(j).getEventUsage().get(k).getEvent_type().equalsIgnoreCase("event"))
							{
								if (l_AppAndMicroService.get(j).getEventUsage().get(k).getApi_hits() !=0)
								{
									l_apiHits += l_AppAndMicroService.get(j).getEventUsage().get(k).getApi_hits();
									l_latency += l_AppAndMicroService.get(j).getEventUsage().get(k).getLatency();
								}
								if (l_AppAndMicroService.get(j).getEventUsage().get(k).getNotify_in() !=0)
								{
									l_apiEvent_in += l_AppAndMicroService.get(j).getEventUsage().get(k).getNotify_in();
									l_apiEvent_out += l_AppAndMicroService.get(j).getEventUsage().get(k).getNotify_out();
								}
								
								if (l_AppAndMicroService.get(j).getEventUsage().get(k).getSess_duration() !=0)
								{
									l_apiDuration += l_AppAndMicroService.get(j).getEventUsage().get(k).getSess_duration();
								}
							}		
						}
						if (l_count > 0)
			            {
				        	apiHitDetail =apiHitDetail +","+ l_AppAndMicroService.get(j).getMs_name()+":"+l_apiHits; 
				        	apiEvent_in =apiEvent_in +","+ l_AppAndMicroService.get(j).getMs_name()+":"+l_apiEvent_in; 
				        	apiEvent_out =apiEvent_out +","+ l_AppAndMicroService.get(j).getMs_name()+":"+l_apiEvent_out; 
				        	apiDuration =apiDuration +","+ l_AppAndMicroService.get(j).getMs_name()+":"+l_apiDuration; 
			            }
			            else
			            {
			            	apiHitDetail = l_AppAndMicroService.get(j).getMs_name()+":"+l_apiHits;
			            	apiEvent_in = l_AppAndMicroService.get(j).getMs_name()+":"+l_apiEvent_in;
			            	apiEvent_out = l_AppAndMicroService.get(j).getMs_name()+":"+l_apiEvent_out;
			            	apiDuration = l_AppAndMicroService.get(j).getMs_name()+":"+l_apiDuration;
			            }
						//calculate avg latency and store at per  ms level 
						//double average_1 = l_latency / l_apiHits;
						//l_AppAndMicroService.get(j).setLatency(average_1);
						l_AppAndMicroService.get(j).setLatency(l_latency);
						l_totalApiHits += l_apiHits;
						l_count += 1;
						l_totalLatency += l_latency;
					}
				}
				appAndMSUsage.setApp_latency(l_totalLatency);
				log.info("Value of appRestAPIdata < "+ l_totalLatency + appAndMSUsage.getApp_latency()  +">"); 
				
				String appRestAPIdata="";
				
				for (int j = 0; j < l_AppAndMicroService.size(); j++) {
					appRestAPIdata="";
					if (l_AppAndMicroService.get(j).getSubscribed().equalsIgnoreCase("N") )
					{
					   appRestAPIdata = "APP" + ";" 
							+ appAndMSUsage.getDev_id() + ";" 					
							+ appAndMSUsage.getApp_name() + ";"                  
							//+ l_applications.get(j).getRegion() + ";"
							+ appAndMSUsage.getTelco_id() + ";"				    	
							+ appAndMSUsage.getCloudlet_id() + ";"              
																				
							+ appAndMSUsage.getTimestamp() + ";" 				
							+ appAndMSUsage.getUuid() +";"			
							+ l_AppAndMicroService.get(j).getCpu_usage()+ ";"	
							+ l_AppAndMicroService.get(j).getImage_reg_usage() + ";"      	
							+ l_AppAndMicroService.get(j).getVol_usage() + ";"			 	
							+ l_AppAndMicroService.get(j).getMem_usage() + ";"  	
							+ l_AppAndMicroService.get(j).getHa_usage() + ";" 	
							+ l_AppAndMicroService.get(j).getSec_usage() + ";"  
							+ l_AppAndMicroService.get(j).getUptime() + ";"			
							+ apiHitDetail + ";"				 
							+ apiEvent_in + ";"
							+ apiEvent_out + ";"
							+ apiDuration + ";"
							+ l_AppAndMicroService.get(j).getStorage_space() + ";" 
							+ appAndMSUsage.getApp_latency() + "\n";	
							// log.info("Value of appRestAPIdata < "+ appRestAPIdata +">"); 
					}
					else if (l_AppAndMicroService.get(j).getSubscribed().equalsIgnoreCase("Y") )
					{
						appRestAPIdata = "MIC"+";"
		                    	+ l_AppAndMicroService.get(j).getUserId() + ";"			 
		                    	+ l_AppAndMicroService.get(j).getMs_name() + ";"		
		    	                //+ l_microService.get(j).getRegion()+ ";"
		    	                + appAndMSUsage.getTelco_id() + ";"						
		    	                + appAndMSUsage.getCloudlet_id() + ";"                  
		                        + l_AppAndMicroService.get(j).getCpu_usage()+ ";"  
		                        + l_AppAndMicroService.get(j).getImage_reg_usage() + ";" 
		                        + l_AppAndMicroService.get(j).getVol_usage() + ";"	     
		                        + l_AppAndMicroService.get(j).getMem_usage() + ";" 		
		                        + l_AppAndMicroService.get(j).getHa_usage() + ";" 		
		                        + l_AppAndMicroService.get(j).getSec_usage() + ";"		
		                        + l_AppAndMicroService.get(j).getUptime() + ";"			
		                        + appAndMSUsage.getTimestamp() + ";"                    
		                        + l_AppAndMicroService.get(j).getStorage_space() + ";"  
		                        + l_AppAndMicroService.get(j).getLatency() + "\n";		
								//log.info("Value of Micro service data < "+ appRestAPIdata +">");
					}
					MapperInputFile.write(appRestAPIdata);
				}
				MapperInputFile.close();
		    }	
		    catch(Throwable e){
	           if (MapperInputFile != null) {
	        	   MapperInputFile.close();
	        	   log.warn("inside AppAndMSUsageServiceImpl::createAppAndMSUsage() exception occured "+ e.toString()); 	
	        	   throw e;
	        }}
	    
		   // JobConf conf = new JobConf(new Configuration(), AppAndMSUsageServiceImpl.class);   
		    Configuration conf = new Configuration();
		    Job job = new Job(conf, "App MService");
            job.setJarByClass(AppAndMSUsageServiceImpl.class);
	        job.setMapperClass(TokenizerMapper.class);
	       //job.setCombinerClass(AvgReducer.class);
	        job.setReducerClass(AvgReducer.class);
	  
	        job.setMapOutputKeyClass(Text.class);
	        job.setMapOutputValueClass(Text.class);
	        
	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(Text.class);
	        job.getConfiguration().set("mapreduce.output.basename", "UsageAggData"); 
       
//	        setInputFormatClass(Text.class); 
//	        job.setOutputFormatClass(Text.class);

	        FileInputFormat.addInputPath(job, new Path("/map_file_input"));
            FileOutputFormat.setOutputPath(job, new Path("/map_file_output"));  
            try {
            	 log.info("Starting the Map reducred process "+ job.toString());
            	//job.submit();
				job.waitForCompletion(true);
			} catch (ClassNotFoundException | InterruptedException e) {
				log.warn("inside AppAndMSUsageServiceImpl::createAppAndMSUsage() exception occured in callin Map reducer  ");
		     	e.printStackTrace();
			}
            
                     
    		List<Application_usage> l_aggAppUsagedata = new ArrayList<Application_usage>();
    		List<Microservice_usage> l_aggMicroServicedata = new ArrayList<Microservice_usage>();
    		
    		try {
    		FileReader l_in = new FileReader("/map_file_output/UsageAggData-r-00000");
    		BufferedReader bufferedReader = new BufferedReader(l_in);
    		StringBuffer stringBuffer = new StringBuffer();
    		String line;
    		Map<String,String> l_MetricMap = new HashMap<>();
    		
    		LocalDateTime now = LocalDateTime.now();
    		int minute = now.getMinute();
    		int hour = now.getHour();
    		int day = now.getDayOfYear() ;
    		
    		while ((line = bufferedReader.readLine()) != null) {

    			String[] l_strings = line.split(";");
    			//System.out.println("<"+Arrays.toString(l_strings)+ ">");
    			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    			 
    			if (l_strings[1].equalsIgnoreCase("APP")){    
    				//Populate the metric details in the map <Metric_type, metrics_value>.
    				l_MetricMap.clear();
    				

    				l_MetricMap.put("TOTAL_API_USAGE", l_strings[16]);
    				l_MetricMap.put("TOTAL_CPU_USAGE", l_strings[8]);
    				l_MetricMap.put("TOTAL_IMAGE_REG_USAGE", l_strings[9]);
    				l_MetricMap.put("TOTAL_VOL_USAGE", l_strings[10]);
    				l_MetricMap.put("TOTAL_MEMORY_USAGE", l_strings[11]);
					l_MetricMap.put("TOTAL_HA_USAGE", l_strings[12]);
					l_MetricMap.put("TOTAL_STORAGE_SPACE", l_strings[13]);
    				l_MetricMap.put("TOTAL_APP_SESSION_LENGTH", l_strings[14]);
    				l_MetricMap.put("TOTAL_SECURITY_USAGE", l_strings[15]);
    				l_MetricMap.put("TOTAL_EVENT_NOTIFY_IN", l_strings[17]);
    				l_MetricMap.put("TOTAL_EVENT_NOTIFY_OUT", l_strings[18]);
    				l_MetricMap.put("TOTAL_EVENT_SESSION_DURATION", l_strings[19]);
    				l_MetricMap.put("TOTAL_APP_USER_COUNT", l_strings[7]);
    				l_MetricMap.put("TOTAL_API_HIT_COUNT", "Germany:" + l_totalApiHits);
    				l_MetricMap.put("TOTAL_APP_LATENCY_TIME",l_strings[21]);
    			

    				for(String key:l_MetricMap.keySet()){
    					Application_usage l_applicationUsage = new Application_usage();
    					
        				l_applicationUsage.setUserId(l_strings[2]);
        				l_applicationUsage.setAppName(l_strings[3]);
        				l_applicationUsage.setRegion("Germany");
        				l_applicationUsage.setTelcoId(l_strings[4]);
        				l_applicationUsage.setCloudletId(l_strings[5]);  
        				l_applicationUsage.setMetric_type(key);
						try {
							l_applicationUsage.setProcessingDate(dateStringToDate(l_strings[6]));
						} catch (Throwable e) 
						{
							log.warn("Exception happened during date convertion" + e.toString());
							//throw e;
						}
        				l_applicationUsage.setMetric_value(l_MetricMap.get(key));
        
        				l_applicationUsage.setflag(0);  //0 -- not process , 1--- Processed 
        				l_applicationUsage.setMin(minute);
        				l_applicationUsage.setDay_hour_min(day+"_"+hour+"_"+minute);
         				l_aggAppUsagedata.add(l_applicationUsage);
     				}
    			}
    			else if (l_strings[1].equalsIgnoreCase("MIC"))
                {
   				
    				//Populate the metric details in the map <Metric_type, metrics_value>.
    				l_MetricMap.clear();
    				l_MetricMap.put("TOTAL_CPU_USAGE", l_strings[8]);
    				l_MetricMap.put("TOTAL_IMAGE_REG_USAGE", l_strings[9]);
    				l_MetricMap.put("TOTAL_VOL_USAGE", l_strings[10]);
    				l_MetricMap.put("TOTAL_MEMORY_USAGE", l_strings[11]);
    				l_MetricMap.put("TOTAL_HA_USAGE", l_strings[12]);
    				l_MetricMap.put("TOTAL_SECURITY_USAGE", l_strings[13]);
        			l_MetricMap.put("TOTAL_APP_SESSION_LENGTH", l_strings[14]);
    				l_MetricMap.put("TOTAL_STORAGE_SPACE", l_strings[15]);
    				l_MetricMap.put("TOTAL_LATENCY_TIME", l_strings[7]);
    				
    				for(String key:l_MetricMap.keySet()){
    					Microservice_usage l_microserviceUsage = new Microservice_usage();
    					
    					l_microserviceUsage.setUserId(l_strings[2]);
    					l_microserviceUsage.setMicroServicename(l_strings[3]);
    					l_microserviceUsage.setRegion("Germany");
    					l_microserviceUsage.setTelcoId(l_strings[4]);
    					l_microserviceUsage.setCloudletId(l_strings[5]);  
    					l_microserviceUsage.setMetric_type(key);
    					
						try {
							l_microserviceUsage.setProcessingDate(dateStringToDate(l_strings[6]));
						} catch (Throwable e) 
						{
							log.warn("Exception happened during date convertion" + e.toString());
							//throw e;
						}
						l_microserviceUsage.setMetric_value(l_MetricMap.get(key));

						l_microserviceUsage.setflag(0);  //0 -- not process , 1--- Processed 
						l_microserviceUsage.setMin(minute);
						l_microserviceUsage.setDay_hour_min(day+"_"+hour+"_"+minute);
        				
						l_aggMicroServicedata.add(l_microserviceUsage);
     				}
                }
    		}
    		l_in.close();
			
    		AppAndMSMetricUsage l_appAndMSMetricUsage = new AppAndMSMetricUsage();
    		l_appAndMSMetricUsage.setApplication(l_aggAppUsagedata);
    		l_appAndMSMetricUsage.setMicroservice(l_aggMicroServicedata);

			File fin = new File("/map_file_output");
        	for(File file : fin.listFiles()){
        		FileDeleteStrategy.FORCE.delete(file);
        		//System.out.println("deleted" + file.toString());
        		}
        	//System.out.println("deleted" + fin.toString());   	
        	FileDeleteStrategy.FORCE.delete(fin);
			
        	appAndMSUsageDAO.createAppAndMSUsage(l_appAndMSMetricUsage);
	        }
	    	catch(Exception exc) {	    		
	    		log.error("Exception occurred while scanning file.\n" + exc.getMessage());
	    	}
        	return;
	    }
	    

	    @Override   
	    public void createCloudletInfraUsage(CloudletVMUsage cloudletVMUsage) throws  IOException  {	
	    	List<Vm_data> l_VM_data = cloudletVMUsage.getVm_data();
	        log.info("AppAndMSUsageServiceImpl::createCloudletInfraUsage() "); 	
		    try {
            int total_VMcpu_usage=0,total_VMMemoryUsed=0,total_VMvol_usage=0,
					total_HighAvailabilityUsage=0,total_SecurityUsage=0,total_NetBandwidth=0,total_StorageSpace=0;
            
            long total_SessionLength=0;
            
			int	l_count=0;

				for (int j = 0; j < l_VM_data.size(); j++) {
					total_VMcpu_usage += l_VM_data.get(j).getCpu_usage();
					total_VMMemoryUsed += l_VM_data.get(j).getMem_usage();
					total_SessionLength += l_VM_data.get(j).getUptime();
					total_VMvol_usage  += l_VM_data.get(j).getVol_usage();
					total_HighAvailabilityUsage += l_VM_data.get(j).getHa_usage();
					total_SecurityUsage += l_VM_data.get(j).getSec_usage();
					total_NetBandwidth += l_VM_data.get(j).getNw_bandwidth();
					total_StorageSpace 	+= l_VM_data.get(j).getStorage_space();	
					l_count += 1;
				}			
					
				double avg_VMcpu_usage = total_VMcpu_usage / l_count;
				double avg_VMMemoryUsed = total_VMMemoryUsed / l_count;
				double avg_VMvol_usage = total_VMvol_usage / l_count;
				double avg_HighAvailabilityUsage = total_HighAvailabilityUsage / l_count;
				double avg_SecurityUsage = total_SecurityUsage / l_count;	
				double avg_NetBandwidth = total_NetBandwidth / l_count;
				double avg_StorageSpace = total_StorageSpace / l_count;
				double avg_SessionLength = total_SessionLength / l_count;
	            
				//log.info("calculated value of avg_SessionLength <" + avg_SessionLength+">");
				
				//long timestamp = (long) avg_SessionLength;
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				String formatted = format.format(avg_SessionLength*1000);
				long diff = ((dateStringToDate(cloudletVMUsage.getTimestamp()).getTime() - dateStringToDate(formatted).getTime()));
				long diffsec = diff/ 1000 ;
				
				//log.info("date diff  <" + diff +"><"+ diffsec +">" );
		  
				
	             Map<String,Double> l_MetricMap = new HashMap<String, Double>();
	            
	            l_MetricMap.put("TOTAL_CLOUDLET_CPU_USAGE", new Double(avg_VMcpu_usage));
	            l_MetricMap.put("TOTAL_CLOUDLET_MEMORY_USAGE",new Double(avg_VMMemoryUsed));
	            l_MetricMap.put("TOTAL_CLOUDLET_SESSION_LENGTH",new Double(diffsec));
	            l_MetricMap.put("TOTAL_CLOUDLET_VOL_USAGE", new Double(avg_VMvol_usage));
	            l_MetricMap.put("TOTAL_CLOUDLET_HA_USAGE", new Double(avg_HighAvailabilityUsage));
	            l_MetricMap.put("TOTAL_CLOUDLET_SECURITY_USAGE",new Double(avg_SecurityUsage));
	            l_MetricMap.put("TOTAL_CLOUDLET_NET_BANDWIDTH", new Double(avg_NetBandwidth));
	            l_MetricMap.put("TOTAL_CLOUDLET_STORAGE_SPACE",new Double(avg_StorageSpace));
	            
	    		LocalDateTime now = LocalDateTime.now();
	    		int l_hour = now.getHour();
	    		List<Cloudlet_usage> l_aggcloudletUsage = new ArrayList<Cloudlet_usage>();
	    		
				for(String key:l_MetricMap.keySet()){
					Cloudlet_usage l_cloudlet_usage = new Cloudlet_usage();
					
					l_cloudlet_usage.setCloudletId(cloudletVMUsage.getCloudlet_id());
					l_cloudlet_usage.setTelcoId(cloudletVMUsage.getTelco_id());
					l_cloudlet_usage.setMetric_type(key);
					l_cloudlet_usage.setflag(0);  //0 -- not process , 1--- Processed 
					l_cloudlet_usage.setHour(l_hour);
					l_cloudlet_usage.setMetric_value(l_MetricMap.get(key));
					
					try {
						l_cloudlet_usage.setProcessingDate(dateStringToDate(cloudletVMUsage.getTimestamp()));
					} catch (Throwable e) 
					{
						log.warn("Exception happened during date convertion" + e.toString());
						//throw e;
					}
					l_aggcloudletUsage.add(l_cloudlet_usage);
 				} 
	            appAndMSUsageDAO.createCloudletUsage(l_aggcloudletUsage);
	        }
	    	catch(Exception exc) {	    		
	    		log.error("inside AppAndMSUsageServiceImpl::createCloudletInfraUsage some exception occurred " + exc.getMessage());
	    	}
        	return;
	    }
	    
	    @Override   
	    public AppDataMonitoring getAppDataMonitoring(AppDataMonitoringInput appDataMonitoringInput) throws  IOException  {	
	    	log.info("AppAndMSUsageServiceImpl::getAppDataMonitoring() "); 	
    		LocalDateTime now = LocalDateTime.now();
    		
    		int l_Startminute = now.getMinute();
    		int l_Starthour = now.getHour();
    		int l_StartDay = now.getDayOfYear() ;
    		int interval =0,l_EndDay =0,l_Endhour=0;
    		
    		AppDataMonitoring l_appDataMonitoring  = new AppDataMonitoring();
    		log.info("AppAndMSUsageServiceImpl::getAppDataMonitoring() --appDataMonitoringInput.getTimeCycle()<"
    		+appDataMonitoringInput.getTimeCycle()+"_"+l_StartDay+"_"+l_Starthour+"_"+l_Startminute+">");
	    	
    		if (appDataMonitoringInput.getTimeCycle().equalsIgnoreCase("LAST_1_DAY"))
			{
	    		return appAndMSUsageDAO.getLastOnedayAppDate(appDataMonitoringInput,l_StartDay,l_Starthour,l_Startminute);
	    		
			}
    		else if (appDataMonitoringInput.getTimeCycle().equalsIgnoreCase("LAST_7_DAYS"))
			{
	    		return appAndMSUsageDAO.getPreviousDaysAppData(appDataMonitoringInput,l_StartDay,l_Starthour,l_Startminute, now.getYear(), 7);
	    		
			}
    		else if (appDataMonitoringInput.getTimeCycle().equalsIgnoreCase("LAST_1_HOUR"))
			{
	    		return appAndMSUsageDAO.getLastOneHourAppData(appDataMonitoringInput,l_StartDay,l_Starthour,l_Startminute);
	    		
			}
            else if (appDataMonitoringInput.getTimeCycle().equalsIgnoreCase("LAST_30_DAYS"))
            {
            	return appAndMSUsageDAO.getPreviousDaysAppData(appDataMonitoringInput,l_StartDay,l_Starthour,l_Startminute, now.getYear(), 30);
            
            }

	    	return l_appDataMonitoring;
	    }
	    
	    
	    public String findUserId(String searchKey, String type) {
	    	
	    	log.info("Inside AppAndMSUsageServiceImpl :: finduserId");
	    	
	    	File file = new File("app_ms_usage/userId_Lookup.txt");
	    	Scanner scanner = null;
	    	String line = null;
	    	String userId = "";
	    	
	    	boolean fileExists = file.exists();
	    	
	    	try {
		    	if(!fileExists) {
		    		file.createNewFile();
		    	}

	    		log.info("Scanning file userId_Lookup for  : " + searchKey);
	    		
		    	int keyFound = 0;
	    		scanner = new Scanner(file);
	    		
	    		while(scanner.hasNext()) {
	    			line = scanner.nextLine();
	    			
	    			if(line.contains(searchKey)) {
	    				String[] parts = line.split("\\|");
	    				keyFound = 1;
	    				userId = parts[1].trim();
	    				log.info(searchKey + " :: " + userId );
	    			}
	    		}
	    		scanner.close();
	    		
	    		if (keyFound == 0) {
	    			log.info("Search string not available in file. searching in database.");
	    			
	    			if (type.equals("MS")) {
	    				//microserviceProfile = appAndMSUsageDAO.getMicroserviceProfile(searchKey);
	    				//userId = microserviceProfile.getUserId();
	    				//userId = findAndRetrunMSUserId(searchKey);
	    				userId = "Mark";
	    				keyFound = 1;
	    			}
	    			
	    			if (userId.equals("") || userId.isEmpty()) {
	    				scanner.close();
	    				
		    			log.error("userId not found in file or database.");
		    			//throw new NullPointerException("userId not found in file or database.");
		    		}
	    			else {
	    				FileWriter filewriter = new FileWriter(file, true);
	    				filewriter.write(searchKey + "|" + userId + "\n");
	    				filewriter.close();
	    			}
	    			
	    		}
	    	}
	    	catch(Exception exception) {	    		
	    		log.error("Inside CreditServiceImpl::findUserId some exception occurred while scanning file.\n" + exception.getMessage());
	    	}
	    	
    		return userId;
	    }
	    
	    private String findAndRetrunMSUserId(String  i_MSname) {
			log.info("Inside  CreditServiceImpl::AppAndMSUsageServiceImpl.");
			String l_MSuser_id="";
			
			try {
				String url_link=System.getenv("CR_LINK")+"microservice/"+i_MSname;
				URL url = new URL(url_link);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				//System.out.println(conn.getResponseCode());
				if (conn.getResponseCode() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				while ((output = br.readLine()) != null) {
					String[] parts = output.split(",");
					String[] parts_2 =parts[0].split(":");
					parts_2 =parts[1].split(":");
					if (parts_2[0].equalsIgnoreCase("\"userId\""))
					{
						l_MSuser_id = parts_2[1].replace("\"", "");
					}
				  }
				}
				else
				{

					log.info("The incoming Microservice id does not exist in the system");
			    	return "";
				}
				conn.disconnect();
			  } catch (Throwable e) {
				 log.warn("Inside  CreditServiceImpl::AppAndMSUsageServiceImpl some exception happened----< " + e.getMessage() +">");
				e.printStackTrace();
			  }
			  return l_MSuser_id;
		}
}

