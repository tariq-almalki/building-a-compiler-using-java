package main.datastructures;

public class Stack {

    @SuppressWarnings("FieldMayBeFinal")
    private int stackSize;
    @SuppressWarnings("FieldMayBeFinal")
    public String[] stackArr;
    private int top;

    public Stack(int size) {
        this.stackSize = size;
        this.stackArr = new String[stackSize];
        this.top = -1;
    }

    public void push(String entry) throws Exception {
        if (this.isStackFull()) {
            throw new Exception("Stack is already full. Can not add element.");
        }
        this.stackArr[++top] = entry;
    }

    public String pop() throws Exception {
        if (this.isStackEmpty()) {
            throw new Exception("Stack is empty. Can not remove element.");
        }
        String entry = this.stackArr[top--];
        return entry;
    }

    public String peek() {
        return stackArr[top];
    }

    public void clear() {
        top = -1;
    }

    public boolean isStackEmpty() {
        return (top == -1);
    }

    public boolean isStackFull() {
        return (top == stackSize - 1);
    }

    public int size() {
        return top + 1;
    }

    public String printStack() {
        String joined = "";
        for (int i = 0; i < top + 1; i++) {
            joined += stackArr[i] + " ";
        }
        return joined;
    }

}
