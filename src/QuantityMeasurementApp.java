public class QuantityMeasurementApp {

    // ---------------- FEET CLASS ----------------
    static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass()) return false;

            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    // ---------------- INCHES CLASS ----------------
    static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass()) return false;

            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    // ---------------- STATIC METHODS ----------------

    // Feet equality check
    public static boolean compareFeet(double v1, double v2) {
        Feet f1 = new Feet(v1);
        Feet f2 = new Feet(v2);
        return f1.equals(f2);
    }

    // Inches equality check
    public static boolean compareInches(double v1, double v2) {
        Inches i1 = new Inches(v1);
        Inches i2 = new Inches(v2);
        return i1.equals(i2);
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {

        // Feet tests
        System.out.println("Feet Same Value (1.0 vs 1.0): " + compareFeet(1.0, 1.0));
        System.out.println("Feet Different Value (1.0 vs 2.0): " + compareFeet(1.0, 2.0));

        // Inches tests
        System.out.println("Inches Same Value (1.0 vs 1.0): " + compareInches(1.0, 1.0));
        System.out.println("Inches Different Value (1.0 vs 2.0): " + compareInches(1.0, 2.0));

        // Edge cases
        Feet f = new Feet(1.0);
        Inches i = new Inches(1.0);

        System.out.println("Null comparison (Feet): " + f.equals(null));
        System.out.println("Different type comparison (Feet vs Inches): " + f.equals(i));
        System.out.println("Same reference (Feet): " + f.equals(f));
    }
}