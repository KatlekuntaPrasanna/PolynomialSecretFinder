import java.io.FileReader;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PolynomialSecretFinder {

  static class Point {
    int x;
    long y;

    Point(int x, long y) {
      this.x = x;
      this.y = y;
    }
  }

  public static double lagrangeInterpolationAtZero(List<Point> points, int k) {
    double result = 0.0;
    for (int i = 0; i < k; i++) {
      double term = points.get(i).y;
      for (int j = 0; j < k; j++) {
        if (i != j) {
          term *= (0.0 - points.get(j).x) / (points.get(i).x - points.get(j).x);
        }
      }
      result += term;
    }
    return result;
  }

  public static void main(String[] args) {
    String filename = "input.json";
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
        long y = Long.parseLong(encodedY, base);
        pointList.add(new Point(x, y));
      }

    } catch (Exception e) {
      System.err.println("Error parsing JSON: " + e.getMessage());
      return;
    }

    pointList.sort(Comparator.comparingInt(p -> p.x));
    if (pointList.size() < k) {
      System.out.println("Not enough points for interpolation.");
      return;
    }

    List<Point> selected = pointList.subList(0, k);
    double c = lagrangeInterpolationAtZero(selected, k);
    System.out.printf("The constant term (secret c) is: %.0f\n", c);
  }
}
