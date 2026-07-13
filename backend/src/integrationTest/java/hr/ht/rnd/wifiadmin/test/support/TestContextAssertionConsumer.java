package hr.ht.rnd.wifiadmin.test.support;

@FunctionalInterface
interface TestContextAssertionConsumer {

    void accept(TestApplicationContext context);
}
