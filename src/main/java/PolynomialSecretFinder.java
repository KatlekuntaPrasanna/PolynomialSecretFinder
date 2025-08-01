import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PolynomialSecretFinder {

  static class Point {
    int x;
    BigInteger y;

    Point(int x, BigInteger y) {
      this.x = x;
      this.y = y;
    }
  }

  // Lagrange interpolation at x = 0
  public static BigDecimal lagrangeInterpolationAtZero(List<Point> points, int k) {
    MathContext mc = new MathContext(30);
    BigDecimal result = BigDecimal.ZERO;

    for (int i = 0; i < k; i++) {
      BigDecimal term = new BigDecimal(points.get(i).y);
      int xi = points.get(i).x;

      for (int j = 0; j < k; j++) {
        if (i != j) {
          int xj = points.get(j).x;
          BigDecimal num = BigDecimal.ZERO.subtract(BigDecimal.valueOf(xj));
          BigDecimal den = BigDecimal.valueOf(xi - xj);
          term = term.multiply(num.divide(den, mc), mc);
        }
      }

      result = result.add(term, mc);
    }

    return result;
  }

  public static void processFile(String filename, int testCaseNumber) {
    List<Point> pointList = new ArrayList<>();
    int k = 0;

    try {
      JSONParser parser = new JSONParser();
      JSONObject obj = (JSONObject) parser.parse(new FileReader(filename));
      JSONObject keys = (JSONObject) obj.get("keys");
      k = Integer.parseInt(keys.get("k").toString());

      for (Object keyObj : obj.keySet()) {
        String key = keyObj.toString();
        if (key.equals("keys"))
          continue;

        int x = Integer.parseInt(key);
        JSONObject valueObj = (JSONObject) obj.get(key);
        int base = Integer.parseInt(valueObj.get("base").toString());
        String encodedY = valueObj.get("value").toString();
        BigInteger y = new BigInteger(encodedY, base);
        pointList.add(new Point(x, y));
      }

    } catch (Exception e) {
      System.err.println("Error parsing " + filename + ": " + e.getMessage());
      return;
    }

    pointList.sort(Comparator.comparingInt(p -> p.x));
    if (pointList.size() < k) {
      System.out.println("Test Case " + testCaseNumber + ": Not enough points for interpolation.");
      return;
    }

    List<Point> selected = pointList.subList(0, k);
    BigDecimal c = lagrangeInterpolationAtZero(selected, k);

    System.out.printf("Test Case %d - The constant term (secret c) is: %.0f\n", testCaseNumber, c);
  }

  public static void main(String[] args) {
    processFile("input1.json", 1);
    processFile("input2.json", 2);
  }
}
