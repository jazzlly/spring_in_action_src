package sia.knights;

import java.util.function.BiConsumer;

/**
 * Created by ryanjiang on 2017/8/15.
 */
public class TestEnvProperty {

    public static void main(String[] args) {
//        System.getenv().forEach(new BiConsumer<String, String>() {
//            @Override
//            public void accept(String s, String s2) {
//                System.out.println("key: " + s + ", value:" +s2);
//            }
//        });
//
        System.getProperties().forEach(new BiConsumer<Object, Object>() {
            public void accept(Object o, Object o2) {
                System.out.println("key: " + o + ", value:" +o2);
            }
        });
    }
}
