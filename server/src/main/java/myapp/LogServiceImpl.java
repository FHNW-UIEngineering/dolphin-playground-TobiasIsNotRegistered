package myapp;

public class LogServiceImpl implements ILogService {
    @Override
    public void log(Object message) {
        System.out.println("message = " + message);
    }
}
