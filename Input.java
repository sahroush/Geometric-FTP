package experimental_results;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Input {
    private static final List<Double> r_uni = Arrays.asList(
            0.8668090066, 0.6908119437, 1.0000000000, 0.4518651596, 0.5888245563,
            0.6006947315, 0.6014420164, 0.2591872143, 0.2661299945, 0.1677456635,
            0.4786578932, 0.6830403567, 0.7828636223, 0.9408239267
    );

    private static final List<Double> theta_uni = Arrays.asList(
            -1.2482720483, -1.5446889945, -0.9595490925, -1.8858647290, -0.3918807490,
            -2.7819198883, 3.0952782145, -3.0323410631, 2.3313784576, 1.5195405273,
            0.4380063900, 0.7226926090, 1.4237899691, 1.1266821262
    );



    private static final List<Double> r_phar = Arrays.asList(
            0.5930275188, 0.1967615002, 0.1633187636, 0.9957279437, 0.4266144302,
            0.1496032539, 0.4135109594, 1.0000000000, 0.3864807464, 0.3274678663,
            0.2000524083, 0.5524080942, 0.1495595578, 0.4934352173, 0.3313547355,
            0.5281760813, 0.4490208471, 0.8798564249, 0.5472033367, 0.5205672662,
            0.5855200984, 0.4210861948, 0.0609233974, 0.1295052775, 0.2263137770,
            0.6758235942, 0.6070110376, 0.3846117635, 0.0002394461, 0.5281760813,
            0.5855200984, 0.1495595578, 0.1633187636, 0.0668798813, 0.0626470896,
            0.1967615002, 0.1633187636, 0.6070110376, 0.5281760813, 0.0007183384,
            0.1495595578, 0.0002394461, 0.1633187636, 0.5281760813
    );

    private static final List<Double> theta_phar = Arrays.asList(
            -1.4707015380, -1.5105245881, -2.0631929701, -1.8877611996, -2.1231273187,
            1.2590669115,  1.2818103093, -1.8920650051,  0.3126070021, -2.1701278591,
            2.2717113775,  1.2236488684,  2.4475791200, -1.4469520574,  1.3733005565,
            -0.6193923966, -1.5319479573, -1.0944116495, -1.9983200835, -2.1422901452,
            0.4987005331, -1.5422441525,  1.4454072065,  2.7639995967, -2.0582011787,
            -1.6618644883, -1.4702117711,  1.4205567936,  0.7853981634, -0.6193923966,
            0.4987005331,  2.4475791200, -2.0631929701,  1.5657330789,  1.5762016796,
            -1.5105245881, -2.0631929701, -1.4702117711, -0.6193923966,  0.7853981634,
            2.4475791200,  0.7853981634, -2.0631929701, -0.6193923966
    );


    public static ArrayList<Node> pharRawData(){
        ArrayList<Node> pharRawData = new ArrayList<>();
        for (int i = 0 ; i < r_phar.size();i++) {
            Node node = new Node();
            node.r = r_phar.get(i);
            node.theta = radToDeg(theta_phar.get(i));
            pharRawData.add(node);
        }
        return pharRawData;
    }

    public static ArrayList<Node> uniRawData(){
        ArrayList<Node> uniRawData = new ArrayList<>();
        for (int i = 0 ; i < r_uni.size();i++) {
            Node node = new Node();
            node.r = r_uni.get(i);
            node.theta = radToDeg(theta_uni.get(i));
            uniRawData.add(node);
        }
        return uniRawData;
    }

//    this method would transfer angle as radians in [-pi,pi] and transfer to degree in [0,360)
    static double radToDeg(double rad) {
        rad = rad + Math.PI;
        double deg = rad * 180.0 / Math.PI;
        deg = deg % 360.0;
        return deg;
    }

}