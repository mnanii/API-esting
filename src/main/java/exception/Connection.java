package exception;

public class Connection implements AutoCloseable{
    public Connection() {
        openDbCollection();
    }

    public void closeConnection(){
        System.out.println("close connection");
    }

    public void openDbCollection()  {
        System.out.println("Open connection");
    }

    public void executeSelect() throws MyException {
        System.out.println("execute select");
        throw new MyException();
    }

    public void executeUpdate() {
        System.out.println("execute update");
    }

    @Override
    public void close(){
        closeConnection();
    }
}
