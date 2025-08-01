# Polynomial Secret Finder

This Java project reads a JSON file with encoded polynomial root points, decodes the y-values using different bases, and finds the constant term (c) of the polynomial using Lagrange interpolation.

---

## 📥 Input (`input.json`)

```json
{
  "keys": {
    "n": 4,
    "k": 3
  },
  "1": {
    "base": "10",
    "value": "4"
  },
  "2": {
    "base": "2",
    "value": "111"
  },
  "3": {
    "base": "10",
    "value": "12"
  },
  "6": {
    "base": "4",
    "value": "213"
  }
}
