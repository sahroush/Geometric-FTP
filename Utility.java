package experimental_results;

import java.util.List;

public class Utility {

    public static void rotate(List<Node> nodes,double degree,boolean is180Mode,boolean anticlockwise) {
        //     all the angles are in degree
        //     rotation
        double mode = is180Mode ? 180.0:360.0;
        degree = anticlockwise ? degree:-1*degree;
        for (Node node : nodes) {
            node.theta = ((node.theta + degree)+mode) % mode;
        }
    }

    public static void mirror(List<Node> nodes) {
        //  note that this function only works for a half circle (0-180), and would mirror w.r.t 90 degree line
        for (Node node:nodes) {
            node.theta = 180.0 - node.theta;
        }
    }
    public static double l2Distance(Node node1,Node node2) {
        double alpha_degree = Math.abs(node1.theta - node2.theta) % 180.0;
        double alpha_radian = Math.toRadians(alpha_degree);
        return Math.sqrt((node1.r * node1.r) + (node2.r * node2.r) - (2* node1.r * node2.r * Math.cos(alpha_radian)));
    }

}