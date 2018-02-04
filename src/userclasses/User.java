package userclasses;

import serverclasses.MessageReceiver;

abstract public class User implements MessageReceiver {
    abstract public void sendMessage(String message);
    abstract public void receive(String message);
}
