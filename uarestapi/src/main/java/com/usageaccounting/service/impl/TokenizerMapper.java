package com.usageaccounting.service.impl;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable; ;

//
public class TokenizerMapper extends  Mapper<LongWritable, Text,Text, Text> {
	
   // public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) 
	// private final static IntWritable one = new IntWritable(1);
	 public void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {
            try{
    	    StringTokenizer itr = new StringTokenizer(value.toString(),";");
    	        String l_AppMicroName= itr.nextToken();
				String l_userId= itr.nextToken();
	    	    String l_AppName= itr.nextToken();
				//String l_Region= itr.nextToken();
				String l_TelcoId= itr.nextToken();
	    	    String l_keycloudletId= itr.nextToken();
				
	    	    String l_fullValue = value.toString();
	     	    //context.write(new Text(l_AppMicroName+"_"+l_userId +"_"+l_AppName+"_"+l_Region+"_"+l_TelcoId+"_"+l_keycloudletId), new Text(l_fullValue));
	    	    context.write(new Text(l_AppMicroName+"_"+l_userId +"_"+l_AppName+"_"+l_TelcoId+"_"+l_keycloudletId), new Text(l_fullValue));
            }
            catch (Throwable e){
            	System.out.println("inside TokenizerMapper::mapper class() exception occured " + e.toString()); 	
            	
            }
    	   } 
}
