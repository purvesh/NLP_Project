package com.nlp.project;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class MetaData {
	
	public MetaData() {
		
	}
	
	public void extractMetadata(String filename, String mode) {
		try {
				BufferedReader fp = new BufferedReader(new FileReader(filename));
				PrintWriter out = new PrintWriter(new File("metadata-" + mode + "-150.txt"));
				PrintWriter out1 = new PrintWriter(new File("url-" + mode + "-150.txt"));
				String s;
				boolean flag;
//				int i=1;
				while ((s = fp.readLine()) != null){
					String[] posts = s.split("}},");
					for (String post : posts){
						flag = false;
						String[] fields = post.split(", \"");
//							out.println(i++ + ". ");
						for( String field : fields) {
							if (field.startsWith("score") || field.startsWith("title") || field.startsWith("created") || field.startsWith("created_utc") || field.startsWith("data\": {\"domain\":") || field.startsWith("permalink") || field.startsWith("url") ) {
								if (field.startsWith("data\": {\"domain\": \"youtube.com")) {
									flag = true;
									break;
								}									
								else if (field.startsWith("created") || field.startsWith("created_utc")) {
									String[] field_parts = field.split(" ");
									String[] field_subparts = field_parts[1].split("\\.");
									long dateLong = Long.parseLong(field_subparts[0]);
									dateLong = dateLong * 1000;
									String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(dateLong));
									out.println(field_parts[0].replaceAll("\":", ": ") + date ); 
								}
									else {
										if (field.startsWith("url")) {
											field.replaceAll("data\": \\{\"", "").replaceAll("\": ", ": ");											
											field = field.replace("\"", "");
											field = field.replace("url: ", "");
											out1.println(field);											
										}
										else {
											out.println(field.replaceAll("data\": \\{\"", "").replaceAll("\": ", ": "));
										}
										
									}
							}
						}
						if(!flag) {
							out.println("\n");
						}
												
					}
				}
				fp.close();
				out.close();
				out1.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static void main(String args[]) {
		MetaData md = new MetaData();
		md.extractMetadata("data-top-tech-reddit-week-150.txt", "top");
		md.extractMetadata("data-bottom-tech-reddit-week-150.txt", "bottom");
	}

}
