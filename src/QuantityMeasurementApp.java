public class QuantityMeasurementApp {

    // Inner class representing Feet measurement
    static class Feet {
        private final double value;

        // Constructor
        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        // Override equals() method
        @Override
        public boolean equals(Object obj) {
            // Reflexive: same reference
            if (this == obj) {
                return true;
            }

            // Null and type check
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            // Cast safely
            Feet other = (Feet) obj;

            // Compare values using Double.compare
            return Double.compare(this.value, other.value) == 0;
        }

        // Override hashCode()
        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    // Main method to demonstrate and test UC1
    public static void main(String[] args) {

        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);
        Feet f3 = new Feet(2.0);

        // Test cases output
        System.out.println("Test Same Value (1.0 vs 1.0): " + f1.equals(f2)); // true
        System.out.println("Test Different Value (1.0 vs 2.0): " + f1.equals(f3)); // false
        System.out.println("Test Null Comparison: " + f1.equals(null)); // false
        System.out.println("Test Same Reference: " + f1.equals(f1)); // true
        System.out.println("Test Non-Numeric Input: " + f1.equals("string")); // false
    }
}