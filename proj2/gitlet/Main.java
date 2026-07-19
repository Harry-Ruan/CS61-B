package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        Repository gitletRepo = new Repository();
        // TODO: what if args is empty?
        if (args == null){
            System.out.println("no input");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                gitletRepo.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                gitletRepo.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                gitletRepo.commit(args[1]);
                break;
            case "rm":
                /* If the file is neither staged nor tracked by the head commit */
                String rmFilename = args[1];
                if (!gitletRepo.fileTracked(rmFilename)){
                    System.out.println("No reason to remove the file.");
                    break;
                }
                gitletRepo.rm(rmFilename);
                break;
            case "log":
                gitletRepo.log();
                break;
            case "global-log":
                gitletRepo.global_log();
                break;
            case "find":
                gitletRepo.find(args[1]);
                break;
            case "status":
                gitletRepo.status();
                break;
            case "checkout":
                if (args.length == 2){
                    gitletRepo.checkoutBranch(args[1]);
                }
                else if (args.length == 3){
                    gitletRepo.checkoutHeadFile(args[2]);
                }
                else if (args.length == 4){
                    gitletRepo.checkoutFile(args[1], args[3]);
                }
                break;
            case "branch":
                gitletRepo.branch(args[1]);
                break;
            case "rm-branch":
                gitletRepo.rm_branch(args[1]);
                break;
            case "reset":
                gitletRepo.reset(args[1]);
                break;
            case "merge":
                gitletRepo.merge(args[1]);
                break;
        }
    }
}
