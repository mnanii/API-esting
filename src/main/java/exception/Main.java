package exception;

public class Main {

    public static void main(String[] args) {
        try (Connection connection = new Connection()){
            connection.executeSelect();
            connection.executeUpdate();
        } catch (MyException ex){
            System.out.println("Exception");
        } finally {

        }
    }


}
