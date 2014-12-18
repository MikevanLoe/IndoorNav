package project.movinindoor.Algorithm;

import java.util.LinkedList;

import project.movinindoor.Graph.Node;

/**
 * Created by Davey on 18-12-2014.
 */
public class NavigationRoute {

    private int num = 0;
    private LinkedList<String> linkedList;

    public NavigationRoute() {
        linkedList = new LinkedList<>();
        linkedList.add("up, Ga rechtdoor");
        linkedList.add("up, Ga rechtdoor");
        linkedList.add("left, Ga links");
        linkedList.add("right, Ga rechts");
        linkedList.add("sRight, Ga schuin naar rechts");
        linkedList.add("sLeft, Ga schuin naar links");
        linkedList.add("left, U bent gearriveerd");
    }

    public LinkedList<String> getLinkedList() {
        return linkedList;
    }

    public void setLinkedList(LinkedList<String> linkedList) {
        this.linkedList = linkedList;
    }

    public String getNextCard() {
        String s = linkedList.get(num);
        if (num < linkedList.size()) num++;
        return s;
    }

    public int getNum() {
        return num;
    }
}
