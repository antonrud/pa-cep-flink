package software.anton.pcep.prediction;

import smile.regression.OLS;

/**
 * @author Anton Rudacov <anton.rudacov @ gmail.com>
 */
public class PredictionTest {

  public static void main(String[] args) {

    double[][] x = {{1, 2}, {4, 2}, {3, 1}, {1, 2}, {4, 2}, {3, 1}, {1, 2}, {4, 2}, {3, 1}};
    double[] y = {4, 5, 4, 3, 2, 3, 4, 4, 3};
    double[] z = {1, 2};

    OLS ols = new OLS(x, y);

    System.out.println(ols);
    System.out.println(ols.predict(z));
  }
}
