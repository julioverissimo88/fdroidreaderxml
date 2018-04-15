package models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;


public class FdroidReader {
    private ArrayList<String> repositoryList = new ArrayList<String>();
    private String url = "https://f-droid.org/repo/index.xml";

    public ArrayList<String> getRepositoryApps(long qtdApps){
        try{
            DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
            bf.setNamespaceAware(true);
            DocumentBuilder dbuilder = bf.newDocumentBuilder();
            Document doc = dbuilder.parse(new URL(url).openStream());

            NodeList nodeL = doc.getElementsByTagName("application");

            for(int i=0; i < qtdApps; i++){
                Element elemento = (Element) nodeL.item(i);
                NodeList dado = elemento.getElementsByTagName("source");
                repositoryList.add(dado.item(0).getTextContent());
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        return repositoryList;
    }
}
