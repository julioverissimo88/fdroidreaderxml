package models;
import com.google.common.collect.Lists;
import net.sourceforge.pmd.PMD;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONObject;
import org.json.XML;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Date;
import java.util.List;

public class GitHistoryBadSmellMiner implements GitHistoryBadSmellMinerInterface {

    //public static final String config[];
    public void detectAll(Repository repository, String branch, String[] argsPMD) throws Exception {
        try {
            Git git = new Git(repository);
            GitService gitService = new GitService();
            List<Ref> branches = git.branchList().call();
            String path = repository.getDirectory().toString().substring(0,repository.getDirectory().toString().length() - 4);

            for(Ref branche : branches) {
                String branchName = branche.getName();
                System.out.println("Commits of branch: " + branchName);
                System.out.println("-------------------------------------");

                Iterable<RevCommit> commits = git.log().add(repository.resolve(branchName)).call();
                List<RevCommit> commitsList = Lists.newArrayList(commits.iterator());

                for (RevCommit commit : commitsList) {
                    System.out.println("Analisando Commit " + commit.getName());
                    System.out.println(commit.getAuthorIdent().getName());
                    System.out.println(new Date(commit.getCommitTime() * 1000L));
                    System.out.println(commit.getFullMessage());
                    gitService.checkout(repository, commit.getName()); //faz chechout no repositorio


                    String[] arguments = {"-d",
                            path,
                            "-f",
                            "xml",
                            "-R",
                            "category/java/design.xml/ExcessiveMethodLength, category/java/design.xml/GodClass", //Long Method and God Class
                            "-r",
                            "/home/matheus/pmd/" + commit.getName() +".xml" };

                    PMD.run(arguments);


                    BufferedReader br = new BufferedReader(new FileReader("/home/matheus/pmd/" + commit.getName() +".xml"));
                    String linha  = "";
                    while(br.ready()){
                        linha += br.readLine();
                    }
                    br.close();

                    JSONObject xmlJSONObj = XML.toJSONObject(linha);
                    String jsonPrettyPrintString = xmlJSONObj.toString(4);



                    File file = new File("/home/matheus/pmd/" + commit.getName() +".json");
                    if(file.createNewFile()) {
                        System.out.println("File is created");
                    } else {
                        System.out.println("File already exists");
                    }


                    FileOutputStream fop = new FileOutputStream(file);

                    if(!file.exists()) {
                        file.createNewFile();
                    }

                    byte[] textBytes = jsonPrettyPrintString.getBytes();

                    fop.write(textBytes);
                    fop.flush();
                    fop.close();

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
