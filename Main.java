package assignment2;

public class Main {

    public static void main(String[] args) {
        Human h = new Cyborg();
        if (h instanceof Cyborg)
            ((Cyborg) h).act();




    }


    public interface Human{
        public void think();
    }

    public interface Machine{
        public void move();
    }

    public static class Cyborg implements Human, Machine{
        public void think(){
            return;

        }

        public void move(){
            return;
        }

        public void act(){

        }

    }
}


