package ohmygod.project.deamo;

public enum demo implements hello{
    USER("用户", "user");
    String name;
    String text;

    demo(String name, String text) {
        this.name = name;
        this.text = text;
    }

    @Override
    public void hello() {

    }
}
