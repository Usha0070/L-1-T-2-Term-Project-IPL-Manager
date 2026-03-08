package com.example.demonew.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketWrapper implements AutoCloseable{
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    // Constructor for client-side usage
    public SocketWrapper(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
    }

    // Constructor for server-side usage
    public SocketWrapper(Socket socket) throws IOException {
        this.socket = socket;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
    }

    public Object read() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    public void write(Object o) throws IOException {
        oos.writeObject(o);
        oos.flush();
    }

    public void closeConnection() throws IOException {
        oos.close();
        ois.close();
        socket.close();
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}

