package ProjectManagement;

public class Job implements Comparable<Job> {
    String name;
    Project project;
    User user;
    int runtime;
    public int completedTime = 0;
    boolean completed;
    int sno;

    Job(String name, Project project, User user, int runtime, int sno){
        this.name = name;
        this.project = project;
        this.user = user;
        this.runtime = runtime;
        completed = false;
        this.sno = sno;
    }

    @Override
    public int compareTo(Job job) {
        return (this.project.priority - job.project.priority != 0)? this.project.priority - job.project.priority : job.sno - this.sno;
    }

    @Override
    public String toString() {
        return "Job{user='" + user.name + "', project='" + project.name + "', jobstatus=" + ((completed)? "COMPLETED" : "REQUESTED") + ", execution_time=" + runtime + ", end_time=" + ((completed)? completedTime : "null") + ", name='" + name + "'}";
    }
}