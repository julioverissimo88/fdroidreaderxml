package models;


import org.eclipse.jgit.lib.Repository;

public interface GitHistoryBadSmellMinerInterface {
    void detectAll(Repository repository, String branch, String[] argsPMD) throws Exception;
    //void detectBetweenCommits(Repository repository, String startCommitId, String endCommitId) throws Exception;
    //void fetchAndDetectNew(Repository repository) throws Exception;
    //void detectCommit(Repository repository, String cloneURL, String commitId) throws Exception;
}
