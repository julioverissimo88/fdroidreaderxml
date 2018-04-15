package models;
import org.eclipse.jgit.lib.Repository;

public interface GitServiceInterface {
    Repository cloneIfNotExists(String folder, String cloneUrl) throws Exception;
    Repository openRepository(String folder) throws Exception;
    int countCommits(Repository repository, String branch) throws Exception;
    void checkout(Repository repository, String commitId) throws Exception;
}
