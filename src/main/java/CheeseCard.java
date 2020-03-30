public class CheeseCard {

    private int value;
    private boolean isTrap;

    public CheeseCard(int value, boolean isTrap) {
        setValue(value);
        setTrap(isTrap);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isTrap() {
        return isTrap;
    }

    public void setTrap(boolean trap) {
        isTrap = trap;
    }
}
