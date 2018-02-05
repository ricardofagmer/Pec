package com.pec.biosistemico.pec.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Ping
{

  public static void main(String args[]) 
  throws IOException
  {
    // create the ping command as a list of strings
    Ping ping = new Ping();
    List<String> commands = new ArrayList<String>();
    commands.add("ping");
    commands.add("-c");
    commands.add("3");
    commands.add("177.94.205.105");
    ping.doCommand(commands);
  }

  public int doCommand(List<String> command) 
  throws IOException
  {
	  int retorno = 0;
    String s = null;

    ProcessBuilder pb = new ProcessBuilder(command);
    Process process = pb.start();

    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
    BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

    // read the output from the command
    System.out.println("Here is the standard output of the command:\n");
    while ((s = stdInput.readLine()) != null)
    {
      retorno =  1;
    }

    // read any errors from the attempted command
    System.out.println("Here is the standard error of the command (if any):\n");
    while ((s = stdError.readLine()) != null)
    {
    	retorno =  0;
    }
    
    return retorno;
    		
  }

}