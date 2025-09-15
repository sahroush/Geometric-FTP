package experimental_results;

import java.util.ArrayList;
import java.util.Comparator;

public class R2L2TreeMaker {
    // consider r_1 be at theta = 0, which would satisfy using some functions in utility class
    // at the beginning of the program.
    Node p1; // robot at location r_1
    ArrayList<Node> upperHalfCircle; // a sorted list due to angles in [0,180] degrees.
    ArrayList<Node> lowerHalfCircle; // a sorted list due to angles in [0,180] degrees.
    // which is first rotates lower half circle to bring it into the range [0,180].
    // then mirrors it to bring r_1 into zero degree.

    static double FREEZE_TAG = 0.3627;


    public R2L2TreeMaker(ArrayList<Node> rawData) {
        p1 = new Node();
        upperHalfCircle = new ArrayList<>();
        lowerHalfCircle = new ArrayList<>();
        // assume raw data's nodes' angles are in [0,360).
        processRawData(rawData);

    }

    public static void main(String[] args) {
        //(for user): put raw data and create object the raw data have to be set as an array list of node objects.
        // also do not put p0 (node at the center in raw data) this is desired to exist always.
        // (take a look at test methods to understand format better) but in general you have to define a node
        // (robot) based on its 0 < r <= 1 and  0 =< theta <360 in degree.
        // i also added comments to the different parts of code for you for better understanding;


        // the point in center 'p0' is not considered in raw data. uncomment following to see how code works
//        ArrayList<experimental_results.Node> rawData = test2();
//        ArrayList<experimental_results.Node> rawData = test3();
        ArrayList<Node> uniData = Input.uniRawData();
        ArrayList<Node> pharData = Input.pharRawData();

        System.out.println(calculateMakeSpan(uniData)+" --makeSpan of university. ");
        System.out.println(calculateMakeSpan(pharData)+" --makeSpan of pharmacy. ");
//        System.out.println(calculateMakeSpan(rawData)+" --makeSpan of rawData. ");



    }

    public static double calculateMakeSpan(ArrayList<Node> rawData) {
        R2L2TreeMaker machine = new R2L2TreeMaker(rawData);

        Node p1 = machine.p1;

        // IMPORTANT : note that the root of ring strategy after r1, is not r2, it is the nearest in angle,
        // so we have to first determine r2 to define our strategy and then find the root. they are not the same.
        double r2u = Double.MAX_VALUE;
        double r2l = Double.MAX_VALUE;
        Node upperRoot = new Node(),lowerRoot = new Node();
        for (Node node : machine.upperHalfCircle) {
            if(node.r < r2u) {
                r2u = node.r;
                upperRoot = node;
            }
        }
        for (Node node : machine.lowerHalfCircle) {
            if(node.r < r2l) {
                r2l = node.r;
                lowerRoot = node;
            }
        }
        // define if strategy is ring, change root
        if (r2u > FREEZE_TAG) {
            upperRoot = machine.upperHalfCircle.get(0);
        }
        if (r2l > FREEZE_TAG) {
            lowerRoot = machine.lowerHalfCircle.get(0);
        }
        upperRoot.distance_from_root = Utility.l2Distance(upperRoot,p1);
        lowerRoot.distance_from_root = Utility.l2Distance(lowerRoot,p1);
        Tree utree = new Tree(upperRoot);
        Tree ltree = new Tree(lowerRoot);
        for (Node node : machine.upperHalfCircle) {
            node.tree = utree;
        }
        for (Node node : machine.lowerHalfCircle) {
            node.tree = ltree;
        }

        if(r2u > FREEZE_TAG) machine.runRing(machine.upperHalfCircle, utree.root, upperRoot.r, 1.0);
        else machine.runArc(machine.upperHalfCircle,utree.root,180,0);
        if(r2l > FREEZE_TAG)  machine.runRing(machine.lowerHalfCircle,ltree.root, lowerRoot.r,1.0);
        else machine.runArc(machine.lowerHalfCircle,ltree.root,180,0);



        double r1 = p1.r;
        double maxDistanceFromRoot = Double.MIN_VALUE;
        for (Node node : utree.nodes) {
            if(node.isLeaf && node.distance_from_root > maxDistanceFromRoot) {
                // uncomment for log
//                System.out.println(r1+node.distance_from_root+" -from upper tree");
                maxDistanceFromRoot = node.distance_from_root;
            }
        }

        for (Node node : ltree.nodes) {
            if(node.isLeaf && node.distance_from_root > maxDistanceFromRoot) {
                // uncomment for log
//                System.out.println(r1+node.distance_from_root);
                maxDistanceFromRoot = node.distance_from_root;
            }
        }
        return maxDistanceFromRoot +r1;
    }

    public void runRing(ArrayList<Node> assignedRegion,Node father,double r1,double r2) {


        assignedRegion.remove(father);

        //note r1 is less
        double middleR = (r1+r2)/2;
        ArrayList<Node> lowerRing = new ArrayList<>();
        ArrayList<Node> upperRing = new ArrayList<>();
        boolean isUpperRingFatherFound = false;
        boolean isLowerRingFatherFound = false;
        Node lower = null;
        Node upper = null;

        for(Node node:assignedRegion) {
            if(node.r < middleR) {
                lowerRing.add(node);
                if(!isLowerRingFatherFound) {
                    lower = node;
                    lower.distance_from_root = father.distance_from_root + Utility.l2Distance(father,lower);
                    father.setLeftChild(lower) ;
                    isLowerRingFatherFound = true;
                    lower.father = father;
                }
            }
            else{
                upperRing.add(node);
                if(!isUpperRingFatherFound) {
                    upper = node;
                    upper.distance_from_root = father.distance_from_root + Utility.l2Distance(father,upper);
                    father.setRightChild(upper);
                    isUpperRingFatherFound = true;
                    upper.father = father;
                }
            }
        }

        //check if leaf found and close loop
        //base case:
        if(father.getRightChild() == null && father.getLeftChild() == null) {// check if nullity syntax is correct
            father.isLeaf = true;
            return;
        }
        if (upper != null) runRing(upperRing,upper,middleR,r2);
        if (lower != null) runRing(lowerRing,lower,r1,middleR);

        // do not forget to update weight



    }
    public  void runArc(ArrayList<Node> assignedRegion,Node father,double leftAngle,double rightAngle) {
        // left and right angles are desired in the upper half which goes from 0 to 180,
        //  so intuitively and also in this code left angle >= right angle
        assignedRegion.remove(father);

        double middleAngle = (leftAngle+rightAngle)/2.0;
        ArrayList<Node> leftArc = new ArrayList<>();
        ArrayList<Node> rightArc = new ArrayList<>();
        double leftFatherR = Double.MAX_VALUE;
        double rightFatherR = Double.MAX_VALUE;
        Node left = null;
        Node right = null;

        for(Node node:assignedRegion) {
            if(node.theta > middleAngle) {
                leftArc.add(node);
                if(node.r < leftFatherR) {
                    leftFatherR = node.r;
                    left = node;

                }
            }
            else{
                rightArc.add(node);
                if(node.r < rightFatherR) {
                    rightFatherR = node.r;
                    right = node;
                }
            }
        }
        father.setLeftChild(left);
        father.setRightChild(right);

        //check if leaf found and close loop
        //base case:
        if(left == null && right == null) {// check if nullity syntax is correct
            father.isLeaf = true;
            return;
        }
        if (left != null) {
            left.father = father;
            left.distance_from_root = father.distance_from_root + Utility.l2Distance(father,left);
            runArc(leftArc, left, middleAngle, leftAngle);

        }
        if (right != null) {
            right.father = father;
            right.distance_from_root = father.distance_from_root + Utility.l2Distance(father,right);
            runArc(rightArc, right, rightAngle, middleAngle);
        }

    }

    public void processRawData(ArrayList<Node> rawData) {

        // clearance p0.
        p1.r = Double.MAX_VALUE;
        for(Node node: rawData) {
            if (node.r < p1.r) {
                p1 = node;
            }
        }

        // rotate to put r_1 on 0 degree
        Utility.rotate(rawData,p1.theta,false,false);

        // clearance upper/lowerHalf components after put r_0 in 0 degree.
        // first removing p1 from raw data.
        rawData.remove(p1);
        for(Node node: rawData) {
            if(node.theta > 180.0) lowerHalfCircle.add(node);
            else upperHalfCircle.add(node);
        }

        // rotate lowerHalf to have it in (0,180) now they are in (180,360)
        Utility.rotate(lowerHalfCircle,180.0,false,false);

        //mirror lower half to have r_1 in 0, if it was in lower half it was in 180 now
        Utility.mirror(lowerHalfCircle);

        // final step is to sort lower and upper half then we have our all desired considerations
        Comparator<Node> angleBase = new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Double.compare(o1.theta, o2.theta);
            }
        };
        upperHalfCircle.sort(angleBase);
        lowerHalfCircle.sort(angleBase);


    }



    public static void test1()  {
        Node n0 = new Node();
        n0.theta = 0;
        n0.r = 0.5;
        Node n1 = new Node();
        n1.theta = 90;
        n1.r = 1;
        Node n2 = new Node();
        n2.theta = 300;
        n2.r = 1;
        Node n3 = new Node();
        n3.theta = 190;
        n3.r = 1;
        Node n4 = new Node();
        n4.theta = 185;
        n4.r = 1;


        ArrayList<Node> raw = new ArrayList<>();
        raw.add(n0);
        raw.add(n1);
        raw.add(n2);
        raw.add(n3);
        raw.add(n4);

        R2L2TreeMaker object = new R2L2TreeMaker(raw);

        for (Node node : object.upperHalfCircle) {
            System.out.println(node.theta);
        }
        for (Node node : object.lowerHalfCircle) {
            System.out.println(node.theta);
        }

    }


    public static ArrayList<Node> test2() {
        Node n0 = new Node();
        n0.theta = 30;
        n0.r = 0.2;

        Node n1 = new Node();
        n1.theta = 60;
        n1.r = 0.5;

        Node n2 = new Node();
        n2.theta = 120;
        n2.r = 1.0;

        Node n3 = new Node();
        n3.theta = 270;
        n3.r = 0.36;

        Node n4 = new Node();
        n4.theta = 300;
        n4.r = 0.5;

//        experimental_results.Node n5 = new experimental_results.Node();
//        n5.theta = 185;
//        n5.r = 1;


        ArrayList<Node> raw = new ArrayList<>();
        raw.add(n0);
        raw.add(n1);
        raw.add(n2);
        raw.add(n3);
        raw.add(n4);
//        raw.add(n5);

        return raw;
    }
    public static ArrayList<Node> test3()  {
        Node n0 = new Node();
        n0.theta = 0;
        n0.r = 1;
        Node n1 = new Node();
        n1.theta = 90;
        n1.r = 1;
        Node n2 = new Node();
        n2.theta = 180;
        n2.r = 1;
        Node n3 = new Node();
        n3.theta = 270;
        n3.r = 1;


        ArrayList<Node> raw = new ArrayList<>();
        raw.add(n0);
        raw.add(n1);
        raw.add(n2);
        raw.add(n3);

        return raw;
    }



}

class Tree {
    Node root;
    ArrayList<Node> nodes;

    public Tree(Node root) {
        this.root = root;
        root.tree = this;
        root.isRoot = true;
        nodes = new ArrayList<>();
        nodes.add(root);
    }
}
class Node{
    Tree tree;
    Node father;
    private Node leftChild;
    private Node rightChild;
//   location parameters:
    double r,theta; // r is the distance from center , also theta is the
    double distance_from_root;
    boolean isRoot; // 0 : no , 1 : yes
    boolean isLeaf; // 0 : no , 1 : yes

    public Node(Tree tree, Node father) {
        this.tree = tree;
        this.father = father;
    }

    public Node() {
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        if (leftChild != null) {
            this.leftChild = leftChild;
            tree.nodes.add(leftChild);
        }
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        if (rightChild != null) {
            this.rightChild = rightChild;
            tree.nodes.add(rightChild);
        }
    }
}

