package hello.proxy.pureproxy.decorator.code2;

public abstract class Decorator implements Component {

    protected final Component component;

    public Decorator(Component component) {
        this.component = component;
    }
}
