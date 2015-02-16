
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
	    //Take the arguments for input and output directories
		String input_dir=args[0];
		String output_dir=args[1];
		
		//Check if the input directory exists. If not, exit the program.
		File directory=new File(input_dir);
		if(!directory.exists())
		{
		System.out.println("\nInput directory does not exist..");
			System.exit(0);
		}
		//Check if the output directory exists. If not, create it.
		File directory1=new File(output_dir);
		 if(!directory1.exists())
                {
               directory1.mkdir();
                }


		 //Find the list of files in the input directory.
		File[] file_list=directory.listFiles();
		HashMap<String,Integer> freq_map;
		//Call to function for creating a hashmap of tokens and their frequencies.
 
		freq_map= create_frequency_map(file_list, input_dir);
		//Calls to functions that write tokens and frequencies to files.
		write_tokenized_files(file_list, input_dir,output_dir);
		write_sorted_by_frequency(freq_map,output_dir);
		write_sorted_by_token_name(freq_map,output_dir);
	}
	private static void write_sorted_by_token_name(HashMap<String, Integer> freq_map, String output_dir) throws IOException {
	    //Create a treemap from the frequency map of tokens and frequencies for sorting by token name.
		Map<String,Integer> freq_treemap = new TreeMap<String, Integer>(freq_map);
		//check if output directory exists. If not, create it.
		File check_file=new File(output_dir+"/Output files with frequencies");
		if(!check_file.exists())
		{
			check_file.mkdir();
		}
		//Take an iterator for the treemap.
		Iterator<String> i1=freq_treemap.keySet().iterator();
		String key;
		Integer val;
		BufferedWriter fw=new BufferedWriter(new FileWriter(output_dir +"/Output files with frequencies/sorted_by_tokens.txt"));
		//Write all the tokens and their frequencies to file one by one. As it is a treemap, they will be in sorted order based on tokens names.
		while(i1.hasNext())
		{
		key=i1.next().toString();
		val=freq_treemap.get(key);
		fw.write(key.toLowerCase()+ "  "+val.toString());
		fw.newLine();
		}
		fw.close();
	}
	@SuppressWarnings("unchecked")
	private static void write_sorted_by_frequency(HashMap<String, Integer> freq_map, String output_dir) throws IOException {
		
	    //Call the function to sort the hashmap of tokens and frequencies based on frequencies.
		Map<String, Integer> freq_sortedmap = sortByFrequency(freq_map);
		File check_file=new File(output_dir+"/Output files with frequencies");
		//Check if output directory exists. If not, create it.
		if(!check_file.exists())
		{
			check_file.mkdir();
		}
		//Take an iterator for the sorted map.
		Iterator<String> i1=freq_sortedmap.keySet().iterator();
		String key;
		Integer val;
		BufferedWriter fw=new BufferedWriter(new FileWriter(output_dir+"/Output files with frequencies/sorted_by_freq.txt"));
		//write the tokens and their frequencies to the file one by one.
		while(i1.hasNext())
		{
		key=i1.next().toString();
		val=freq_sortedmap.get(key);
		
		fw.write(key.toLowerCase()+ "  "+val);
		fw.newLine();
		}
		fw.close();
	}
	static void write_tokenized_files(File[] file_list, String input_dir, String output_dir) throws IOException{
		String line;
		String token = null;
		//loop for processing each input file
		for(File file: file_list)
		{
		
			BufferedReader br=null;
				
				if(file.isFile())
				{
					br=new BufferedReader(new FileReader(input_dir+"/"+file.getName()));
				}
		  //create names of output files based on names of input files.
		Integer lastIndex=file.getName().lastIndexOf('.');
		BufferedWriter fw=new BufferedWriter(new FileWriter(output_dir +"/"+ file.getName().substring(0,lastIndex)+".txt"));
		//Tokenize the input file line by line and write to output file
		        while((line=br.readLine())!=null)
			{
			    //Tokenizing the line
				StringTokenizer s1=new StringTokenizer(line);
					while(s1.hasMoreElements())
					{
				
					token=s1.nextElement().toString();
					//removing the tokens that are at start or end of a HTML tag.
					if(!token.startsWith("<") && !token.endsWith(">") && !token.startsWith("-->") && !token.startsWith("<!--"))
					{
					    //downcase the token and remove any special characters at its start or end.
					fw.write(token.toLowerCase().replaceAll("^[^\\p{L}\\p{Nd}]+ | [^\\p{L}\\p{Nd}]+$", "")+"");
					}
					
					}
				
			}
			fw.close();
	
			br.close();
		
		
		}
	}
	static HashMap<String,Integer> create_frequency_map(File[] file_list, String input_dir) throws IOException{

		HashMap<String,Integer> freq_map=new HashMap<String,Integer>();
		//loop for processing each input file
		for(File file: file_list)
		{
			BufferedReader br=null;
			
			String line;
			int freq;
			String token;

			
				if(file.isFile())
				{
					br=new BufferedReader(new FileReader(input_dir +"/" + file.getName()));
				}
				//Read the file line by line
			while((line=br.readLine())!=null)
			{
			    //Tokenize the line
				StringTokenizer s1=new StringTokenizer(line);
				while(s1.hasMoreElements())
				{
					token=s1.nextElement().toString();
					//remove tokens that are at start or end of a HTML tag.
					if(!token.startsWith("<") && !token.endsWith(">") && !token.startsWith("-->") && !token.startsWith("<!--"))
					{		
					    //If the token is already in hashmap, increment its frequency by 1. Remove any special characters at start or end of the token.
						if(freq_map.containsKey(token.replaceAll("^[^\\p{L}\\p{Nd}]+ | [^\\p{L}\\p{Nd}]+$", "")))
						{
						freq=freq_map.get(token.replaceAll("^[^\\p{L}\\p{Nd}]+ | [^\\p{L}\\p{Nd}]+$", ""));
						
						freq_map.put(token.replaceAll("^[^\\p{L}\\p{Nd}]+ | [^\\p{L}\\p{Nd}]+$", ""),freq+1);
						}
						//If the token is not in hashmap, put it in hashmap with frequency 1. Remove any special characters at start or end of token.
						else
						{
						freq_map.put(token.replaceAll("^[^\\p{L}\\p{Nd}]+ | [^\\p{L}\\p{Nd}]+$", ""),1);
						}
					
					}
				}
			}
		}

		return freq_map;	
		
		
	
	}
	
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static HashMap sortByFrequency(HashMap freq_map) { 
	    //Create a linked list from the frequency map.   
	    List list = new LinkedList(freq_map.entrySet());
	       // Define custom comparator and sort the linked list by frequencies.
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });

	       // Copy the sorted linked list in hashmap
	       // LinkedHashMap is used to preserve the insertion order
	       HashMap sorted_freqmap = new LinkedHashMap();
	       for (Iterator i1 = list.iterator(); i1.hasNext();) {
	              Map.Entry entry = (Map.Entry) i1.next();
	              sorted_freqmap.put(entry.getKey(), entry.getValue());
	       } 
	       return sorted_freqmap;
	  }
	
}
