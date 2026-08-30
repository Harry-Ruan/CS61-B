package gitlet;

/** Command-line entry point for Gitlet, a small persistent version-control system. */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        Repository gitletRepo = new Repository();
        if (args == null || args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                gitletRepo.init();
                break;
            case "add":
                gitletRepo.add(args[1]);
                break;
            case "commit":
                gitletRepo.commit(args[1]);
                break;
            case "rm":
                /* If the file is neither staged nor tracked by the head commit */
                gitletRepo.rm(args[1]);
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
                    if (!args[1].equals("--")){
                        System.out.println("Incorrect operands.");
                    }
                    gitletRepo.checkoutHeadFile(args[2]);
                }
                else if (args.length == 4){
                    if (!args[2].equals("--")){
                        System.out.println("Incorrect operands.");
                    }
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
            default:System.out.println("No command with that name exists.");
        }
    }
}
