package javaeetutorial.helloservice;

import javax.jws.WebService;
import javax.jws.WebMethod;

@WebService
public class Hello {
    private final String message = "Hello, ";

    public void Hello() {
    }

    @WebMethod
    public String sayHello(String name) {
        return message + name + ".";
    }
}
