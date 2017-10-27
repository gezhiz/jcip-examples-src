package net.jcip.examples;

/**
 * StuffIntoPublic
 * <p/>
 * Unsafe publication
 *
 * @author Brian Goetz and Tim Peierls
 */
public class StuffIntoPublic {
    public Holder holder;

    public void initialize() {
        holder = new Holder(42);
    }

    public static void main(String[] args) {
        final StuffIntoPublic stuffIntoPublic = new StuffIntoPublic();
        while (true) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    stuffIntoPublic.initialize();
                    stuffIntoPublic.holder.assertSanity();
                }
            });
        }
    }
}
