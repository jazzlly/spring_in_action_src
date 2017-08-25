package sia.knights;

import org.junit.Test;
import org.springframework.core.AliasRegistry;
import org.springframework.core.SimpleAliasRegistry;

/**
 * Created by ryanjiang on 2017/8/14.
 */
public class AliasRegisterTest {

    @Test
    public void smoke() throws Exception {
        AliasRegistry registry = new SimpleAliasRegistry();

        try {
            registry.registerAlias("jiangrui", "jr");
            registry.registerAlias("jr", "ryan");
            // registry.registerAlias("ryan", "jiangrui");
            registry.registerAlias("ryan", "jr");
            registry.registerAlias("ryan", "rj");
            // registry.registerAlias("jjrr", "jr");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
