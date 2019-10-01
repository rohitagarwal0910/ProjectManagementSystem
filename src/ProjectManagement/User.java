package ProjectManagement;

public class User implements Comparable<User> {
    String name;

    User(String name){
        this.name = name;
    }

    @Override
    public int compareTo(User user) {
        return this.name.compareTo(user.name);
    }
}
