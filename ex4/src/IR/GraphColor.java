package IR;

import java.util.*;

public class GraphColor {
    HashSet<node> valid = new HashSet<node>();
    HashSet<node> overall = new HashSet<node>();

    public GraphColor(BasicBlocks h) {
        BasicBlocks curr = h;
        while (curr != null) {
            Iterator<String> it = curr.inSet.iterator();
            node nd;
            while (it.hasNext()) {
                String n = it.next();
                nd = find(n);
                if (nd == null) {nd = new node(n);this.overall.add(nd);this.valid.add(nd);}
                for (String s : curr.inSet) {if (!s.equals(nd.name)) {nd.node_hash.add(s);}}
                nd.active_node = nd.node_hash.size();
            }
            curr = curr.direct;
        }
    }

    public Boolean isEmpty() {return this.valid.isEmpty();}

    public node find(String check) {
        for (node node2 : this.valid) {if (Objects.equals(node2.name, check)) {return node2;}}
        return null;
    }

    public class node {
        String name;
        HashSet<String> node_hash;
        int active_node;
        String paint;

        public node(String name) {
            this.name = name;
            this.node_hash = new HashSet<String>();
            this.active_node = 0;
            this.paint = null;
        }
    }
}