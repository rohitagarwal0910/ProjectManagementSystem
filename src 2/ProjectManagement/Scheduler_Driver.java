package ProjectManagement;

import PriorityQueue.PriorityQueueDriverCode;
import Trie.*;
import RedBlack.*;
import PriorityQueue.*;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

public class Scheduler_Driver extends Thread implements SchedulerInterface {
    public Trie<Project> projects = new Trie<Project>();
    public MaxHeap<Job> jobs = new MaxHeap<Job>();
    public Trie<User> users = new Trie<User>();
    public Trie<Job> jobTrie = new Trie<Job>();
    public RBTree<String, Job> jobsLeft = new RBTree<String, Job>();
    public ArrayList<Job> completedJobs = new ArrayList<Job>();
    int time = 0;
    int sno = 0;

    public static void main(String[] args) throws IOException {
        Scheduler_Driver scheduler_driver = new Scheduler_Driver();

        File file;
        if (args.length == 0) {
            URL url = Scheduler_Driver.class.getResource("INP");
            file = new File(url.getPath());
        } else {
            file = new File(args[0]);
        }

        scheduler_driver.execute(file);
    }

    public void execute(File file) throws IOException {

        URL url = Scheduler_Driver.class.getResource("INP");
        file = new File(url.getPath());

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.err.println("Input file Not found. " + file.getAbsolutePath());
        }
        String st;
        while ((st = br.readLine()) != null) {
            String[] cmd = st.split(" ");
            if (cmd.length == 0) {
                System.err.println("Error parsing: " + st);
                return;
            }

            switch (cmd[0]) {
            case "PROJECT":
                handle_project(cmd);
                break;
            case "JOB":
                handle_job(cmd);
                break;
            case "USER":
                handle_user(cmd[1]);
                break;
            case "QUERY":
                handle_query(cmd[1]);
                break;
            case "":
                handle_empty_line();
                break;
            case "ADD":
                handle_add(cmd);
                break;
            default:
                System.err.println("Unknown command: " + cmd[0]);
            }
        }

        run_to_completion();

        print_stats();

    }

    @Override
    public void run() {
        // till there are JOBS
        schedule();
        System.out.println("System execution completed");
    }

    @Override
    public void run_to_completion() {
        while(jobs.list.size() > 0){
            run();
        }
    }

    @Override
    public void handle_project(String[] cmd) {
        System.out.println("Creating project");
        projects.insert(cmd[1], new Project(cmd[1], Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3])));
    }

    @Override
    public void handle_job(String[] cmd) {
        System.out.println("Creating job");
        TrieNode project = projects.search(cmd[2]);
        if (project == null) {
            System.out.println("No such project exists. " + cmd[2]);
            return;
        }
        TrieNode user = users.search(cmd[3]);
        if (user == null) {
            System.out.println("No such user exists: " + cmd[3]);
            return;
        }
        Job job = new Job(cmd[1], (Project) project.getValue(), (User) user.getValue(), Integer.parseInt(cmd[4]), sno++);
        jobs.insert(job);
        jobTrie.insert(job.name, job);
    }

    @Override
    public void handle_user(String name) {
        System.out.println("Creating user");
        users.insert(name, new User(name));
    }

    @Override
    public void handle_query(String key) {
        System.out.println("Querying");
        TrieNode job = jobTrie.search(key);
        if (job == null) {
            System.out.println(key + ": NO SUCH JOB");
            return;
        } else {
            if (((Job) job.getValue()).completed) {
                System.out.println(key + ": COMPLETED");
            } else
                System.out.println(key + ": NOT FINISHED");
        }
    }

    @Override
    public void handle_empty_line() {
        schedule();
        System.out.println("Execution cycle completed");
    }

    @Override
    public void handle_add(String[] cmd) {
        System.out.println("ADDING Budget");
        TrieNode project = projects.search(cmd[1]);
        if (project == null) {
            System.out.println("No such project exists. " + cmd[1]);
            return;
        }
        ((Project) project.getValue()).budget += Integer.parseInt(cmd[2]);
        RedBlackNode r = jobsLeft.search(cmd[1]);
        if(r.key == null) return;
        List<Job> toAdd = r.getValues();
        for(int i = 0; i < toAdd.size(); i++){
            jobs.insert(toAdd.get(i));
        }
        r.values.clear();
    }

    MaxHeap<Job> getUnfinishedJobs(){
        MaxHeap<Job> unfinishedJobs = new MaxHeap<Job>();
        RedBlackNode cn = jobsLeft.root;
        if (cn == null) return unfinishedJobs;
        Queue<RedBlackNode> queue = new LinkedList<RedBlackNode>();
        queue.add(cn);
        while(queue.size() > 0){
            RedBlackNode t = queue.remove();
            List<Job> j = t.getValues();
            for (int i=0; i<j.size(); i++){
                unfinishedJobs.insert(j.get(i));
            }
            if (t.left.key != null) {queue.add(t.left);}
            if (t.right.key != null) {queue.add(t.right);}
        }
        return unfinishedJobs;
    }

    @Override
    public void print_stats() {
        MaxHeap<Job> unfinishedJobs = getUnfinishedJobs();
        int no = unfinishedJobs.list.size();
        System.out.println("--------------STATS---------------");
        System.out.println("Total jobs done: " + completedJobs.size());
        for (int i = 0; i < completedJobs.size(); i++){
            System.out.println(completedJobs.get(i).toString());
        }
        System.out.println("------------------------");
        System.out.println("Unfinished jobs: ");
        while(unfinishedJobs.list.size() > 0){
            System.out.println(unfinishedJobs.extractMax().toString());
        }
        System.out.println("Total unfinished jobs: " + no);
        System.out.println("--------------STATS DONE---------------");
    }

    @Override
    public void schedule() {
        System.out.println("Running code");
        System.out.println("Remaining jobs: " + jobs.list.size());
        while (true) {
            Job job = jobs.extractMax();
            if (job == null) {
                break;
            }
            System.out.println("Executing: " + job.name + " from: " + job.project.name);
            if (job.project.budget >= job.runtime) {
                job.completed = true;
                time += job.runtime;
                job.completedTime = time;
                job.project.budget -= job.runtime;
                System.out.println("Project: " + job.project.name + " budget remaining: " + job.project.budget);
                completedJobs.add(job);
                break;
            } else {
                System.out.println("Un-sufficient budget.");
                jobsLeft.insert(job.project.name, job);
            }
        }
    }
}
