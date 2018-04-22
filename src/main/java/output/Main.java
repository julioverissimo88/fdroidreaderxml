package output;

import models.*;

import java.util.ArrayList;
import java.util.Scanner;
import org.eclipse.jgit.lib.Repository;

public class Main {
    public static void main(String[] args) {
        System.out.println("F-Droid Xml Reader");
        System.out.println("Informe a quantidade de aplicativos a baixar");
        Scanner sc = new Scanner(System.in);
        int qtdApps = sc.nextInt();
        System.out.println("Informe o caminho para clonar os repositorios");
        sc = new Scanner(System.in);
        String pathClone = sc.next();

        GitService gitService = new GitService();
        GitHistoryBadSmellMiner gitHistoryBadSmellMiner = new GitHistoryBadSmellMiner();

        try{
            FdroidReader fd = new FdroidReader();
            ArrayList<App> repositorios = fd.getRepositoryApps(qtdApps);

            for(App rep : repositorios){
                System.out.println(rep.getAppId());
                Repository r = gitService.cloneIfNotExists(pathClone + rep.getAppId(),rep.getRepository() );
                //Captura Reviews
                Reviews.getreviewsApp(rep.getAppId(),pathClone);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
