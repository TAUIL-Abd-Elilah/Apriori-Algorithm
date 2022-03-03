package com.company;
import java.util.Arrays ;
import java.util.ArrayList;
import java.io.* ;
import java.util.List;
import java.util.Scanner ;


public class Main  {
    /*
    * the  requirements of this class are :
    * a path of a valid txt file with given dataset in the forme of
    *                       {2, 8, 11, 13, 15, 16, 20, 47}
                            {6, 7, 8, 10, 13, 17, 23, 25, 33, 37, 41, 43, 46}
                            {4, 6, 10, 11, 16, 17, 19, 23, 26, 32, 43, 46, 48}
                            {17, 19, 28, 32, 33, 37, 41, 42, 43, 46, 47}
                            {4, 17, 18, 19, 23, 32, 43, 46}
    * A float value between 0 and 1  represents the minimum support of the frequent items 
    *
    *
     */
    public static void main(String[] args) throws Exception {

        // relative minimum support wich means 50% of the total transactions 
        float minsupport  =  .1f ;
        // file object wich contains the datasets 
        File dataset   = new File("C:\\\\Users\\\\redouan\\\\Desktop\\\\test1.txt") ;

        Main ma = new Main();
        // pass the min  and the file to the apriori function to generate frequent itemsets
        ma.apriori(dataset , minsupport);
    }
    // the purpose of the function is to display  an arrayList of integer arrays as : [{array1}{array2}{array3}]
    // it will be used to display the generated frequent itemsets since they are stored as Integer arrays in a Arraylist
    private void show_Arraylist(ArrayList<Integer[]> itemsets){
        for (int i =0 ; i <itemsets.size()  ; i++) {
            //System.out.println("");
            System.out.print("[");
            for (int j =0 ; j< itemsets.get(i).length ; j++) {
                System.out.print(" {" + itemsets.get(i)[j].toString() + " }"); }
            System.out.print("]**"); }
    }
    // this function generates the first condidates from a given set
    private ArrayList<Integer[]> generate_1_size_Condidates(Integer[] set) {
        ArrayList<Integer[]> returnArray = new ArrayList<>() ;
        for (int i =0 ; i< set.length ; i++){
            Integer[] temp = new Integer[1] ;
            temp[0] = set[i];
            returnArray.add(temp.clone()); }
        return returnArray;
    }
    // this function generates condidates of 2-size from a given 1-size datasets
    private ArrayList<Integer[]>  generate_2_size_condidates (ArrayList<Integer[]> itemsets){
        ArrayList<Integer[]>  returnarray  = new ArrayList<>() ;
        for (int i=0 ; i<itemsets.size()-1 ; i++ ){
            for (int j = i+1; j< itemsets.size() ;j++ ) {
                returnarray.add(new Integer[]{itemsets.get(i)[0], itemsets.get(j)[0]}) ; }
        }
        return returnarray ;
    }
    // this function generates condidates of k-size from a given 2-size datasets
    private ArrayList<Integer[]>  generate_k_size_condidates (ArrayList<Integer[]> itemsets) {
       ArrayList<Integer[]>  returnarray  = new ArrayList<>() ;
       for (int i=0 ; i<itemsets.size()-1 ; i++ ){
           for (int j =  i+1; j< itemsets.size() ;j++ ) {
               Integer[] thereturnarray = compare_2_arrays(itemsets.get(i), itemsets.get(j)) ;
               if(thereturnarray != null)
               returnarray.add(thereturnarray ) ; }
       }
        return  returnarray;
   }
    // this funcion is used by generation functions it simply adds to arrays to each other if they have the same first elements and diffrent last elemnt
    // to avoid occurrence of the same element in one array as : {a,b} and {b,a}
    private Integer[] compare_2_arrays(Integer[] a , Integer[] b ) {
        Integer[] returnarray ;
       if(Arrays.equals(Arrays.copyOf(a, a.length-1), Arrays.copyOf(b, b.length-1)) && !a[a.length-1].equals(b[b.length-1])) {
           returnarray = new Integer[a.length +1 ] ;
           for (int i =0  ; i < a.length ; i++) {
               returnarray[i] = a[i] ; }
           returnarray[returnarray.length -1] = b [b.length-1] ;
           return  returnarray ;
       }
       return  null ;
    }
    // this function calculates  the support of each elemnt of a layer  so we can determine the infrequent itemes
    private float checksupport (File fl, Integer[] condidate)throws IOException{
      BufferedReader br = new BufferedReader(new FileReader(fl)) ;
      String line ;
      int NumberOfAparrenceinDataFile = 0  ;
      int linesnumber = 0;
      while ((line =br.readLine()) != null ) {
          linesnumber ++;
          String line1 = line.substring(1, line.length() - 1);
          String[] elements = line1.split(",");
          for (int i =0 ; i < elements.length ; i++) {
              elements[i] = elements[i].replaceAll("\\s","");
          }
          Integer[] compareset = new Integer[elements.length];
          for (int i = 0; i < compareset.length; i++)
              compareset[i] = Integer.valueOf(elements[i]);
          if (Arrays.asList(compareset).containsAll(Arrays.asList(condidate))){
              NumberOfAparrenceinDataFile ++ ;
          }
      }if (linesnumber== 0) return 0 ;
        //System.out.println(NumberOfAparrenceinDataFile);
        //System.out.println(linesnumber);
      return  (float) NumberOfAparrenceinDataFile/linesnumber;
  }
    // this function parses all elements from the dataset txt  file and returns an array of Integers
    private Integer[] parsedatafromfile  (File fl ) throws  IOException {
        ArrayList<Integer> temparray  = new ArrayList<>() ;
        BufferedReader br = new BufferedReader(new FileReader(fl)) ;
        String line ;
        String line1 ;
        String[] split ;
            line = br.readLine() ;
            line1 = line.substring(1 , line.length()-1) ;
            split = line1.split(",") ;
        for (int i =0 ; i < split.length ; i++) {
            split[i] = split[i].replaceAll("\\s","");
        }
            for (int i = 0 ; i<split.length ; i++){
                temparray.add(Integer.parseInt(split[i])) ;
            }
        while ((line = br.readLine()) != null){
            line1 = line.substring(1 , line.length()-1) ;
            split = line1.split(",") ;
            for (int i =0 ; i < split.length ; i++) {
                split[i] = split[i].replaceAll("\\s","");
            }
            for (int i = 0 ; i<split.length ; i++){
                if( !(temparray.contains(Integer.parseInt(split[i]))))
                temparray.add(Integer.parseInt(split[i])) ;
            }
        }
        return temparray.toArray(new Integer[temparray.size()]);
    } 
    // this is the general function that uses all of the declared methodes and it prints the frequent itemsets depending on the given minsupp
    private void apriori(File dataset , float minsup) throws IOException {

            ArrayList<Integer[]> frequantItemsets = new ArrayList<>();
            Integer[] a = parsedatafromfile(dataset);
            Arrays.sort(a);
            ArrayList<Integer[]> size1 = generate_1_size_Condidates(a);
            eliminateInfrequentitems(size1, dataset, minsup);
            frequantItemsets.addAll(size1);
            System.out.println("\n Frequant items : ");
            show_Arraylist(frequantItemsets);
            frequantItemsets.clear();
            size1 = generate_2_size_condidates(size1);
            eliminateInfrequentitems(size1, dataset, minsup);
            frequantItemsets.addAll(size1);
            System.out.println("\n Frequant items : ");
            show_Arraylist(frequantItemsets);
            frequantItemsets.clear();

            while ((size1 = generate_k_size_condidates(size1)).size() != 0) {
                eliminateInfrequentitems(size1, dataset, minsup);
                frequantItemsets.addAll(size1);
                System.out.println("\n Frequant items : ");
                show_Arraylist(frequantItemsets);
                frequantItemsets.clear();
            }
    }
    // this function eliminates the infrequent items from a given arraylist using the checkSupport methode
    private void eliminateInfrequentitems(ArrayList<Integer[]> condidates , File fl , float minsupp ) throws IOException{
       for (int i = 0 ;  i < condidates.size(); i++){
           if (checksupport(fl , condidates.get(i) ) < minsupp) {
               condidates.remove(i) ;
               i-- ;
           }
       }
    }
}

