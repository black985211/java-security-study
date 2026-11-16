import java.util.ArrayList;

public class Main {
    static User findUser(ArrayList<User> users, String username) {
        // 遍历 users，找到 username 相同的就返回这个对象
        for (User u : users){
            if (u.username.equals(username)) {
                return u;
            }
        }
        // 循环结束都没找到，就返回 null
        return null;

    }

    static int parseId(String input){
        int n;
        try{
            n = Integer.parseInt(input);
        }catch (NumberFormatException e){
            System.out.println("bad input: "+e.getMessage());
            System.out.println("invalid id");
            return -1;
        }
        return n;
    }

    public static void main(String[] args) {
        System.out.println(parseId("5"));
        parseId("abc");
        parseId("1 OR 1=1");



//        User u1=new User("admin","123456","admin");
//        User u2=new User("alice","1234567","user");
//
//        ArrayList<User> users=new ArrayList<>();
//        users.add(u1);
//        users.add(u2);
//        User fu1=findUser(users,"alice");
//        User fu2=findUser(users,"Bob");
//        if (fu1!=null){
//            System.out.println(fu1.role);
//        }
//        if(fu2==null){
//            System.out.println("user no find");
//        }

    }

}
