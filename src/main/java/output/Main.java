package output;

import models.FdroidReader;
import models.GitService;
import models.GitHistoryBadSmellMiner;
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
            ArrayList<String> repositorios = fd.getRepositoryApps(qtdApps);

            for(String rep : repositorios){
                System.out.println(rep);
                Repository r = gitService.cloneIfNotExists(pathClone,rep );
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
