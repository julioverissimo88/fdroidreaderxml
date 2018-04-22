package models;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RenameDetector;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.RevWalkUtils;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;


public class GitService implements GitServiceInterface {

    public static final String REMOTE_REFS_PREFIX = "refs/remotes/origin/";
    //Logger logger = LoggerFactory.getLogger(GitService.class);

    public Repository cloneIfNotExists(String projectPath, String cloneUrl) throws Exception {

        File folder = new File(projectPath);
        Repository repository;

        if(folder.exists()) {
            RepositoryBuilder builder = new RepositoryBuilder();
            repository = builder
                    .setGitDir(new File(folder,".git"))
                    .readEnvironment()
                    .findGitDir()
                    .build();
        } else {
            Git git = Git.cloneRepository()
                    .setDirectory(folder)
                    .setURI(cloneUrl)
                    .setCloneAllBranches(true)
                    .call();
            repository = git.getRepository();
        }

        return repository;

    }

    public Repository openRepository(String repositoryPath) throws Exception {
        File folder = new File(repositoryPath);
        Repository repository;
        if (folder.exists()) {
            RepositoryBuilder builder = new RepositoryBuilder();
            repository = builder
                    .setGitDir(new File(folder, ".git"))
                    .readEnvironment()
                    .findGitDir()
                    .build();
        } else {
            throw new FileNotFoundException(repositoryPath);
        }
        return repository;

    }

    public int countCommits(Repository repository, String branch) throws Exception {
        RevWalk walk = new RevWalk(repository);
        try {
            Ref ref = repository.getRef(REMOTE_REFS_PREFIX + branch);
            ObjectId objectId = ref.getObjectId();
            RevCommit start = walk.parseCommit(objectId);
            walk.setRevFilter(RevFilter.NO_MERGES);
            return RevWalkUtils.count(walk, start, null);
        } finally {
            walk.dispose();
        }
    }

    public void checkout(Repository repository, String commitId) throws Exception {
        try (Git git = new Git(repository)) {
            CheckoutCommand checkout = git.checkout().setName(commitId);
            checkout.call();
        }
    }


    /** Esturutra de File Tree Diff **/
    public void fileTreeDiff(Repository repository, //respositório
                             RevCommit current, //referencia do commit atual
                             List<String> javaFilesBefore, //arquivos antes
                             List<String> javaFilesCurrent, //arquivos atuais
                             Map<String, String> renamedFilesHint, //dica de arquivo renomedo
                             boolean detectRenames) //se quer identificar arquivo renomeado
            throws Exception {



        if (current.getParentCount() > 0) {
            ObjectId oldHead = current.getParent(0).getTree();
            ObjectId head = current.getTree();

            // prepare the two iterators to compute the diff between
            ObjectReader reader = repository.newObjectReader();
            CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
            oldTreeIter.reset(reader, oldHead);
            CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
            newTreeIter.reset(reader, head);
            // finally get the list of changed files
            try (Git git = new Git(repository)) {
                List<DiffEntry> diffs = git.diff()
                        .setNewTree(newTreeIter)
                        .setOldTree(oldTreeIter)
                        .setShowNameAndStatusOnly(true)
                        .call();
                if (detectRenames) {
                    RenameDetector rd = new RenameDetector(repository);
                    rd.addAll(diffs);
                    diffs = rd.compute();
                }

                for (DiffEntry entry : diffs) {
                    DiffEntry.ChangeType changeType = entry.getChangeType();
                    if (changeType != DiffEntry.ChangeType.ADD) {
                        String oldPath = entry.getOldPath();
                        if (isJavafile(oldPath)) {
                            javaFilesBefore.add(oldPath);
                        }
                    }
                    if (changeType != DiffEntry.ChangeType.DELETE) {
                        String newPath = entry.getNewPath();
                        if (isJavafile(newPath)) {
                            javaFilesCurrent.add(newPath);
                            if (changeType == DiffEntry.ChangeType.RENAME) {
                                String oldPath = entry.getOldPath();
                                renamedFilesHint.put(oldPath, newPath);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isJavafile(String path) {
        return path.endsWith(".java");
    }
}
