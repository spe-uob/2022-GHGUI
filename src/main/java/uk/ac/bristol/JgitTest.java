package uk.ac.bristol;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JgitTest {

    public static void main(String[] args) throws GitAPIException {
//        try (Repository repository = CookbookHelper.openJGitCookbookRepository()) {
//            System.out.println("Starting fetch");
//            try (Git git = new Git(repository)) {
//                FetchResult result = git.fetch().setCheckFetchedObjects(true).call();
//                System.out.println("Messages: " + result.getMessages());
//            }
//        }

        String localPath = "C:\\Users\\luo\\Desktop\\git-gui\\a1\\2022-GHGUI";
        Git git = Git.init().setDirectory(new File(localPath)).call();
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider("su21085","Ww121213.");
        git.pull().setRemoteBranchName("dev-su").setCredentialsProvider(usernamePasswordCredentialsProvider).call();
        System.out.println("ok");
//        List<Ref> call = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call();
//        call.forEach(ref -> {
//            System.out.println(ref);
//        });
//        listBranchCommand.
//        System.out.println(git);
//        getRemoteBranchs("C:\\Users\\luo\\Desktop\\git-gui\\a1\\2022-GHGUI");

    }

//    /**
//     * 获取所有分支
//     *
//     * @param url
//     */
//    public static void getRemoteBranchs(String url) {
//        try {
//            Collection<Ref> refList;
//
//            UsernamePasswordCredentialsProvider pro = new UsernamePasswordCredentialsProvider("su21085", "Ww121213.");
//            refList = Git.lsRemoteRepository().setRemote(url).setCredentialsProvider(pro).call();
//            List<String> branchnameList = new ArrayList<>(4);
//            for (Ref ref : refList) {
//
//                String refName = ref.getName();
//                if (refName.startsWith("refs/remotes/origin/")) {                       //需要进行筛选
//                    String branchName = refName.replace("refs/remotes/origin/", "");
//                    if(!branchName.equals("HEAD")){
//                        branchnameList.add(branchName);
//                    }
//                }
//            }
//
//            System.out.println("共用分支" + branchnameList.size() + "个");
//            branchnameList.forEach(System.out::println);
//        } catch (Exception e) {
//            System.out.println("error");
//        }
//    }

}
