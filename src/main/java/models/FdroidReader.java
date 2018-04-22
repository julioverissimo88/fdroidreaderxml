package models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;


public class FdroidReader {
    private ArrayList<App> repositoryList = new ArrayList<App>();
    private String url = "https://f-droid.org/repo/index.xml";
    private App app = new App();
    private Element elemento = null;
    private NodeList dado = null;

    public ArrayList<App> getRepositoryApps(long qtdApps){
        try{
            DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
            bf.setNamespaceAware(true);
            DocumentBuilder dbuilder = bf.newDocumentBuilder();
            Document doc = dbuilder.parse(new URL(url).openStream());

            NodeList nodeL = doc.getElementsByTagName("application");

            for(int i=0; i < qtdApps; i++){
                elemento = (Element) nodeL.item(i);
                dado = elemento.getElementsByTagName("id");
                app.setAppId(dado.item(0).getTextContent());

                dado = elemento.getElementsByTagName("source");
                app.setRepository(dado.item(0).getTextContent());

                dado = elemento.getElementsByTagName("license");
                app.setAppLicense(dado.item(0).getTextContent());
                repositoryList.add(app);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        return repositoryList;
    }
}
