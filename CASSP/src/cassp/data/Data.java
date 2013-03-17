/**
*   Data.java
*
*   Copyright (c) 2013 Vladimír Brigant
*   This software is distributed under the terms of the GNU General Public License.
*/

package cassp.data;


import java.util.*;
import java.io.*;
import java.util.regex.*;

import org.apache.log4j.*;

import cassp.utils.*;



public class Data {

    static Logger logger = Logger.getLogger(Data.class);

    public ArrayList<DataItem> data;
    public HashMap<Character, AminoAcid> amino_acids;


    public Data(){
        this.data = new ArrayList<DataItem>();
        this.amino_acids = new HashMap<Character, AminoAcid>();
    }

    public Data(String file_path){
        this.data = new ArrayList<DataItem>();
        this.amino_acids = new HashMap<Character, AminoAcid>();
        this.loadData(file_path);
    }


    public ArrayList<DataItem> getData(){
        return this.data;
    }


    public void loadData(String file_path){

        try{
            FileInputStream fstream = new FileInputStream(file_path);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String aa_seq;
            String ssp_seq;

            while ((aa_seq = br.readLine()) != null)   {
                ssp_seq = br.readLine();
                br.readLine();

                DataItem di = new DataItem();
                di.aa_seq = aa_seq;
                di.ssp_seq = ssp_seq;
                this.add(di);
            }

            in.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }

        logger.info("dataset length: " + this.data.size());
    }

    public void computeChouFasman(){
        // TODO

        // conformation states of all amino acids
        HashMap<Character, ArrayList> cs = new HashMap<Character, ArrayList>();
        // relative frequencies of amino acids
        HashMap<Character, ArrayList> relat_f = new HashMap<Character, ArrayList>();
        // amino acids acounts
        HashMap<Character, Integer> aa_counts = new HashMap<Character, Integer>();
        // final chou-fasman parameters of 20 classic amino acids
        HashMap<Character, ArrayList> final_cf = new HashMap<Character, ArrayList>();
        // chou fasman parameters of ambiguous amino acids
        HashMap<Character, ArrayList> ambiguous_cf = new HashMap<Character, ArrayList>();


        for (char amino_acid : Utils.aminoAcids){
            cs.put(amino_acid, new ArrayList(3));
            relat_f.put(amino_acid, new ArrayList(3));
            aa_counts.put(amino_acid, 0);
            final_cf.put(amino_acid, new ArrayList(3));
        }

        for (char amino_acid: Utils.ambiguousAminoAcids){
            ambiguous_cf.put(amino_acid, new ArrayList(3));
        }


        for (DataItem di: this.data){
            for (int i = 0; i < di.ssp_seq.length(); i++) {
                if ("ZBJX".indexOf(di.aa_seq.charAt(i)) == -1)
                    continue;

                int count = aa_counts.get(di.aa_seq.charAt(i));
                aa_counts.put(di.aa_seq.charAt(i), count + 1);

                if (di.ssp_seq.charAt(i) == 'H'){
                    //count = cs.get(di.aa_seq.charAt(i)).get(0).intValue();
                    //cs.get(di.aa_seq.charAt(i)).set(0, count + 1);
                }
            }
        }
    }


    public void loadChouFasman(String file_path){

        try{
            FileInputStream fstream = new FileInputStream(file_path);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            Pattern pattern = Pattern.compile("(\\w):\\s(\\d+),\\s(\\d+),\\s(\\d+)");


            while ((line = br.readLine()) != null)   {

                Matcher match = pattern.matcher(line);

                if (match.matches()) {
                    AminoAcid amino = new AminoAcid();
                    amino.abbrev = match.group(1).charAt(0);

                    amino.cf_a = Integer.parseInt(match.group(2));
                    amino.cf_b = Integer.parseInt(match.group(3));
                    amino.cf_c = Integer.parseInt(match.group(4));

                    this.amino_acids.put(new Character(amino.abbrev), amino);
                }
            }

            in.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }



    public double q3(){
        int all_count = 0;
        int ok_count = 0;

        for (DataItem di : this.data){
            for (int i = 0; i <  di.ssp_seq.length(); i++) {
                if (di.ssp_seq.charAt(i) == di.predicted_seq.charAt(i))
                    ok_count++;
            }
            all_count += di.ssp_seq.length();
        }

        return (double) ok_count/all_count;
    }



    public double sov(){
        // TODO
        return 0;
    }


    public int length(){
        return this.data.size();
    }

    public DataItem get(int i){
        return this.data.get(i);
    }

    public void add(DataItem di){
        this.data.add(di);
    }

    public void merge(Data data){
        this.amino_acids = data.amino_acids;
        this.data.addAll(data.data);
    }
}
