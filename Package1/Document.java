package Package1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

@SuppressWarnings("unused")
public class Document {
	
	public static void main(String[] args) throws IOException{
		String input_dir=args[0];
		String output_dir=args[1];
		//String input_dir="C:/Users/Aishwarya/Desktop/IR project 1 files/Input Files/";
		//String output_dir="C:/Users/Aishwarya/Desktop/IR project 1 files/Output files/";
		//File directory=new File("C:/Users/Aishwarya/Desktop/IR project 1 files/Input Files/");
		File directory=new File(input_dir);
		File[] file_list=directory.listFiles();
		HashMap<String,Integer> freq_map;
		freq_map= create_frequency_map(file_list, input_dir);
		write_tokenized_files(file_list, input_dir,output_dir);
		write_sorted_by_frequency(freq_map,output_dir);
		write_sorted_by_token_name(freq_map,output_dir);
	}
	private static void write_sorted_by_token_name(HashMap<String, Integer> freq_map, String output_dir) throws IOException {
		// TODO Auto-generated method stub
		Map<String,Integer> freq_treemap = new TreeMap<String, Integer>(freq_map);
		File check_file=new File(output_dir+"Output files with frequencies");
		if(!check_file.exists())
		{
			check_file.mkdir();
		}
		Iterator<String> i1=freq_treemap.keySet().iterator();
		String key;
		Integer val;
		BufferedWriter fw=new BufferedWriter(new FileWriter(output_dir +"Output files with frequencies/sorted_by_tokens.txt"));
		while(i1.hasNext())
		{
		key=i1.next().toString();
		val=freq_treemap.get(key);
		//System.out.println("\n"+key+" "+val.toString());
		//fw.write(""+key+" "+val.toString());
		fw.write(key.toLowerCase()+ "  "+val.toString());
		fw.newLine();
		}
		fw.close();
	}
	@SuppressWarnings("unchecked")
	private static void write_sorted_by_frequency(HashMap<String, Integer> freq_map, String output_dir) throws IOException {
		// TODO Auto-generated method stub
		
		Map<String, Integer> freq_sortedmap = sortByFrequency(freq_map);
		File check_file=new File(output_dir+"Output files with frequencies");
		if(!check_file.exists())
		{
			check_file.mkdir();
		}
		Iterator<String> i1=freq_sortedmap.keySet().iterator();
		String key;
		Integer val;
		BufferedWriter fw=new BufferedWriter(new FileWriter(output_dir+"Output files with frequencies/sorted_by_freq.txt"));
		while(i1.hasNext())
		{
		key=i1.next().toString();
		val=freq_sortedmap.get(key);
		//System.out.println("\n"+key+" "+val.toString());
		//fw.write(""+key+" "+val.toString());
		fw.write(key.toLowerCase()+ "  "+val);
		fw.newLine();
		}
		fw.close();
	}
	static void write_tokenized_files(File[] file_list, String input_dir, String output_dir) throws IOException{
		String line;
		String token = null;

		for(File file: file_list)
		{
		//	System.out.println(file.getName());
			BufferedReader br=null;
				
				if(file.isFile())
				{
					br=new BufferedReader(new FileReader(input_dir+file.getName()));
				}
		Integer lastIndex=file.getName().lastIndexOf('.');
		BufferedWriter fw=new BufferedWriter(new FileWriter(output_dir + file.getName().substring(0,lastIndex)+".txt"));
			while((line=br.readLine())!=null)
			{
				StringTokenizer s1=new StringTokenizer(line);
					while(s1.hasMoreElements())
					{
				
					token=s1.nextElement().toString();
				//	System.out.println(token);	
					if(!token.startsWith("<") && !token.endsWith(">") && !token.startsWith("-->") && !token.startsWith("<!--"))
					{
					fw.write(token.toLowerCase().replaceAll("^[^\\p{L}\\p{Nd}]+ | [^\\p{L}\\p{Nd}]+$", "")+" ");
					}
					
					}
				//System.out.println(line);
			}
			fw.close();
//		return freq_map;	
			br.close();
		
		
		}
	}
	static HashMap<String,Integer> create_frequency_map(File[] file_list, String input_dir) throws IOException{
		//HashMap<String, Integer> freq_map;
		HashMap<String,Integer> freq_map=new HashMap<String,Integer>();
		for(File file: file_list)
		{
			BufferedReader br=null;
			
			String line;
			int freq;
			String token;

			
				if(file.isFile())
				{
					br=new BufferedReader(new FileReader(input_dir +file.getName()));
				}
			
			while((line=br.readLine())!=null)
			{
				StringTokenizer s1=new StringTokenizer(line);
				while(s1.hasMoreElements())
				{
					token=s1.nextElement().toString();
					
					if(!token.startsWith("<") && !token.endsWith(">") && !token.startsWith("-->") && !token.startsWith("<!--"))
					{		
					
						if(freq_map.containsKey(token.replaceAll("^[^\\p{L}\\p{Nd}]+ | [^\\p{L}\\p{Nd}]+$", "")))
						{
						freq=freq_map.get(token.replaceAll("^[^\\p{L}\\p{Nd}]+ | [^\\p{L}\\p{Nd}]+$", ""));
						
						freq_map.put(token.replaceAll("^[^\\p{L}\\p{Nd}]+ | [^\\p{L}\\p{Nd}]+$", ""),freq+1);
						}
						else
						{
						freq_map.put(token.replaceAll("^[^\\p{L}\\p{Nd}]+ | [^\\p{L}\\p{Nd}]+$", ""),1);
						}
					
					}
				}
				//System.out.println(line);
			}
		}
	//	br.close();
		return freq_map;	
		
		
	
	}
	
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static HashMap sortByFrequency(HashMap freq_map) { 
	       List list = new LinkedList(freq_map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sorted_freqmap = new LinkedHashMap();
	       for (Iterator i1 = list.iterator(); i1.hasNext();) {
	              Map.Entry entry = (Map.Entry) i1.next();
	              sorted_freqmap.put(entry.getKey(), entry.getValue());
	       } 
	       return sorted_freqmap;
	  }
	
}
