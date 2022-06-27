package part2;

import java.util.List;
import java.util.Set;

public abstract class PseudoPaths {

    private List<Node> roots;

    class Node{
        String name;
        List<Node> childs;
    }

    public PseudoPaths(Set<String> paths) {
    }

    public abstract boolean belongs(String path);
}
