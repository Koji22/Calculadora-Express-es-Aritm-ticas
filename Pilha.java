public class Pilha {
    private Object[] stack;
    private int position;

    public Pilha() {
        this.position = -1;
        this.stack = new Object[100];
    }
    
    public Pilha(int size) {
        this.position = -1;
        this.stack = new Object[size];
    }
    
    public boolean isEmpty() {
        if (this.position == -1) {
            return true;
        }
        return false;
    }
    
    public int size() {
        if (this.isEmpty()) {
            return 0;
        }
        return this.position + 1;
    }
    
    public void push(Object value) {
        if (this.position < this.stack.length - 1) {
            this.stack[++position] = value;
        }
    }
    
    public Object pop() {
        if (this.isEmpty()) {
            return null;
        }
        return this.stack[position--];
    }
    
    public Object peek() {
        if (this.isEmpty()) {
            return null;
        }
        return this.stack[position];
    }
}