package com.usageaccounting.service.impl;

import java.io.IOException;

import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.util.Arrays;
import java.util.StringTokenizer;

public class AvgReducer extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		int total_dockerContainerUsage = 0,total_DockerImageRegistryUsage=0,total_ContainerDataVolumeUsage=0,total_containerCacheUsage=0;
		int number = 0;
		String l_appName = "", l_cloudletId = "",l_microServiceName="";
		
		int total_MicroServiceDockerContainerUsage=0,total_MicroServiceDockerImageRegistryUsage=0,
		          total_MicroServiceContainerDataVolumeUsage=0,total_MicroServiceContainerCacheUsage=0,
		          total_NetBandwidth=0,total_StorageSpace=0,total_SecurityUsage=0,
		          total_MemoryUsed=0,total_SessionLength=0, total_Notify_in=0, total_Notify_out=0,total_HighAvailabilityUsage=0; 
		
		String appValue = key.toString().substring(0, 3).trim();
		String[] tokens =null;
		if (appValue.equalsIgnoreCase("APP")) {
			for (Text val : values) {
				tokens = val.toString().split(";");
				//System.out.println(Arrays.toString(tokens));
				l_cloudletId = tokens[4];
				l_appName = tokens[2];
				
				total_dockerContainerUsage += Integer.parseInt(tokens[7].isEmpty() ? "0" :tokens[7]);
                total_DockerImageRegistryUsage += Integer.parseInt(tokens[8].isEmpty() ? "0" :tokens[8]);
                total_ContainerDataVolumeUsage += Integer.parseInt(tokens[9].isEmpty() ? "0" :tokens[9]);
				total_containerCacheUsage += Integer.parseInt(tokens[10].isEmpty() ? "0" :tokens[10]);
             	total_HighAvailabilityUsage += Integer.parseInt(tokens[11].isEmpty() ? "0" :tokens[11]);
                total_SecurityUsage += Integer.parseInt(tokens[12].isEmpty() ? "0" :tokens[12]);
				total_SessionLength += Integer.parseInt(tokens[13]);
				total_StorageSpace += Integer.parseInt(tokens[18].isEmpty() ? "0" :tokens[18]);
				number += 1;
			}
			double average_1 = total_dockerContainerUsage / number;
			double average_2 = total_DockerImageRegistryUsage / number;
			double average_3 = total_ContainerDataVolumeUsage / number;
			double average_4 = total_containerCacheUsage / number;
            //double average_5 = total_NetBandwidth / number;
            double average_5 = total_HighAvailabilityUsage / number;
            double average_6 = total_StorageSpace / number;
            //double average_7 = total_MemoryUsed / number;
            double average_8 = total_SecurityUsage / number;
            
            context.write(key, new Text(";APP;" + tokens[1] + ";" + l_appName + ";" + tokens[3]+ ";" + tokens[4] + ";"  
												+ tokens[5] + ";" + tokens[6]  + ";" 
					                            + average_1 +";"+ average_2+ ";"+ average_3 +";"+ average_4 +";"
												+ average_5+";"+average_6 +";"+total_SessionLength+";"+average_8 +";"
					                            + tokens[14] +";"+ tokens[15]+";"
					                            + tokens[16] +";"+ tokens[17] +";"+ tokens[18]+";"+ tokens[19]));
			
		}
		else if (appValue.equalsIgnoreCase("MIC"))
        {
          String [] mtokens=null;
          //String total_HighAvailabilityUsage ="[";
              for (Text val : values) {
            	  mtokens = val.toString().split(";");
            	  //System.out.println(Arrays.toString(mtokens));
            	  l_cloudletId = mtokens[4];
                  l_microServiceName = mtokens[2];
                  total_MicroServiceDockerContainerUsage += Integer.parseInt(mtokens[5].isEmpty() ? "0" :mtokens[5]);
                  total_MicroServiceDockerImageRegistryUsage += Integer.parseInt(mtokens[6].isEmpty() ? "0" :mtokens[6]);
                  total_MicroServiceContainerDataVolumeUsage += Integer.parseInt(mtokens[7].isEmpty() ? "0" :mtokens[7]);
                  total_MicroServiceContainerCacheUsage += Integer.parseInt(mtokens[8].isEmpty() ? "0" :mtokens[8]); 
                  total_HighAvailabilityUsage += Integer.parseInt(mtokens[9].isEmpty() ? "0" :mtokens[9]);
                  total_SecurityUsage += Integer.parseInt(mtokens[10].isEmpty() ? "0" :mtokens[10]);
                  total_SessionLength += Integer.parseInt(mtokens[11]);
                  total_StorageSpace += Integer.parseInt(mtokens[13].isEmpty() ? "0" :mtokens[13]);	
                  number += 1;
              }
              
              //total_HighAvailabilityUsage = total_HighAvailabilityUsage + "]";
              
              double average_1 = total_MicroServiceDockerContainerUsage / number;
              double average_2 = total_MicroServiceDockerImageRegistryUsage / number;
              
              double average_3 = total_MicroServiceContainerDataVolumeUsage / number;
              double average_4 = total_MicroServiceContainerCacheUsage / number;
              double average_6 = total_StorageSpace / number;
              double average_8 = total_SecurityUsage / number;
		  
     //         System.out.println("Inside AvgReducer Mic <" + ","+l_microServiceName+","+l_cloudletId+","+average_1 
     //       		     +","+average_2 +","+ total_HighAvailabilityUsage +">");
              
              context.write(key, new Text(";MIC;" +mtokens[1]+";"+l_microServiceName+ ";" +mtokens[3]+";"+mtokens[4]+";"
            		  							  +mtokens[12]+";" +mtokens[14]+";"
												  +average_1 +";"+average_2+";"+average_3 +";"+average_4+";"
												  +total_HighAvailabilityUsage+";"+average_8+";"+total_SessionLength+";"
												  +average_6+";"+average_8));
        }  

	}
}
