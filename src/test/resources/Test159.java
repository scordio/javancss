package foobar;

public interface InterfaceWithDefaultMethod {

    void foo();

    default void bar() {
        foo();
    }
}
