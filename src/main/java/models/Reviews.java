package models;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Reviews {

    public static void getreviewsApp(String appId,String pathReview){
        try{
            Process processo = Runtime.getRuntime().exec("node getReviews.js " + appId);
            BufferedReader br = new BufferedReader(new InputStreamReader(processo.getInputStream()));
            String linha;

            FileWriter arq = new FileWriter(pathReview + "/" + appId + "/"+ appId +".txt");
            PrintWriter gravarArq = new PrintWriter(arq);

            while((linha = br.readLine()) != null)
                gravarArq.printf(linha);

            arq.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
