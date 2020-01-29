package edu.iastate.cs.egroum.aug;

import java.io.File;
import java.io.PrintWriter;

public class Scratch {

	public static void main(String...strings ) {
		File f = new File("hello_world"); 
		try (PrintWriter pw = new PrintWriter(f)) {
			
			int i = 5 + 5;
		} catch(Exception e) {
			
			
		} finally {
			System.out.println("hello world");
		}
		
		
		Long l = null;
//		long ll = l;
		if (l instanceof Long) {
			System.out.println("");
		}
		
//		Object s = (int) l;
		
			
	}
	
}
