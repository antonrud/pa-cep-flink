package software.anton.pcep.testing;

import smile.regression.RegressionTree;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class RegressTest {

  public static void main(String[] args) {

    double[][] x = {{1, 2}, {4, 2}, {3, 1}, {1, 2}, {4, 2}, {3, 1}, {1, 2}, {4, 2}, {3, 1}};
    double[] y = {4, 5, 4, 3, 2, 3, 4, 4, 3};
    double[] z = {1, 2};
    int nodes = 10;

    RegressionTree model = new RegressionTree(x, y, 10);

    System.out.println(model.predict(z));
  }
}
