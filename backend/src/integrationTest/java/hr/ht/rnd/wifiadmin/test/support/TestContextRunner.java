package hr.ht.rnd.wifiadmin.test.support;

interface TestContextRunner {

    TestContextRunner withPropertyValues(String... properties);

    TestContextRunner withUserConfiguration(Class<?>... configs);

    void run(TestContextAssertionConsumer consumer);
}
